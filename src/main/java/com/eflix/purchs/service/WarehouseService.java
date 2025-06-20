package com.eflix.purchs.service;
import java.util.List;
import com.eflix.purchs.dto.WarehouseDTO;


public interface WarehouseService {
    // 조회
    public List<WarehouseDTO> getWarehouse();
    // 삽입
    public int insertWarehouse(WarehouseDTO warehouseDTO);
    // 삭제
    public int deleteWarehouse(WarehouseDTO warehouseDTO);
}
