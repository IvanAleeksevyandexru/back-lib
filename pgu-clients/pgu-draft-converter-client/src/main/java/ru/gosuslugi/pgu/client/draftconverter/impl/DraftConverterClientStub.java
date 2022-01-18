package ru.gosuslugi.pgu.client.draftconverter.impl;

import lombok.RequiredArgsConstructor;
import ru.gosuslugi.pgu.client.draftconverter.DraftConverterClient;
import ru.gosuslugi.pgu.dto.ExternalOrderRequest;
import ru.gosuslugi.pgu.dto.ScenarioDto;
import ru.gosuslugi.pgu.dto.XmlCustomConvertRequest;
import ru.gosuslugi.pgu.dto.XmlDraftConvertRequest;

import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
public class DraftConverterClientStub implements DraftConverterClient {

    @Override
    public ScenarioDto convertExternalOrderRequest(ExternalOrderRequest request) {
        return null;
    }

    @Override
    public ScenarioDto convertXmlDraft(XmlDraftConvertRequest request) {
        return null;
    }

    @Override
    public ScenarioDto convertXmlDraft(XmlDraftConvertRequest request, int timeout) {
        return null;
    }

    @Override
    public Map<Object, Object> convertXmlCustom(XmlCustomConvertRequest request) {
        return Collections.emptyMap();
    }
}
