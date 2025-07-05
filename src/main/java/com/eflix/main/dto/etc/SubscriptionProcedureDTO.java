package com.eflix.main.dto.etc;

import lombok.Data;

@Data
public class SubscriptionProcedureDTO {
    private String spiIdx;
    private String spkIdx;
    private String coIdx;
    private String empIdx;
    private String spiPay;
    private int spiPeriod;
    private int spiCycle;
    private String spiCtrt;
    private String spiUid;
    private String checkedModules;

    private String mstId;
    private String mstPw;
    private String mstName;
    private String mstEmail;
    private String mstTel;
}
