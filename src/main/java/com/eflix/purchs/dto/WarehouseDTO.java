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
    String warehouseScale;
    String warehouseManager;
}
