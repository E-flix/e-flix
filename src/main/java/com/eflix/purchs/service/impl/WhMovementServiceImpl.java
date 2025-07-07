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

    @Autowired
    WhMovementMapper whMovementMapper;

    // 이동전 창고 목록
    @Override
    public List<WhMovementDTO> fromWarehouse() {
        return whMovementMapper.fromWarehouse();
    }

    // 선택한 이동 전 창고 안에 있는 제품 목록 + 수량
    @Override
    public List<WhMovementDTO> fromProd(String warehouseId) {
        return whMovementMapper.fromProd(warehouseId);
    }

    // 이동 후 창고 목록 + 남은 수용 가능 수량 (출발창고 제외)
    @Override
    public List<WhMovementDTO> toWarehouse(String from_wh_id) {
        return whMovementMapper.toWarehouse(from_wh_id);
    }

    @Override
    @Transactional
    public void executeWarehouseTransfer(WhMovementDTO dto) {
        try {
            whMovementMapper.executeWarehouseTransfer(dto);
        } catch (Exception e) {
            // RuntimeException은 unchecked exception이므로 throws 선언 불필요
            throw new RuntimeException("창고 이동 실패: " + e.getMessage());
        }
    }
}
