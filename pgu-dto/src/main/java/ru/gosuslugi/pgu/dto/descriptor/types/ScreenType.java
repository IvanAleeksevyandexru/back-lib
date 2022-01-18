package ru.gosuslugi.pgu.dto.descriptor.types;

/**
 * Типы поддерживаемых экранов
 * Custom - состоит из ряда простых компонентов, которые могут быть сгруппированы на одном экране
 * Component - экран, состоящий из одного компонента с заранее прописанной логикой, например, страницы с опросом пользоваля
 */
public enum ScreenType {
    /**
     * Screen that can have many application field in components attribute
     * always simple application field, without actions and preset procedures
     */
    CUSTOM ("Custom"),

    /**
     * Screen that contains only one complex application field,
     * that supports:
     *      preset,
     *      complex validation process (including interaction with other systems)
     *      custom actions
     */
    COMPONENT ("Component"),

    /**
     * Screen that contains QuestionScr
     * Possible answers are set using actions
     */
    QUESTION ("Question"),

    /**
     * Test used for children list component draft
     */
    UNIQUE("Unique"),
    /**
     * Provides a gender support for the {@link #UNIQUE} screen.
     */
    GUNIQUE("Gender Unique"),

    /**
     * Common screen that contains label image
     * includes welcome screen, finish, decline screns
     */
    INFO("Info"),

    /**
     * Screen used for redirect mechanism
     */
    EMPTY("Empty"),

    INVITATION_ERROR("Invitation error"),

    GCUSTOM("Gender Custom"),
    GCOMPONENT("Gender Component"),
    GINFO("Gender Info"),
    GQUESTION("Gender Question"),
    TSSCREEN("Ts Screen"),

    /**
     * Screen for Repeatable fields
     */
    REPEATABLE ("Repeatable"),
    GREPEATABLE ("Gender Repeatable"),
    APP("Mini application screen")

    ;

    String name;
    ScreenType(String name) {
        this.name = name;
    }
}
