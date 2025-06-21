package com.eflix.bsn.dto;

import lombok.Getter;
import lombok.Setter;

// 주문서 상세 DTO
@Getter
@Setter
public class OrdersDetailDTO {
  private String orderNo;
  private String itemCd;
  private String itemName;
  private String qty;
  private String unitPrice;
  private String discountRate;
  private String amount;
  private String remarks;
}
