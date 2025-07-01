package com.eflix.bsn.service;

import java.util.List;

import com.eflix.bsn.dto.QuotationDTO;
import com.eflix.bsn.dto.QuotationDetailDTO;

public interface QuotationService {
    
    void createQuotation(QuotationDTO dto);

    // 견적서 번호 생성
    String generateNextQuotationNo();

    List<QuotationDTO> getQuotationList();

    List<QuotationDetailDTO> getQuotationDetails(String quotationNo);
}
