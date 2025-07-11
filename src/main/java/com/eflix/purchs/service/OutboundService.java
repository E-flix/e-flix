package com.eflix.purchs.service;

import java.util.List;

import com.eflix.purchs.dto.OutboundDTO;

public interface OutboundService {
    public List<OutboundDTO> selectOutboundRequest();
    public List<OutboundDTO> outboundRequestDetail(String outboundNo);
}
