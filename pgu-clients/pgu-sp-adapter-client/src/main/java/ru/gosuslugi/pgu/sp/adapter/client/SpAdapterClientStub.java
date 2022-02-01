package ru.gosuslugi.pgu.sp.adapter.client;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import ru.gosuslugi.pgu.common.core.exception.PguException;
import ru.gosuslugi.pgu.common.esia.search.dto.UserPersonalData;
import ru.gosuslugi.pgu.dto.SmevRequestDto;
import ru.gosuslugi.pgu.sp.adapter.SpAdapterClient;
import ru.gosuslugi.pgu.sp.adapter.SpAdapterProperties;
import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
@RequiredArgsConstructor
public class SpAdapterClientStub implements SpAdapterClient {

    private final ResourceLoader resourceLoader;
    private final SpAdapterProperties properties;

    @Override
    public SmevRequestDto createXmlAndPdf(Long orderId, Long userId, Long orgId, String requestGuid) {
        return new SmevRequestDto(requestGuid);
    }

    @Override
    public byte[] getTsReportPdf(Long orderId, @Nonnull UserPersonalData userPersonalData) {

        byte[] fileContent = null;
        try {

            File file = resourceLoader.getResource(properties.getMockFile()).getFile();
            fileContent = Files.readAllBytes(file.toPath());

        } catch (IOException e) {
            throw new PguException("Can't read file. Check link or file location." + e.getMessage(), e);
        }

        return fileContent;
    }
}