package com.eflix.purchs.mapper;

import java.util.List;

import com.eflix.purchs.dto.OutboundDTO;

public interface OutboundMapper {
    public List<OutboundDTO> selectOutboundRequest();
    public List<OutboundDTO> outboundRequestDetail(String outboundNo);
}
