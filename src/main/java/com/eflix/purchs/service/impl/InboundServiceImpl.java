package com.eflix.purchs.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eflix.purchs.dto.InboundDTO;
import com.eflix.purchs.mapper.InboundMapper;
import com.eflix.purchs.service.InboundService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class InboundServiceImpl implements InboundService {
    // 입고조회
    @Autowired
    InboundMapper inboundMapper;

    // 조회
    @Override
    public List<InboundDTO> getInbound() {
        return inboundMapper.getInbound();
    }

    // 생산입력
    @Override
    public int insertProd(InboundDTO inboundDTO) {
        return inboundMapper.insertProd(inboundDTO);
    }

    // 가장 마지막 prod_id 가져오기
    @Override
    public String getNextProdId() {
        return inboundMapper.getNextProdId();
    };

    // 반품
    @Override
    public int deleteProd(InboundDTO inboundDTO) {
        return inboundMapper.deleteProd(inboundDTO);
    };
    // 가장 마지막 inbound_id 가져오기
    @Override
    public String getNextInboundId() {
        return inboundMapper.getNextInboundId();
    }
    // 가장 마지막 inbound_lot 가져오기
    @Override
    public String getNextInboundLot() {
        return inboundMapper.getNextInboundLot();
    }
    // 입고등록
    @Override
    public int inbound(InboundDTO inboundDTO) {
        return inboundMapper.inbound(inboundDTO);
    }

}
