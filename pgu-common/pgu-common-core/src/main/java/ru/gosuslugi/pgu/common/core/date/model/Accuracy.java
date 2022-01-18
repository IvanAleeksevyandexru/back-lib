package ru.gosuslugi.pgu.common.core.date.model;

public enum Accuracy {
    YEAR("year"),
    MONTH("month"),
    DAY("day"),
    HOUR("hour"),
    MINUTE("minute"),
    SECOND("second");


    Accuracy(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }
}
