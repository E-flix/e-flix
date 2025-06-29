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
    // 조회
    @Override
    public List < WarehouseDTO > getWarehouse() {
        return warehouseMapper.getWarehouse();
    };
    // 마지막 창고번호 들고오기
    @Override
    public String getNextWarehouseId() {
        return warehouseMapper.getNextWarehouseId();
    }
    // 등록
    @Override
    public int insertWarehouse(WarehouseDTO warehouseDTO) {
        return warehouseMapper.insertWarehouse(warehouseDTO);
    }
    // 삭제
    @Override
    public int deleteWarehouse(WarehouseDTO warehouseDTO) {
        return warehouseMapper.deleteWarehouse(warehouseDTO);
    }



}