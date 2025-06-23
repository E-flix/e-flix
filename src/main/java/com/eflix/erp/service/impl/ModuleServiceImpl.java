package com.eflix.erp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eflix.erp.dto.ModuleDTO;
import com.eflix.erp.mapper.ModuleMapper;
import com.eflix.erp.service.ModuleService;

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