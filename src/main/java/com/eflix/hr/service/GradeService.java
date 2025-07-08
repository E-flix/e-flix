package com.eflix.hr.service;

import java.util.List;

import com.eflix.hr.dto.GradeDTO;

public interface GradeService {
    public List<GradeDTO> findAllByCoIdx(String coIdx);
}
