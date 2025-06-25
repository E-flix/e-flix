/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 권한 ServiceImpl
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김어진): 클래스 생성
============================================ */
package com.eflix.hr.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eflix.hr.dto.RoleDTO;
import com.eflix.hr.mapper.RoleMapper;
import com.eflix.hr.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

  @Autowired
  private RoleMapper roleMapper;

  @Override
  public List<RoleDTO> getAllRoles() {
    throw new UnsupportedOperationException("Unimplemented method 'getAllRoles'");
  }

  @Override
  public RoleDTO getRoleById(String roleId) {
    throw new UnsupportedOperationException("Unimplemented method 'getRoleById'");
  }

  @Override
  public int createRole(RoleDTO dto) {
    throw new UnsupportedOperationException("Unimplemented method 'createRole'");
  }

  @Override
  public int updateRole(RoleDTO dto) {
    throw new UnsupportedOperationException("Unimplemented method 'updateRole'");
  }

  @Override
  public int deleteRole(String roleId) {
    throw new UnsupportedOperationException("Unimplemented method 'deleteRole'");
  }

  // 사원관리 페이지 권한 드롭다운
  @Override
  public List<RoleDTO> roleList() {
    return roleMapper.roleList();
  }

}
