package com.chatroom.pojo;

// 建议用 java.util.Date 或 java.sql.Timestamp 对应 datetime，Time 只存时分秒
import java.util.Date;

public class User {
    private Integer id;
    private String username; // 改成全小写！
    private String password;
    private Date createTime; // 建议改个名对应数据库的 create_time

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    // 注意看这里：全是小写
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // ... 其他 getter setter
}