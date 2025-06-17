package com.eflix.util.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.extern.slf4j.Slf4j;

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
