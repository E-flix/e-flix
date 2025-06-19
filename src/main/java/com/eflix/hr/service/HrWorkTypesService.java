/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 인사 근무 유형 Service
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김어진): 인터페이스 생성
============================================ */
package com.eflix.hr.service;

import java.util.List;

import com.eflix.hr.dto.HrWorkTypesDTO;

public interface HrWorkTypesService {
    public List<HrWorkTypesDTO> getAllWorkTypes();
    public HrWorkTypesDTO getWorkTypeById(String workTypeId);
    int createWorkType(HrWorkTypesDTO dto);
    int updateWorkType(HrWorkTypesDTO dto);
    int deleteWorkType(String workTypeId);
}