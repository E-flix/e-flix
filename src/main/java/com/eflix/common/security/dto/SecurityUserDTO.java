package com.eflix.common.security.dto;

import java.util.Date;

import lombok.Data;

@Data
public class SecurityUserDTO {
    private String userIdx; // user-000
    private String userId;
    private String userPw;
    private String userName;
    private String userTel;
    private String userEmail;
    private Date userRegdate;
    private Date userModdate;

    private String userRole;
}
