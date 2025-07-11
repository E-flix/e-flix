package com.eflix.bsn.service;

import com.eflix.bsn.dto.SalesOutboundDTO;
import com.eflix.bsn.dto.SoutboundDetailDTO;

import java.util.List;

public interface SOutboundService {
    
    /** 출하 의뢰서 목록 조회 */
    List<SalesOutboundDTO> getOutboundList();
    
    /** 출하 의뢰서 상세 조회 */
    List<SoutboundDetailDTO> getOutboundDetails(String outboundNo);
    
    /** 출하 의뢰서 등록 */
    String createOutbound(SalesOutboundDTO dto);
    
    /** ★ 추가: 출하번호 자동 생성 */
    String generateNextOutboundNo();
    
    /** ★ 추가: 출하 의뢰서 수정 */
    void updateOutbound(SalesOutboundDTO dto);
    
    /** ★ 추가: 출하 의뢰서 삭제 */
    void deleteOutbound(String outboundNo);
    
    /** ★ 추가: 주문서 기반 출하 의뢰서 생성 */
    String createOutboundFromOrder(String orderNo);
}