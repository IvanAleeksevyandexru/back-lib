package ru.gosuslugi.pgu.dto.descriptor.types;

public enum OrderBehaviourType {
    /**
     * При такой настройке услуги не создается ордер и не сохраняется черновик
     */
    NO_ORDER,
    /**
     * дефолтная настройка - и драфт и ордер существуют
     */
    ORDER_AND_DRAFT,
    /**
     * Создается только ордер в ЛК, черновик не сохраняется
     */
    ONLY_ORDER,
    /**
     * И ордер и черновик создаются только в момент перед отправкой в SP-adapter
     */
    ORDER_AND_DRAFT_BEFORE_SEND,
    /**
     * Ордер и черновик создаются при прохождении услуги
     * При открытии заявления по ордеру который не в статусе драфта - начинаем услугу заново, в процессе прохождения черновик не сохраняем
     */
    SMEV_ORDER;

    public static Boolean canDraftExists(OrderBehaviourType orderBehaviourType){
        return ORDER_AND_DRAFT_BEFORE_SEND.equals(orderBehaviourType) || ORDER_AND_DRAFT.equals(orderBehaviourType)
                || SMEV_ORDER.equals(orderBehaviourType);
    }
}
