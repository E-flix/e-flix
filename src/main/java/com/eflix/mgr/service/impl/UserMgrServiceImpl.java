package com.eflix.mgr.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.eflix.common.security.auth.AuthContext;
import com.eflix.common.security.auth.AuthUtil;
import com.eflix.hr.dto.EmployeeDTO;
import com.eflix.hr.mapper.EmployeeMapper;
import com.eflix.mgr.dto.EmployeeRoleDTO;
import com.eflix.mgr.dto.UserMgrDTO;
import com.eflix.mgr.mapper.UserMgrMapper;
import com.eflix.mgr.service.UserMgrService;

@Service
public class UserMgrServiceImpl implements UserMgrService {

    @Autowired
    private UserMgrMapper userMgrMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String getCoIdx() {
        return AuthUtil.getCoIdx();
    }

    @Override
    public int insertUser(UserMgrDTO mgrUserDTO) {
        mgrUserDTO.getEmployeeDTO().setCoIdx(getCoIdx());
        mgrUserDTO.getEmployeeDTO().setEmpPw(passwordEncoder.encode(mgrUserDTO.getEmployeeDTO().getEmpPw()));
        return userMgrMapper.insertUser(mgrUserDTO);
    }

    @Override
    public List<UserMgrDTO> findAllUserByCoIdx(String coIdx) {
        return userMgrMapper.findAllUserByCoIdx(coIdx);
    }

    @Override
    public List<EmployeeRoleDTO> findAllEmployeeRoleDTO() {
        return userMgrMapper.findAllEmployeeRole();
    }

    @Override
    public int updateUser(UserMgrDTO userMgrDTO) {
        EmployeeDTO employeeDTO = userMgrDTO.getEmployeeDTO();

        EmployeeDTO existing = employeeMapper.findByEmpIdx(employeeDTO.getEmpIdx());
        String existingPw = existing.getEmpPw();

        String newPw = employeeDTO.getEmpPw();

        if (newPw == null || newPw.isBlank()) {
            employeeDTO.setEmpPw(existingPw);
        } else {
            employeeDTO.setEmpPw(passwordEncoder.encode(newPw));
        }
        return userMgrMapper.updateUser(userMgrDTO);
    }

}
