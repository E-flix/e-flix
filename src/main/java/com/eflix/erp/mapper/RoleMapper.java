package com.eflix.erp.mapper;

import java.util.List;

public interface RoleMapper {

    List<String> findRoleIdsByEmpIdx(String empIdx);
    
}
