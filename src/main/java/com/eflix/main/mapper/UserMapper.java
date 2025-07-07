package com.eflix.main.mapper;

import java.util.List;

import com.eflix.main.dto.UserDTO;
import com.eflix.main.dto.etc.UserSearchDTO;

public interface UserMapper {
    public UserDTO findByUser_idx(String userIdx);

    public UserDTO findByUserId(String user_id);

    public int insert(UserDTO userDTO);

    public int updateUser(UserDTO userDTO);

    public List<UserDTO> findAllUsers(UserSearchDTO userSearchDTO);

    public int findAllUsersCount(UserSearchDTO userSearchDTO);

    public UserDTO findUserPwByUserIdx(String userIdx);

}
