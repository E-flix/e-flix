/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 급여 매핑 관리 Service
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김어진): 인터페이스 생성
============================================ */
package com.eflix.hr.service;

import java.util.List;

import com.eflix.hr.dto.etc.SalaryMappingDTO;
import com.eflix.hr.dto.etc.SalaryMappingSearchDTO;

public interface SalaryMappingService {
  
    // 0712
    public int findAllItemCount(SalaryMappingSearchDTO salaryMappingSearchDTO);

    public List<SalaryMappingDTO> findAllBySearch(SalaryMappingSearchDTO salaryMappingSearchDTO);

    public int insert(SalaryMappingDTO salaryMappingDTO);

    public SalaryMappingDTO findByMpIdx(String mpIdx);

    public int update(SalaryMappingDTO salaryMappingDTO);

    public int delete(String mpIdx);

    
    public List<SalaryMappingDTO> findAllByCoIdx(String coIdx);

    // public SalaryMappingDTO findByMpIdx(String coIdx, String mpIdx);

    // public void insert(SalaryMappingDTO salaryMappingDTO);

    // public void update(SalaryMappingDTO salaryMappingDTO);

    // public void delete(String salaryIdx);
}
