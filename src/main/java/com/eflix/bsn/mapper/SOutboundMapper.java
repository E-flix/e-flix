// ============ SOutboundMapper.java ============
package com.eflix.bsn.mapper;

import com.eflix.bsn.dto.SalesOutboundDTO;
import com.eflix.bsn.dto.SoutboundDetailDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface SOutboundMapper {
    
    /** 출하 의뢰서 목록 조회 */
    List<SalesOutboundDTO> selectAllOutbound(@Param("coIdx") String coIdx);
    
    /** 출하 의뢰서 상세 조회 */
    List<SoutboundDetailDTO> selectOutboundDetails(@Param("outboundNo") String outboundNo);
    
    /** 출하 의뢰서 헤더 INSERT */
    int insertOutbound(SalesOutboundDTO dto);
    
    /** 출하 의뢰서 상세 INSERT */
    int insertOutboundDetail(SoutboundDetailDTO dto);
    
    /** ★ 출하 의뢰서 상세 일괄 INSERT */
    int insertOutboundDetailBatch(@Param("list") List<SoutboundDetailDTO> list);
    
    /** ★ 출하번호 자동 생성 */
    String selectNextOutboundNo(@Param("coIdx") String coIdx);
    
    /** ★ 출하 의뢰서 헤더 UPDATE */
    int updateOutbound(SalesOutboundDTO dto);
    
    /** ★ 출하 의뢰서 상세 UPDATE (OUTBOUND_STATUS 포함) */
    int updateOutboundDetail(SoutboundDetailDTO dto);
    
    /** ★ 출하 의뢰서 삭제 */
    int deleteOutbound(@Param("outboundNo") String outboundNo, @Param("coIdx") String coIdx);
    
    /** ★ 주문서 정보 조회 (출하 의뢰서 생성용) */
    SalesOutboundDTO selectOrderForOutbound(@Param("orderNo") String orderNo, @Param("coIdx") String coIdx);
    
    /** ★ 주문서 상세 정보 조회 (출하 의뢰서 생성용) */
    List<SoutboundDetailDTO> selectOrderDetailsForOutbound(@Param("orderNo") String orderNo, @Param("coIdx") String coIdx);
    
    /** ★ 출하 상태별 통계 조회 (대시보드용) */
    List<Map<String, Object>> selectOutboundStatusStats(@Param("coIdx") String coIdx);
    
    /** ★ 출하 지연 현황 조회 (3일 이상 대기 상태) */
    List<Map<String, Object>> selectDelayedOutbounds(@Param("coIdx") String coIdx);
    
    /** ★ 특정 출하 의뢰서의 전체 상태 현황 조회 */
    List<Map<String, Object>> selectOutboundDetailStatusSummary(@Param("outboundNo") String outboundNo);
    
    /** ★ 거래처별 출하 현황 조회 */
    List<Map<String, Object>> selectOutboundByCustomer(@Param("coIdx") String coIdx, @Param("customerCd") String customerCd);
    
    /** ★ 기간별 출하 실적 조회 */
    List<Map<String, Object>> selectOutboundByDateRange(@Param("coIdx") String coIdx, 
                                                         @Param("startDate") String startDate, 
                                                         @Param("endDate") String endDate);
}