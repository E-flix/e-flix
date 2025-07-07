package com.eflix.bsn.mapper;

import com.eflix.bsn.dto.SalesOutboundDTO;
import com.eflix.bsn.dto.SoutboundDetailDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SOutboundMapper {
    List<SalesOutboundDTO> selectAllOutbound(@Param("coIdx") String coIdx);
    List<SoutboundDetailDTO> selectOutboundDetails(@Param("outboundNo") String outboundNo);

    int insertOutbound(SalesOutboundDTO dto);
    int insertOutboundDetail(SoutboundDetailDTO dto);
}
