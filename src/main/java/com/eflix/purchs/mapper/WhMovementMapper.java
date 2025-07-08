package com.eflix.purchs.mapper;

import java.util.List;

import com.eflix.purchs.dto.WhMovementDTO;

public interface WhMovementMapper {
    public List<WhMovementDTO> fromWarehouse();
    public List<WhMovementDTO> fromProd(String fromWhId);
    public List<WhMovementDTO> toWarehouse(String toWhId);
    public void executeWarehouseTransfer(WhMovementDTO warehouseDTO);
}
