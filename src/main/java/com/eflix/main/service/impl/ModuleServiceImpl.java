package com.eflix.main.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eflix.main.dto.ModuleDTO;
import com.eflix.main.mapper.ModuleMapper;
import com.eflix.main.service.ModuleService;

@Service
public class ModuleServiceImpl implements ModuleService {

    @Autowired
    private ModuleMapper mapper;

    @Override
    public List<ModuleDTO> findAll() {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'findAll'");
        return mapper.findAll();
    }
}