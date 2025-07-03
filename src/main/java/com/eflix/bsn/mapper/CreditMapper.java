package com.eflix.bsn.mapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.eflix.bsn.dto.CreditInfoDTO;

@Mapper
public interface CreditMapper {
  
  // 여신 정보 조회
  CreditInfoDTO getCreditInfo(@Param("customerCd") String customerCd);

  CreditInfoDTO selectCreditInfo(@Param("customerCd") String customerCd);

  CreditInfoDTO selectByCustomerCd(@Param("customerCd") String customerCd);

  // 남은 여신 계산
  BigDecimal getAvailableCredit(@Param("customerCd") String customerCd);

  // 거래정지 설정
  void setTradeStop(@Param("customerCd") String customerCd,
                    @Param("reason") String reason,
                    @Param("resumeDate") LocalDate resumeDate);

  // 거래정지 해제
  void releaseTradeStop(@Param("customerCd") String customerCd);

  //거래정지 고객 목록 조회
  List<CreditInfoDTO> seleceTradeStoppedCustomers();

  //거래정지 예정 고객 목록 조회
  List<CreditInfoDTO> selectTradeResumeScheduled(@Param("targetDate") LocalDate targetDate);
}
