package com.eflix.acc.service;

import java.util.List;
import com.eflix.acc.dto.PurchaseSalesReportDTO;

/**
 * 매입매출장 Service
 */
public interface PurchaseSalesReportService {
    List<PurchaseSalesReportDTO> getReportList(String startDate, String endDate, String type, String taxType, String electronicType);
}
