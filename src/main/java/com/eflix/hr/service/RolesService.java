/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 권한 Service
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김어진): 인터페이스 생성
============================================ */
package com.eflix.hr.service;

import java.util.List;

import com.eflix.hr.dto.RolesDTO;

public interface RolesService {
    public List<RolesDTO> getAllRoles();
    public RolesDTO getRoleById(String roleId);
    int createRole(RolesDTO dto);
    int updateRole(RolesDTO dto);
    int deleteRole(String roleId);
}