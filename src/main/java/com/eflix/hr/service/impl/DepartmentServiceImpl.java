/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 부서 ServiceImpl
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김어진): 클래스 생성
  - 2025-06-23 (김어진): 조건별 조회 구현
  - 2025-06-23 (김어진): 부서등록구현
============================================ */
package com.eflix.hr.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.eflix.hr.dto.DepartmentDTO;
import com.eflix.hr.mapper.DepartmentMapper;
import com.eflix.hr.service.DepartmentService;

@Service
public class DepartmentServiceImpl implements DepartmentService{

  @Autowired
  private DepartmentMapper departmentMapper;

  private String coIdx = "co-101";

  public void initAuth() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    // 1) Authentication 자체가 AnonymousAuthenticationToken 이면 비로그인
    if (auth == null || auth instanceof AnonymousAuthenticationToken) {
        // 비로그인 처리
        coIdx = "co-101";
        System.out.println("로그인 안 함");
        return;
    }

    // 2) principal 이 UserDetails 가 아니면 (익명 문자열인 경우) 비로그인
    Object principal = auth.getPrincipal();
    if (!(principal instanceof UserDetails)) {
        // 비로그인 처리
        System.out.println("로그인 안 함 (principal is " + principal + ")");
        return;
    }
  }

  // 부서조회
  @Override
  public List<DepartmentDTO> selectAll(DepartmentDTO departmentDTO) {
    initAuth();
    departmentDTO.setCoIdx(coIdx);
    return departmentMapper.selectAll(departmentDTO);
  }

    // 부서등록
  @Override
  public int insertDept(DepartmentDTO dept) {
    initAuth();
    dept.setCoIdx(coIdx);
    return departmentMapper.insertDept(dept);
  }
  
  @Override
  public DepartmentDTO getDepartmentById(String deptIdx) {
    throw new UnsupportedOperationException("Unimplemented method 'getDepartmentById'");
  }

  @Override
  public int createDepartment(DepartmentDTO dto) {
    throw new UnsupportedOperationException("Unimplemented method 'createDepartment'");
  }

  @Override
  public int updateDepartment(DepartmentDTO dto) {
    throw new UnsupportedOperationException("Unimplemented method 'updateDepartment'");
  }

  @Override
  public int deleteDepartment(String deptIdx) {
    throw new UnsupportedOperationException("Unimplemented method 'deleteDepartment'");
  }

  @Override
  public List<DepartmentDTO> findAllDepts() {
    initAuth();
    return departmentMapper.findAllDepts(coIdx);
  }

  @Override
  public List<DepartmentDTO> findAllDeptsUP(String deptIdx) {
    initAuth();
    return departmentMapper.findAllDeptsUp(coIdx, deptIdx);
  }


}
