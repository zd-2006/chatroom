package com.chatroom.View;

import com.chatroom.Service.UserService;

import java.util.Scanner;

public class RegisterView {
    private UserService userService = new UserService();

    public void show(Scanner sc) {
        System.out.println("\n>>> 进入注册流程");
        String validName = null;
        while (true) {
            System.out.println("请输入您的注册呢称:");
            String inputName = sc.nextLine().trim();
            if (inputName.equals("")) {
                System.out.println("昵称不能为空");
                continue;
            }
            if (userService.checkUsername(inputName)) {
                System.out.println("昵称已经存在,请更换");
            } else {
                System.out.println("昵称可用");
                validName = inputName;
                break;
            }
        }
        String validPwd = getValidPassword(sc);

        boolean success = userService.register(validName, validPwd);
        if (success) {
            System.out.println("注册成功!可以使用账号登录了");
        } else {
            System.out.println("注册失败,系统繁忙");
        }
    }
    private String getValidPassword(Scanner sc) {
        while (true){
            while (true){
                System.out.println("请输入密码");
                String p1 = sc.nextLine().trim();
                System.out.println("请确认密码");
                String p2 = sc.nextLine().trim();
                if (p1.equals(p2)){
                    return p1;
                }else {
                    System.out.println("两次密码不一致");
                }

            }
        }
    }
}
