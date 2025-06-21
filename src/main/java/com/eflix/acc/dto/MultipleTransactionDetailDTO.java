package com.eflix.acc.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;

/* ============================================
  - 작성자   : 김희정
  - 최초작성 : 2025-06-20
  - 설명     : 복수거래 상세DTO
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-20 (김희정): DTO 생성 
=============================================== */

@Getter
@AllArgsConstructor
public class MultipleTransactionDetailDTO {
  private int lineNumber;
  private int etaxNumber;
  private String itemName;
  private int quantity;
  private int unitPrice;
  private int supplyAmount;
  private int taxAmount;
  private int totalAmount;
  private Date createdAt;
  private Date updatedAt;
}
