package com.eflix.purchs.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eflix.purchs.dto.WhMovementDTO;
import com.eflix.purchs.mapper.WhMovementMapper;
import com.eflix.purchs.service.WhMovementService;

import jakarta.transaction.Transactional;
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
        return whMovementMapper.fromProd(warehouseDTO);
    }

    @Override
    public List<WhMovementDTO> toWarehouse(String toWhId) {
        WhMovementDTO warehouseDTO = new WhMovementDTO();
        warehouseDTO.setFromWhId(toWhId);
        return whMovementMapper.toWarehouse(warehouseDTO);
    }

    @Override
    @Transactional
    public int executeWarehouseTransfer(WhMovementDTO warehouseDTO) {
        try {
            whMovementMapper.executeWarehouseTransfer(warehouseDTO);
        } catch (Exception e) {
            throw new RuntimeException("창고 이동 실패: " + e.getMessage());
        }
        return 0;
    }
}
