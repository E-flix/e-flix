package com.eflix.acc.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;

/* ============================================
  - 작성자   : 김희정
  - 최초작성 : 2025-06-20
  - 설명     : 전표마스터DTO
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-20 (김희정): DTO 생성 
=============================================== */

@Getter
@AllArgsConstructor
public class EntryMasterDTO {
  private int entryNumber;
  private String entryType;
  private Date entryDate;
  private String entryStatus;
  private String creator;
  private Date createdAt;
  private Date updatedAt;
  private String transactionType;
  private String partnerCode;
  private String electronicType;
  private String approvalNumber;
  private String itemName;
  private int quantity;
  private int unitPrice;
  private int totalSupplyAmount;
  private int totalTaxAmount;
  private String remarks;
}
