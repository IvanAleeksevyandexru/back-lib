package ru.gosuslugi.pgu.common.esia.search.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import ru.atc.carcass.security.rest.model.orgs.Org;
import ru.gosuslugi.pgu.common.esia.search.service.OrgSearchService;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class OrgSearchServiceStub extends AbstractSearchServiceStub implements OrgSearchService {

    private List<Org> orgList;

    public OrgSearchServiceStub(String stubDataFilePath) {
        super(stubDataFilePath);
        try (InputStream targetStream = getSourceFileStream()) {
            orgList = objectMapper.readValue
                    (targetStream,
                            new TypeReference<>() {
                            });
        } catch (IOException e) {
            orgList = new ArrayList<>();
            log.error("Error of initialization mock for person search data", e);
        }
    }

    @Override
    public Org findOrgById(String oid) {
        return orgList.stream().filter(org -> Objects.equals(org.getOid(), oid)).findFirst().orElse(null);
    }

    private InputStream getSourceFileStream() throws IOException {
        if (!Optional.ofNullable(stubDataFilePath).orElse("").isEmpty()) {
            return new FileInputStream(stubDataFilePath);
        }
        return getClass().getClassLoader().getResourceAsStream("orgs.json");
    }


}
