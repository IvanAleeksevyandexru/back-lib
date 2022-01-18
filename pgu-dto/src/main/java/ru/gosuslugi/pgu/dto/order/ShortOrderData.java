package ru.gosuslugi.pgu.dto.order;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShortOrderData {

    private Long orderId;

    private String region;

    private Date createdAt;
}
