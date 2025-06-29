package com.eflix.purchs.service;

import java.util.List;

import com.eflix.purchs.dto.InboundDTO;

public interface InboundService {
        // 입고조회
     public List<InboundDTO> getInbound();
         // 생산입력
     public int insertProd(InboundDTO inboundDTO);
         // 가장 마지막 prod_id 가져오기
     public String getNextProdId();
}
