package com.eflix.common.security.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class SecurityMasterDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String mstIdx;
    private String coIdx;
    private String mstId;
    private String mstPw;
    private String mstName;
    private String mstEmail;
}
