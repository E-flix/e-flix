package com.eflix.common.security.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class SecurityUserDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userIdx; // user-000
    private String userId;
    private String userPw;
    private String userName;
    private String userTel;
    private String userEmail;
    private Date userRegdate;
    private Date userModdate;

    private String userRole;
    private String coIdx;
    
}
