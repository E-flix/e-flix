package com.eflix.common.security.service;

import java.util.List;

import com.eflix.common.security.dto.SecurityEmpDTO;
import com.eflix.common.security.dto.SecurityMasterDTO;
import com.eflix.common.security.dto.SecurityUserDTO;

public interface SecurityService {
    public SecurityEmpDTO findEmpForLogin(String coIdx, String username);

    public List<String> findRoleCodesByEmpIdx(String empIdx);

    public SecurityMasterDTO findMasterForLogin(String coIdx, String username);

    public SecurityUserDTO findByUserId(String username);

    // 회사가 구매한 모듈 ERP 권한 목록
    public List<String> findCompanyRolesByCoIdx(String coIdx);

    public String findRoleIdByEmpIdx(String empIdx, String coIdx);
}
