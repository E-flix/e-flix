package com.eflix.main.mapper;

import com.eflix.main.dto.MasterDTO;

public interface MasterMapper {
    public int insertMaster(MasterDTO masterDTO);

    // 0714
    public int existMstId(String masterId);
}
