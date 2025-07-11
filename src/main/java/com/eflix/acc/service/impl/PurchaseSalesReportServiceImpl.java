package com.eflix.acc.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.eflix.acc.dto.EntryMasterDTO;
import com.eflix.acc.mapper.PurchaseSalesReportMapper;
import com.eflix.acc.service.PurchaseSalesReportService;
import com.eflix.common.security.auth.AuthContext;
import lombok.RequiredArgsConstructor;

/**
 * 매입매출장 ServiceImpl
 */
@Service
@RequiredArgsConstructor
public class PurchaseSalesReportServiceImpl implements PurchaseSalesReportService {
    private final PurchaseSalesReportMapper purchaseSalesReportMapper;
    private final AuthContext authContext;

    @Override
    public List<EntryMasterDTO> getReportList(Map<String, Object> params) {
        params.put("coIdx", authContext.getCoIdx());
        return purchaseSalesReportMapper.getReportList(params);
    }
}
