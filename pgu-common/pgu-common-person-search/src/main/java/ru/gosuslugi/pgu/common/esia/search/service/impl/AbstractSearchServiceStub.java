package ru.gosuslugi.pgu.common.esia.search.service.impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import ru.gosuslugi.pgu.common.core.json.deserializer.OffsetDateTimeDeserializer;
import ru.gosuslugi.pgu.common.core.json.serializer.OffsetDateTimeSerializer;

import java.time.OffsetDateTime;

public abstract class AbstractSearchServiceStub {

    protected final ObjectMapper objectMapper;
    protected final String stubDataFilePath;

    public AbstractSearchServiceStub(String stubDataFilePath) {
        this.objectMapper = createObjectMapper();
        this.stubDataFilePath = stubDataFilePath;
    }

    protected ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(new SimpleModule()
                .addSerializer(OffsetDateTime.class, new OffsetDateTimeSerializer())
                .addDeserializer(OffsetDateTime.class, new OffsetDateTimeDeserializer())
        );
        return objectMapper;
    }
}
