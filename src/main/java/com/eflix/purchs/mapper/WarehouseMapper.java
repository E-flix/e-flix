package com.eflix.purchs.mapper;
import java.util.List;

// import org.apache.ibatis.annotations.Mapper;

import com.eflix.purchs.dto.WarehouseDTO;
// @Mapper
public interface WarehouseMapper {
    // 목록
    public List<WarehouseDTO> getWarehouse();
    // 삽입
    public int insertWarehouse(WarehouseDTO warehouseDTO);
    // 삭제
    public int deleteWarehouse(WarehouseDTO warehouseDTO);
}
