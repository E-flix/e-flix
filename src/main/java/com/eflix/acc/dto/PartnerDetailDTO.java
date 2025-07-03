package com.eflix.acc.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/* ============================================
  - 작성자   : 김희정
  - 최초작성 : 2025-06-20
  - 설명     : 일반 거래처 상세DTO
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-20 (김희정): DTO 생성 
=============================================== */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartnerDetailDTO {
  private int partnerCode;
  private String partnerName;
  private String partnerType;
  private String businessNumber;
  private String residentNumber;
  private String representativeName;
  private String businessType;
  private String businessItem;
  private String postalCode;
  private String address;
  private String addressDetail;
  private String phoneNumber;
  private String extensionNumber;
  private String faxNumber;
  private String bankCode;
  private String bankBranch;
  private String accountHolder;
  private String accountNumber;
  private Date tradeStartDate;
  private Date tradeEndDate;
  private String useFlag;
  private Date createdAt;
  private Date updatedAt;
  private String remarks;
  private String coIdx; // 회사번호 추가
}
