/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 사원 CRUD용 Mybatis Mapper
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김어진): Mapper 생성
  - 2025-06-23 (김어진): 사원관리 페이지 조건검색 구현
============================================ */
package com.eflix.hr.mapper;

import java.util.List;

import org.springframework.data.repository.query.Param;

import com.eflix.hr.dto.EmployeeDTO;

public interface EmployeeMapper {

    public EmployeeDTO findByEmpEmailAndCompany(String empEmail, String coIdx);

    // 사원관리 페이지 검색조건 드롭다운 조회
    List<EmployeeDTO> selectAll(@Param("option") String option, @Param("keyword") String keyword);

    EmployeeDTO selectById(@Param("empIdx") String empIdx);

    // 사원관리 페이지 직급 드롭다운 조회
    List<EmployeeDTO> gradeList();

    int insert(EmployeeDTO dto);
    int update(EmployeeDTO dto);
    int deleteById(@Param("empIdx") String empIdx);
}
