package ru.gosuslugi.pgu.dto.kindergarten;

import ru.gosuslugi.pgu.dto.ScenarioDto;
import ru.gosuslugi.pgu.dto.descriptor.types.ComponentType;

/**
 * Описывает все возможные статус-коды, записываемые в {@link ScenarioDto#getApplicantAnswers()} в компоненте {@link ComponentType#KinderGartenDraftHandler}.
 * Коды используются для того, чтобы управлять текстовками ошибок и переходами на ошибочные экраны через screenRules в json услуг.
 */
public enum KinderGartenHandlerStatus {
    /** Ошибка СМЭВ или техническая ошибка при подготовке к отправке сообщения, а также при разборе ответа от СМЭВ. */
    SmevError,
    /** Ошибка отваливания по таймауту или по отсутствию ответа с нужным correlationId в очереди. */
    TimeoutError,
    /** Ошибка из драфт-конвертера из шаблона. */
    ConverterError,
    /** Ошибка при получении приложенных к черновику файлов по orderId. */
    TerrabyteError,
    /** Ошибка при запросе nsi-справочников. */
    NsiError
}
