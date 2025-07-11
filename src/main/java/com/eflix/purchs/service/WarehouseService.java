package com.eflix.purchs.service;

import java.util.List;
import com.eflix.purchs.dto.WarehouseDTO;

public interface WarehouseService {
    // 조회
    public List<WarehouseDTO> getWarehouse();

    // 가장 마지막 창고번호 가져오기
    public String getNextWarehouseId();

    // 등록
    public int insertWarehouse(WarehouseDTO warehouseDTO);

    // 삭제
    public int deleteWarehouse(WarehouseDTO warehouseDTO);

    // 사용가능한 창고 조회
    public List<WarehouseDTO> warehouseState();
}
