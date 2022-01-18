package ru.gosuslugi.pgu.dto.pdf.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileType {

    PDF("pdf"),

    XML("xml"),

    COMMON_PDF("commonPdf"),

    REQUEST("request");

    private String value;

}
