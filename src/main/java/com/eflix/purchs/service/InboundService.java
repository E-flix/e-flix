package com.eflix.purchs.service;

import java.util.List;

import com.eflix.purchs.dto.InboundDTO;

public interface InboundService {
        // 입고조회
     public List<InboundDTO> getInbound();
}
