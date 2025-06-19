package com.eflix.purchs.mapper;
import java.util.List;

// import org.apache.ibatis.annotations.Mapper;

import com.eflix.purchs.dto.WarehouseDTO;
// @Mapper
public interface WarehouseMapper {
    public List<WarehouseDTO> getWarehouse();
    
}
