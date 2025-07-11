package com.eflix.bsn.service;

import java.time.LocalDate;
import java.util.List;

import com.eflix.bsn.dto.CreditInfoDTO;

public interface CreditService {

  CreditInfoDTO getCreditInfo(String customerCd);
  
  CreditInfoDTO getCreditByCustomer(String customerCd);

  //거래정지 설정
  void setTradeStop(String customerCd, String reason, LocalDate resumDate);

  //거래정지 해제
  void releaseTradeStop(String customerCd);

  // 거래정지 고객 목록
  List<CreditInfoDTO> getTradeStoppedCustomers();

  // 거래재개 예정 고객 목록
  List<CreditInfoDTO> getTodayTradeResumeCustomers();
}
