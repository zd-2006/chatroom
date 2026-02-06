package com.chatroom.Client;

import com.chatroom.pojo.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private String currentChannel = "ALL";

    public  void start(User user) throws IOException {
        Scanner sc = new Scanner(System.in);
        Socket socket = new Socket("127.0.0.1", 6666);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // 发送名字
        out.println(user.getUsername());

        Thread t = new Thread(() -> {
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t.start();

        while (true) {
            String input = sc.nextLine();

            // 防止空输入
            if (input == null || input.trim().isEmpty()) continue;

            // === 1. 指令处理 ===
            if (input.startsWith("/to")) {
                // 【修复点1】先切割，判断长度够不够，防止报错
                String[] parts = input.split(" ");
                if (parts.length > 1) {
                    currentChannel = parts[1];
                    System.out.println(">>> 切换到私聊模式: @" + currentChannel);
                } else {
                    System.out.println(">>> 指令错误，请使用格式: /to 名字");
                }
                continue; // 处理完指令直接进入下一轮循环，不发送
            }

            if (input.equals("/all")) {
                currentChannel = "ALL";
                System.out.println(">>> 切换频道：返回群聊");
                continue;
            }

            if ("bye".equals(input) || "exit".equals(input)) {
                out.println(input);
                break;
            }

            // === 2. 消息发送 ===
            // 【修复点2】把原来的双重发送逻辑合并了，只在这里发一次
            if (currentChannel.equals("ALL")) {
                // 群聊模式
                out.println(input);
            } else {
                // 私聊模式：自动包装
                out.println("@" + currentChannel + " " + input);
            }
        }

        socket.close();
        sc.close();
    }
}