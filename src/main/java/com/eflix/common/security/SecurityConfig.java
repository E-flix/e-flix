package com.eflix.common.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.eflix.common.security.details.SecurityUserDetailService;
import com.eflix.common.security.filter.CustomUsernamePasswordAuthenticationFilter;
import com.eflix.common.security.handler.CustomAuthenticationEntryPoint;
import com.eflix.common.security.handler.CustomFailurHandler;
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
 *   <li>2025-06-21: securityMatcher, formLogin, remember, logout 설정 추가</li>
 *   <li>2025-06-24: /erp Security 설정</li>
 *   <li>2025-06-30: 경로 필터 해제</li>
 *   <li>2025-07-03: CustomAuthenticationFilter 추가</li>
 * </ul>
 */

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private AuthenticationConfiguration authenticationConfiguration;

	@Bean
    public AuthenticationManager authenticationManager() {
		try {
			return authenticationConfiguration.getAuthenticationManager();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return null;
    }

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityUserDetailService securityUserDetailService() {
		return new SecurityUserDetailService();  
	}

	@Bean
	public SecurityFilterChain erpSecurityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
		
		CustomUsernamePasswordAuthenticationFilter customAuthenticationFilter = new CustomUsernamePasswordAuthenticationFilter();
		customAuthenticationFilter.setAuthenticationManager(authenticationManager);
		customAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
		customAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler());

		http.securityMatcher("/**")
				.authorizeHttpRequests(auth -> auth
					.requestMatchers("/", "/signup", "/inquiry/**", "/main/error/**", "/main/assets/**", "/main/css/**", "/main/js/**", "/bootstrap/**", "/common/**", "/bootstrap/**", "/img/**"
					, "/erp/login").permitAll()
					// , "/login", "/erp/login", "/hr/**", "/acc/**", "/bsn/**", "/purchs/**", "/**").permitAll()
					.requestMatchers("/hr/**").hasRole("HR")
					.requestMatchers("/bnz/**").hasRole("BNZ")
					.requestMatchers("/purchs/**").hasRole("PURCHS")
					.requestMatchers("/acc/**").hasRole("ACC")
					// .requestMatchers("/admin/**").hasRole("USER")
					.requestMatchers("/erp/**").authenticated()
					.requestMatchers("/**").authenticated())
			.addFilterAt(customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
			.formLogin(form -> form
				.loginProcessingUrl("/login")
				// .loginPage("/")
				.usernameParameter("username")
				.passwordParameter("password")
				.failureHandler(authenticationFailureHandler())  // 지워도 됨
				.successHandler(authenticationSuccessHandler())  // 지워도 됨
				.permitAll())
				.rememberMe(remember -> remember
					.key("eflix")
					.tokenValiditySeconds(60 * 60 * 24))
			.userDetailsService(securityUserDetailService())
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

	// @Bean
	// public CustomUsernamePasswordAuthenticationFilter customAuthenticationFilter() throws Exception {
	// 	CustomUsernamePasswordAuthenticationFilter filter = new CustomUsernamePasswordAuthenticationFilter();
	// 	filter.setAuthenticationManager(authenticationManager());
	// 	filter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
	// 	return filter;
	// }

	@Bean
	public AuthenticationSuccessHandler authenticationSuccessHandler() {
		return new CustomLoginSuccessHandler();
	}

	@Bean
	public AuthenticationFailureHandler authenticationFailureHandler() {
		return new CustomFailurHandler();
	}

	@Bean
	public LogoutSuccessHandler logoutSuccessHandler() {
		return new CustomLogoutSuccessHandler();
	}

	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint() {
		return new CustomAuthenticationEntryPoint();
	}
}