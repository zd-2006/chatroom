package com.chatroom.mapper;

import com.chatroom.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int addUser(User user);
    User login(@Param("username")String username, @Param("password")String password);
    User getUserByName(@Param("username")String username);
}
