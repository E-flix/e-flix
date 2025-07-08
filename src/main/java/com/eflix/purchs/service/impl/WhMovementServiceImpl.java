package com.eflix.purchs.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eflix.purchs.dto.WhMovementDTO;
import com.eflix.purchs.mapper.WhMovementMapper;
import com.eflix.purchs.service.WhMovementService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WhMovementServiceImpl implements WhMovementService {

    private final WhMovementMapper whMovementMapper;

    @Override
    public List<WhMovementDTO> fromWarehouse() {
        return whMovementMapper.fromWarehouse();
    }

    @Override
    public List<WhMovementDTO> fromProd(String fromWhId) {
        WhMovementDTO warehouseDTO = new WhMovementDTO();
        warehouseDTO.setFromWhId(fromWhId);
        return whMovementMapper.fromProd(fromWhId);
    }
    
    @Override
    public List<WhMovementDTO> toWarehouse(String toWhId) {
        WhMovementDTO warehouseDTO = new WhMovementDTO();
        warehouseDTO.setFromWhId(toWhId);
        return whMovementMapper.toWarehouse(toWhId);
    }
    // 창고 이동 트랜잭션
    @Override
    public void executeWarehouseTransfer(WhMovementDTO warehouseDTO) {
        whMovementMapper.executeWarehouseTransfer(warehouseDTO);
    }
}
