package ru.gosuslugi.pgu.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class TimeSlotDoctorInput {
    private Long orderId;
    private String eserviceId;
    private String serviceId;
    private String serviceCode;
    private String department;
    private List<TimeSlotRequestAttr> timeSlotRequestAttrs;
    private List<TimeSlotRequestAttr> bookingRequestAttrs;
    private String organizationId;
    private String bookAttributes;
    private String userSelectedRegion;
}
