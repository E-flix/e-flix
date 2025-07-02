package com.eflix.acc.service;

import java.util.List;
import com.eflix.acc.dto.AccountDTO;

/* ============================================
  - 작성자   : 김희정
  - 최초작성 : 2025-06-19
  - 설명     : accountService interface
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김희정): 계정과목 전체조회 작성
  - 2025-06-23 (김희정): 단건 조회(accountCode) 작성
=============================================== */
public interface AccountService {
	// 계정과목 전체조회
  public List<AccountDTO> getList();
  // 계정과목 코드로 계정과목 조회
  AccountDTO getListByCode(AccountDTO accountDTO);
}
