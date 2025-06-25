package com.eflix.purchs.dto;

/* ============================================
  - 작성자   : 이혁진
  - 최초작성 : 2025-06-19
  - 설명     : 창고DTO
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (이혁진): 완성
============================================  */
import lombok.Data;

@Data
public class WarehouseDTO {
  String warehouseId;
  String warehouseLocation;
  int warehouseScale; //       사용전 기존 용량 100L
  String warehouseManager;
  int currentQuantity; //      사용중인 용량    30L
  int availableCapacity; //    사용가능 용량    70L
}
