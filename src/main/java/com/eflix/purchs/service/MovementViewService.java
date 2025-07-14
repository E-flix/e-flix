package com.eflix.purchs.service;

import java.util.List;

import com.eflix.purchs.dto.MovementViewDTO;

public interface MovementViewService {
    public List<MovementViewDTO> warehouseMovementList(MovementViewDTO warehouseView);
    public List<MovementViewDTO> searchProdName();
}
