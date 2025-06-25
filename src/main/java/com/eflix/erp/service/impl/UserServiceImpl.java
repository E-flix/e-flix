package com.eflix.erp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eflix.common.security.dto.UserDTO;
import com.eflix.erp.mapper.UserMapper;
import com.eflix.erp.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public int insertUser(UserDTO userDTO) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'insert'");

        return userMapper.insert(userDTO);
    }

    @Override
    public UserDTO findByUserIdx(String userIdx) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'findByUserIdx'");
        return userMapper.findByUser_idx(userIdx);
    }

    @Override
    public int updateUser(UserDTO userDTO) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'updateUser'");
        return userMapper.updateUser(userDTO);
    }
}