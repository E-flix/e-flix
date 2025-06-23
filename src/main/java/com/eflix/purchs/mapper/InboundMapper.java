package com.eflix.purchs.mapper;

import java.util.List;

import com.eflix.purchs.dto.InboundDTO;

public interface InboundMapper {
    // 입고조회
     public List<InboundDTO> getInbound();
}
