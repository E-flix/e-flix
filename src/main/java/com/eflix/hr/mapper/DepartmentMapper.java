/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 부서 CRUD용 Mybatis Mapper
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김어진): Mapper 생성
  - 2025-06-23 (김어진): 조건별 조회 구현
  - 2025-06-26 (김어진): 부서등록 구현
============================================ */
package com.eflix.hr.mapper;

import java.util.List;

import org.springframework.data.repository.query.Param;

import com.eflix.hr.dto.DepartmentDTO;

public interface DepartmentMapper {
    // 부서조회
    List<DepartmentDTO> selectAll(DepartmentDTO departmentDTO);

    // 부서등록
    public int insertDept(DepartmentDTO dept);
    
    DepartmentDTO selectById(@Param("deptIdx") String deptIdx);
    int insert(DepartmentDTO dto);
    int update(DepartmentDTO dto);
    int deleteById(@Param("deptIdx") String deptIdx);

    List<DepartmentDTO> findAllDepts(String coIdx);
    List<DepartmentDTO> findAllDeptsUp(String coIdx, String deptIdx);

    
    List<DepartmentDTO> findUpAllByCoIdx(String coIdx);
    List<DepartmentDTO> findDownAllByCoIdx(String coIdx, String deptUpIdx);

    DepartmentDTO findByEmpIdx(String empIdx);

}
