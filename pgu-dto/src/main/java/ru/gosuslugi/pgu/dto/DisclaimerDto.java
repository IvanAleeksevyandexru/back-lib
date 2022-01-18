package ru.gosuslugi.pgu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisclaimerDto {
    Long id;
    String title;
    String message;
    DisclaimerLevel level;
    String mnemonic;
}
