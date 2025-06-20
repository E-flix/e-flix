package com.eflix.bsn.mapper;

import java.util.List;

import com.eflix.bsn.dto.QuotationDTO;

/**
 * 견적서 Mapper
 */
public interface QuotationMapper {
    List<QuotationDTO> getQuotationList();
}
