package com.eflix.main.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eflix.main.dto.MasterDTO;
import com.eflix.main.mapper.MasterMapper;
import com.eflix.main.service.MasterService;

@Service
public class MasterServiceImpl implements MasterService {

    @Autowired
    private MasterMapper masterMapper;

    @Override
    public int insertMaster(MasterDTO masterDTO) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'insertMaster'");

        return masterMapper.insertMaster(masterDTO);
    }
    
}
