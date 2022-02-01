package ru.gosuslugi.pgu.sp.adapter;

import ru.gosuslugi.pgu.common.esia.search.dto.UserPersonalData;
import ru.gosuslugi.pgu.dto.SmevRequestDto;
import javax.annotation.Nonnull;

public interface SpAdapterClient {

    SmevRequestDto createXmlAndPdf(Long orderId, Long userId, Long orgId, String requestGuid);

    byte[] getTsReportPdf(Long orderId, @Nonnull UserPersonalData userPersonalData);
}