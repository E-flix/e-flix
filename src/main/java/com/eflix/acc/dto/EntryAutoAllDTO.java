package com.eflix.acc.dto;

import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntryAutoAllDTO {
  // Salary fields
  private String attMonth;
  private String payMonth;
  private BigDecimal baseSalary;
  private BigDecimal bonus;
  private BigDecimal overtimePay;
  private BigDecimal nightWorkPay;
  private BigDecimal tax;
  private BigDecimal healthInsurance;
  private BigDecimal nationalPension;
  private BigDecimal employmentIns;
  private BigDecimal otherDeductions;
  private String salaryType;
  private String coIdx;

  // EntryMaster fields
  private Integer entryNumber;
  private String entryType;
  private Date entryDate;
  private String entryStatus;
  private String creator;
  private Date createdAt;
  private String remarks;
  private Integer transactionType;
  private String electronicType;
  private BigDecimal totalSupplyAmount;
  private BigDecimal totalTaxAmount;
  private String ledgerCheck;
  private Date updatedAt;

  // EntryDetail fields
  private Integer lineNumber;
  private Integer dEntryNumber; // d.entry_number AS d_entry_number 매핑용
  private String partnerCode;
  private String partnerName;
  private String accountCode;
  private String resentmenType;
  private BigDecimal amount;
  private String description;
}
