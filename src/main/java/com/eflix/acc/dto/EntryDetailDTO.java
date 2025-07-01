package com.eflix.acc.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/* ============================================
  - 작성자   : 김희정
  - 최초작성 : 2025-06-20
  - 설명     : 전표상세DTO
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-20 (김희정): DTO 생성 
=============================================== */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntryDetailDTO {
  private int lineNumber;
  private int entryNumber;
  private String partnerCode;
  private String partnerName;
  private String accountCode;
  private String resentmenType;
  private int amount;
  private String description;
  private Date createdAt;
  private Date updatedAt;

  private String accountName; // 계정과목명
  private String majorCategory; // 계정과목 대분류
  //private String partner_name, 
  private String businessNumber; // 사업자등록번호
  private String residentNumber; // 주민등록번호
  private String representativeName; // 대표자명
  private String institutionName; // 금융기관명
  private String accountNumber; // 계좌번호
  private String cardName; // 카드명
  private String cardNumber; // 카드번호
}
