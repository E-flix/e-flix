package com.eflix.acc.mapper;

import java.util.List;
import com.eflix.acc.dto.EntryMasterDTO;

/* ============================================
  - 작성자   : 김희정
  - 최초작성 : 2025-06-20
  - 설명     : EntryMapper interface
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-20 (김희정): 전표 전체조회 작성
=============================================== */
public interface EntryMapper {
  // 전표 전체조회
  public List<EntryMasterDTO> getList();

}
