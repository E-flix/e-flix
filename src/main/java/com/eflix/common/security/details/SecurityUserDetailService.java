package com.eflix.common.security.details;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.eflix.common.security.dto.SecurityEmpDTO;
import com.eflix.common.security.dto.SecurityMasterDTO;
import com.eflix.common.security.dto.UserDTO;
import com.eflix.common.security.mapper.SecurityMapper;
import com.eflix.main.mapper.UserMapper;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SecurityUserDetailService implements UserDetailsService {

	@Autowired
	private SecurityMapper securityMapper;

	@Autowired
	private UserMapper userMapper;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

		String uri = req.getRequestURI();
		String coIdx = (String) req.getAttribute("co_idx");
		String masterChecked = (String) req.getAttribute("masterChecked");

		log.info("로그인 시도 - username: {}, uri: {}, coIdx: {}, masterChecked: {}", username, uri, coIdx, masterChecked);

		// ERP 로그인
		if (uri.startsWith("/erp")) {
			log.info("erp 로그인 시도");
			// 사원 로그인
			// if (masterChecked != null && !masterChecked.isBlank()) {
			if (!masterChecked.equals("on")) {
			
				log.info("erp - 사원 로그인 시도");
				SecurityEmpDTO securityEmpDTO = securityMapper.findEmpForLogin(coIdx, username);
				if (securityEmpDTO == null) throw new UsernameNotFoundException("사원 정보 없음");

				List<String> roleCodes = securityMapper.findRoleCodesByEmpIdx(securityEmpDTO.getEmpIdx());
				return new SecurityUserDetails(securityEmpDTO, roleCodes);
			}
			// 마스터 로그인
			else {
				log.info("erp - 마스터 로그인 시도");
				SecurityMasterDTO securityMasterDTO = securityMapper.findMasterForLogin(coIdx, username); // username = mst_id

				if (securityMasterDTO == null) throw new UsernameNotFoundException("마스터 계정 없음");
				return new SecurityUserDetails(securityMasterDTO);
			}
		}

		// 메인 로그인
		log.info("메인 로그인 시도");
		
		UserDTO user = userMapper.findByUserId(username);
		if (user == null) throw new UsernameNotFoundException("일반 사용자 없음");

		return new SecurityUserDetails(user);
	}
}