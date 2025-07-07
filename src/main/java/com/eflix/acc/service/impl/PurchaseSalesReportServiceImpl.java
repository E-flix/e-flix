package com.eflix.acc.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import com.eflix.acc.dto.PurchaseSalesReportDTO;
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
    public List<PurchaseSalesReportDTO> getReportList(String startDate, String endDate, String type, String taxType, String electronicType) {
        String coIdx = authContext.getCoIdx();
        return purchaseSalesReportMapper.getReportList(startDate, endDate, type, taxType, electronicType, coIdx);
    }
}
