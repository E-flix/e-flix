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

  private String accountName;
}
