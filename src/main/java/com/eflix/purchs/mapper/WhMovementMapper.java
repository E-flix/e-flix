package com.eflix.purchs.mapper;

import java.util.List;

import com.eflix.purchs.dto.WhMovementDTO;

public interface WhMovementMapper {
    public List<WhMovementDTO> fromWarehouse();
    public List<WhMovementDTO> fromProd(WhMovementDTO warehouseDTO);
    public List<WhMovementDTO> toWarehouse(WhMovementDTO warehouseDTO);
    public int executeWarehouseTransfer(WhMovementDTO warehouseDTO);
}
