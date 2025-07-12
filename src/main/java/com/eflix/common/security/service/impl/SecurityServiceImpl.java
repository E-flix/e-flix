package com.eflix.common.security.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eflix.common.security.dto.SecurityEmpDTO;
import com.eflix.common.security.dto.SecurityMasterDTO;
import com.eflix.common.security.dto.SecurityUserDTO;
import com.eflix.common.security.mapper.SecurityMapper;
import com.eflix.common.security.service.SecurityService;

@Service
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    private SecurityMapper securityMapper;

    @Override
    public SecurityEmpDTO findEmpForLogin(String coIdx, String username) {
        return securityMapper.findEmpForLogin(coIdx, username);
    }

    @Override
    public List<String> findRoleCodesByEmpIdx(String empIdx) {
        return securityMapper.findRoleCodesByEmpIdx(empIdx);
    }

    @Override
    public SecurityMasterDTO findMasterForLogin(String coIdx, String username) {
        return securityMapper.findMasterForLogin(coIdx, username);
    }

    @Override
    public SecurityUserDTO findByUserId(String username) {
        return securityMapper.findByUserId(username);
    }

    @Override
    public List<String> findCompanyRolesByCoIdx(String coIdx) {
        return securityMapper.findCompanyRolesByCoIdx(coIdx);
    }

    @Override
    public String findRoleIdByEmpIdx(String empIdx, String coIdx) {
        return securityMapper.findRoleIdxByEmpIdx(empIdx, coIdx);
    }
    
}
