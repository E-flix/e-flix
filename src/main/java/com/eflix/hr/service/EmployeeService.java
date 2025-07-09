/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 사원 Service
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김어진): 인터페이스 생성
  - 2025-06-23 (김어진): 조건별 조회기능 구현
  - 2025-06-24 (김어진): 사원관리 페이지 구현
  - 2025-06-26 (김어진): 사원수정 모달창 구현
============================================ */
package com.eflix.hr.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.eflix.hr.dto.EmployeeDTO;
import com.eflix.hr.dto.etc.EmpSearchDTO;

public interface EmployeeService {
  // 사원관리 페이지 검색조건 드롭다운 조회
    
    public EmployeeDTO selectById(String empIdx);
    int updateEmployee(EmployeeDTO dto);

    // 사원관리 페이지 직급 드롭다운 조회 
    public List<EmployeeDTO> gradeList(); 

    // 재직상태 드롭다운 조회
    public List<EmployeeDTO> empStatusList();

    public List<EmployeeDTO> getAllEmployees(Map<String, Object> params);
    public int insertEmp(EmployeeDTO emp);

    // 0708
    public List<EmployeeDTO> findAllEmployee(EmployeeDTO employeeDTO);
    public Date findAllEmpRegdateByEmpIdx(String empIdx);

    // 0709
    public List<EmployeeDTO> findAllEmployeeSearch(EmpSearchDTO empSearchDTO);
    public int findAllEmpCount(EmpSearchDTO empSearchDTO);

    public int insert(EmployeeDTO employeeDTO);
    public EmployeeDTO findByEmpIdx(String empIdx);
    public int update(EmployeeDTO employeeDTO);

    public int mergeEmployee(EmployeeDTO employeeDTO);
}
