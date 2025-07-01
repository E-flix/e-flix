/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 인사 근무 유형 Service
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김어진): 인터페이스 생성
  - 2025-06-19 (김어진): 근무유형 드롭다운 구현
============================================ */
package com.eflix.hr.service;

import java.util.List;

import com.eflix.hr.dto.HrWorkTypeDTO;

public interface HrWorkTypeService {
    public List<HrWorkTypeDTO> getAllWorkTypes();
    public HrWorkTypeDTO getWorkTypeById(String workTypeId);
    int createWorkType(HrWorkTypeDTO dto);
    int updateWorkType(HrWorkTypeDTO dto);
    int deleteWorkType(String workTypeId);
    
    // 근무유형 드롭다운
    public List<HrWorkTypeDTO> workList();
}