package com.eflix.acc.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import com.eflix.acc.dto.EntryMasterDTO;
import com.eflix.acc.mapper.EntryMapper;
import com.eflix.acc.service.EntryService;
import lombok.RequiredArgsConstructor;

/* ============================================
  - 작성자   : 김희정
  - 최초작성 : 2025-06-20
  - 설명     : entryServiceImpl 구현
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-20 (김희정): 전표 전체조회 작성
=============================================== */
@Service
@RequiredArgsConstructor
public class EntryServiceImpl implements EntryService {
  private final EntryMapper entryMapper;
  
  /**
   * 전표 전체조회
   */
  @Override
  public List<EntryMasterDTO> getList() {
    return entryMapper.getList();
  }
}
