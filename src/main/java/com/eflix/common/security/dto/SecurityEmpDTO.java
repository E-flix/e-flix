package com.eflix.common.security.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class SecurityEmpDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String empIdx;
    private String coIdx;
    private String empAccount;
    private String empPw;
    private String empName;
    private String empEmail;
    private String roleId;
    private String empImg;
    private List<String> roleCodes; // ROLE_HR, ROLE_ACC 등
}
