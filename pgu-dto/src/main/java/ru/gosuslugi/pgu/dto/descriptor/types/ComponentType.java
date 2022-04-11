package ru.gosuslugi.pgu.dto.descriptor.types;

/**
 * ApplicationFields types
 * TODO add links to figma or confluence to keep track of components
 */
public enum ComponentType {
    //Welcome screen deprecated
    WelcomeScr,
    QuestionScr,
    //Service descriptor screen deprecated
    ServiceDscrScr,
    ConfirmPersonalUserData,
    ConfirmPersonalUserPhone,
    ConfirmPersonalUserEmail,
    ConfirmPersonalUserRegAddr,
    ConfirmPersonalUserRegReadOnlyAddr,
    ConfirmPersonalUserRegAddrChange,
    LabelSection,
    Dictionary,
    GDictionary,
    ForeignCitizenship,
    StringInput,
    GStringInput,
    DateInput,
    CalendarInput,
    RadioButtonInput,

    ChildrenList,
    RegistrationAddr,
    RegistrationLegalAddr,
    RegistrationLegalAddrReadOnly,
    DocInput,
    /** Компонент для ввода и проверки номеров карт алгоритмом Луна. */
    CardNumberInput,
    ChangeList,
    UserList,
    FileUploadComponent,
    PhotoUploadComponent,
    PhotoEditorComponent,
    //Info screen that contain markup label and possible image link
    InfoScr,
    GInfoScr,
    //Possible should be combined with InfoScr
    ConfirmCreatedRequestScreen,
    //Possible should be combined with InfoScr
    NotificationAboutAbsentAccountScreen,

    /**
     * Redirect to main gosuslugi page or welcome screen (requires EMPTY screen type)
     */
    Redirect,
    RadioButton,
    GenderSelector,

    /**
     * Component for searching for gosuslugi's account by passport
     * (requires COMPONENT screen type)
     */
    PassportLookup,

    /**
     * Данные о ТС
     */
    CarInfo,
    CarDetailInfo,
    /**
     * Данные о транспортном средстве владельца
     */
    CarOwnerInfo,
    /**
     * Список ТС
     */
    CarList,

    /**
     * Select country for 'spravka o sudimosti' service
     */
    CountryApostil,


    /**
     * ApplicationField that has several labels according to applicant's gender
     * this applicationField translates into QuestionScr with one selected label
     */
    GQuestionScr,

    /**
     * ApplicationField that has several labels according to applicant's gender
     * this applicationField translates into LabelSection with one selected label
     */
    GLabelSection,

    /**
     * Component that is used in sudimost.json
     * TODO add util class
     */
    MvdGiac,



    /**
     * TODO might be deleted
     */
    LastFIO,

    /**
     * TODO might be deleted
     */
    LastRegion,

    /**
     * zagran
     */
    ConfirmData,
    /**
     * zagran
     */
    ConfirmPhone,

    /**
     * Universal  component to show list of simple application fields
     */
    RepeatableFields,
    /**
     * {"id": "rb1", "type": "RadioInput", "label": "Я училась", "attrs": {"supportedValues":  [{"label": "обучение", "value": "0"}, {"label": "работа", "value": "1"}]}, "value": "", "visited": false},
     */
    RadioInput,
    GLookup,
    Lookup,
    RestLookup,
    CheckBox,
    CheckBoxList,
    DropDown,
    SelectFromList,
    TextArea,

    EmployeeHistory,
    /**
     * Выбор учреждения на карте
     */
    MapService,
    KindergartenMapService,
    BarbarbokMapService,
    ConfirmBirthUserData,
    HiddenLookup,
    HiddenInput,
    email,
    header,
    SignAppLink,
    SnilsInput,
    UnusedPayments,

    PaymentScr,
    /**
     * Dictionary component that is hidden by default, and do actions according some other dictionary value
     * Example of json usage:
     * {“id”: “fai30”, “type”: “HiddenDictionary”, “label”: “Регионы“, “attrs”: {“dictionaryType”: “STRANI_IST”, “ref”: [{relatedRel: ‘fai12’, val: ‘RUS’, relation: ‘displayOn’}]},  “value”: “”, “visited”: false};
     */
    HiddenDictionary,

    /**
     * Выбор слота для записи в учреждение
     */
    TimeSlot,
    TimeSlotWithComputableDepartment,
    TimeSlotDoctor,
    /**
     * Календарный интервал с указанием времени
     */
    DateTimePeriod,
    DatePeriod,
    ChildRepresentative,

    /**
     * Выбор места проживания с указанием дат периода "с и по
     */
    AddressInput,
    InvitationError,

    /**
     * https://jira.egovdev.ru/browse/EPGUCORE-50619: приглашение по email, даже если человек не в ESIA
     */
    LkInvitationInput,

    /**
     * Поле изменения номера телефона в профиле пользователя
     */
    PhoneNumberChangeInput,

    /**
     * Компонент подтверждения номера телефона через код
     */
    PhoneNumberConfirmCodeInput,
    ConfirmCodeInput,

    ConfirmNewEmail,
    ConfirmLegalNewEmail,
    NewEmailInput,
    NewLegalEmailInput,
    Disclaimer,
    HtmlString,
    PersonInnInput,
    PersonUserInn,
    LegalInnInput,
    OgrnInput,
    OgrnipInput,
    ConfirmAnotherUserData,
    /**
     * Компонент Таймер для бронирования (Загсов).
     * Пример json:
     * <pre>
     * {
     *    "id":"pd9",
     *    "type":"Timer",
     *    "label":"На оплату пошлины для подтверждения бронирования осталось:",
     *    "attrs":{
     *       "place":{
     *          "label":"Место регистрации:",
     *          "type":"ref",
     *          "value":""
     *       },
     *       "address":{
     *          "label":"Адрес места регистрации:",
     *          "type":"ref",
     *          "value":""
     *       },
     *       "time":{
     *          "label":"Выбранное время:",
     *          "type":"ref",
     *          "value": ""
     *       },
     *       "timer": {
     *          "start": {
     *            "type":"ref",
     *            "value": ""
     *          },
     *          "finish": {
     *            "type":"ref",
     *            "value": ""
     *          }
     *       }
     *    },
     *    "value":"",
     *    "visited":false
     * }</pre>
     */
    Timer,

    Snippet,

    /**
     * Компонет для подписания xml и pdf файлов
     */
    EsepSign,

    /**
     * Согласие на расторжение брака
     */
    DivorceConsent,

    /**
     * Получение свидетельства о браке и разводе из ЛК
     */
    MaritalStatusInput,

    /**
     * Выбор города
     */
    CityInput,
    ConfirmChildData,
    /**
     * Выбор маски для StringInput (компонент-хелпер)
     */
    MaskDropDown,
    BillInfo,
    CreateBill,
    PaymentTypeSelector,
    FieldList,
    GFieldList,
    InvoiceScr,
    /**
     * Отображение подразделений в зависимости от выбранного города
     */
    CityDepartment,
    InformationCenterPfr,
    InformationCenterPfrSop,
    InformationCenterFss,

    /**
     * Компонент по отображению блока персонализации детей
     */
    PersonInfo,
    ConfirmLegalPhone,
    ConfirmLegalEmail,
    ConfirmLegalData,

    /**
     * Компонент по отображению информации под заголовком экрана
     */
    CycledInfo,

    /**
     * Список с возможностью множественного выбора
     * @see <a href="https://confluence.egovdev.ru/pages/viewpage.action?pageId=184951863">Список с возможностью множественного выбора</a>
     */
    MultipleChoiceDictionary,
    GMultipleChoiceDictionary,

    /**
     * Компонент выплат по материнскому капиталу
     */
    MatPeriod,

    /**
     * Компонент cправочника подразделений с фильтрацией и подгрузкой
     */
    DropDownDepts,

    /** <a href="https://jira.egovdev.ru/browse/EPGUCORE-49518">Квадрат чекбоксов</a> */
    CheckboxCube,

    ConfirmUserCorpEmail,
    ConfirmUserCorpPhone,
    /** Расчетный счет */
    CheckingAccount,
    /** Компонент запроса внешних данных */
    RestCall,
    BackRestCall,
    BarbarbokRestCall,
    /** Компонент для вычисления значений через calc, ref, json logic */
    ValueCalculator,
    /** Компонент загрузки внешних файлов в storageService (ака террабайт) */
    FileLoad,
    /** Компонент обработки файлов */
    OrderFileProcessingComponent,
    AttachmentContent,

    /** Компонент для получения информации из ГЭПС */
    GepsData,

    /** Компонент для получения мед свидетельств */
    MedicalBirthCertificates,
    /** Компонент для получения информации по мед. направлениям */
    MedicalReferrals,
    /** Компонент для получения информации мед. должностям */
    MedicalInfoDropDown,
    /** Компонент для выбора детского кружка */
    ChildrenClubs, /* Depricated: удалить, когда завершится миграция с моно-компонента на самостоятельные unique-компоненты */
    ProgramList,
    ProgramView,
    GroupList,

    /** Компонент поиска направления по введенному номеру */
    ReferralNumber,
    MonthPicker,

    /** Компонент - наследник DropDown. Используется для разделения логики и будущих улучшений - чтобы не нужно было делать рефакторинг. */
    SearchableDropDown,

    /**
     * Новый компонент способов оплаты, используется в кружках.
     * Способы оплаты являются динамическими, рисуются в зависимости от того, что пришло в ответе компонента.
     * @see <a href="https://confluence.egovdev.ru/pages/viewpage.action?pageId=193308710">Компонент "Выбор способа оплаты"</a>
     * @see <a href="https://jira.egovdev.ru/browse/EPGUCORE-56734">Jira</a>
     **/
    PaymentWay,

    /** Компонент для получения информации о стоимости кружка */
    EaisdoGroupCost,

    /**
     * Сертификат дополнительного профессионального образования
     */
    CertificateEaisdo,


    /*
     *   Компонент, осуществляющий отправку контрольного кода на e-mail пользователя, и вызывающий метод бэкенда,
     *   служащий для ввода кода, проверки его корректности, осуществления повторной отправки,
     */
    ConfirmEmailCodeInput,

    /* Компонент, осуществляющий отправку приложенного фото в сервис идентификации */
    IdentificationUploadComponent,

    /* Компонент, осуществляющий отправку фото полученного из видеопотока в сервис идентификации */
    IdentificationStreamComponent,

    /**
     * Компонент "Выбор оборудования" осуществляет последовательные rest-запросы к nsi-справочникам,
     * подставляет атрибуты в последующие запросы в '${}', копирует поэлементно атрибуты предыдущего ответа в следующий.
     * В случае нескольких элементов, в следующий запрос динамически подставляется фильтр union с OR unionKind вместо simple.
     * @see <a href="https://confluence.egovdev.ru/pages/viewpage.action?pageId=193312111">Компонент "Выбор оборудования"</a>
     */
    EquipmentChoice,

    /**
     * Компонент подтверждения полиса ОМС пользователя
     */
    ConfirmPersonalPolicy,
    /**
     * Компонент редактирования полиса ОМС пользователя
     */
    ConfirmPersonalPolicyChange,

    /** Логический компонент для отправки сообщений в аналитический кластер*/
    AnalyticNotifier,

    /** Компонент, который будет работать с многоуровневым справочником */
    ComplexChoiceDictionary,

    /** Компонент, который будет возвращать id сессии и записывать его в ApplicantAnswers */
    SessionId,

    /**
     * Компонент, получающий данные из СМЭВ по orderId черновика. Используется для запроса данных сертификата финансирования дополнительного образования.
     * @see <a href="https://confluence.egovdev.ru/pages/viewpage.action?pageId=196587005">Компонент "Информер загрузки данных из ВС"</a>
     */
    KinderGartenDraftHandler

}
