package com.eflix.purchs.mapper;

import java.util.List;


import com.eflix.purchs.dto.OutboundViewDTO;
import com.eflix.purchs.dto.WarehouseViewDTO;

public interface OutboundViewMapper {
    public List<OutboundViewDTO> outboundViewList(WarehouseViewDTO outboundId);
    public List<OutboundViewDTO> outboundViewDetailList(String outboundId);
    public List<OutboundViewDTO> searchOutboundId(); 
}
