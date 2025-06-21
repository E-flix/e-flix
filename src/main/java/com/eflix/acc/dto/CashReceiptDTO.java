package com.eflix.acc.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;

/* ============================================
  - 작성자   : 김희정
  - 최초작성 : 2025-06-20
  - 설명     : 현금영수증DTO
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-20 (김희정): DTO 생성 
=============================================== */

@Getter
@AllArgsConstructor
public class CashReceiptDTO {
  private String approvalNumber;
  private int entryNumber;
  private String transactionType;
  private String partnerCode;
  private Date transactionDate;
  private int supplyAmount;
  private int taxAmount;
  private int totalAmount;
  private String approvalStatus;
  private Date createdAt;
  private Date updatedAt;
}
