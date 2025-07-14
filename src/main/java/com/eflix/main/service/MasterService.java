package com.eflix.main.service;

import com.eflix.main.dto.MasterDTO;

public interface MasterService {
    public int insertMaster(MasterDTO masterDTO);

    public int existMstId(String mstId);
}
