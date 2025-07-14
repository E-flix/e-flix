package com.eflix.purchs.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eflix.purchs.dto.MovementViewDTO;
import com.eflix.purchs.mapper.MovementViewMapper;
import com.eflix.purchs.service.MovementViewService;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class MovementViewServiceImpl implements MovementViewService {
    private final MovementViewMapper mapper;
    @Override
    public List<MovementViewDTO> warehouseMovementList(MovementViewDTO warehouseView) {
        return mapper.warehouseMovementList(warehouseView);
    }
    @Override
    public List<MovementViewDTO> searchProdName() {
        return mapper.searchProdName();
    }
    
}
