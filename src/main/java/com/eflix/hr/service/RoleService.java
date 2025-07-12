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

import com.eflix.hr.dto.RoleDTO;

public interface RoleService {
    public List<RoleDTO> getAllRoles();
    public RoleDTO getRoleById(String roleId);
    int createRole(RoleDTO dto);
    int updateRole(RoleDTO dto);
    int deleteRole(String roleId);
    public List<RoleDTO> roleList();

    // 0712
    public List<RoleDTO> findAllByCoIdx(String coIdx);
}