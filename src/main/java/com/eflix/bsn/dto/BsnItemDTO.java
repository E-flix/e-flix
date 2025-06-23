package com.eflix.bsn.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BsnItemDTO {
  private String        itemCode;    // ITEM_CODE (VARCHAR2(50))
  private String        itemName;    // ITEM_NAME (VARCHAR2(100))
  private String        spec;        // SPEC (VARCHAR2(100))
  private String        unit;        // UNIT (VARCHAR2(50))
  private BigDecimal    salePrice;   // SALE_PRICE (NUMBER(15,2))
  private BigDecimal    taxRate;     // TAX_RATE (NUMBER(5,2))
  private String        remark;      // REMARK (VARCHAR2(200))
  private LocalDateTime createdAt;   // CREATED_AT (TIMESTAMP)
  private LocalDateTime updatedAt;   // UPDATED_AT (TIMESTAMP)

  // 기본 생성자
  public BsnItemDTO() {}

  // 전체 필드 생성자
  public BsnItemDTO(String itemCode, String itemName, String spec, String unit,
                BigDecimal salePrice, BigDecimal taxRate, String remark,
                LocalDateTime createdAt, LocalDateTime updatedAt) {
      this.itemCode  = itemCode;
      this.itemName  = itemName;
      this.spec      = spec;
      this.unit      = unit;
      this.salePrice = salePrice;
      this.taxRate   = taxRate;
      this.remark    = remark;
      this.createdAt = createdAt;
      this.updatedAt = updatedAt;
  }

  // getters & setters
  public String getItemCode()   { return itemCode; }
  public void setItemCode(String itemCode) { this.itemCode = itemCode; }
  public String getItemName()   { return itemName; }
  public void setItemName(String itemName) { this.itemName = itemName; }
  public String getSpec()       { return spec; }
  public void setSpec(String spec) { this.spec = spec; }
  public String getUnit()       { return unit; }
  public void setUnit(String unit) { this.unit = unit; }
  public BigDecimal getSalePrice() { return salePrice; }
  public void setSalePrice(BigDecimal salePrice) { this.salePrice = salePrice; }
  public BigDecimal getTaxRate()   { return taxRate; }
  public void setTaxRate(BigDecimal taxRate) { this.taxRate = taxRate; }
  public String getRemark()     { return remark; }
  public void setRemark(String remark) { this.remark = remark; }
  public LocalDateTime getCreatedAt() { return createdAt; }
  public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
  public LocalDateTime getUpdatedAt() { return updatedAt; }
  public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

  @Override
  public String toString() {
      return "BsnItemDTO{" +
            "itemCode='" + itemCode + '\'' +
            ", itemName='" + itemName + '\'' +
            ", spec='" + spec + '\'' +
            ", unit='" + unit + '\'' +
            ", salePrice=" + salePrice +
            ", taxRate=" + taxRate +
            ", remark='" + remark + '\'' +
            ", createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
            '}';
  }
}