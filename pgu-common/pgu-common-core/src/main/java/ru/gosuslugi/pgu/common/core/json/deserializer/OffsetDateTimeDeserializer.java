package ru.gosuslugi.pgu.common.core.json.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.util.StringUtils;
import ru.gosuslugi.pgu.common.core.exception.JsonParsingException;

import java.io.IOException;
import java.time.OffsetDateTime;

import static ru.gosuslugi.pgu.common.core.json.serializer.OffsetDateTimeSerializer.OFFSET_DATE_TIME_FORMATTER;

public class OffsetDateTimeDeserializer extends JsonDeserializer<OffsetDateTime> {

    @Override
    public OffsetDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        String src = jsonParser.getText();
        try {
            if (!StringUtils.isEmpty(src)) {
                return OffsetDateTime.parse(src, OFFSET_DATE_TIME_FORMATTER);
            } else {
                return null;
            }
        } catch (Exception ex){
            throw new JsonParsingException("Error on parsing date format", ex);
        }
    }
}