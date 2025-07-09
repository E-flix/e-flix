package com.eflix.purchs.dto;

import lombok.Data;

@Data
public class WarehouseViewDTO {
    // 창고조회
    String warehouseId; // 창고ID
    String warehouseLocation; // 창고위치
    int warehouseScale; // 창고크기
    int warehouseCurrent; // 창고에 입고된 수량
    String warehouseManager; // 창고 관리자
    String warehouseStatusName; // 창고 상태 (사용가능 - 불가능)
    String warehouseStatus;
    String existProd; // 제품존재유무
    
    // 창고조회 상세 (모달)
    String prodId; // 제품ID
    String prodName; // 제품이름
    String currentQuantity; // 각각 제품의 수량

}