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
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eflix.hr.dto.SalaryDTO;
import com.eflix.hr.dto.etc.SalaryDetailDTO;
import com.eflix.hr.dto.etc.SalaryFullDetailDTO;
import com.eflix.hr.dto.etc.SalarySummaryDTO;
import com.eflix.hr.mapper.SalaryMapper;
import com.eflix.hr.service.SalaryService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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

  @Override
  public List<SalarySummaryDTO> findSalaryList(String coIdx, String salaryMonth, String payMonth, String empName,
      String deptIdx) {

    return salaryMapper.findSalaryList(coIdx, salaryMonth, payMonth, empName, deptIdx);
  }

  @Override
  public List<SalaryDetailDTO> findSalaryDetail(String coIdx, String salaryIdx) {

    return salaryMapper.findSalaryDetail(coIdx, salaryIdx);
  }

  @Override
  public List<SalaryFullDetailDTO> getSalaryDetailItems(String coIdx, String salaryIdx) {
    return salaryMapper.getSalaryDetailItems(coIdx, salaryIdx);
  }

  @Override
  public void calculateSalary(String coIdx, List<String> salaryIdxList) {
    for (String salaryIdx : salaryIdxList) {
      log.info("회사코드 : {} , 급여 번호 : {}", coIdx, salaryIdx);
        salaryMapper.calculateSalary(coIdx, salaryIdx);
    }
  }

  @Override
  public void confirmSalary(Map<String, Object> map) {
    salaryMapper.confirmSalary(map);
  }

  @Override
  public List<SalaryDetailDTO> selectSalaryDetail(String coIdx, String salaryIdx) {
    return salaryMapper.selectSalaryDetail(coIdx, salaryIdx);
  }

}
