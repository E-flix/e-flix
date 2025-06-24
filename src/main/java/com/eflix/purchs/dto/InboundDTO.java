package com.eflix.purchs.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
// public class InboundDTO {
//     String inboundId;
//     String inboundLot;
//     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
//     Date inboundDate;
//     String memo;
//     String inboundManager;
// }

public class InboundDTO {
    String prodId;
    String prodName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    Date makeProdDate;
    int prodQuantity;
    int safetyStockAmount;
    String productType;
}
