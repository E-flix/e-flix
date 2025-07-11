package com.eflix.common.security.handler;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Spring Security 접근 거부(403 Forbidden) 핸들러
 * <p>
 * 인증된 사용자가 인가되지 않은 자원에 접근할 경우 실행되는 처리 로직을 정의합니다.
 * </p>
 *
 * <h3>기능 설명</h3>
 * <ul>
 *   <li>AccessDeniedException 발생 시 사용자 정의 에러 페이지로 리다이렉트 처리 가능</li>
 *   <li>현재는 기본 메서드만 오버라이딩 되어 있으며, 향후 로직 구현 필요</li>
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

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handle'");
    }

}
