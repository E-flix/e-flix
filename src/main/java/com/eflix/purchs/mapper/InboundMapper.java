package com.eflix.purchs.mapper;

import java.util.List;

import com.eflix.purchs.dto.InboundDTO;

public interface InboundMapper {
    // 입고조회
    public List<InboundDTO> getInbound();

    // 생산등록
    public int insertProd(InboundDTO inboundDTO);

    // 가장 마지막 prod_id 가져오기
    public String getNextProdId();

    // 반품
    public int deleteProd(InboundDTO inboundDTO);

    // 가장 마지막 inbound_id 가져오기
    public String getNextInboundId();

    // 가장 마지막 inbound_lot 가져오기
    public String getNextInboundLot();

    // 입고등록
    public int prodToWarehouse(InboundDTO inboundDTO);
}
