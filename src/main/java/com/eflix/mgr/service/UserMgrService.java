package com.eflix.mgr.service;

import java.util.List;

import com.eflix.mgr.dto.EmployeeRoleDTO;
import com.eflix.mgr.dto.UserMgrDTO;

public interface UserMgrService {

    public int insertUser(UserMgrDTO mgrUserDTO);

    public List<UserMgrDTO> findAllUserByCoIdx(String coIdx);

    public List<EmployeeRoleDTO> findAllEmployeeRoleDTO();

    public int updateUser(UserMgrDTO userMgrDTO);
}
