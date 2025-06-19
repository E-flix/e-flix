/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 사원 CRUD용 Mybatis Mapper
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김어진): Mapper 생성
============================================ */
package com.eflix.hr.mapper;

import java.util.List;

import org.springframework.data.repository.query.Param;

import com.eflix.hr.dto.EmployeesDTO;

public interface EmployeesMapper {
    List<EmployeesDTO> selectAll();
    EmployeesDTO selectById(@Param("empIdx") String empIdx);
    int insert(EmployeesDTO dto);
    int update(EmployeesDTO dto);
    int deleteById(@Param("empIdx") String empIdx);
}