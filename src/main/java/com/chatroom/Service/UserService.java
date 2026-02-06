package com.chatroom.Service;

import com.chatroom.mapper.UserMapper;
import com.chatroom.pojo.User;
import com.chatroom.utils.MyBatisUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;

public class UserService {
    public boolean register(String username, String password) {

        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) return false;
        else if (mapper.getUserByName(username) == null) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            int count = mapper.addUser(user);
            if (count > 0) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
    public boolean checkUsername(String username) {

        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        return mapper.getUserByName(username) != null;
    }
    public User login(String username, String password) {
        UserMapper mapper = MyBatisUtils.getSqlSession().getMapper(UserMapper.class);
        User user = mapper.login(username, password);
        return user;
    }
}
