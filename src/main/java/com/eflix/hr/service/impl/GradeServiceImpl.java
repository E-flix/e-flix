package com.eflix.hr.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eflix.hr.dto.GradeDTO;
import com.eflix.hr.dto.etc.GradeSearchDTO;
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

    @Override
    public int findAllGrdCount(GradeSearchDTO gradeSearchDTO) {
        return gradeMapper.findAllGrdCount(gradeSearchDTO);
    }

    @Override
    public int insert(GradeDTO gradeDTO) {
        return gradeMapper.insert(gradeDTO);
    }

    @Override
    public List<GradeDTO> findAllGrdBySearch(GradeSearchDTO gradeSearchDTO) {
        return gradeMapper.findAllGrdBySearch(gradeSearchDTO);
    }

    @Override
    public GradeDTO findByGrdIdx(String grdIdx) {
        return gradeMapper.findByGrdIdx(grdIdx);
    }

    @Override
    public int updateByGrdIdx(String grdIdx) {
        return gradeMapper.updateByGrdIdx(grdIdx);
    }

    @Override
    public int deleteByGrdIdx(String grdIdx) {
        return gradeMapper.deleteByGrdIdx(grdIdx);
    }
    
}
