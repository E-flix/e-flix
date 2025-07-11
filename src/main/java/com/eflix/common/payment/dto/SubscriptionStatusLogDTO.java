package com.eflix.common.payment.dto;

import java.util.Date;

import lombok.Data;

// 0706

@Data
public class SubscriptionStatusLogDTO {
    private String logIdx;
    private String spiIdx;
    private String oldStatus;
    private String newStatus;
    private Date change_date;
    private String changeReason;
}
