package com.eflix.acc.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import com.eflix.acc.dto.EntryMasterDTO;
import com.eflix.acc.mapper.ClientsLedgerMapper;
import com.eflix.acc.service.ClientsLedgerService;
import lombok.RequiredArgsConstructor;

/* ============================================
  - 작성자   : 김희정
  - 최초작성 : 2025-06-26
  - 설명     : ClientsLedgerServiceImpl 구현
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-26 (김희정): 전체조회 작성
=============================================== */
@Service
@RequiredArgsConstructor
public class ClientsLedgerServiceImpl implements ClientsLedgerService {
  private final ClientsLedgerMapper clientsLedgerMapper;

  /**
   * 전표 전체조회
   */
  @Override
  public List<EntryMasterDTO> getList(String startDate, String endDate, String accountCode, String partnerCode) {
      return clientsLedgerMapper.getList(startDate, endDate, accountCode, partnerCode);
  }
}
