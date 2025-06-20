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

import com.eflix.hr.dto.RolesDTO;

public interface RolesMapper {
    List<RolesDTO> selectAll();
    RolesDTO selectById(@Param("roleId") String roleId);
    int insert(RolesDTO dto);
    int update(RolesDTO dto);
    int deleteById(@Param("roleId") String roleId);
}
