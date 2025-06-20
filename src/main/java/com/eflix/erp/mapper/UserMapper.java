package com.eflix.erp.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.eflix.common.security.dto.UserDTO;

@Mapper
public interface UserMapper {
    public UserDTO findById(String userIdx);
}
