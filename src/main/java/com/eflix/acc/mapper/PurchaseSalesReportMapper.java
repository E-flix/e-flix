package com.eflix.acc.mapper;

import java.util.List;
import java.util.Map;
import com.eflix.acc.dto.EntryMasterDTO;

/**
 * 매입매출장 Mapper
 */
public interface PurchaseSalesReportMapper {
    List<EntryMasterDTO> getReportList(Map<String, Object> params);
}
