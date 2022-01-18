package ru.gosuslugi.pgu.common.core.attachments;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.gosuslugi.pgu.common.core.attachments.exception.RetrieveDigestValueException;
import ru.gosuslugi.pgu.dto.AttachmentInfo;
import ru.gosuslugi.pgu.dto.ScenarioDto;
import ru.gosuslugi.pgu.terrabyte.client.TerrabyteClient;
import ru.gosuslugi.pgu.terrabyte.client.model.DigestServiceDto;
import ru.gosuslugi.pgu.terrabyte.client.model.FileDigestServiceDto;
import ru.gosuslugi.pgu.terrabyte.client.model.FileInfo;
import ru.gosuslugi.pgu.terrabyte.client.model.FileType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttachmentService {

    public static final String DIGEST_VALUE_ALGORITHM = "GOST3411_2012_256";
    public static final String DEFAULT_DIGEST_VALUE = "MA==";

    private final TerrabyteClient terrabyteClient;

    public String saveAttachment(Long orderId, String mimeType, String fileName, String mnemonic, byte[] fileBody, Set<String> attachments, Set<String> generatedFiles) {

        terrabyteClient.internalSaveFile(fileBody, fileName, mnemonic, mimeType, orderId, FileType.ATTACHMENT.getType());

        if(attachments!=null ) {
            attachments.add(mnemonic);
        }

        if (generatedFiles != null) {
            generatedFiles.add(mnemonic);
        }
        return mnemonic;
    }

    public void removeFile(Long orderId, String mnemonic) {
        terrabyteClient.deleteFile(orderId, FileType.ATTACHMENT.getType(), mnemonic);
    }

    public HashMap<String, String> getAttachmentsDigestValues(ScenarioDto draftBody, boolean defaultDigestEnabled) {
        HashMap<String, String> result = new HashMap<>();

        log.info("Getting digest values for attachments");

        for (Map.Entry<String, List<AttachmentInfo>> entry : draftBody.getAttachmentInfo().entrySet()) {
            String key = entry.getKey();
            List<AttachmentInfo> attachmentInfos = entry.getValue();
            for (int i=0; i < attachmentInfos.size(); i++) {
                result.put(key+"_digestValue_"+i, getDigestValue(attachmentInfos.get(i), defaultDigestEnabled));
            }
        }

        log.debug("Digest received: {}", result);
        return result;
    }

    /**
     * Метод при невозможности получить digestValue в ряде случаев выбрасывает RetrieveDigestValueException
     * @param attachmentInfo Атач инфо
     * @param defaultDigestEnabled Нужно ли выбрасывать исключение
     * @return DigestValue
     */
    private String getDigestValue(AttachmentInfo attachmentInfo, boolean defaultDigestEnabled) {
        log.info("Sending get digest request for a file: {}", attachmentInfo.getObjectId());
        DigestServiceDto digestServiceDto = new DigestServiceDto(DIGEST_VALUE_ALGORITHM, List.of(
                new FileDigestServiceDto(attachmentInfo.getObjectId(), attachmentInfo.getObjectTypeId(), attachmentInfo.getUploadMnemonic())));
        String digestValue = DEFAULT_DIGEST_VALUE;
        try {
            digestValue = terrabyteClient.getDigestValue(digestServiceDto);
        } catch (Exception e) {
            log.warn("Cannot get digest value for orderId {} and file {}. Ignoring setting defaultValue", attachmentInfo.getObjectId(), attachmentInfo.getUploadFilename());
            if(!defaultDigestEnabled){
                throw new RetrieveDigestValueException(e);
            }
        }
        return digestValue;
    }

    public Map<String, List<AttachmentInfo>> copyAttachments(ScenarioDto sourceDraft, long sourceOrderId, long targetOrderId) {
        Map<String, List<AttachmentInfo>> result = new HashMap<>();
        if (sourceDraft.getAttachmentInfo() == null) {
            return result;
        }
        sourceDraft.getAttachmentInfo().forEach((key, attachments) -> {
            List<AttachmentInfo> copiedAttachments = new ArrayList<>();
            for (AttachmentInfo attachment : attachments) {
                String mnemonic = attachment.getUploadMnemonic();
                int objectTypeId = Integer.parseInt(attachment.getObjectTypeId());
                FileInfo src = new FileInfo();
                src.setObjectId(sourceOrderId);
                src.setObjectTypeId(objectTypeId);
                src.setMnemonic(mnemonic);
                FileInfo trg = new FileInfo();
                trg.setObjectId(targetOrderId);
                trg.setObjectTypeId(objectTypeId);
                trg.setMnemonic(mnemonic);

                terrabyteClient.copyFile(src, trg);
                copiedAttachments.add(new AttachmentInfo(attachment.getUploadId(), mnemonic, attachment.getUploadFilename(), Long.toString(targetOrderId), Long.toString(objectTypeId)));
            }
            result.put(key, copiedAttachments);
        });
        return result;
    }
}
