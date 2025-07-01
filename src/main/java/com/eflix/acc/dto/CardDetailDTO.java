package com.eflix.acc.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;

/* ============================================
  - 작성자   : 김희정
  - 최초작성 : 2025-06-20
  - 설명     : 카드 거래처 상세DTO
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-20 (김희정): DTO 생성 
=============================================== */

@Getter
@AllArgsConstructor
public class CardDetailDTO {
  private int partnerCode;
  private String cardName;
  private String cardNumber;
  private String cardType;
  private int paymentDate;
  private String empIdx;
  private String paymentAccount;
  private int creditLimit;
  private String phoneNumber;
  private String extensionNumber;
  private String faxNumber;
  private Date expiryDate;
  private String useFlag;
  private Date createdAt;
  private Date updatedAt;
  private String remarks;
}
