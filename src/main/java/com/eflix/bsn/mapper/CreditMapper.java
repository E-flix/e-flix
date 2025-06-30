package com.eflix.bsn.mapper;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.eflix.bsn.dto.CreditInfoDTO;

@Mapper
public interface CreditMapper {
  
  // 여신 정보 조회
  CreditInfoDTO getCreditInfo(@Param("customerCd") String customerCd);

  CreditInfoDTO selectByCustomerCd(@Param("customerCd") String customerCd);

  // 남은 여신 계산
  BigDecimal getAvailableCredit(@Param("customerCd") String customerCd);
}
