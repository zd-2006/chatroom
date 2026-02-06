package com.chatroom.utils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

import static org.apache.ibatis.io.Resources.getResourceAsStream;

public class MyBatisUtils {
   private   static SqlSessionFactory sqlSession;
   static {
       try {
           String resource = "mybatis-config.xml";
           InputStream inputStream = getResourceAsStream(resource);
           sqlSession = new SqlSessionFactoryBuilder().build(inputStream);
       } catch (IOException e) {
           throw new RuntimeException(e);
       }
   }
   public  static SqlSession getSqlSession() {
       if(sqlSession!=null){
           return sqlSession.openSession(true);
       }
       return null;
   }
}
