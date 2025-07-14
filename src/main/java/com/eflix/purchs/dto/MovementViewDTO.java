package com.eflix.purchs.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class MovementViewDTO {
            private String transferId;
            private String prodId;
            private String prodName;
            private String fromWarehouseId;
            private String toWarehouseId;
            private int quantity;
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
            private Date transferDate;
            private String manager;
            private String startDate;
            private String endDate;
}
