package com.eflix.bsn.service;

import com.eflix.bsn.dto.SalesOutboundDTO;
import com.eflix.bsn.dto.SoutboundDetailDTO;

import java.util.List;
import java.util.Map;

/**
 * =======================================================
 * 출하 의뢰서(SOutbound) 서비스 인터페이스
 * =======================================================
 * 출고와 관련된 모든 비즈니스 로직의 명세를 정의합니다.
 */
public interface SOutboundService {
    
    /**
     * 특정 회사의 전체 출하 의뢰서 목록을 조회합니다.
     * @return SalesOutboundDTO 리스트
     */
    List<SalesOutboundDTO> getOutboundList();
    
    /**
     * 특정 출하 의뢰서 번호에 해당하는 모든 상세 품목을 조회합니다.
     * @param outboundNo 조회할 출하 의뢰서 번호
     * @return SoutboundDetailDTO 리스트
     */
    List<SoutboundDetailDTO> getOutboundDetails(String outboundNo);
    
    /**
     * 새로운 출하 의뢰서를 등록합니다.
     * @param dto 등록할 출하 의뢰서 정보 (헤더 및 상세 포함)
     * @return 생성된 출하 의뢰서 번호
     */
    String createOutbound(SalesOutboundDTO dto);
    
    /**
     * 주문서 정보를 기반으로 새로운 출하 의뢰서를 생성합니다.
     * @param orderNo 출고의 기반이 될 주문서 번호
     * @return 생성된 출하 의뢰서 번호
     */
    String createOutboundFromOrder(String orderNo);

    /**
     * 기존 출하 의뢰서 정보를 수정합니다.
     * @param dto 수정할 출하 의뢰서 정보
     */
    void updateOutbound(SalesOutboundDTO dto);
    
    /**
     * 특정 출하 의뢰서를 삭제합니다.
     * @param outboundNo 삭제할 출하 의뢰서 번호
     */
    void deleteOutbound(String outboundNo);

    /**
     * 새로운 출하 의뢰서 번호를 채번합니다.
     * @return 'OUT-YYYYMMDD-XXXX' 형식의 신규 번호
     */
    String generateNextOutboundNo();

    /**
     * 대시보드용 출고 상태별 통계를 조회합니다.
     * @return 상태(status), 건수(count), 합계금액(totalAmount)을 포함하는 Map 리스트
     */
    List<Map<String, Object>> getOutboundStatusStats();
}
