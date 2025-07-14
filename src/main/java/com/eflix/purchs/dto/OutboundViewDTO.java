package com.eflix.purchs.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class OutboundViewDTO {
   private String outboundId;

   private String outboundLot;

   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
   private Date outboundDate;
   private String startDate;
   private String endDate;

   private String memo;
   private String outboundManager;
   private String outboundNumber;

   private String outboundDetailId;
   private String warehouseId;
   private String prodId;
   private String prodName;
   private int outboundQuantity;

}
