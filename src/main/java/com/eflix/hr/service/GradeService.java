package com.eflix.hr.service;

import java.util.List;

import com.eflix.hr.dto.GradeDTO;
import com.eflix.hr.dto.etc.GradeSearchDTO;

public interface GradeService {
    public List<GradeDTO> findAllByCoIdx(String coIdx);

    // 0716
    public int findAllGrdCount(GradeSearchDTO gradeSearchDTO);

    public List<GradeDTO> findAllGrdBySearch(GradeSearchDTO gradeSearchDTO);

    public int insert(GradeDTO gradeDTO);

    public GradeDTO findByGrdIdx(String grdIdx);

    public int updateByGrdIdx(String grdIdx);

    public int deleteByGrdIdx(String grdIdx);
}
