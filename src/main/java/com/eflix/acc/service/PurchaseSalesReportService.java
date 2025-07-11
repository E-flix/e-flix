package com.eflix.acc.service;

import java.util.List;
import java.util.Map;
import com.eflix.acc.dto.EntryMasterDTO;

/**
 * 매입매출장 Service
 */
public interface PurchaseSalesReportService {
    List<EntryMasterDTO> getReportList(Map<String, Object> params);
}
