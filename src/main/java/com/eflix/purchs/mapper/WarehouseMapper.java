package com.eflix.purchs.mapper;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.eflix.purchs.dto.WarehouseDTO;
@Repository
public interface WarehouseMapper {
    public List<WarehouseDTO> getWarehouse();
    
}
