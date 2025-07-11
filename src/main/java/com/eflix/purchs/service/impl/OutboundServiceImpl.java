package com.eflix.purchs.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eflix.purchs.dto.OutboundDTO;
import com.eflix.purchs.mapper.OutboundMapper;
import com.eflix.purchs.service.OutboundService;

@Service
public class OutboundServiceImpl implements OutboundService {
    @Autowired
    private OutboundMapper outboundMapper;

    @Override

    public List<OutboundDTO> selectOutboundRequest() {
        return outboundMapper.selectOutboundRequest();
    }

    @Override
    public List<OutboundDTO> outboundRequestDetail(String outboundNo) {
        return outboundMapper.outboundRequestDetail(outboundNo);
    }

    @Override
    public List<OutboundDTO> outboundProcedure(String OutboundDTO) {
        return outboundMapper.outboundProcedure(OutboundDTO);
    }

}
