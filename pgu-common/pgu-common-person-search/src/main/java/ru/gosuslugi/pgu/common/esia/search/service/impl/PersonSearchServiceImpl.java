package ru.gosuslugi.pgu.common.esia.search.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.atc.carcass.security.rest.model.person.Person;
import ru.gosuslugi.pgu.common.core.exception.EntityNotFoundException;
import ru.gosuslugi.pgu.common.core.exception.ExternalServiceException;
import ru.gosuslugi.pgu.common.core.json.JsonProcessingUtil;
import ru.gosuslugi.pgu.common.esia.search.dto.PersonWithAge;
import ru.gosuslugi.pgu.common.esia.search.service.PersonSearchService;
import ru.gosuslugi.pgu.common.esia.search.service.UddiService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PersonSearchServiceImpl extends AbstractSearchService implements PersonSearchService {

    private static final String SEARCH_BY_SNILS = "pso/srch/by-snils?snils={snils}";
    private static final String SEARCH_BY_SNILS_SCOPE = "http://esia.gosuslugi.ru/usr_srch_by_snils";

    private static final String SEARCH_BY_PASSPORT= "pso/srch/by-passport?series={series}&number={number}";
    private static final String SEARCH_BY_PASSPORT_SCOPE = "http://esia.gosuslugi.ru/usr_srch_by_passport";

    private static final String SEARCH_USER_BY_ID = "rs/prns/{id}";
    private static final String SEARCH_USER_BY_ID_SCOPE = "http://esia.gosuslugi.ru/usr_inf?oid=%s";

    public PersonSearchServiceImpl(RestTemplate restTemplate,
                                   UddiService uddiService) {
        super(restTemplate, uddiService);
    }

    @Override
    public Person findUserById(String oid) {
        Map<String, String> parameters = Map.of("id", oid);
        String scope = String.format(SEARCH_USER_BY_ID_SCOPE, oid);
        Map personMap = sendRequest(SEARCH_USER_BY_ID, parameters, scope, Map.class);
        return JsonProcessingUtil.getObjectMapper().convertValue(personMap, Person.class);
    }

    @Override
    public List<PersonWithAge> bySnils(String snils) {

        final HashMap<String, String> parameters = new HashMap<>();
        parameters.put("snils", snils);
        return sendRequest(SEARCH_BY_SNILS, parameters, SEARCH_BY_SNILS_SCOPE);
    }

    @Override
    public List<PersonWithAge> byPassport(String series, String number) {

        final HashMap<String, String> parameters = new HashMap<>();
        parameters.put("series", series);
        parameters.put("number", number);
        return sendRequest(SEARCH_BY_PASSPORT, parameters, SEARCH_BY_PASSPORT_SCOPE);
    }

    private List<PersonWithAge> sendRequest(String path, Map<String, String> parameters, String scope) {
        String token = receiveToken(scope);

        String endpoint = uddiService.getEndpoint(UDDI_INTERNAL_ESIA);

        try {
            ResponseEntity<List<PersonWithAge>> responseEntity = restTemplate.exchange(endpoint + path,
                    HttpMethod.GET,
                    new HttpEntity<String>(this.prepareSecurityHeader(token)),
                    new ParameterizedTypeReference<>() {
                    },
                    parameters
            );
            List<PersonWithAge> persons = responseEntity.getBody();
            if (log.isDebugEnabled()) log.debug("Response from ESIA:\n {}", persons);

            return persons;
        } catch (EntityNotFoundException e) {
            if (log.isWarnEnabled()) log.warn("ESIA find user service [ {} ] return code : 404. parameters:  {}", path, parameters);
            return null;
        } catch (RestClientException e) {
            throw new ExternalServiceException(e);
        }
    }

}
