package com.eflix.acc.mapper;

import java.util.List;
import com.eflix.acc.dto.EntryMasterDTO;

/* ============================================
  - 작성자   : 김희정
  - 최초작성 : 2025-06-26
  - 설명     : clientsLedgerMapper interface
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-26 (김희정): 잔액 조건 전체 조회 작성
  - 2025-06-27 (김희정): 내용 조건 전체 조회 작성
=============================================== */
public interface ClientsLedgerMapper {
  // 잔액 조건 전체 조회
  public List<EntryMasterDTO> getList(String startDate, String endDate, String accountCode, String partnerCode);
  // 내용 조건 전체 조회
  public List<EntryMasterDTO> getListDetail(String startDate, String endDate, String accountCode, String partnerCode);
}
