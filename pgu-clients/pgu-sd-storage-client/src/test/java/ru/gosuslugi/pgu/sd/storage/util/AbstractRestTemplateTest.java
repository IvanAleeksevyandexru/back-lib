/*
 * Copyright 2017 Russian Post
 *
 * This source code is Russian Post Confidential Proprietary.
 * This software is protected by copyright. All rights and titles are reserved.
 * You shall not use, copy, distribute, modify, decompile, disassemble or reverse engineer the software.
 * Otherwise this violation would be treated by law and would be subject to legal prosecution.
 * Legal use of the software provides receipt of a license from the right holder only.
 */
package ru.gosuslugi.pgu.sd.storage.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;

/**
 * Integration tests for {@link RestTemplate} methods
 *
 * @author vbalovnev
 */
public abstract class AbstractRestTemplateTest {


    protected RestTemplate restTemplate;

    @Before
    public void setUp() throws Exception {
        final JacksonTestConfig config = new JacksonTestConfig();
        restTemplate = new RestTemplate(Collections.singletonList(config.jackson2Converter()));
        restTemplate.setErrorHandler(getErrorHandler());
    }

    /**
     * Return default error handler
     * @return error handler
     */
    public static ResponseErrorHandler getErrorHandler() {
        return new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return response.getStatusCode() != HttpStatus.OK;
            }

            @Override
            public void handleError(ClientHttpResponse response) throws IOException {

                throw new RuntimeException();
            }
        };
    }
}