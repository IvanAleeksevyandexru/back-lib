package ru.gosuslugi.pgu.common.core.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.extern.slf4j.Slf4j;
import ru.gosuslugi.pgu.common.core.exception.JsonParsingException;
import ru.gosuslugi.pgu.common.core.json.deserializer.OffsetDateTimeDeserializer;
import ru.gosuslugi.pgu.common.core.json.serializer.OffsetDateTimeSerializer;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Optional;

@Slf4j
public class JsonProcessingUtil {

    private static final ObjectMapper OBJECT_MAPPER = createObjectMapper();

    private static ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(new SimpleModule()
                .addSerializer(OffsetDateTime.class, new OffsetDateTimeSerializer())
                .addDeserializer(OffsetDateTime.class, new OffsetDateTimeDeserializer())
        );
        objectMapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
        return objectMapper;
    }

    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    public static String toJson(Object o) {
        try {
            if (o instanceof String) {
                return o.toString();
            } else {
                return OBJECT_MAPPER.writeValueAsString(o);
            }
        } catch (JsonProcessingException e) {
            throw new JsonParsingException("Error while processing json to string", e);
        }
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            throw new JsonParsingException("Error while processing object from json", e);
        }
    }

    /**
     * Десериализует json в T, используя {@link ObjectMapper#readValue(String, TypeReference)}.
     *
     * @param json         входная строка.
     * @param valueTypeRef ссылка на тип возвращаемого значения.
     * @param <T>          тип возвращаемого значения.
     * @return объект типа T.
     * @throws JsonParsingException если json невозможно десериализовать в объект типа T.
     */
    public static <T> T fromJson(String json, TypeReference<T> valueTypeRef) {
        try {
            return OBJECT_MAPPER.readValue(json, valueTypeRef);
        } catch (IOException e) {
            throw new JsonParsingException("Error while processing object from json", e);
        }
    }

    public static String jsonNodeToString(JsonNode jsonObj) {
        return Optional.ofNullable(jsonObj).filter(json -> !json.isNull()).map(JsonNode::asText).orElse(null);
    }

}
