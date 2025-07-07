package com.eflix.bsn.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eflix.bsn.dto.SalesOutboundDTO;
import com.eflix.bsn.dto.SoutboundDetailDTO;
import com.eflix.bsn.mapper.SOutboundMapper;
import com.eflix.bsn.service.SOutboundService;
import com.eflix.common.security.auth.AuthUtil;

@Service
public class SOutboundServiceImpl implements SOutboundService {

    private final SOutboundMapper mapper;

    public SOutboundServiceImpl(SOutboundMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<SalesOutboundDTO> getOutboundList() {
        String coIdx = AuthUtil.getCoIdx();
        return mapper.selectAllOutbound(coIdx);
    }

    @Override
    public List<SoutboundDetailDTO> getOutboundDetails(String outboundNo) {
        return mapper.selectOutboundDetails(outboundNo);
    }

    @Override
    @Transactional
    public String createOutbound(SalesOutboundDTO dto) {
        String outboundNo = UUID.randomUUID().toString();
        dto.setOutboundNo(outboundNo);

        String coIdx = AuthUtil.getCoIdx();
        dto.setCoIdx(coIdx);

        mapper.insertOutbound(dto);

        if (dto.getDetails() != null) {
            for (SoutboundDetailDTO d : dto.getDetails()) {
                d.setOrderNo(outboundNo);
                d.setCoIdx(coIdx);
                mapper.insertOutboundDetail(d);
            }
        }
        return outboundNo;
    }
}
