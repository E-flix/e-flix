package com.eflix.purchs.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
@Data
public class OutboundDTO {
    String outboundNo;

    String writeDt;
    String customerName;
    String representativeNm;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    Date orderDt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    Date outboundDt;
    String remarks;

    String itemName;
    String standard;
    int qty;
}
