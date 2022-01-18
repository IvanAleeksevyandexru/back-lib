package ru.gosuslugi.pgu.dto.pdf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.gosuslugi.pgu.dto.ScenarioDto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Параметры запроса на генерацию и сохранение файла PDF
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeneratePdfRequestDto {

    /** Префикс файла vm шаблона, роль Applicant */
    @NotEmpty(message = "prefix должен быть указан и не должен быть пустым")
    private String prefix;

    /** Идентификатор черновика */
    private Long orderId;

    /** идентификатор (oid) пользователя */
    @NotNull(message = "Идентификатор пользователя должен быть указан")
    private Long userId;

    /** роль пользователя, может не передаваться */
    @NotEmpty(message = "должен быть указан и не должен быть пустым")
    private String userRole;

    /** Генерировать дополнительный документ */
    private boolean additional;

    private ScenarioDto draft;

}
