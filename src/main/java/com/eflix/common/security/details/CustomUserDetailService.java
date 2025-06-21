package com.eflix.common.security.details;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.eflix.common.security.dto.EmployeeDTO;
import com.eflix.common.security.dto.UserDTO;
import com.eflix.erp.mapper.CompanyMapper;
import com.eflix.erp.mapper.RoleMapper;
import com.eflix.erp.mapper.UserMapper;
import com.eflix.hr.mapper.EmployeeMapper;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomUserDetailService implements UserDetailsService {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private EmployeeMapper employeeMapper;

	@Autowired
	private RoleMapper roleMapper;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest();
		String uri = req.getRequestURI();

		if (uri.startsWith("/erp")) {
			// 일반 사용자
			UserDTO user = userMapper.findByUserId(username);
			if (user == null) {
				throw new UsernameNotFoundException("사용자 없음");
			}
			return new CustomUserDetails(user);

		} else {
			// ERP 관리자 로그인 (emp_id + co_idx 등으로 조회)
			String empEmail = req.getParameter("emp_email");
			String coIdx = req.getParameter("co_idx");

			EmployeeDTO employee = employeeMapper.findByEmpEmailAndCompany(empEmail, coIdx);
			if (employee == null) {
				throw new UsernameNotFoundException("사원 없음");
			}

			// 해당 사원의 권한 리스트 조회 (role_code만 뽑아오면 됨)
			List<String> roleIds = roleMapper.findRoleIdsByEmpIdx(employee.getEmpIdx());

			return new CustomUserDetails(employee, roleIds);
		}
	}
}
