package ru.gosuslugi.pgu.dto.action;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ActionResponseDto {

    Map<String,Object> responseData = new HashMap<>();

    Boolean result;

    List<String> errorList = new ArrayList<>();
}
