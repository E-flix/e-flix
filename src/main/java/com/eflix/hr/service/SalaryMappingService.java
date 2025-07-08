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

public interface SalaryMappingService {
    public List<SalaryMappingDTO> findAllByCoIdx(String coIdx);
    public SalaryMappingDTO findByMpIdx(String coIdx, String mpIdx);
    public void insert(SalaryMappingDTO salaryMappingDTO);
    public void update(SalaryMappingDTO salaryMappingDTO);
    public void delete(String salaryIdx);
}
