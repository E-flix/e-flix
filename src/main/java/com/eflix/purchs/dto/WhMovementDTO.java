package com.eflix.purchs.dto;

import lombok.Data;

@Data
public class WhMovementDTO {
    // 제품 정보
    private String prodId;
    private String prodName;

    // 이동 전 창고 정보
    private String fromWhId;
    private String fromLocation;
    private int fromBeforeQty;
    private int currentQuantity;

    // 이동 후 창고 정보
    private String toWhId;
    private String toLocation;
    private int toBeforeQty;
    private int warehouseScale;
    private int remainingCapacity;

    // 이동 수량
    private int transferQty;

    // 담당자
    private String manager;
}
