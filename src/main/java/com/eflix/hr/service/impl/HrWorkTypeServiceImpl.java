/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 인사 근무 유형 ServiceImpl
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김어진): 클래스 생성
  - 2025-06-24 (김어진): 근무유형 드롭다운 구현
============================================ */
package com.eflix.hr.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eflix.hr.dto.HrWorkTypeDTO;
import com.eflix.hr.mapper.HrWorkTypeMapper;
import com.eflix.hr.service.HrWorkTypeService;

@Service
public class HrWorkTypeServiceImpl implements HrWorkTypeService {

  @Autowired
  HrWorkTypeMapper hrWorkTypeMapper;

  @Override
  public List<HrWorkTypeDTO> getAllWorkTypes() {
    throw new UnsupportedOperationException("Unimplemented method 'getAllWorkTypes'");
  }

  @Override
  public HrWorkTypeDTO getWorkTypeById(String workTypeId) {
    throw new UnsupportedOperationException("Unimplemented method 'getWorkTypeById'");
  }

  @Override
  public int createWorkType(HrWorkTypeDTO dto) {
    throw new UnsupportedOperationException("Unimplemented method 'createWorkType'");
  }

  @Override
  public int updateWorkType(HrWorkTypeDTO dto) {
    throw new UnsupportedOperationException("Unimplemented method 'updateWorkType'");
  }

  @Override
  public int deleteWorkType(String workTypeId) {
    throw new UnsupportedOperationException("Unimplemented method 'deleteWorkType'");
  }


  // 근무유형 드롭다운 조회
  @Override
  public List<HrWorkTypeDTO> workList() {
    return hrWorkTypeMapper.workList();
  }
  }
