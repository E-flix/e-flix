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

import org.springframework.stereotype.Service;

import com.eflix.hr.dto.RoleDTO;
import com.eflix.hr.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

  @Override
  public List<RoleDTO> getAllRoles() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getAllRoles'");
  }

  @Override
  public RoleDTO getRoleById(String roleId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getRoleById'");
  }

  @Override
  public int createRole(RoleDTO dto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'createRole'");
  }

  @Override
  public int updateRole(RoleDTO dto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'updateRole'");
  }

  @Override
  public int deleteRole(String roleId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deleteRole'");
  }

}
