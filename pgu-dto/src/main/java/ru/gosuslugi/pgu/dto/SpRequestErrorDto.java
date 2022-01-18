package ru.gosuslugi.pgu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.MultiValueMap;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpRequestErrorDto {
    private String cause;
    private String url;
    private String body;
    private Map<String, String> headers;
    private SpAdapterDto adapterRequestDto;
}
