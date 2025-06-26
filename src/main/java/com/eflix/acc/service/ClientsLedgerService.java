package com.eflix.acc.service;

import java.util.List;
import com.eflix.acc.dto.EntryMasterDTO;
/* ============================================
  - 작성자   : 김희정
  - 최초작성 : 2025-06-26
  - 설명     : clientsLedgerService interface
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-26 (김희정): 전체조회 작성
=============================================== */
public interface ClientsLedgerService {
  // 조건 전체 조회
  public List<EntryMasterDTO> getList(String startDate, String endDate, String accountCode, String partnerCode);
}
