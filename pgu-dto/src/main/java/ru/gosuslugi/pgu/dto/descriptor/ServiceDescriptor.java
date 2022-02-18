package ru.gosuslugi.pgu.dto.descriptor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;
import ru.gosuslugi.pgu.common.core.exception.ValidationException;
import ru.gosuslugi.pgu.dto.RateLimitDescriptor;
import ru.gosuslugi.pgu.dto.descriptor.analytic.AnalyticsTag;
import ru.gosuslugi.pgu.dto.descriptor.transformation.TransformationBlock;
import ru.gosuslugi.pgu.dto.descriptor.transformation.TransformationRule;
import ru.gosuslugi.pgu.dto.descriptor.types.OrderBehaviourType;
import ru.gosuslugi.pgu.dto.descriptor.types.OrderType;
import ru.gosuslugi.pgu.dto.descriptor.value.service.ServiceIds;
import ru.gosuslugi.pgu.dto.descriptor.value.target.TargetIds;

import java.util.*;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDescriptor {
    public static final List<Long> DEFAULT_AVAILABLE_ORDER_STATUSES = Arrays.asList(0L, 14L, 15L, 25L);
    public static final String CLARIFICATIONS_ATTR = "clarifications";
    public static final String EMPOWERMENT_ID_PARAM = "empowerment";

    private String service;
    private String serviceName;

    /**
     * Описывает максимальное количество ордеров по данной услуге
     */
    private Long ordersLimit = 1L;
    private OrderBehaviourType orderBehaviourType = OrderBehaviourType.ORDER_AND_DRAFT;
    private OrderType orderType = OrderType.ORDER;
    /**
     * Init screen reference (ScreenDescriptor.getId())
     */
    private List<ScreenDescriptor> screens;
    /**
     *  Список скринов, которые доступны для доступа из внешних систем
     */
    private List<String> externalScreenIds = new ArrayList<>();
    private List<FieldComponent> applicationFields;
    private Map<String, Map<String, Object>> initScreens;
    private Map<String, List<ScreenRule>> screenRules;
    private Map<String, List<ScreenRule>> cycledScreenRules;
    /** Переопределение компонентов вспомогательных услуг (редактирование телефона, адреса элетронной почты и т.п.) */
    private Map<String, List<FieldComponent>> overrideSubServiceComponents;
    private String init;
    private Integer draftTtl;
    private Integer orderTtl;
    /**
     * В ряде услуг, черновики для которых создают внешние системы,
     * не должно предлагаться возможность начинать проходить услугу заново.
     * Для этих целей добавлена данная настройка
     */
    private Boolean alwaysContinueScenario = false;
    private ServiceIds serviceIds;
    private TargetIds targetIds;
    private String smevEnv;
    private boolean multipleOrders = true;
    private Map<String, Object> clarifications;
    private HighloadParameters highloadParameters;
    /**
     * Секция описывает ограничения на количество подаваемых заявок
     */
    private RateLimitDescriptor rateLimits;

    private Map<String, List<TransformationRule>> statusTransformationRules;

    private List<TransformationBlock> transformation;

    /**
     * Список ролей заявления с указанием ID-полей с подсказками
     * Применяется для сохранения подсказок с привязкой к пользователю по его роли
     */
    private Map<String, Set<String>> suggestionRoles;

    /**
     * Информация о записи на прием
     * Применяется для брони таймслота,
     * если во время прохождения основной услуги пользователь не посещал выполнял брони
     */
    private BookingInfo bookingInfo;


    /**
     * Префикс у applicantAnswers
     * который нужно будет удалить
     */
    private String answerServicePrefix;

    /**
     * Параметры, которые нужно скопировать в serviceParameters черновика
     * Используется для сгенерированных дескрипторов
     */
    private Map<String, String> parameters;

    /**
     * Список кодов статусов, доступных для перехода к заявлению
     */
    private List<Long> availableOrderStatuses = DEFAULT_AVAILABLE_ORDER_STATUSES;

    /**
     * Список кодов статусов при которых черновик НЕ будет сохранен при {@code orderBehaviorType == SMEV_ORDER}
     */
    private Set<Long> smevOrderStatuses = Collections.emptySet();

    /**
     * Если флаг выставлен в true - то имя топика в кафке будет сгененировано по правилу
     * service-{serviceCode}-topic
     */
    private boolean sendToServiceTopic = false;

    private List<AnalyticsTag> analyticsTags = new ArrayList<>();

    /**
     * Флаг, указывающий нужно ли проверять текущий регион пользователя с регионом в заявлении
     */
    private Boolean compareRegions = false;


    @JsonIgnore
    public Optional<FieldComponent> getFieldComponentById(String id) {
        return applicationFields.stream()
                .filter(field -> field.getId().equals(id))
                .findAny();
    }
    /**
     *  Метод проверяет что в заявлении есть только Role - Applicant
     *  и только один Stage - Applicant для него
     */
    @JsonIgnore
    public Boolean checkOnlyOneApplicantAndStage(){
        if(Objects.nonNull(initScreens) && !initScreens.isEmpty()) {
            if (initScreens.keySet().size() == 1) {
                Map<String, Object> roleToStage = initScreens.get("Applicant");
                return roleToStage != null && roleToStage.keySet().size() == 1;
            }
            return false;
        }
        return true;
    }

    @JsonIgnore
    public Optional<ScreenDescriptor> getScreenDescriptorById(String id) {
        return screens.stream().filter(sd -> sd.getId().equals(id)).findAny();
    }

    @JsonIgnore
    public List<FieldComponent> getFieldComponentsForScreen(ScreenDescriptor screenDescriptor) {
        return screenDescriptor.getComponentIds().stream()
                .map(id -> this.getFieldComponentById(id).orElseThrow(() -> new ValidationException("Cannot find fieldComponent by id " + id)))
                .collect(Collectors.toList());
    }

    @JsonIgnore
    public Boolean hasOrderCreateCustomParameter(){
        if(Objects.isNull(this.getApplicationFields())){
            return false;
        }
        return this.getApplicationFields()
                .stream()
                .filter(Objects::nonNull)
                .anyMatch(FieldComponent::getCreateOrder);
    }

    @JsonIgnore
    public Optional<String> getParameter(String key) {
        return Optional.ofNullable(parameters)
                .map(p -> p.get(key));
    }

    public List<String> getEmpowerments() {
        Optional<String> empowerment = getParameter(EMPOWERMENT_ID_PARAM);
        if (empowerment.isPresent() && StringUtils.hasText(empowerment.get())) {
            return Arrays.stream(empowerment.get().split(",")).map(String::trim).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    /**
     * Копирует дескриптор услуги
     * @param descriptor    Дескриптор
     * @return              Скопированный дескриптор
     */
    public static ServiceDescriptor getCopy(ServiceDescriptor descriptor) {
        // копируем компоненты
        List<FieldComponent> fields = descriptor.getApplicationFields().stream().map(FieldComponent::getCopy).collect(Collectors.toList());
        return ServiceDescriptor.builder()
                .service(descriptor.service)
                .serviceName(descriptor.serviceName)
                .orderBehaviourType(descriptor.orderBehaviourType)
                .screens(descriptor.screens)
                .applicationFields(fields)
                .initScreens(descriptor.initScreens)
                .screenRules(descriptor.screenRules)
                .cycledScreenRules(descriptor.cycledScreenRules)
                .init(descriptor.init)
                .draftTtl(descriptor.draftTtl)
                .alwaysContinueScenario(descriptor.alwaysContinueScenario)
                .serviceIds(descriptor.serviceIds)
                .targetIds(descriptor.targetIds)
                .multipleOrders(descriptor.multipleOrders)
                .clarifications(descriptor.clarifications)
                .statusTransformationRules(descriptor.statusTransformationRules)
                .bookingInfo(descriptor.bookingInfo)
                .answerServicePrefix(descriptor.answerServicePrefix)
                .parameters(descriptor.parameters)
                .availableOrderStatuses(descriptor.availableOrderStatuses)
                .build();
    }
}
