package com.eflix.common.security.details;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.eflix.common.security.dto.UserDTO;
import com.eflix.hr.dto.EmployeeDTO;

import lombok.Getter;

@Getter
public class CustomUserDetails implements UserDetails {

	private final UserDTO userDTO;
	private final EmployeeDTO employeeDTO;

	private final Collection<? extends GrantedAuthority> authorities;

	public CustomUserDetails(UserDTO user) {
		this.userDTO = user;
		this.employeeDTO = null;
		this.authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
	}

	public CustomUserDetails(EmployeeDTO employee, List<String> roleCodes) {
		this.employeeDTO = employee;
		this.userDTO = null;
		this.authorities = roleCodes.stream()
				.map(role -> new SimpleGrantedAuthority("ROLE_" + role))
				.collect(Collectors.toList());
	}

	@Override
	public String getPassword() {
		return userDTO != null ? userDTO.getUserPw() : employeeDTO.getEmpPw();
	}

	@Override
	public String getUsername() {
		return userDTO != null ? userDTO.getUserId() : employeeDTO.getEmpEmail();
	}

	// 나머지 override 생략
}