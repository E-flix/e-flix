package com.eflix.common.security.dto;

import java.util.List;

import lombok.Data;

@Data
public class SecurityEmpDTO {
    private String empIdx;
    private String coIdx;
    private String empAccount;
    private String empPw;
    private String empName;
    private String empEmail;
    private List<String> roleCodes; // ROLE_HR, ROLE_ACC 등
}
