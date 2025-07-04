package com.eflix.purchs.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eflix.purchs.dto.WhMovementDTO;
import com.eflix.purchs.mapper.WhMovementMapper;
import com.eflix.purchs.service.WhMovementService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WhMovementServiceImpl implements WhMovementService {
    
    @Autowired
    WhMovementMapper whMovementMapper;

    // 입고전 제품, 창고 조회
    @Override
    public List<WhMovementDTO> sendWarehouse() {
        return whMovementMapper.sendWarehouse();
    }
    // 입고후 창고 조회
    @Override
    public List<WhMovementDTO> toWarehouse() {
        return whMovementMapper.toWarehouse();
    }
}
