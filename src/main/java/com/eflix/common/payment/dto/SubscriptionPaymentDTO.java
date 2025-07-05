package com.eflix.common.payment.dto;

import java.util.Date;

import lombok.Data;

@Data
public class SubscriptionPaymentDTO {
    // subscription
    private String spiIdx;
    private String spkIdx;
    private String coIdx;
    private String empIdx;
    private String spiPay;
    private String spiStatus;
    private Date spiStart;
    private Date spiEnd;
    private int spiPeriod;
    private int spiCycle;
    private Date spiNext;
    private String spiCtrt;
    private String spiUid;

    // companies
    private String coName;
    private String pschName;
    private String pschTel;
    private String pschEmail;

    // subscription_packages
    private String spkName;
    private int spkPrice;
}
