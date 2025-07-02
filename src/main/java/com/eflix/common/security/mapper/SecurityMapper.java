package com.eflix.common.security.mapper;

import java.util.List;

import com.eflix.common.security.dto.SecurityEmpDTO;
import com.eflix.common.security.dto.SecurityMasterDTO;
import com.eflix.common.security.dto.SecurityUserDTO;

public interface SecurityMapper {

    public SecurityMasterDTO findByCoIdxAndMstId(String coIdx, String mstId);

    public SecurityEmpDTO findEmpForLogin(String coIdx, String empEmail);

    public List<String> findRoleCodesByEmpIdx(String empIdx);

    public SecurityMasterDTO findMasterForLogin(String coIdx, String mstId);
    
    public void passwordUpdate(String newPassword);

    public SecurityUserDTO findByUserId(String username);
}
