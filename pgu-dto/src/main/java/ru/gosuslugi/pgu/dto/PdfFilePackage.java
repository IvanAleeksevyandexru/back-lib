package ru.gosuslugi.pgu.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * Описывает структуру для упаковки файлов в PDF
 */
@Data
@NoArgsConstructor
public class PdfFilePackage {
    /** Название файла PDF */
    private String filename;
    /** Список файлов, которые войдут в PDF */
    private Set<TerrabyteFileInfo> fileInfos = new HashSet<>();

    public PdfFilePackage(String filename) {
        this.filename = filename;
    }
}
