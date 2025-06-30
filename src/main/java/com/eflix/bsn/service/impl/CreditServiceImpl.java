package com.eflix.bsn.service.impl;

import org.springframework.stereotype.Service;

import com.eflix.bsn.dto.CreditInfoDTO;
import com.eflix.bsn.mapper.CreditMapper;
import com.eflix.bsn.service.CreditService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreditServiceImpl implements CreditService {
    private final CreditMapper creditMapper;

    @Override
    public CreditInfoDTO getCreditByCustomer(String customerCd) {
        CreditInfoDTO dto = creditMapper.selectByCustomerCd(customerCd);
        // 남은 한도는 DTO 내부 getter 에서 계산하므로 추가 로직 필요 없음
        return dto;
    }
}
