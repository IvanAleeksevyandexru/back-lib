package ru.gosuslugi.pgu.common.esia.search.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.atc.carcass.security.rest.model.orgs.Org;
import ru.gosuslugi.pgu.common.core.json.JsonProcessingUtil;
import ru.gosuslugi.pgu.common.esia.search.service.OrgSearchService;
import ru.gosuslugi.pgu.common.esia.search.service.UddiService;

import java.util.Map;

@Slf4j
@Service
public class OrgSearchServiceImpl extends AbstractSearchService implements OrgSearchService {

    private static final String SEARCH_ORG_BY_ID= "rs/orgs/{oid}";
    private static final String SEARCH_ORG_BY_ID_SCOPE = "http://esia.gosuslugi.ru/org_inf?org_oid=%s";

    public OrgSearchServiceImpl(RestTemplate restTemplate,
                                UddiService uddiService) {
        super(restTemplate, uddiService);
    }

    @Override
    public Org findOrgById(String oid) {
        Map<String, String> parameters = Map.of("oid", oid);
        String scope = String.format(SEARCH_ORG_BY_ID_SCOPE, oid);
        Map orgMap = sendRequest(SEARCH_ORG_BY_ID, parameters, scope, Map.class);
        return JsonProcessingUtil.getObjectMapper().convertValue(orgMap, Org.class);
    }

}
