// src/main/java/com/eflix/bsn/mapper/QuotationMapper.java
package com.eflix.bsn.mapper;

import java.util.List;
import com.eflix.bsn.dto.QuotationDTO;
import com.eflix.bsn.dto.QuotationDetailDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface QuotationMapper {

    /** 가장 최근 견적서 번호 한 건 조회 */
    String findLastQuotationNo();

    /** 메인 견적서 INSERT */
    void insertQuotation(QuotationDTO dto);

    /** 디테일 견적서 INSERT */
    void insertQuotationDetail(QuotationDetailDTO detail);

    /** 견적서 목록 조회 */
    List<QuotationDTO> getQuotationList();

    // 견적서 상세 조회
    List<QuotationDetailDTO> selectQuotationDetails(@Param("quotationNo") String quotationNo);
}
