package ru.gosuslugi.pgu.dto.pdf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeneratePdfResponseDto {

    private byte[] pdfContent;

}
