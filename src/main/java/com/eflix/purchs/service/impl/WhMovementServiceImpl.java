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

    // 제품 조회
    @Override
    public List<WhMovementDTO> searchProdIdList() {
        return whMovementMapper.searchProdIdList();
    }
}
