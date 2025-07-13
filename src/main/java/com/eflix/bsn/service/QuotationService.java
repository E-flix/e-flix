package com.eflix.bsn.service;

import java.util.List;
import java.util.Map;

import com.eflix.bsn.dto.QuotationDTO;
import com.eflix.bsn.dto.QuotationDetailDTO;

public interface QuotationService {
    
    void createQuotation(QuotationDTO dto);

    // 견적서 번호 생성
    String generateNextQuotationNo();

    List<QuotationDTO> getQuotationList();

    List<QuotationDetailDTO> getQuotationDetails(String quotationNo);
    
    // ===== 휴지통 관련 기능 =====
    
    /** 휴지통 견적서 목록 조회 */
    List<QuotationDTO> getArchivedQuotationList();
    
    /** 만료된 견적서 자동 아카이브 */
    int archiveExpiredQuotations();
    
    /** 견적서 휴지통으로 이동 */
    boolean moveToTrash(String quotationNo);
    
    /** 견적서 복원 */
    boolean restoreQuotation(String quotationNo);
    
    /** 견적서 완전 삭제 */
    boolean deleteQuotationPermanently(String quotationNo);
    
    /** 휴지통 통계 정보 */
    Map<String, Object> getTrashStatistics();
}