package com.eflix.main.mapper;

import java.util.List;

import com.eflix.main.dto.UserDTO;
import com.eflix.main.dto.etc.UserSearchDTO;

public interface UserMapper {
    public UserDTO findByUserIdx(String userIdx);

    public UserDTO findByUserId(String user_id);

    public int insert(UserDTO userDTO);

    public int updateUserByUserIdx(UserDTO userDTO);

    public List<UserDTO> findAllUsers(UserSearchDTO userSearchDTO);

    public int findAllUsersCount(UserSearchDTO userSearchDTO);

    public UserDTO findUserPwByUserIdx(String userIdx);

    // 0714
    public int existByUserId(String userId);

}
