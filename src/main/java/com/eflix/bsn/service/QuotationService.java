package com.eflix.bsn.service;

import java.util.List;

import com.eflix.bsn.dto.QuotationDTO;

public interface QuotationService {
    List<QuotationDTO> getQuotationList();

    /**
     * 새 견적서를 등록한다.
     *
     * @param quotation 등록할 견적 정보
     * @return 등록 건수
     */
    int insertQuotation(QuotationDTO quotation);
}
