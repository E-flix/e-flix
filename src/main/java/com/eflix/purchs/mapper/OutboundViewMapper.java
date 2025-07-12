package com.eflix.purchs.mapper;

import java.util.List;


import com.eflix.purchs.dto.OutboundViewDTO;
public interface OutboundViewMapper {
    public List<OutboundViewDTO> outboundViewList(OutboundViewDTO outbound);
    public List<OutboundViewDTO> outboundViewDetailList(String outboundId);
    public List<OutboundViewDTO> searchOutboundId(); 
}
