package ru.gosuslugi.pgu.common.logging.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.unit.DataSize;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.regex.Pattern;

@Slf4j
public class LogUtils {

    private static final String RESPONSE_LOGGING_BODY_SKIP_PROP_KEY = "logging.remote-rest.body-max-length";
    private static final Pattern NEW_LINE = Pattern.compile("(\\r)?\\n");

    public static int parseLoggingMaxBodyLen(ConfigurableEnvironment env) {
        int bodyMaxLen = -1;
        try {
            bodyMaxLen = Optional.ofNullable(env.getProperty(RESPONSE_LOGGING_BODY_SKIP_PROP_KEY, String.class))
                    .map(DataSize::parse).map(DataSize::toBytes).orElse(-1L).intValue();
        } catch (RuntimeException e) {
            log.error("Unable to read env property " + RESPONSE_LOGGING_BODY_SKIP_PROP_KEY + " max logging body length.", e);
        }
        return bodyMaxLen;
    }

    public static String truncateAndConvertToStr(byte[] bytes, int maxLen) {
        if (maxLen > 0 && bytes.length > maxLen) {
            int truncatedLen = bytes.length - maxLen;
            return new String(bytes, 0, maxLen, StandardCharsets.UTF_8)
                    .concat("... " + truncatedLen + " bytes truncated");
        } else {
            return new String(bytes, StandardCharsets.UTF_8);
        }
    }

    public static String truncate(String str, int maxLen) {
        if (str == null) {
            return null;
        }
        if (maxLen > 0 && str.length() > maxLen) {
            int truncatedLen = str.length() - maxLen;
            return str.substring(0, maxLen) + "... " + truncatedLen + " bytes truncated";
        } else {
            return str;
        }
    }

    public static String flatString(String s) {
        return s == null ? "" : NEW_LINE.matcher(s).replaceAll("");
    }

}
