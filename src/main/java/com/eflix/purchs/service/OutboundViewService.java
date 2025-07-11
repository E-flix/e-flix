package com.eflix.purchs.service;

import java.util.List;

import com.eflix.purchs.dto.OutboundViewDTO;

public interface OutboundViewService {
    public List<OutboundViewDTO> outboundViewList(OutboundViewDTO outboundId);

    public List<OutboundViewDTO> outboundViewDetailList(String outboundId);

    public List<OutboundViewDTO> searchOutboundId();
}
