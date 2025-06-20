package com.eflix.util.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.extern.slf4j.Slf4j;

/**
 * Spring Security 설정 클래스
 * <p>
 * 웹 애플리케이션의 보안 설정을 담당하며, 
 * 현재 프로젝트에서는 모든 요청을 인증 없이 허용하고,
 * CSRF, 로그인 폼, 로그아웃 기능을 비활성화합니다.
 * <br><br>
 * 추후 보안 요구사항에 따라 인증/인가 로직 및 세부 정책이 추가될 수 있습니다.
 * </p>
 * 
 * <h3>설정 내용</h3>
 * <ul>
 *   <li>모든 요청에 대해 인증 없이 접근 허용</li>
 *   <li>CSRF 보호 비활성화</li>
 *   <li>Form Login 비활성화</li>
 *   <li>Logout 기능 비활성화</li>
 *   <li>BCryptPasswordEncoder 빈 등록</li>
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

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// 프로젝트 완성 후 수정
		http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll() // 모든 요청을 인증 없이 허용
		).csrf(csrf -> csrf.disable()) // CSRF 비활성화
				.formLogin(form -> form.disable()) // 로그인 폼 비활성화
				.logout(logout -> logout.disable()); // 로그아웃 비활성화
		return http.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
