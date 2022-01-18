package ru.gosuslugi.pgu.client.draftconverter;

import ru.gosuslugi.pgu.dto.ExternalOrderRequest;
import ru.gosuslugi.pgu.dto.ScenarioDto;
import ru.gosuslugi.pgu.dto.XmlCustomConvertRequest;
import ru.gosuslugi.pgu.dto.XmlDraftConvertRequest;

import java.util.Map;

public interface DraftConverterClient {
    ScenarioDto convertExternalOrderRequest(ExternalOrderRequest request);
    ScenarioDto convertXmlDraft(XmlDraftConvertRequest request);
    ScenarioDto convertXmlDraft(XmlDraftConvertRequest request, int timeout);
    Map<Object, Object> convertXmlCustom(XmlCustomConvertRequest request);
}
