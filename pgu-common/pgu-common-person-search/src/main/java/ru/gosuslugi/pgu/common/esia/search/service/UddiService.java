package ru.gosuslugi.pgu.common.esia.search.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.atc.carcass.common.ws.JaxWSClientFactoryImpl;
import ru.gosuslugi.pgu.common.logging.service.SpanService;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UddiService {

    private final JaxWSClientFactoryImpl jaxWSClientFactory;
    private final SpanService spanService;

    public String getEndpoint(String uddi) {
        return spanService.runExternalService(
                "esiaRestClientService: get all person data",
                "esia.request.getPersonAll",
                () -> jaxWSClientFactory.getEndpointByUddi(uddi),
                Map.of("uddi", uddi));
    }
}
