package com.eflix.acc.mapper;

import java.util.List;
import com.eflix.acc.dto.PartnerDetailDTO;

/* ============================================
  - 작성자   : 김희정
  - 최초작성 : 2025-06-23
  - 설명     : partnerMapper interface
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-23 (김희정): 일반거래처 조회 작성
  - 2025-07-03 (김희정): 조회조건 회사번호(coIdx) 추가
=============================================== */
public interface PartnerMapper {
  // 일반거래처 전체조회
  List<PartnerDetailDTO> getList(String coIdx);
  // 거래처 코드로 일반거래처 조회
  PartnerDetailDTO getListByCode(PartnerDetailDTO partnerDetailDTO);
  // 거래처 이름으로 일반거래처 조회 (부분검색)
  List<PartnerDetailDTO> getListByName(PartnerDetailDTO partnerDetailDTO);
}
