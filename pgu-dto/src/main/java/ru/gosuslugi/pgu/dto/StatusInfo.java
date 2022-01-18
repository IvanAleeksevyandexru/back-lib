package ru.gosuslugi.pgu.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.Date;

/**
 * Class that contains attachement information, such as file mnemonic, upload id
 * This information is used in sp-adapter module
 * in order to link attachments with service processing & SMEV requests
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class StatusInfo {
    @EqualsAndHashCode.Include
    private Long historyId;
    private Long statusId;
    private Date date;
}
