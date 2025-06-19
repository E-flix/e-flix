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

import com.eflix.hr.dto.RolesDTO;
import com.eflix.hr.service.RolesService;

@Service
public class RolesServiceImpl implements RolesService {

  @Override
  public List<RolesDTO> getAllRoles() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getAllRoles'");
  }

  @Override
  public RolesDTO getRoleById(String roleId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getRoleById'");
  }

  @Override
  public int createRole(RolesDTO dto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'createRole'");
  }

  @Override
  public int updateRole(RolesDTO dto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'updateRole'");
  }

  @Override
  public int deleteRole(String roleId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deleteRole'");
  }

}
