package com.eflix.acc.mapper;

import java.util.List;
import com.eflix.acc.dto.PurchaseSalesReportDTO;

/**
 * 매입매출장 Mapper
 */
public interface PurchaseSalesReportMapper {
    List<PurchaseSalesReportDTO> getReportList(String startDate, String endDate, String type, String taxType, String electronicType, String coIdx);
}
