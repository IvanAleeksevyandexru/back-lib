package ru.gosuslugi.pgu.common.core.file.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

public class FileUtil {

    public static byte[] readAllBytes(File file) {
        Objects.requireNonNull(file, "File is empty");

        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException("Error reading file", e);
        }
    }
}
