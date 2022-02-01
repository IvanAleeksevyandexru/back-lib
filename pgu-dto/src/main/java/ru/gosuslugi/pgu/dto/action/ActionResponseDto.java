package ru.gosuslugi.pgu.dto.action;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ActionResponseDto {

    @Schema(description = "Результаты выполнения действия")
    Map<String, Object> responseData = new HashMap<>();

    @Schema(description = "Флаг успешного выполнения действия")
    Boolean result;

    @Schema(description = "Список ошибок")
    List<String> errorList = new ArrayList<>();

}
