package com.eflix.bsn.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

/** 거래처 여신 정보 */
@Data
public class CreditInfoDTO {

    private String     customerCd;          // CUSTOMER_CD
    private BigDecimal creditLimit;         // CREDIT_LIMIT
    private BigDecimal creditUsed;          // CREDIT_USED

    /** 남은 한도 = 한도 - 사용액 (계산용) */
    public BigDecimal getRemainingCredit() {
        return creditLimit == null || creditUsed == null
              ? null
              : creditLimit.subtract(creditUsed);
    }

    private String     paymentTerms;        // PAYMENT_TERMS
    private BigDecimal overdueAmount;       // OVERDUE_AMOUNT
    private String     creditStatus;        // CREDIT_STATUS

    private String     creditHoldFlg;       // CREDIT_HOLD_FLG
    private String     creditHoldReason;    // CREDIT_HOLD_REASON
    private LocalDate  creditHoldUntil;     // CREDIT_HOLD_UNTIL

    private String     creditRating;        // CREDIT_RATING
    private BigDecimal guaranteeAmount;     // GUARANTEE_AMOUNT
    private BigDecimal lockOrderThreshold;  // LOCK_ORDER_THRESHOLD

    private LocalDate  effectiveFrom;       // EFFECTIVE_FROM
    private LocalDate  effectiveTo;         // EFFECTIVE_TO
    private Integer    gracePeriod;         // GRACE_PERIOD
    private LocalDate  lastSettleDt;        // LAST_SETTLE_DT
}
