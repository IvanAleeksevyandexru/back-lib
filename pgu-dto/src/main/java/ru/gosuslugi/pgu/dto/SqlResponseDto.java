package ru.gosuslugi.pgu.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * https://jira.egovdev.ru/browse/EPGUCORE-87000
 */
@Getter
@JsonIgnoreProperties(value = {"rows", "columnMeta"}, allowSetters = true)
public class SqlResponseDto {

    private final String createdAt;
    private final UUID queryId;
    private final List<MetaDto> columnMeta;
    private final List<List<String>> rows;

    public SqlResponseDto(@JsonProperty("created_at") String createdAt,
                          @JsonProperty("query_id") UUID queryId,
                          @JsonProperty("meta") List<MetaDto> columnMeta,
                          @JsonProperty("rows") List<List<String>> rows) {
        this.createdAt = createdAt;
        this.queryId = queryId;
        this.columnMeta = columnMeta;
        this.rows = rows;
    }

    @JsonInclude()
    public List<Map<String, String>> getItems() {
        var items = new ArrayList<Map<String, String>>();

        for (List<String> row: rows) {
            if (row.isEmpty()) {
                return null;
            }
            var item = new HashMap<String, String>();
            for (int i = 0; i < row.size(); i++) {
                item.put(columnMeta.get(i).getName(), row.get(i));
            }
            items.add(item);
        }

        return items;
    }

    @Getter
    static class MetaDto {
        @JsonProperty("name") private String name;
        @JsonProperty("type") private String type;
    }
}
