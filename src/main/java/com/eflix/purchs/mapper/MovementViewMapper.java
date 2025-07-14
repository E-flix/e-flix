package com.eflix.purchs.mapper;

import java.util.List;

import com.eflix.purchs.dto.MovementViewDTO;

public interface MovementViewMapper {
    public List<MovementViewDTO> warehouseMovementList(MovementViewDTO warehouseView);
    public List<MovementViewDTO> searchProdName();
}
