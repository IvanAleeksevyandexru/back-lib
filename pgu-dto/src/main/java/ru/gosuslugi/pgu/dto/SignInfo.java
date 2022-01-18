package ru.gosuslugi.pgu.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.gosuslugi.pgu.dto.esep.SignedFileInfo;

import java.util.List;

/**
 * Информация о подписании заявления ЭЦП
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(description = "Информация о подписании заявления ЭЦП")
public class SignInfo {
    /** Идентификатор операции */
    @ApiModelProperty(notes = "Идентификатор операции")
    private String operationID;
    /** Ссылка на форму подписания */
    @ApiModelProperty(notes = "Ссылка на форму подписания")
    private String url;
    /** Коды доступа к файлам */
    @ApiModelProperty(notes = "Информаиция о подписанных файлах, включающая в себя коды доступа к файлам и мнемоники")
    private List<SignedFileInfo> signedFilesInfo;
    /** Признак подписи заявления */
    @ApiModelProperty(notes = "Признак подписи заявления")
    private Boolean alreadySigned = false;
}
