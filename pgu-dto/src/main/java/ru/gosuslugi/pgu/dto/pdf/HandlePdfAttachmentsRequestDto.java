package ru.gosuslugi.pgu.dto.pdf;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.gosuslugi.pgu.dto.pdf.data.FileDescription;

import java.util.List;


/**
 * Параметры запроса для вызова метода handlePdfAttachments
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HandlePdfAttachmentsRequestDto {

    private String serviceId;
    private Long orderId;
    private Long oid;
    private Long orgId;
    private String roleId;

    private FileDescription options;

}
