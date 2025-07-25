package com.eflix.main.dto.etc;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class InvoiceDTO {
    private String spiIdx;      // spi-000
    private String coIdx;       // co-000
    private String spkIdx;      // spk-000
    private String empIdx;      // emp-000
    private String spiPay;      // SP01: 무통장, SP02: 카드, SP03: 계좌, SP04: 카카오, SP05: 네이버
    private String spiStatus;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date spiStart;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date spiEnd;
    private int spiPeriod;
    private String spiCtrt;     // 계약서 파일
    private String spiUid;      // PortOne UID

    private String spkName;
    private int spkPrice;

    private String coName;
    private String coNameEn;
    private String ceoName;
    private String pschName;
    private String pschTel;
    private String pschEmail;

    private String userIdx;       // user-000
    private String coAddr;
    private String coAddrDetail;
    private String coZip;
    private String bizrNo;
    private String bizrCert;
    private String ceoTel;

    private List<String> checkedModules;
    private String moduleNames; // 예: "인사, 회계"
}
