package com.eflix.purchs.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eflix.purchs.dto.WarehouseViewDTO;
import com.eflix.purchs.mapper.WarehouseViewMapper;
import com.eflix.purchs.service.WarehouseViewService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // autowired 자동
public class WarehouseViewServiceImpl implements WarehouseViewService {

    private final WarehouseViewMapper warehouseViewMapper;

    @Override
    public List<WarehouseViewDTO> warehouseViewList() {
        return warehouseViewMapper.warehouseViewList();
    }

    @Override
    public List<WarehouseViewDTO> warehouseViewListDetail(String warehouseId) {
        WarehouseViewDTO warehouseViewDTO = new WarehouseViewDTO();
        warehouseViewDTO.setWarehouseId(warehouseId);
        return warehouseViewMapper.warehouseViewListDetail(warehouseId);
    }
}
