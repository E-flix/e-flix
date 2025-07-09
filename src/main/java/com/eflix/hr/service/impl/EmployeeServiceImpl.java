/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 사원 ServiceImpl
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김어진): 클래스 생성
  - 2025-06-23 (김어진): 조건별 조회기능 구현
  - 2025-06-24 (김어진): 사원관리 드롭다운 구현
  - 2025-06-26 (김어진): 사원수정 구현
============================================ */
package com.eflix.hr.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eflix.common.security.auth.AuthContext;
import com.eflix.common.security.auth.AuthUtil;
import com.eflix.hr.dto.EmployeeDTO;
import com.eflix.hr.dto.SalaryDTO;
import com.eflix.hr.dto.etc.EmpSearchDTO;
import com.eflix.hr.mapper.EmployeeMapper;
import com.eflix.hr.mapper.SalaryMapper;
import com.eflix.hr.service.EmployeeService;
import com.eflix.hr.service.SalaryService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    EmployeeMapper employeeMapper;

    @Autowired
    private SalaryMapper salaryMapper;

    @Override
    public EmployeeDTO selectById(String empIdx) {
        return employeeMapper.selectById(AuthUtil.getCoIdx(), empIdx);
    }

    @Override
    public int updateEmployee(EmployeeDTO dto) {
        return employeeMapper.update(dto);
    }

    // 사원관리 페이지 직급 드롭다운용 조회
    @Override
    public List<EmployeeDTO> gradeList() {
        return employeeMapper.gradeList();
    }

    // 재직 상태 드롭다운 조회
    @Override
    public List<EmployeeDTO> empStatusList() {
        return employeeMapper.empStatusList();
    }

    @Override
    public List<EmployeeDTO> getAllEmployees(Map<String, Object> params) {
        params.put("coIdx", AuthUtil.getCoIdx());
        return employeeMapper.selectAll(params);
    }

    // 사원등록
    @Override
    @Transactional
    public int insertEmp(EmployeeDTO emp) {
        if (employeeMapper.insertEmp(emp) <= 0) {
            return 0;
        }

        SalaryDTO salaryDTO = new SalaryDTO();
        salaryDTO.setEmpIdx(emp.getEmpIdx());
        salaryDTO.setBaseSalary(emp.getBaseSalary());

        return salaryMapper.insert(salaryDTO);
    }

    // 0708
    public List<EmployeeDTO> findAllEmployee(EmployeeDTO employeeDTO) {
        return employeeMapper.findAllEmployee(employeeDTO);
    }

    @Override
    public Date findAllEmpRegdateByEmpIdx(String empIdx) {
        return employeeMapper.findAllEmpRegdateByEmpIdx(empIdx);
    }

    @Override
    public List<EmployeeDTO> findAllEmployeeSearch(EmpSearchDTO empSearchDTO) {
        return employeeMapper.findAllEmployeeSearch(empSearchDTO);
    }

    @Override
    public int findAllEmpCount(EmpSearchDTO empSearchDTO) {
        return employeeMapper.findAllEmpCount(empSearchDTO);
    }
}
