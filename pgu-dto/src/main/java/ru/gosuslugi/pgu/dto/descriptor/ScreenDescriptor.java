package ru.gosuslugi.pgu.dto.descriptor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.gosuslugi.pgu.dto.descriptor.suggestion.SuggestionGroup;
import ru.gosuslugi.pgu.dto.descriptor.types.ScreenButton;
import ru.gosuslugi.pgu.dto.descriptor.types.ScreenType;
import ru.gosuslugi.pgu.dto.descriptor.types.SubHeader;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class describing screen to be displayed
 * Supported screen types: Custom, Component, Question
 * Screen also contains info about header, label for next screen transition button, and an array of components on it
 * Компоненты отрисовываются в том же порядке, что и представлены в массиве. Предполагается, что один элемент занимает одну строчку (width: 100%)
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScreenDescriptor {
    private String id;
    /**
     * Name that will be used on gui to configure service
     */
    private String name;

    /** Custom (means has array of components to show on one page) or single screen component */
    private ScreenType type;

    /**
     * Top label with  page description
     */
    private String header;
    private SubHeader subHeader;

    /**
     * Field components ids (FieldComponent.getId())
     */
    @JsonProperty("components")
    private List<String> componentIds;

    @JsonProperty("logicComponents")
    private List<String> logicComponentIds;

    @JsonProperty("afterOrderCreatedComponents")
    private List<String> afterOrderCreatedComponentIds;

    /** Specifies that additional integration steps should be made, e.g. posting to Delirium */
    private Boolean isTerminal;

    /** флаг не показывать кнопку назад, при этом не являясь финальным экраном */
    private Boolean hideBackButton;

    /** pecifies that additional integration steps should be ignored */
    private Boolean ignoreSmevSend;

    /**
     * Specifies that Delirium should be called even if order does not contain participants
     * Works only with terminal screens
     */
    private Boolean forceDeliriumCall;

    /** if isTerminal, then check if need sent to Service Processing service */
    private Boolean notSendToSp;

    /** if need to force filling additional parameters on this screen  */
    private Boolean needToUpdateAdditionalParameters;

    /** Delirium accept status */
    private Boolean isAccepted;

    /** CSS classes for the screen*/
    private String cssClass;

    /** Specifies to return to main scenario from internal scenarios*/
    private Boolean isFirstScreen;

    /** if isImpasse - delete order draft */
    private Boolean isImpasse;

    /** Отправляет черновик в микросервис саджестов для обработки
     * P.S. Использовать только для услуг, у которых нет сохранения черновика.
     */
    private Boolean forceSendToSuggestions;

    /** Specifies conditions for skipping screen with setting some applicationAnswer */
    private Set<RuleCondition> skipConditions;

    /** Список компонентов на экране, для которых будет выполнен метод preload */
    private List<String> preloadComponents;

    /** Specifies whether need to check draft stage and return to init screen in case when stage was changed */
    private Boolean changeStage;

    /** Аналогично changeStage. Предварительно сохраняет черновик, чтобы использовать его при проверке. */
    private Boolean changeStageWithDraft;

    /** Stages that trigger init screen logic.  */
    private List<String> initStages = new LinkedList<>();

    /** Info components, displayed under the screen header */
    private List<String> infoComponents = new LinkedList<>();

    private List<ScreenButton> buttons;

    private Map<String, Object> attrs;

    /** Индентификатор группы для сохранения полей срина в Suggestion Service  */
    private SuggestionGroup suggestion;

    /** Modal windows for confirm continue, displayed over form */
    private Map<String, ActionConfirmation> confirmations;

    /** Поля вычисляемые на экране, будут доступны в виде ответов */
    private Map<String, ComputeItem> computedAnswers;

    private String pronounceText;
    private String pronounceTextType;

    /**
     * Mapping instructions for component arguments
     */
    private List<LinkedValue> linkedValues;

    @JsonIgnore
    public ScreenDescriptor getCopy() {
        return ScreenDescriptor.builder()
                .id(id)
                .name(name)
                .type(type)
                .header(header)
                .subHeader(subHeader)
                .componentIds(componentIds)
                .logicComponentIds(logicComponentIds)
                .afterOrderCreatedComponentIds(afterOrderCreatedComponentIds)
                .isTerminal(isTerminal)
                .hideBackButton(hideBackButton)
                .ignoreSmevSend(ignoreSmevSend)
                .forceDeliriumCall(forceDeliriumCall)
                .notSendToSp(notSendToSp)
                .needToUpdateAdditionalParameters(needToUpdateAdditionalParameters)
                .isAccepted(isAccepted)
                .cssClass(cssClass)
                .isFirstScreen(isFirstScreen)
                .isImpasse(isImpasse)
                .forceSendToSuggestions(forceSendToSuggestions)
                .skipConditions(skipConditions)
                .preloadComponents(preloadComponents)
                .changeStage(changeStage)
                .changeStageWithDraft(changeStageWithDraft)
                .initStages(initStages)
                .infoComponents(infoComponents)
                .buttons(buttons)
                .attrs(attrs)
                .suggestion(suggestion)
                .confirmations(confirmations)
                .computedAnswers(computedAnswers)
                .pronounceText(pronounceText)
                .pronounceTextType(pronounceTextType)
                .linkedValues(linkedValues)
                .build();
    }
}
