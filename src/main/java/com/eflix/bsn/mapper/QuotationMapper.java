package com.eflix.bsn.mapper;

import java.util.List;

import com.eflix.bsn.dto.QuotationDTO;

/**
 * 견적서 Mapper
 */
public interface QuotationMapper {
    List<QuotationDTO> getQuotationList();

    /**
     * 견적서를 저장한다.
     *
     * @param quotation 저장할 견적 정보
     * @return insert 결과
     */
    int insertQuotation(QuotationDTO quotation);
}
