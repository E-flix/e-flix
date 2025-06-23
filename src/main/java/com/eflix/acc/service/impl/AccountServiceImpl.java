package com.eflix.acc.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import com.eflix.acc.dto.AccountDTO;
import com.eflix.acc.mapper.AccountMapper;
import com.eflix.acc.service.AccountService;
import lombok.RequiredArgsConstructor;

/* ============================================
  - 작성자   : 김희정
  - 최초작성 : 2025-06-19
  - 설명     : accountServiceImpl 구현
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김희정): 계정과목 전체조회 작성
  - 2025-06-23 (김희정): 단건 조회(accountCode) 작성
=============================================== */
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
  private final AccountMapper accountMapper;

  /**
   * 계정과목 전체조회
   * 
   * @return List<AccountDTO>
   */
  @Override
  public List<AccountDTO> getList() {
    return accountMapper.getList();
  }

  // 계정과목 코드로 계정과목 조회
  @Override
  public AccountDTO getListByCode(int accountCode) {
    return accountMapper.getListByCode(accountCode);
  }
}
