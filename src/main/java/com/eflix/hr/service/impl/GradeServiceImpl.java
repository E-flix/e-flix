package com.eflix.hr.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eflix.hr.dto.GradeDTO;
import com.eflix.hr.mapper.GradeMapper;
import com.eflix.hr.service.GradeService;

@Service
public class GradeServiceImpl implements GradeService{

    @Autowired
    private GradeMapper gradeMapper;

    @Override
    public List<GradeDTO> findAllByCoIdx(String coIdx) {
        return gradeMapper.findAllByCoIdx(coIdx);
    }
    
}
