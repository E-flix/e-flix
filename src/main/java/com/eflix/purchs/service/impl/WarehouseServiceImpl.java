package com.eflix.purchs.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eflix.purchs.dto.WarehouseDTO;
import com.eflix.purchs.mapper.WarehouseMapper;
import com.eflix.purchs.service.WarehouseService;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    @Autowired
    WarehouseMapper warehouseMapper;

    @Override
    public List <WarehouseDTO> getWarehouse() {
        return warehouseMapper.getWarehouse();
    };

    @Override
    public int insertWarehouse(WarehouseDTO warehouseDTO) {
        return warehouseMapper.insertWarehouse(warehouseDTO);
    }

    @Override
    public int deleteWarehouse(WarehouseDTO warehouseDTO) {
        return warehouseMapper.deleteWarehouse(warehouseDTO);
    }

}