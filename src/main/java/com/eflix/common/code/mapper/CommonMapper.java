package com.eflix.common.code.mapper;

import java.util.List;

import com.eflix.common.code.dto.CommonDTO;

public interface CommonMapper {
    public List<CommonDTO> getCommon(String groupCode);
}
