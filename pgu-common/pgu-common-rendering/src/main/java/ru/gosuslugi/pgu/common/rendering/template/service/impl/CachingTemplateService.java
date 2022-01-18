package ru.gosuslugi.pgu.common.rendering.template.service.impl;

import ru.gosuslugi.pgu.common.rendering.template.data.PackageProcessingStatus;
import ru.gosuslugi.pgu.common.rendering.template.data.ProcessingStatus;
import ru.gosuslugi.pgu.common.rendering.template.config.props.TemplateServiceProperties;
import ru.gosuslugi.pgu.common.rendering.template.exception.TemplateServiceException;
import ru.gosuslugi.pgu.common.rendering.template.service.TemplateService;
import ru.gosuslugi.pgu.sd.storage.ServiceDescriptorClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.xml.bind.DatatypeConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Управляет блокировкой на архив с файлами шаблонов, загружая их из дескриптора услуг в локальное
 * хранилище в случае отсутствия.
 * <p>
 * Также по расписанию следит за обновлениями архивов в дескрипторе услуг Cм. {@link
 * #refreshTemplatePackages()}.
 */
@Slf4j
@RequiredArgsConstructor
@Service
@EnableScheduling
public class CachingTemplateService implements TemplateService {

    private static final String CHECKSUM_FILE_SUFFIX = ".checksum";
    private static final String EMPTY_CHECKSUM = "";
    private static final String TEMP_FILE_PREFIX = "vm-templates";
    private static final String TEMP_FILE_SUFFIX = ".zip";
    private final Map<String, ReentrantReadWriteLock> serviceTemplateLocks =
            new ConcurrentHashMap<>();
    private final Map<String, PackageProcessingStatus> packageProcessingStatusMap;
    private final TemplateServiceProperties templateServiceProps;
    private final ServiceDescriptorClient serviceDescriptorClient;

    @Override
    public ReentrantReadWriteLock getOrDownloadAndLock(String serviceId) {
        if (!serviceTemplateLocks.containsKey(serviceId)) {
            loadTemplatePackage(serviceId);
        }
        return serviceTemplateLocks.get(serviceId);
    }

    @Scheduled(fixedRateString = "${template-service.refresh-rate}",
            initialDelayString = "#{ T(java.util.concurrent.ThreadLocalRandom).current().nextInt(100*60*10) }")
    public void refreshTemplatePackages() {
        Collection<String> serviceIds = serviceTemplateLocks.keySet();
        if (serviceIds.isEmpty()) {
            return;
        }
        Map<String, String> mapChecksums =
                serviceDescriptorClient.getTemplatePackageChecksums(serviceIds);
        for (Map.Entry<String, String> entry : mapChecksums.entrySet()) {
            String serviceId = entry.getKey();
            try {
                String serviceChecksum = entry.getValue();
                String serviceDir =
                        templateServiceProps.getFileResourceLoaderPath() + File.separator
                                + serviceId;
                String checksum = EMPTY_CHECKSUM;

                File checksumFile = new File(serviceDir, serviceId + CHECKSUM_FILE_SUFFIX);
                if (checksumFile.exists()) {
                    checksum = Files.readString(checksumFile.toPath());
                }
                if (!checksum.equals(serviceChecksum)) {
                    loadTemplatePackage(serviceId);
                } else {
                    setPackageProcessingStatus(serviceId, ProcessingStatus.ACTUAL,
                            "Шаблоны актуальны. КС: " + checksum);
                }
            } catch (IOException ex) {
                setPackageProcessingStatus(serviceId, ProcessingStatus.REFRESH_FAIL,
                        ex.getMessage());
                String errorMessage =
                        String.format("В процессе запланированной распаковки архива с шаблонами"
                                        + " по услуге %s произошла ошибка: %s",
                                serviceId, ex.getMessage());
                log.error(errorMessage, ex);
            }
        }
    }

    private void loadTemplatePackage(String serviceId) {

        ByteBuffer templatesPack;
        try {
            templatesPack = serviceDescriptorClient.getTemplatePackage(serviceId);
        } catch (Exception e) {
            throw new TemplateServiceException(
                    String.format(
                            "Произошла ошибка загрузки архива с шаблонами для услуги '%s': %s",
                            serviceId, e.getMessage()));
        }

        if (!serviceTemplateLocks.containsKey(serviceId)) {
            serviceTemplateLocks.putIfAbsent(serviceId, new ReentrantReadWriteLock());
        }
        try {
            unPack(templatesPack, serviceId);
            setPackageProcessingStatus(serviceId, ProcessingStatus.REFRESH_SUCCESS,
                    "Архив с шаблонами успешно загружен и распакован");
        } catch (IOException ex) {
            setPackageProcessingStatus(serviceId, ProcessingStatus.REFRESH_FAIL, ex.getMessage());
        }
    }

    private void setPackageProcessingStatus(String serviceId, ProcessingStatus status,
            String statusDescription) {
        PackageProcessingStatus processingStatus = PackageProcessingStatus.builder()
                .serviceId(serviceId)
                .processedOn(LocalDateTime.now().toString())
                .status(status)
                .statusDescription(statusDescription)
                .build();
        packageProcessingStatusMap.put(serviceId, processingStatus);
    }

    private void unPack(ByteBuffer templatePackage, String serviceId) throws IOException {
        ReentrantReadWriteLock writeLock = serviceTemplateLocks.get(serviceId);
        writeLock.writeLock().lock();
        try {
            File fileZip = File.createTempFile(TEMP_FILE_PREFIX, TEMP_FILE_SUFFIX);
            FileOutputStream zipOutputStream = new FileOutputStream(fileZip);
            FileChannel fc = zipOutputStream.getChannel();
            fc.write(templatePackage);
            fc.close();
            zipOutputStream.close();

            processZipFile(serviceId, fileZip);
            computeAndSaveZipChecksum(serviceId, templatePackage);
        } catch (IOException | NoSuchAlgorithmException | IllegalArgumentException ex) {
            log.error("В процессе распаковки архива с шаблонами для услуги {} произошла ошибка: {}",
                    serviceId, ex.getMessage());
            throw new IOException(ex.getMessage(), ex.getCause());
        } finally {
            writeLock.writeLock().unlock();
        }
    }

    private void processZipFile(String serviceId, File fileZip) throws IOException {
        File destDir = new File(templateServiceProps.getFileResourceLoaderPath(), serviceId);
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip), StandardCharsets.UTF_8);
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            File newFile = newFile(destDir, zipEntry);
            if (zipEntry.isDirectory()) {
                if (!newFile.isDirectory() && !newFile.mkdirs()) {
                    log.error("Произошла ошибка при создании каталога для услуги: {}", serviceId);
                    throw new IOException("Невозможно создать каталог " + newFile);
                }
                zipEntry = zis.getNextEntry();
                continue;
            }
            // fix for Windows-created archives
            File parent = newFile.getParentFile();
            if (!parent.isDirectory() && !parent.mkdirs()) {
                throw new IOException("Невозможно создать каталог " + parent);
            }
            // write file content
            FileOutputStream fos = new FileOutputStream(newFile);
            int len;
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
        Files.delete(fileZip.toPath());
    }

    private void computeAndSaveZipChecksum(String serviceId, ByteBuffer templatePackage)
            throws NoSuchAlgorithmException, IOException {
        File destDir = new File(templateServiceProps.getFileResourceLoaderPath(), serviceId);
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(templatePackage.array());
        byte[] digest = md.digest();
        FileWriter fileWriter = new FileWriter(
                destDir.getAbsolutePath() + File.separator + serviceId + CHECKSUM_FILE_SUFFIX);
        String checksum = DatatypeConverter.printHexBinary(digest).toUpperCase();
        fileWriter.write(checksum);
        fileWriter.close();
    }

    private File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());
        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();
        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException(
                    String.format("Элемент zip-архива '%s' находится вне целевого каталога '%s'",
                            zipEntry.getName(), destinationDir.getAbsolutePath()));
        }
        return destFile;
    }
}
