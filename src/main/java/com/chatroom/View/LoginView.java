package com.chatroom.View;

import com.chatroom.Service.UserService;
import com.chatroom.pojo.User;

import java.util.Scanner;

public class LoginView { // 1. 修正：去掉了这里的 ()

    private UserService service = new UserService();

    public User show(Scanner sc) {
        System.out.println("\n>>> 进入登录流程");
        while (true) {
            System.out.println("请输入昵称:");
            String name = sc.nextLine();

            boolean isUsed = service.checkUsername(name);

            if (isUsed) {

                System.out.println("输入密码:");
                String psw = sc.nextLine();


                if (psw.isEmpty()) {
                    System.out.println("密码不能为空，请重新操作");
                } else {
                    User loginUser = service.login(name, psw);
                    if (loginUser != null) {
                        System.out.println("登录成功！");
                        return loginUser;
                    } else {
                        System.out.println("密码错误，请重新输入");
                    }
                }
            } else {
                System.out.println("昵称不存在或输入错误。");
                System.out.println("输入 '1' 重试，输入其他任意键退出登录返回主菜单");
                String choice = sc.nextLine();
                if (!"1".equals(choice)) {
                    return null;
                }
            }
        }
    }
}