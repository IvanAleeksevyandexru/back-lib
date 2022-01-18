package ru.gosuslugi.pgu.dto.descriptor.suggestion;

import lombok.Data;

@Data
public class SuggestionGroup {

    private String groupId;

    // Поле suggestionId чекбокса на этом скрине. Если это поле не пустое и чекбокс не выбран, то значения не сохраняются.
    // Необязательный параметр, если его не указать группу сохраняем.
    private String saveGroupDataCheckboxId;

    // Поле suggestionId филда на этом скрине. Значение этого поля используется как имя группы для шаблонов (см. фигму)
    private String groupNameFieldId;


}
