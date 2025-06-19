/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 부서 CRUD용 Mybatis Mapper
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김어진): Mapper 생성
============================================ */
package com.eflix.hr.mapper;

import java.util.List;

import org.springframework.data.repository.query.Param;

import com.eflix.hr.dto.DepartmentsDTO;

public interface DepartmentsMapper {
    List<DepartmentsDTO> selectAll();
    DepartmentsDTO selectById(@Param("deptIdx") String deptIdx);
    int insert(DepartmentsDTO dto);
    int update(DepartmentsDTO dto);
    int deleteById(@Param("deptIdx") String deptIdx);
}
