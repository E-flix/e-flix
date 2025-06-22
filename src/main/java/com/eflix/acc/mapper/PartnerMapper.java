package com.eflix.acc.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.eflix.acc.dto.PartnerDetailDTO;

/* ============================================
  - 작성자   : 김희정
  - 최초작성 : 2025-06-23
  - 설명     : partnerMapper interface
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-23 (김희정): 일반거래처 조회 작성
=============================================== */
public interface PartnerMapper {
  // 일반거래처 전체조회
  List<PartnerDetailDTO> getList();
  // 거래처 코드로 일반거래처 조회
  PartnerDetailDTO getListByCode(@Param("partnerCode") int partnerCode);
  // 거래처 이름으로 일반거래처 조회 (부분검색)
  List<PartnerDetailDTO> getListByName(@Param("partnerName") String partnerName);
}
