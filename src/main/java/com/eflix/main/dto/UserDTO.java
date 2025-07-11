package com.eflix.main.dto;

import java.util.Date;

import lombok.Data;

/**
 * Spring Security User 정보 저장 클래스
 * <p>
 * </p>
 * 
 * <h3>설정 내용</h3>
 * <ul>
 *   <li>모든 요청에 대해 인증 없이 접근 허용</li>
 * </ul>
 * 
 * @author 복성민 (bokseongmin@gmail.com)
 * @version 1.0
 * @since 2025-06-20
 * 
 * @see
 * 
 * @changelog
 * <ul>
 *   <li>2025-06-20: 최초 생성 (복성민)</li>
 * </ul>
 */

@Data
public class UserDTO {
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