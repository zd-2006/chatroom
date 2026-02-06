package com.chatroom.View;

import com.chatroom.Service.UserService;
import com.chatroom.pojo.User;

import java.util.Scanner;

public class AppView{
        private LoginView loginView = new LoginView();
        private RegisterView registerView = new RegisterView();
        public User start(Scanner sc) {
            while (true) {
                System.out.println("欢迎来到聊天室首页");
                System.out.println("1. 登录");
                System.out.println("2. 注册");
                System.out.println("3. 退出");
                System.out.println("请选择:");
                String choice = sc.nextLine();
                switch (choice) {
                    case "1":
                        User loginUser = loginView.show(sc);
                        if (loginUser != null) {
                            System.out.println("正在进入聊天室");
                            return loginUser;
                        }
                        break;
                    case "2":
                        registerView.show(sc);
                        System.out.println("请选择1重新登录");
                        break;
                    case "3":
                        System.out.println("拜拜");
                        return null;
                    default:
                        System.out.println("无效输入");
                        break;
                }
            }
        }
}