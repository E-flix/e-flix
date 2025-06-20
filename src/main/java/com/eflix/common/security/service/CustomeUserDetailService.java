package com.eflix.common.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.eflix.common.security.dto.UserDTO;
import com.eflix.erp.mapper.UserMapper;

public class CustomeUserDetailService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String userIdx) throws UsernameNotFoundException {
        // TODO Auto-generated method stub
        UserDTO userDTO = userMapper.findById(userIdx);

        // 계정 존재 여부
        if(userDTO == null) {
            throw new UsernameNotFoundException("User Not Found");
        }

        // 계정 비밀번호가 비어 있는지
        if(userDTO.getUserPw() == null || userDTO.getUserPw().isEmpty()) {
            throw new UsernameNotFoundException("Password is missing" + userDTO.getUserId());
        }

        // throw new UnsupportedOperationException("Unimplemented method 'loadUserByUsername'");
        return new CustomerUserDTO(userDTO);
    }
    
}
