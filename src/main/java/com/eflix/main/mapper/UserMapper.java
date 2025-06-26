package com.eflix.main.mapper;

import com.eflix.common.security.dto.UserDTO;

public interface UserMapper {
    public UserDTO findByUser_idx(String userIdx);

    public UserDTO findByUserId(String user_id);

    public int insert(UserDTO userDTO);

    public int updateUser(UserDTO userDTO);
}
