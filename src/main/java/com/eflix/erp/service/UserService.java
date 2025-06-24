package com.eflix.erp.service;

import com.eflix.common.security.dto.UserDTO;

/**
 * ERP 회원 관리를 위한 Service 클래스
 * 
 * <p>
 * 사용자 계정의 생성, 조회, 수정, 삭제 및 인증 관련 비즈니스 로직을 처리합니다.
 * 회원가입부터 로그인, 개인정보 관리까지의 전체 사용자 라이프사이클을 관리합니다.
 * </p>
 * 
 * <h3>주요 기능</h3>
 * <ul>
 *   <li>사용자 회원가입 및 중복 검증</li>
 *   <li>로그인/로그아웃 처리</li>
 *   <li>개인정보 조회 및 수정</li>
 *   <li>비밀번호 변경 및 찾기</li>
 *   <li>회원 탈퇴 처리</li>
 * </ul>
 * 
 * @author 복성민 (bokseongmin@gmail.com)
 * @version 1.0
 * @since 2025-06-19
 * 
 * @see
 * 
 * @changelog
 * <ul>
 *   <li>2025-06-19: 최초 생성 (복성민)</li>
 * </ul>
 */

public interface UserService {

    public int insert(UserDTO userDTO);
    
}
