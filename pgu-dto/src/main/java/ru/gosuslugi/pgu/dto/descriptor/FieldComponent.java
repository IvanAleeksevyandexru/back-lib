package ru.gosuslugi.pgu.dto.descriptor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.SerializationUtils;
import ru.gosuslugi.pgu.common.core.json.JsonProcessingUtil;
import ru.gosuslugi.pgu.dto.descriptor.types.ComponentType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Class that describes components (one input field element on a screen)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FieldComponent {

    /**
     * Unique id. It's used for referenced in validation and rules
     */
    private String id;

    private String name;

    private ComponentType type;

    /**
     * Если в услуге есть компонент с таким флагом
     * То order будет создан в LKApi после ответа на этот компонент
     */
    private Boolean createOrder = false;

    /**
     * Если в услуге есть компонент с таким флагом,
     * то обработчик осуществляет проверку на присутствие потенциальных дубликатов заявлений
     * в соответствии с данными отраженными в блоках {@link #fieldsForCheck} и {@code valuesForSave}
     */
    private Boolean checkForDuplicate = false;

    /**
     * Если в компоненте есть флаг createOrder, то необходимо заполнить этот лист значениями по которым будет
     * проходить проверка на создание order, а так же эти значения будут сохраняться в ЛК для последующих проверок
     */
    private List<String> fieldsForCheck;

    /**
     * Если ответ на текущий компонент отличается от того что указан в cacheAnswers (если он там присутствует),
     * то будут удалены все cachedValues указанные в этом поле
     */
    private List<String> clearCacheForComponentIds = new ArrayList<>();

    /**
     * Label, that supports markup language
     */
    private String label;

    /**
     * Если у компонента указан флаг "skipValidation = true" - валидация входных параметров не происходит
     */
    private Boolean skipValidation = false;

    /**
     * Additional attributes that can be specific for each type
     * In json represented as a Json object
     */
    private Map<String, Object> attrs;

    /**
     * Mapping instructions for component arguments
     */
    private List<LinkedValue> linkedValues;

    /**
     * Component specific arguments
     *
     * @see <a href="https://confluence.egovdev.ru/pages/viewpage.action?pageId=178656367">Component with arguments(example)</a>
     */
    private Map<String, String> arguments = new HashMap<>();

    private String value;

    private boolean required = true;

    /** Индентификатор для сохранения поля в Suggestion Service  */
    private String suggestionId;

    /** Тэги, помечающий компонент для аналитического кластера  */
    private String analyticsTag;
    private boolean sendAnalytics;

    /** Описание ошибок компонентов - задается после инициализации компонента */
    private List<FieldComponentError> errors;

    private String pronounceText;
    private String pronounceTextType;

    @JsonIgnore
    public boolean isComponentInCycle() {
        if (Objects.isNull(this.getAttrs())) {
            return false;
        }
        if (this.getAttrs().containsKey("isCycledComponent") && this.getAttrs().get("isCycledComponent") instanceof Boolean) {
            return Boolean.parseBoolean(String.valueOf(this.getAttrs().get("isCycledComponent"))) ;
        }
        return false;
    }

    @JsonIgnore
    public boolean isCycled() {
        if (Objects.isNull(this.getAttrs())) {
            return false;
        }
        if (this.getAttrs().containsKey("isCycled") && this.getAttrs().get("isCycled") instanceof Boolean) {
            return Boolean.parseBoolean(String.valueOf(this.getAttrs().get("isCycled")));
        }
        return false;
    }

    public void addArgument(String argumentName, String argumentValue) {
        if (argumentValue == null) return;
        if (arguments == null) {
            arguments = new HashMap<>();
        }
        arguments.put(argumentName, argumentValue);
    }

    public String getArgument(String key) {
        return getArgument(key, "");
    }

    public String getArgument(String key, String defaultValue) {
        if (arguments == null) {
            arguments = new HashMap<>();
        }
        return arguments.getOrDefault(key, defaultValue);
    }

    public String getName() {
        if (name == null) {
            return label;
        }
        return name;
    }

    @JsonIgnore
    public boolean getBooleanAttr(String booleanAttrName) {
        Object booleanAttrValue = this.attrs.get(booleanAttrName);
        if (Objects.nonNull(booleanAttrValue)) {
            if (booleanAttrValue instanceof Boolean) return (boolean) booleanAttrValue;
            return Boolean.parseBoolean(booleanAttrValue.toString());
        }
        return false;
    }

    @JsonIgnore
    public boolean getBooleanCapableAttr(String booleanAttrName, Boolean defaultValue) {
        Object booleanAttrValue = this.attrs.getOrDefault(booleanAttrName, defaultValue);
        return (booleanAttrValue instanceof String) ? Boolean.parseBoolean((String) booleanAttrValue) : Boolean.TRUE.equals(booleanAttrValue);
    }

    public static FieldComponent getCopy(FieldComponent fieldComponent) {
        HashMap<String, Object> attrOriginalMap = (HashMap<String, Object>) fieldComponent.getAttrs();
        byte[] originalMapBytes = SerializationUtils.serialize(attrOriginalMap);
        HashMap<String, Object> deserializedMap = (HashMap<String, Object>) SerializationUtils.deserialize(originalMapBytes);

        List<LinkedValue> clonedLinkedValues = JsonProcessingUtil.fromJson(
                JsonProcessingUtil.toJson(fieldComponent.getLinkedValues()), new TypeReference<>() {});

        return FieldComponent.builder()
                .id(fieldComponent.getId())
                .type(fieldComponent.getType())
                .label(fieldComponent.getLabel())
                .attrs(deserializedMap)
                .value(fieldComponent.getValue())
                .required(fieldComponent.isRequired())
                .linkedValues(clonedLinkedValues)
                .arguments(new HashMap<>())
                .suggestionId(fieldComponent.getSuggestionId())
                .pronounceText(fieldComponent.getPronounceText())
                .pronounceTextType(fieldComponent.getPronounceTextType())
                .skipValidation(fieldComponent.getSkipValidation())
                .build();
    }
}
