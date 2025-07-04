package com.eflix.purchs.service;

import java.util.List;

import com.eflix.purchs.dto.WhMovementDTO;

public interface WhMovementService {
    public List<WhMovementDTO> sendWarehouse();
    public List<WhMovementDTO> toWarehouse();
}
