package ru.gosuslugi.pgu.common.core.json;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
public class JsonFileUtil {
    public static String getJsonFromFile(Class clazz, String suffix) {
        try {
            return Files.readString(Paths.get(clazz.getResource(clazz.getSimpleName() + suffix).toURI()));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
