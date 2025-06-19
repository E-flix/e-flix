/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 부서 이력 관리 CRUD용 Mybatis Mapper
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김어진): Mapper 생성
============================================ */
package com.eflix.hr.mapper;

import java.util.List;

import org.springframework.data.repository.query.Param;

import com.eflix.hr.dto.DepartmentRecordDTO;

public interface DepartmentRecordMapper {
    List<DepartmentRecordDTO> selectAll();
    DepartmentRecordDTO selectById(@Param("deptHistId") String deptHistId);
    int insert(DepartmentRecordDTO dto);
    int update(DepartmentRecordDTO dto);
    int deleteById(@Param("deptHistId") String deptHistId);
}
