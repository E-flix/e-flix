/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 권한 CRUD용 Mybatis Mapper
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김어진): Mapper 생성
============================================ */
package com.eflix.hr.mapper;

import java.util.List;

import org.springframework.data.repository.query.Param;

import com.eflix.hr.dto.RoleDTO;

public interface RoleMapper {
    List<RoleDTO> selectAll();
    RoleDTO selectById(@Param("roleId") String roleId);
    int insert(RoleDTO dto);
    int update(RoleDTO dto);
    int deleteById(@Param("roleId") String roleId);
    
    List<String> findRoleIdsByEmpIdx(String empIdx);

    List<RoleDTO> roleList();
}
