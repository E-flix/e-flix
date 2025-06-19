/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 급여 매핑 관리 ServiceImpl
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김어진): 클래스 생성
============================================ */
package com.eflix.hr.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eflix.hr.dto.SalaryMappingDTO;
import com.eflix.hr.service.SalaryMappingService;

@Service
public class SalaryMappingServiceImpl implements SalaryMappingService {

  @Override
  public List<SalaryMappingDTO> getAllSalaryMappings() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getAllSalaryMappings'");
  }

  @Override
  public SalaryMappingDTO getSalaryMappingById(String mpIdx) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getSalaryMappingById'");
  }

  @Override
  public int createSalaryMapping(SalaryMappingDTO dto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'createSalaryMapping'");
  }

  @Override
  public int updateSalaryMapping(SalaryMappingDTO dto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'updateSalaryMapping'");
  }

  @Override
  public int deleteSalaryMapping(String mpIdx) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deleteSalaryMapping'");
  }

}
