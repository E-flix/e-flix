package com.eflix.main.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.eflix.main.dto.UserDTO;
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
        return userMapper.findByUserIdx(userIdx);
    }

    @Override
    public int updateUserByUserIdx(UserDTO userDTO) {
        return userMapper.updateUserByUserIdx(userDTO);
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
        if(passwordEncoder.matches(userPw, userDTO.getUserPw())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int existByUserId(String userId) {
        return userMapper.existByUserId(userId);
    }
}