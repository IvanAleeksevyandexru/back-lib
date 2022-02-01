package ru.gosuslugi.pgu.dto.order;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShortOrderData {

    @Schema(description = "Id заявления")
    private Long orderId;

    @Schema(description = "Регион пользователя создавшего заявление")
    private String region;

    @Schema(description = "Дата и время создания заявления")
    private Date createdAt;
}
