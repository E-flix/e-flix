package com.eflix.acc.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;

/* ============================================
  - 작성자   : 김희정
  - 최초작성 : 2025-06-20
  - 설명     : 고정자산DTO
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-20 (김희정): DTO 생성 
=============================================== */

@Getter
@AllArgsConstructor
public class FixedAssetDTO {
  private String assetCode;
  private String assetName;
  private String accountCode;
  private Date acquisitionDate;
  private String depreciationMethod;
  private int usefulLife;
  private String expenseType;
  private int initialAmount;
  private int accumulatedDepreciation;
  private int bookValue;
  private int currentIncrease;
  private int acquisitionQuantity;
  private Date disposalDate;
  private Date retirementDate;
  private String useFlag;
  private Date createdAt;
  private Date updatedAt;
  private String remarks;
}
