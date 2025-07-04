package com.eflix.main.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.eflix.common.security.dto.UserDTO;
import com.eflix.main.dto.etc.UserSearchDTO;
import com.eflix.main.mapper.UserMapper;
import com.eflix.main.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public int insertUser(UserDTO userDTO) {
        return userMapper.insert(userDTO);
    }

    @Override
    public UserDTO findByUserIdx(String userIdx) {
        return userMapper.findByUser_idx(userIdx);
    }

    @Override
    public int updateUser(UserDTO userDTO) {
        return userMapper.updateUser(userDTO);
    }

    @Override
    public List<UserDTO> findAllUsers(UserSearchDTO userSearchDTO) {
        return userMapper.findAllUsers(userSearchDTO);
    }

    @Override
    public int findAllUsersCount(UserSearchDTO userSearchDTO) {
        return userMapper.findAllUsersCount(userSearchDTO);
    }

    @Override
    public boolean verifyPassword(String userIdx, String userPw) {
        UserDTO userDTO = userMapper.findUserPwByUserIdx(userIdx);
        System.out.println("입력 비번: " + userPw); // 평문
System.out.println("DB 비번: " + userDTO.getUserPw()); // 암호화된 값
System.out.println("matches? " + passwordEncoder.matches(userPw, userDTO.getUserPw()));
        if(passwordEncoder.matches(userPw, userDTO.getUserPw())) {
            return true;
        } else {
            return false;
        }
    }
}