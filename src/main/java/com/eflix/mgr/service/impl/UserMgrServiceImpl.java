package com.eflix.mgr.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eflix.mgr.dto.EmployeeRoleDTO;
import com.eflix.mgr.dto.UserMgrDTO;
import com.eflix.mgr.mapper.UserMgrMapper;
import com.eflix.mgr.service.UserMgrService;

@Service
public class UserMgrServiceImpl implements UserMgrService {

    @Autowired
    private UserMgrMapper userMgrMapper;

    @Override
    public int insertUser(UserMgrDTO mgrUserDTO) {
        return userMgrMapper.insertUser(mgrUserDTO);
    }

    @Override
    public List<UserMgrDTO> findAllUser() {
        return userMgrMapper.findAllUser();
    }

    @Override
    public List<EmployeeRoleDTO> findAllEmployeeRoleDTO() {
        return userMgrMapper.findAllEmployeeRole();
    }

    @Override
    public int updateUser(UserMgrDTO userMgrDTO) {
        return userMgrMapper.updateUser(userMgrDTO);
    }

}
