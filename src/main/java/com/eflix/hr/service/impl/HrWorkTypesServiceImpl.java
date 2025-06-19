/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 인사 근무 유형 ServiceImpl
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김어진): 클래스 생성
============================================ */
package com.eflix.hr.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eflix.hr.dto.HrWorkTypesDTO;
import com.eflix.hr.service.HrWorkTypesService;

@Service
public class HrWorkTypesServiceImpl implements HrWorkTypesService {

  @Override
  public List<HrWorkTypesDTO> getAllWorkTypes() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getAllWorkTypes'");
  }

  @Override
  public HrWorkTypesDTO getWorkTypeById(String workTypeId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getWorkTypeById'");
  }

  @Override
  public int createWorkType(HrWorkTypesDTO dto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'createWorkType'");
  }

  @Override
  public int updateWorkType(HrWorkTypesDTO dto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'updateWorkType'");
  }

  @Override
  public int deleteWorkType(String workTypeId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deleteWorkType'");
  }

}
