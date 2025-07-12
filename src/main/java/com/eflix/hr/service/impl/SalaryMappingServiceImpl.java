/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 급여 매핑 관리 ServiceImpl
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김어진): 클래스 생성
  - 2025-07-07 (복성민): 급여 매핑 조회, 추가, 수정, 삭제 추가
============================================ */
package com.eflix.hr.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eflix.hr.dto.etc.SalaryMappingDTO;
import com.eflix.hr.dto.etc.SalaryMappingSearchDTO;
import com.eflix.hr.mapper.SalaryMappingMapper;
import com.eflix.hr.service.SalaryMappingService;

@Service
public class SalaryMappingServiceImpl implements SalaryMappingService {

    @Autowired
    private SalaryMappingMapper salaryMappingMapper;

    @Override
    public int findAllItemCount(SalaryMappingSearchDTO salaryMappingSearchDTO) {
        return salaryMappingMapper.findAllItemCount(salaryMappingSearchDTO);
    }

    @Override
    public List<SalaryMappingDTO> findAllBySearch(SalaryMappingSearchDTO salaryMappingSearchDTO) {
        return salaryMappingMapper.findAllBySearch(salaryMappingSearchDTO);
    }

    @Override
    public int insert(SalaryMappingDTO salaryMappingDTO) {
        return salaryMappingMapper.insert(salaryMappingDTO);
    }

    @Override
    public SalaryMappingDTO findByMpIdx(String mpIdx) {
        return salaryMappingMapper.findByMpIdx(mpIdx);
    }

    @Override
    public int update(SalaryMappingDTO salaryMappingDTO) {
        return salaryMappingMapper.update(salaryMappingDTO);
    }

    @Override
    public int delete(String mpIdx) {
        return salaryMappingMapper.delete(mpIdx);
    }

    @Override
    public List<SalaryMappingDTO> findAllByCoIdx(String coIdx) {
        return salaryMappingMapper.findAllByCoIdx(coIdx);
    }

    // @Override
    // public SalaryMappingDTO findByMpIdx(String coIdx, String mpIdx) {
    //     return salaryMappingMapper.findByMpIdx(coIdx, mpIdx);
    // }

    // @Override
    // public void insert(SalaryMappingDTO salaryMappingDTO) {
    //     salaryMappingMapper.insert(salaryMappingDTO);
    // }

    // @Override
    // public void update(SalaryMappingDTO salaryMappingDTO) {
    //     salaryMappingMapper.update(salaryMappingDTO);
    // }

    // @Override
    // public void delete(String salaryIdx) {
    //     salaryMappingMapper.delete(salaryIdx);
    // }
}
