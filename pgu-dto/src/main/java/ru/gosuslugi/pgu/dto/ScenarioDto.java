package ru.gosuslugi.pgu.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.gosuslugi.pgu.dto.cycled.CycledApplicantAnswer;
import ru.gosuslugi.pgu.dto.cycled.CycledApplicantAnswerContext;
import ru.gosuslugi.pgu.dto.cycled.CycledApplicantAnswers;
import ru.gosuslugi.pgu.dto.descriptor.FieldComponent;

import java.util.*;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(description = "Информация о текущем заявлении")
public class ScenarioDto {
    /**
     * Код услуги, который по дефолту приходит из ЛК
     */
    @ApiModelProperty(notes = "Код услуги, который по дефолту приходит из ЛК")
    @Schema(description = "Код услуги, который по дефолту приходит из ЛК")
    private String serviceCode;

    /**
     * Цель услуги, которая по дефолту приходит из ЛК
     */
    @ApiModelProperty(notes = "Цель услуги, которая по дефолту приходит из ЛК")
    @Schema(description = "Цель услуги, которая по дефолту приходит из ЛК")
    private String targetCode;

    @ApiModelProperty(notes = "Id заявления главного заявителя")
    @Schema(description = "Id заявления главного заявителя")
    private Long masterOrderId;

    @ApiModelProperty(notes = "Id заявления")
    @Schema(description = "Id заявления")
    private Long orderId;

    @ApiModelProperty(notes = "Id сообщения ГЭПС", allowEmptyValue = true)
    @Schema(description = "Id сообщения ГЭПС")
    private Long gepsId;

    @ApiModelProperty(notes = "Id текущего сценария")
    @Schema(description = "Id текущего сценария")
    private Long currentScenarioId;

    /**
     * Фактический код услуги, который нужен в SP (например используется в Рег.ТС)
     */
    @ApiModelProperty(notes = "Фактический код услуги")
    @Schema(description = "Фактический код услуги")
    private String serviceId;

    /**
     * Код услуги родительского сценария
     */
    @ApiModelProperty(notes = "Код услуги родительского сценария")
    @Schema(description = "Код услуги родительского сценария")
    private String parentServiceId;

    /**
     * Идентификатор дескриптора услуги
     * Для сгенерированной услуги отличается от serviceId
     */
    @ApiModelProperty(notes = "Id дескриптора услуги. Для сгенерированной услуги отличается от serviceId")
    @Schema(description = "Id дескриптора услуги. Для сгенерированной услуги отличается от serviceId")
    private String serviceDescriptorId;

    /**
     * Фактическая цель услуги, которая нужен в SP (например используется в услуге по уходу за недееспособными)
     */
    @ApiModelProperty(notes = "Фактическая цель услуги")
    @Schema(description = "Фактическая цель услуги")
    private String targetId;

    @ApiModelProperty(notes = "текущий URL на котором находится пользователь")
    @Schema(description = "Текущий URL на котором находится пользователь")
    private String currentUrl;

    @ApiModelProperty(notes = "Список экранов которые прошел пользователь + на котором находится в данный момент")
    @Schema(description = "Список экранов которые прошел пользователь + на котором находится в данный момент")
    private LinkedList<String> finishedAndCurrentScreens = new LinkedList<>();

    /**
     * Answers to be saved for further usage in case of back button was clicked
     */
    @ApiModelProperty(notes = "Кеш ответов пользователя для каждого компонента")
    @Schema(description = "Кеш ответов пользователя для каждого компонента")
    private Map<String, ApplicantAnswer> cachedAnswers = new HashMap<>();

    /**
     * One step all data fields values
     */
    @ApiModelProperty(notes = "Ответы пользователя для каждого компонента на текущем экране")
    @Schema(description = "Ответы пользователя для каждого компонента на текущем экране")
    private Map<String, ApplicantAnswer> currentValue = new HashMap<>();

    /**
     * One step validation errors of currentValue map
     */
    @ApiModelProperty(notes = "Ошибки валидации")
    @Schema(description = "Ошибки валидации")
    private Map<String, String> errors = new HashMap<>();

    @ApiModelProperty(notes = "Ошибки валидации уникальности списковых компонентов")
    @Schema(description = "Ошибки валидации уникальности списковых компонентов")
    private List<List<Map<String, String>>> uniquenessErrors = new ArrayList<>();

    /**
     * This map contains data field id and values, that consists of value (as string) & visited flag
     * Can be optional (in case of chunk processing)
     */
    @ApiModelProperty(notes = "Ответы пользователя для каждого компонента")
    @Schema(description = "Ответы пользователя для каждого компонента")
    private Map<String, ApplicantAnswer> applicantAnswers = new HashMap<>();

    /**
     * Значения компонентов со вложенными items
     */
    @ApiModelProperty(notes = "Ответы пользователя для зацикленных блоков сценария(например информация о детях)")
    @Schema(description = "Ответы пользователя для зацикленных блоков сценария(например информация о детях)")
    private CycledApplicantAnswers cycledApplicantAnswers = new CycledApplicantAnswers();

    /**
     * Все участники заявления, исключая самого заявителя.
     */
    @ApiModelProperty(notes = "Все участники заявления, исключая самого заявителя")
    @Schema(description = "Все участники заявления, исключая самого заявителя")
    private Map<String, ApplicantDto> participants = new HashMap<>();

    @ApiModelProperty(notes = "Текущий экран для отображения пользователю")
    @Schema(description = "Текущий экран для отображения пользователю")
    private DisplayRequest display;

    /** Компоненты которые не будут отображаться на стороне фронтенда, но при этом могут исполнять бизнесс-логику */
    @ApiModelProperty(notes = "Компоненты которые не будут отображаться пользователю, но при этом могут исполнять бизнесс-логику")
    @Schema(description = "Компоненты которые не будут отображаться пользователю, но при этом могут исполнять бизнесс-логику")
    private List<FieldComponent> logicComponents;

    @JsonProperty(value = "gender", required = false)
    @ApiModelProperty(notes = "Пол пользователя")
    @Schema(description = "Пол пользователя")
    private String gender;

    @ApiModelProperty(notes = "Новый идентификатор контакта пользователя. Используется при смене номера телефона или электронной почты")
    @Schema(description = "Новый идентификатор контакта пользователя. Используется при смене номера телефона или электронной почты")
    private String newContactId;

    @ApiModelProperty(notes = "DTO с информацией о региональном ведомстве")
    @Schema(description = "DTO с информацией о региональном ведомстве")
    private ServiceInfoDto serviceInfo = new ServiceInfoDto();
    /**
     * Информация о подписании заявления ЭЦП, ключи идентификаторы заявлений, значения данные формы подписания
     */
    @ApiModelProperty(notes = "Информация о подписании заявления ЭЦП")
    @Schema(description = "Информация о подписании заявления ЭЦП")
    private Map<Long, SignInfo> signInfoMap = new HashMap<>();

    /**
     * Attachment info map
     * As a key component id is used
     * This information is required in sp-adapter module to form proper SMEV request with attachments
     * This map should be validated at the end of scenario (only used only components in applicantAnswers or currentValues should be used)
     */
    @ApiModelProperty(notes = "Файлы, приложенные к заявлению")
    @Schema(description = "Файлы, приложенные к заявлению")
    private Map<String, List<AttachmentInfo>> attachmentInfo = new HashMap<>();

    /**
     * Additional maps for extra parameters for template engine
     */
    @ApiModelProperty(notes = "Дополнительные параметры для заявления, заполняются в конце сценария")
    @Schema(description = "Дополнительные параметры для заявления, заполняются в конце сценария")
    private Map<String, String> additionalParameters = new HashMap<>();

    /** Набор файлов из терабайта для склейки в один pdf. */
    @ApiModelProperty(notes = "Набор файлов из терабайта для склейки в один pdf")
    @Schema(description = "Набор файлов из терабайта для склейки в один pdf")
    private Set<PdfFilePackage> packageToPdf;

    /**
     * Дополнительные параметры из service descriptor
     */
    @ApiModelProperty(notes = "Дополнительные параметры услуги")
    @Schema(description = "Дополнительные параметры услуги")
    private Map<String, String> serviceParameters = new HashMap<>();

    /**
     * Generated and uploaded xml and pdf files.
     * It is needed to prevent duplicate files in terrabyte
     */
    @ApiModelProperty(notes = "Сгенерированные и загруженные в Терабайт фаилы")
    @Schema(description = "Сгенерированные и загруженные в Терабайт фаилы")
    private Set<String> generatedFiles = new HashSet<>();

    /**
     * Список обработанных статусов
     */
    @ApiModelProperty(notes = "Список обработанных статусов")
    @Schema(description = "Список обработанных статусов")
    private List<StatusInfo> statuses;

    /**
     * Список дисклеймеров с уровнем ниже CRITICAL
     */
    @ApiModelProperty(notes = "Список дисклеймеров")
    @Schema(description = "Список дисклеймеров")
    private List<DisclaimerDto> disclaimers = new ArrayList<>();

    public Set<String> getGeneratedFiles() {
        if (this.generatedFiles == null) {
            this.generatedFiles = new HashSet<>();
        }
        return this.generatedFiles;
    }

    /**
     * Add to cached answers request data field ids and values
     */
    public void addToCachedAnswers(Map<String, ApplicantAnswer> cachedAnswers) {
        if(this.cachedAnswers == null) {
            this.cachedAnswers = new HashMap<>();
        }
        this.cachedAnswers.putAll(cachedAnswers);
    }

    public ApplicantAnswer getApplicantAnswerByFieldId(String fieldId) {
        return applicantAnswers.get(fieldId);
    }

    public String getServiceDescriptorId() {
        return Optional.ofNullable(serviceDescriptorId).orElse(serviceCode);
    }

    public String getTargetId() {
        return targetId;
    }


    /**
     * возвращает контекст текущего цилклического ответа
     * @return non-null CycledApplicantAnswerContext instance
     */
    public CycledApplicantAnswerContext getCycledApplicantAnswerContext() {
        CycledApplicantAnswerContext cycledApplicantAnswerContext = new CycledApplicantAnswerContext();
        cycledApplicantAnswerContext.setCycledApplicantAnswerItem(
                Optional.ofNullable(getCycledApplicantAnswers())
                        .map(CycledApplicantAnswers::getCurrentAnswer)
                        .map(CycledApplicantAnswer::getCurrentAnswerItem)
                        .orElse(null)
        );
        return cycledApplicantAnswerContext;
    }
}
