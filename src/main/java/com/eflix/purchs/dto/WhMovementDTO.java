package com.eflix.purchs.dto;

import lombok.Data;

@Data
public class WhMovementDTO {
    String prodId;
    String prodName;
    String sendWh;
    String toWh;
    int sendBeforeQty;
    int toBeforeQty;
    int warehouseScale;
}
