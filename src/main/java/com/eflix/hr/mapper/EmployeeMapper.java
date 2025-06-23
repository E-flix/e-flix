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

import com.eflix.hr.dto.DepartmentDTO;
import com.eflix.hr.dto.EmployeeDTO;

public interface EmployeeMapper {

    public EmployeeDTO findByEmpEmailAndCompany(String empEmail, String coIdx);
    List<EmployeeDTO> selectAll();
    EmployeeDTO selectById(@Param("empIdx") String empIdx);
    int insert(EmployeeDTO dto);
    int update(EmployeeDTO dto);
    int deleteById(@Param("empIdx") String empIdx);
}
