package com.eflix.common.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 공통 모델 속성 주입 클래스
 * <p>
 * 이 클래스는 {@code @ControllerAdvice}를 사용하여 모든 컨트롤러에 공통적으로
 * {@code @ModelAttribute} 값을 주입하는 역할을 합니다.
 * <br><br>
 * 현재 요청 URI를 기반으로 상단 메뉴(activeMenu)와 하위 메뉴(activeSubMenu)를 추출하여,
 * 뷰(View)에서 메뉴 활성화 등에 활용할 수 있도록 제공합니다.
 * </p>
 *
 * <h3>기능 설명</h3>
 * <ul>
 *   <li><b>activeMenu</b> : 요청 URI의 첫 번째 경로(예: /hr → "hr")</li>
 *   <li><b>activeSubMenu</b> : 요청 URI가 /hr/** 인 경우 두 번째 경로(예: /hr/el → "el")</li>
 * </ul>
 *
 * @author 복성민 (bokseongmin@gmail.com)
 * @version 1.0
 * @since 2025-06-21
 *
 * @see
 *
 * @changelog
 * <ul>
 *   <li>2025-06-21: 최초 생성 (복성민)</li>
 * </ul>
 */

@ControllerAdvice
public class GlobalModelAttribute {
    
    @ModelAttribute("activeMenu")
    public String setActiveMenu(HttpServletRequest request) {
        String uri = request.getRequestURI(); // 예: /hr/el, /purchs/cgy

        String[] parts = uri.split("/");

        if (parts.length >= 2) {
            return parts[1]; // "hr", "purchs"
        }

        return "home";
    }

    @ModelAttribute("activeSubMenu")
    public String setActiveSubMenu(HttpServletRequest request) {

        String uri = request.getRequestURI(); // 예: /hr/el, /purchs/cgy
        String[] parts = uri.split("/");

        if (parts.length >= 3) {
            return parts[2]; // "el", "cgy"
        }

        return "home";
    }
}
