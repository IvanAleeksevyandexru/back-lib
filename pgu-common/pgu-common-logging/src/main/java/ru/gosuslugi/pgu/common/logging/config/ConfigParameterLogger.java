package ru.gosuslugi.pgu.common.logging.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.*;
import java.util.stream.Collectors;

import static org.apache.logging.log4j.util.Strings.isNotBlank;

@Slf4j
@RequiredArgsConstructor
public class ConfigParameterLogger {

    private final ConfigurableEnvironment env;

    private static final String separator = System.lineSeparator();

    public void printActiveProperties() {

        List<MapPropertySource> propertySources = new ArrayList<>();
        Set<String> maskedProps = getPropertiesToMaskPasswords(env);

        env.getPropertySources().forEach(it -> {
            if (it instanceof MapPropertySource) {
                propertySources.add((MapPropertySource) it);
            }
        });

        String properties = propertySources.stream()
                .map(propertySource -> propertySource.getSource().keySet())
                .flatMap(Collection::stream)
                .distinct()
                .sorted()
                .map(key -> key + "=" + getProperty(env, maskedProps, key))
                .collect(Collectors.joining(separator));
        log.info("************************* Current configuration properties ******************************" + separator
                + properties + separator
                + "*****************************************************************************************");
    }

    private static String getProperty(ConfigurableEnvironment env, Set<String> propsToMask, String key) {
        String value = env.getProperty(key);
        if (isNotBlank(value) && isNeedToMask(propsToMask, key)) {
            return maskString(value);
        }
        return value;
    }

    private static Set<String> getPropertiesToMaskPasswords(ConfigurableEnvironment env) {
        Set<String> result = new HashSet<>();
        result.add(".password");

        String props = env.getProperty("mask-password-properties");

        if (isNotBlank(props)) {
            for (String prop : props.split(",")) {
                result.add(prop.trim());
            }
        }
        return result;
    }

    private static boolean isNeedToMask(Set<String> propsToMask, String key) {
        return propsToMask.stream()
                .anyMatch(key::endsWith);
    }

    private static String maskString(String s) {
        char[] stars = new char[s.length() - 1];
        Arrays.fill(stars, '*');
        return s.charAt(0) + String.valueOf(stars);
    }

}
