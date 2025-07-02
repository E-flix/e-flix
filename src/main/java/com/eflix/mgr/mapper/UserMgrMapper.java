package com.eflix.mgr.mapper;

import java.util.List;

import com.eflix.mgr.dto.EmployeeRoleDTO;
import com.eflix.mgr.dto.ErpEmployeeRoleDTO;
import com.eflix.mgr.dto.UserMgrDTO;

public interface UserMgrMapper {
    public int insertUser(UserMgrDTO mgrUserDTO);
    public int insertErpEmpRole(ErpEmployeeRoleDTO employeeRoleDTO);
    public List<UserMgrDTO> findAllUser();
    public List<EmployeeRoleDTO> findAllEmployeeRole();
    public int updateUser(UserMgrDTO userMgrDTO);
}
