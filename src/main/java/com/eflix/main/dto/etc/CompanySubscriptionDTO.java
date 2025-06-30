package com.eflix.main.dto.etc;

import java.util.Date;

import lombok.Data;

@Data
public class CompanySubscriptionDTO {
    private String coIdx;
    private String coName;
    private String ceoName;
    private String pschName;
    private String pschTel;
    private String pschEmail;
    private String svcStatus;

    private String spiIdx;
    private String spiStatus;
    private Date spiStart;
    private Date spiEnd;
    private Integer spiPeriod;

    private Integer remainingDays;
}
