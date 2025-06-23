package com.eflix.purchs.mapper;
import java.util.List;

// import org.apache.ibatis.annotations.Mapper;

import com.eflix.purchs.dto.WarehouseDTO;
// @Mapper
public interface WarehouseMapper {
    // 조회
    public List<WarehouseDTO> getWarehouse();
    // 마지막 창고번호 가져오기
    public String getNextWarehouseId();
    // 등록
    public int insertWarehouse(WarehouseDTO warehouseDTO);
    // 삭제
    public int deleteWarehouse(WarehouseDTO warehouseDTO);
}
