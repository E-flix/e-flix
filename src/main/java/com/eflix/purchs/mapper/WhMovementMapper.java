package com.eflix.purchs.mapper;

import java.util.List;

import com.eflix.purchs.dto.WhMovementDTO;

public interface WhMovementMapper {
    public List<WhMovementDTO> fromWarehouse();
    public List<WhMovementDTO> fromProd(String warehouseId);
    public List<WhMovementDTO> toWarehouse(String from_wh_id);
    void executeWarehouseTransfer(WhMovementDTO dto);
}
