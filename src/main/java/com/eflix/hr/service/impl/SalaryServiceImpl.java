/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 급여 ServiceImpl
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김어진): 클래스 생성
============================================ */
package com.eflix.hr.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eflix.hr.dto.SalaryDTO;
import com.eflix.hr.mapper.SalaryMapper;
import com.eflix.hr.service.SalaryService;

@Service
public class SalaryServiceImpl implements SalaryService {

  @Autowired
  private SalaryMapper salaryMapper;

  @Override
  public List<SalaryDTO> getAllSalaries() {
    throw new UnsupportedOperationException("Unimplemented method 'getAllSalaries'");
  }

  @Override
  public SalaryDTO getSalaryById(String salaryIdx) {
    throw new UnsupportedOperationException("Unimplemented method 'getSalaryById'");
  }

  @Override
  public int createSalary(SalaryDTO dto) {
    throw new UnsupportedOperationException("Unimplemented method 'createSalary'");
  }

  @Override
  public int updateSalary(SalaryDTO dto) {
    throw new UnsupportedOperationException("Unimplemented method 'updateSalary'");
  }

  @Override
  public int deleteSalary(String salaryIdx) {
    throw new UnsupportedOperationException("Unimplemented method 'deleteSalary'");
  }

  @Override
  public List<SalaryDTO> bankList() {
    return salaryMapper.bankList();
  }

}
