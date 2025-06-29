package com.eflix.bsn.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.eflix.bsn.dto.QuotationDTO;
import com.eflix.bsn.dto.QuotationDetailDTO;

/**
 * 견적서 Mapper
 */

@Mapper
public interface QuotationMapper {
    
    // 메인 테이블 삽입
    void insertQuotation(QuotationDTO q);

    //디테일 테이블 삽입
    void insertQuotationDetail(QuotationDetailDTO d);
    
    /** 가장 최근 견적서 번호 한 건을 ROWNUM으로 제한 */
    @Select(
        "SELECT QUOTATION_NO " +
        "  FROM (" +
        "        SELECT QUOTATION_NO " +
        "          FROM QUOTATION " +
        "         ORDER BY QUOTATION_NO DESC" +
        "       ) " +
        " WHERE ROWNUM = 1"
    )
    String findLastQuotationNo();


    List<QuotationDTO> getQuotationList();
}
