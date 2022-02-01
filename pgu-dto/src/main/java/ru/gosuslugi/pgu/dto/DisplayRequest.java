package ru.gosuslugi.pgu.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;
import ru.gosuslugi.pgu.dto.descriptor.ActionConfirmation;
import ru.gosuslugi.pgu.dto.descriptor.FieldComponent;
import ru.gosuslugi.pgu.dto.descriptor.LinkedValue;
import ru.gosuslugi.pgu.dto.descriptor.ScreenDescriptor;
import ru.gosuslugi.pgu.dto.descriptor.suggestion.SuggestionGroup;
import ru.gosuslugi.pgu.dto.descriptor.types.ScreenButton;
import ru.gosuslugi.pgu.dto.descriptor.types.ScreenType;
import ru.gosuslugi.pgu.dto.descriptor.types.SubHeader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * DTO that is used for frontend form player
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ToString
public class DisplayRequest {
    private String id;
    private String name;
    private ScreenType type;
    private String header;
    private SubHeader subHeader;
    private List<FieldComponent> components;
    private List<ScreenButton> buttons;
    private SuggestionGroup suggestion;
    private Map<String, ActionConfirmation> confirmations;
    private boolean hideBackButton;
    private String pronounceText;
    private String pronounceTextType;

    /** Список инфорационных компонентов, выводятся, как правило, под заголовком */
    private List<FieldComponent> infoComponents;

    private List<FieldComponent> logicAfterValidationComponents;

    @JsonIgnore
    private boolean isTerminal;
    @JsonIgnore
    private boolean notSendToSp;
    @JsonIgnore
    private boolean forceSendToSuggestions;
    @JsonIgnore
    private boolean needToUpdateAdditionalParameters;
    @JsonIgnore
    private boolean isAccepted;
    @JsonIgnore
    private boolean isFirstScreen;
    @JsonIgnore
    private boolean isImpasse;
    @JsonIgnore
    private boolean forceDeliriumCall;

    @JsonIgnore
    private Map<String, Object> attrs;

    @JsonIgnore
    private List<LinkedValue> linkedValues;

    private Map<String, String> arguments = new HashMap<>();

    private String cssClass;

    public DisplayRequest() {}

    public DisplayRequest(ScreenDescriptor screenDescriptor, List<FieldComponent> components) {
        this.id = screenDescriptor.getId();
        this.name = screenDescriptor.getName();
        this.type = screenDescriptor.getType();
        this.header = screenDescriptor.getHeader();
        this.subHeader = screenDescriptor.getSubHeader();
        this.isTerminal = Boolean.TRUE.equals(screenDescriptor.getIsTerminal());
        this.needToUpdateAdditionalParameters = Boolean.TRUE.equals(screenDescriptor.getNeedToUpdateAdditionalParameters());
        // по умолчанию true
        this.isAccepted = screenDescriptor.getIsAccepted() == null || screenDescriptor.getIsAccepted();
        // default false
        this.notSendToSp = screenDescriptor.getNotSendToSp() != null && screenDescriptor.getNotSendToSp();
        this.forceDeliriumCall = screenDescriptor.getForceDeliriumCall() != null && screenDescriptor.getForceDeliriumCall();

        this.cssClass = screenDescriptor.getCssClass();
        this.isFirstScreen = Boolean.TRUE.equals(screenDescriptor.getIsFirstScreen());
        this.isImpasse = Boolean.TRUE.equals(screenDescriptor.getIsImpasse());
        this.forceSendToSuggestions = Boolean.TRUE.equals(screenDescriptor.getForceSendToSuggestions());
        this.hideBackButton = Boolean.TRUE.equals(screenDescriptor.getHideBackButton());
        this.buttons = screenDescriptor.getButtons();
        this.attrs = screenDescriptor.getAttrs();
        this.suggestion = screenDescriptor.getSuggestion();

        this.confirmations = screenDescriptor.getConfirmations();
        this.pronounceText = screenDescriptor.getPronounceText();
        this.pronounceTextType = screenDescriptor.getPronounceTextType();
        this.linkedValues = screenDescriptor.getLinkedValues();
        if(Objects.nonNull(components)){
            this.components = components
                    .stream()
                    .filter(c -> screenDescriptor.getComponentIds().contains(c.getId()) && !screenDescriptor.getInfoComponents().contains(c.getId()))
                    .collect(Collectors.toList());
            this.infoComponents =  components
                    .stream()
                    .filter(c -> screenDescriptor.getInfoComponents().contains(c.getId()))
                    .collect(Collectors.toList());
            this.logicAfterValidationComponents =  components
                    .stream()
                    .filter(c -> screenDescriptor.getLogicAfterValidationComponentIds().contains(c.getId())
                            && !screenDescriptor.getInfoComponents().contains(c.getId())
                            && !screenDescriptor.getComponentIds().contains(c.getId())
                    )
                    .collect(Collectors.toList());
        }
    }

    @JsonIgnore
    public Optional<FieldComponent> findFieldByComponentId(final String componentId) {
        if (Objects.isNull(components) || components.isEmpty()) return Optional.empty();
        return components.stream().filter(c -> c.getId().equals(componentId)).findFirst();
    }

    public void addArgument(String argumentName, String argumentValue) {
        if (argumentValue == null) return;
        if (arguments == null) {
            arguments = new HashMap<>();
        }
        arguments.put(argumentName, argumentValue);
    }

    public String getArgument(String key) {
        if (arguments == null) {
            arguments = new HashMap<>();
        }
        return arguments.getOrDefault(key, "");
    }
}
