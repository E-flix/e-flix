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
    @Autowired
    InboundMapper inboundMapper;
    // 조회
    public List<InboundDTO> getInbound() {
        return inboundMapper.getInbound();
    }
}
