package com.eflix.acc.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;

/* ============================================
  - 작성자   : 김희정
  - 최초작성 : 2025-06-20
  - 설명     : 금융기관 거래처 상세DTO
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-20 (김희정): DTO 생성 
=============================================== */

@Getter
@AllArgsConstructor
public class financialInstitutionDetailDTO {
  private int partnerCode;
  private String institutionName;
  private String accountNumber;
  private String institutionType;
  private String bankCode;
  private String bankBranch;
  private Date tradeStartDate;
  private Date tradeEndDate;
  private int interestRate;
  private String businessNumber;
  private String businessAccountFlag;
  private String phoneNumber;
  private String extensionNumber;
  private String faxNumber;
  private String useFlag;
  private Date createdAt;
  private Date updatedAt;
  private String remarks;
}
