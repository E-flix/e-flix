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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eflix.hr.dto.etc.SalaryMappingDTO;
import com.eflix.hr.mapper.SalaryMappingMapper;
import com.eflix.hr.service.SalaryMappingService;

@Service
public class SalaryMappingServiceImpl implements SalaryMappingService {

  @Autowired
  private SalaryMappingMapper salaryMappingMapper;

  @Override
  public List<SalaryMappingDTO> getAllSalaryMappings() {
    throw new UnsupportedOperationException("Unimplemented method 'getAllSalaryMappings'");
  }

  @Override
  public SalaryMappingDTO getSalaryMappingById(String mpIdx) {
    throw new UnsupportedOperationException("Unimplemented method 'getSalaryMappingById'");
  }

  @Override
  public int createSalaryMapping(SalaryMappingDTO dto) {
    throw new UnsupportedOperationException("Unimplemented method 'createSalaryMapping'");
  }

  @Override
  public int updateSalaryMapping(SalaryMappingDTO dto) {
    throw new UnsupportedOperationException("Unimplemented method 'updateSalaryMapping'");
  }

  @Override
  public int deleteSalaryMapping(String mpIdx) {
    throw new UnsupportedOperationException("Unimplemented method 'deleteSalaryMapping'");
  }

  @Override
  public List<SalaryMappingDTO> findAllByCoIdx(String coIdx) {
    return salaryMappingMapper.findAllByCoIdx(coIdx);
  }

  @Override
  public void insert(SalaryMappingDTO salaryMappingDTO) {
    salaryMappingMapper.insert(salaryMappingDTO);
  }

  @Override
  public void modify(SalaryMappingDTO salaryMappingDTO) {
    salaryMappingMapper.modify(salaryMappingDTO);
  }

  @Override
  public void delete(String salaryIdx) {
    salaryMappingMapper.delete(salaryIdx);
  }

}
