package com.eflix.bsn.service;

import com.eflix.bsn.dto.SalesOutboundDTO;
import com.eflix.bsn.dto.SoutboundDetailDTO;

import java.util.List;

public interface SOutboundService {
    List<SalesOutboundDTO> getOutboundList();
    List<SoutboundDetailDTO> getOutboundDetails(String outboundNo);
    String createOutbound(SalesOutboundDTO dto);
}
