package com.eflix.bsn.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eflix.bsn.dto.QuotationDTO;
import com.eflix.bsn.mapper.QuotationMapper;
import com.eflix.bsn.service.QuotationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuotationServiceImpl implements QuotationService {

    private final QuotationMapper quotationMapper;

    @Override
    public List<QuotationDTO> getQuotationList() {
        return quotationMapper.getQuotationList();
    }
}
