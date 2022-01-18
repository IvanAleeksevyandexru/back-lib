package ru.gosuslugi.pgu.dto.descriptor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.atc.idecs.refregistry.ws.ListRefItemsRequest;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComputeItem {

    private ComputeItemType type;
    private String value;
    private ListRefItemsRequest nsiRequest;
    private String result;

    public enum ComputeItemType {
        NSI
    }
}