package com.eflix.common.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.eflix.common.security.details.SecurityUserDetailService;
import com.eflix.common.security.handler.CustomAuthenticationEntryPoint;
import com.eflix.common.security.handler.CustomLoginSuccessHandler;
import com.eflix.common.security.handler.CustomLogoutSuccessHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * Spring Security 설정 클래스
 * <p>
 * 웹 애플리케이션의 보안 설정을 담당하며,
 * 현재 프로젝트에서는 모든 요청을 인증 없이 허용하고,
 * CSRF, 로그인 폼, 로그아웃 기능을 비활성화합니다.
 * 
 * <br>
 * 
 * 추후 보안 요구사항에 따라 인증/인가 로직 및 세부 정책이 추가될 수 있습니다.
 * </p>
 * 
 * <h3>설정 내용</h3>
 * <ul>
 * <li>모든 요청에 대해 인증 없이 접근 허용</li>
 * <li>CSRF 보호 비활성화</li>
 * <li>Form Login 비활성화</li>
 * <li>Logout 기능 비활성화</li>
 * <li>BCryptPasswordEncoder 빈 등록</li>
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
 *   <li>2025-06-21: securityMatcher, formLogin, remember, logout 설정 추가</li>
 *   <li>2025-06-24: /erp Security 설정</li>
 * </ul>
 */

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private SecurityUserDetailService customUserDetailService;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain erpSecurityFilterChain(HttpSecurity http) throws Exception {
		http.securityMatcher("/**")
				.authorizeHttpRequests(auth -> auth
					.requestMatchers("/", "/erp", "/login", "/signup", "/inquiry/**", "/main/error/**", "/main/assets/**", "/main/css/**", "/main/js/**", "/bootstrap/**", "/common/**",
						"/bootstrap/**", "/img/**",
						"/hr/**", "/acc/**", "/bsn/**", "/purchs/**").permitAll()
					.requestMatchers("/erp/**").authenticated()
					.requestMatchers("/**").authenticated())
			.formLogin(form -> form
				.loginProcessingUrl("/login")
				// .loginPage("/")
				.usernameParameter("user_id")
				.passwordParameter("user_pw")
				.successHandler(authenticationSuccessHandler())
				.permitAll())
			// .rememberMe(remember -> remember
			// 	.key("eflix")
			// 	.tokenValiditySeconds(60 * 60 * 24)
			// 	.rememberMeParameter("user_remember")
			// 	.userDetailsService(customUserDetailService))
			.userDetailsService(customUserDetailService)
			.logout(logout -> logout
				.logoutSuccessHandler(logoutSuccessHandler())
				.deleteCookies("JSESSIONID", "user_remember"))
			.csrf(csrf -> csrf.disable())
			.headers(headers -> headers
            	.frameOptions(frame -> frame.disable()))
			.exceptionHandling(exception -> exception
				.accessDeniedPage("/common/error/403.html")
				.authenticationEntryPoint(authenticationEntryPoint()));

		return http.build();
	}

	@Bean
	public AuthenticationSuccessHandler authenticationSuccessHandler() {
		return new CustomLoginSuccessHandler();
	}

	@Bean
	public LogoutSuccessHandler logoutSuccessHandler() {
		return new CustomLogoutSuccessHandler();
	}

	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint() {
		return new CustomAuthenticationEntryPoint();
	}

	// @Bean
	public UserDetailsService userDetailsService() {
		return new SecurityUserDetailService();
	}
}