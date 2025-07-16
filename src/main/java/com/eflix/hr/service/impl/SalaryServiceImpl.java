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
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.eflix.hr.dto.SalaryDTO;
import com.eflix.hr.dto.etc.SalaryCalcDTO;
import com.eflix.hr.dto.etc.SalaryDetailDTO;
import com.eflix.hr.dto.etc.SalaryEmpDTO;
import com.eflix.hr.dto.etc.SalaryFullDetailDTO;
import com.eflix.hr.dto.etc.SalaryListDTO;
import com.eflix.hr.dto.etc.SalarySearchDTO;
import com.eflix.hr.dto.etc.SalarySummaryDTO;
import com.eflix.hr.mapper.SalaryMapper;
import com.eflix.hr.service.SalaryService;

import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SalaryServiceImpl implements SalaryService {

    @Autowired
    private SalaryMapper salaryMapper;

    @Override
    public List<SalaryDTO> bankList() {
        return salaryMapper.bankList();
    }

    @Override
    public List<SalarySummaryDTO> findSalaryList(String coIdx, String attMonth, String payMonth, String empName,
            String deptIdx) {

        return salaryMapper.findSalaryList(coIdx, attMonth, payMonth, empName, deptIdx);
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

    @Override
    public int insertSalary(SalaryDTO salaryDTO) {
        return salaryMapper.insertSalary(salaryDTO);
    }

    @Override
    public int findAllCountBySearch(SalarySearchDTO salarySearchDTO) {
        return salaryMapper.findAllCountBySearch(salarySearchDTO);
    }

    @Override
    public List<SalaryListDTO> findAllBySearch(SalarySearchDTO salarySearchDTO) {
        return salaryMapper.findAllBySearch(salarySearchDTO);
    }

    @Override
    public boolean calcSalary(SalaryCalcDTO salaryCalcDTO) {
        try {
            salaryMapper.calcSalary(salaryCalcDTO);
            return true;
        } catch (DataAccessException | PersistenceException ex) {
            // 로깅
            log.error("급여 계산 프로시저 호출 실패. empDate={}, coIdx={}", salaryCalcDTO.getDate(), salaryCalcDTO.getCoIdx(), ex);
            return false;
        }
    }

    @Override
    public int calcSalaryByEmpIdx(SalaryCalcDTO salaryCalcDTO) {
        int successCount = 0;

        try {
            for (String salaryIdx : salaryCalcDTO.getEmpIdxList()) {
                salaryCalcDTO.setEmpIdx(salaryIdx);
                salaryMapper.calcSalaryByEmpIdx(salaryCalcDTO);
                successCount++;
            }
            return successCount;
        } catch (DataAccessException | PersistenceException ex) {
            // 로깅
            log.error("급여 계산 프로시저 호출 실패. empDate={}, coIdx={}", salaryCalcDTO.getDate(), salaryCalcDTO.getCoIdx(), ex);
            return 0;
        }
    }

    @Override
    public SalaryEmpDTO salaryEmpInfo(String empIdx, String salaryIdx) {
        return salaryMapper.salaryEmpInfo(empIdx, salaryIdx);
    }

    @Override
    public List<SalaryDetailDTO> salaryDetailBySalaryIdxWithCoIdx(String salaryIdx, String coIdx) {
        return salaryMapper.salaryDetailBySalaryIdxWithCoIdx(salaryIdx, coIdx);
    }

    @Override
    public int confirmSalary(List<String> salaryIdxList) {
        int successCount = 0;

        for (String salaryIdx : salaryIdxList) {
            SalaryDTO dto = new SalaryDTO();
            dto.setSalaryIdx(salaryIdx);
            dto.setSalaryType("ST03");

            int affected = salaryMapper.updateSalary(dto);
            if (affected > 0) successCount++;
        }

        return successCount;
    }

    @Override
    public String findSalaryIdxByEmpIdxAndDate(String empIdx, String date) {
        return salaryMapper.findSalaryIdxByEmpIdxAndDate(empIdx, date);
    }

    @Override
    public int findCountByEmpIdx(SalarySearchDTO salarySearchDTO) {
        return salaryMapper.findCountByEmpIdx(salarySearchDTO);
    }

    @Override
    public List<SalaryListDTO> findByEmpIdx(SalarySearchDTO salarySearchDTO) {
        return salaryMapper.findByEmpIdx(salarySearchDTO);
    }
}
