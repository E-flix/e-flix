package com.eflix.acc.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;

/* ============================================
  - 작성자   : 김희정
  - 최초작성 : 2025-06-20
  - 설명     : 전자세금계산서DTO
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-20 (김희정): DTO 생성 
=============================================== */

@Getter
@AllArgsConstructor
public class ElectronicTaxInvoiceDTO {
  private int etaxNumber;
  private int entryNumber;
  private String approvalNumber;
  private String transactionType;
  private String partnerCode;
  private Date issueDate;
  private int supplyAmount;
  private int taxAmount;
  private int totalAmount;
  private String issueStatus;
  private String multipleTransactionFlag;
  private Date createdAt;
  private Date updatedAt;
}
