package ru.gosuslugi.pgu.common.esia.search.service;

import ru.atc.carcass.security.rest.model.orgs.Org;

public interface OrgSearchService {

    Org findOrgById(String oid);
}
