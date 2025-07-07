package com.eflix.purchs.service;

import java.util.List;

import com.eflix.purchs.dto.WhMovementDTO;

public interface WhMovementService {
   List<WhMovementDTO> fromWarehouse();
    List<WhMovementDTO> fromProd(String fromWhId);
    List<WhMovementDTO> toWarehouse(String toWhId);
    public int executeWarehouseTransfer(WhMovementDTO warehouseDTO);
}
