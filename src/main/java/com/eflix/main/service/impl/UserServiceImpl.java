package com.eflix.main.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eflix.common.security.dto.UserDTO;
import com.eflix.main.dto.etc.UserSearchDTO;
import com.eflix.main.mapper.UserMapper;
import com.eflix.main.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

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
}