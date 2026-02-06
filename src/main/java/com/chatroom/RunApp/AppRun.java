package com.chatroom.RunApp;

import com.chatroom.Client.Client;
import com.chatroom.Server.Server;
import com.chatroom.View.AppView;
import com.chatroom.pojo.User;

import java.util.Scanner;

public class AppRun {
    public static void main(String[] args) {
        // 1. 唯一的 Scanner 资源
        // 放在这里统一管理，防止在各个子界面乱 close 导致流关闭
        Scanner sc = new Scanner(System.in);
        AppView appView = new AppView();
        System.out.println(">>> 程序正在启动...");
        User loginUser = null;
        try {
            loginUser = appView.start(sc);
            System.out.println("登录成功");
            Client client = new Client();
            client.start(loginUser);
        } catch (Exception e) {
            System.err.println("程序运行出现意外错误: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // 4. 程序彻底退出前，关闭资源
            System.out.println(">>> 程序已退出");
            sc.close();
        }
    }
}