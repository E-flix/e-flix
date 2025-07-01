package com.eflix.common.security.details;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.eflix.common.security.dto.SecurityEmpDTO;
import com.eflix.common.security.dto.SecurityMasterDTO;
import com.eflix.common.security.dto.UserDTO;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class SecurityUserDetails implements UserDetails {

	private final UserDTO userDTO;
	private final SecurityEmpDTO securityEmpDTO;
	private final SecurityMasterDTO securityMasterDTO;

	private final Collection<? extends GrantedAuthority> authorities;

	// 메인 사용자 로그인
	public SecurityUserDetails(UserDTO user) {
		this.userDTO = user;
		this.securityEmpDTO = null;
		this.securityMasterDTO = null;
		this.authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getUserRole()));
	}

	// ERP 사원 로그인
	public SecurityUserDetails(SecurityEmpDTO employee, List<String> roleCodes) {
		this.userDTO = null;
		this.securityEmpDTO = employee;
		this.securityMasterDTO = null;
		this.authorities = roleCodes.stream()
			.map(code -> new SimpleGrantedAuthority("ROLE_" + code))
			.collect(Collectors.toList());
	}

	// ERP 마스터 로그인
	public SecurityUserDetails(SecurityMasterDTO master) {
		this.userDTO = null;
		this.securityEmpDTO = null;
		this.securityMasterDTO = master;
		this.authorities = List.of(new SimpleGrantedAuthority("ROLE_MASTER"));

		log.info("마스터 계정 - {}", this.securityMasterDTO);
	}

	@Override
	public String getPassword() {
		if (userDTO != null) return userDTO.getUserPw();
		if (securityEmpDTO != null) return securityEmpDTO.getEmpPw();
		if (securityMasterDTO != null) return securityMasterDTO.getMstPw();
		return null;
	}

	@Override
	public String getUsername() {
		if (userDTO != null) return userDTO.getUserId();
		if (securityEmpDTO != null) return securityEmpDTO.getEmpEmail();
		if (securityMasterDTO != null) return securityMasterDTO.getMstId();
		return null;
	}

	@Override public boolean isAccountNonExpired() { return true; }
	@Override public boolean isAccountNonLocked() { return true; }
	@Override public boolean isCredentialsNonExpired() { return true; }
	@Override public boolean isEnabled() { return true; }
}