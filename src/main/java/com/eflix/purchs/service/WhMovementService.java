package com.eflix.purchs.service;

import java.util.List;

import com.eflix.purchs.dto.WhMovementDTO;

public interface WhMovementService {
   public List<WhMovementDTO> fromWarehouse();
   public List<WhMovementDTO> fromProd(String fromWhId);
   public List<WhMovementDTO> toWarehouse(String toWhId);
    public void executeWarehouseTransfer(WhMovementDTO warehouseDTO);
}
