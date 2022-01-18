package ru.gosuslugi.pgu.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ApplicantAnswerItem extends ApplicantAnswer {
    private Integer index;
}
