package com.eflix.hr.mapper;

import java.util.List;

import com.eflix.hr.dto.GradeDTO;

public interface GradeMapper {
    public List<GradeDTO> findAllByCoIdx(String coIdx);
}
