package com.chatroom.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.*;

public class Server {

    private static volatile boolean running = true;
    public static ServerSocket ss = null;
    // 存储在线用户的 Session，key是用户名
    public static Map<String, ClientSession> users = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        // 线程池配置
        ThreadPoolExecutor pool = new ThreadPoolExecutor(
                5,
                2000,
                60L,
                TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );

        try {
            ss = new ServerSocket(6666);
            System.out.println("聊天室服务器启动成功，监听端口 6666...");

            while (running) {
                try {
                    // 阻塞等待客户端连接
                    Socket socket = ss.accept();
                    // 将任务交给线程池处理
                    pool.execute(new ServerRunnable(socket));
                } catch (IOException e) {
                    if (running) e.printStackTrace();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭服务器资源
            running = false;
            pool.shutdownNow();
            if (ss != null && !ss.isClosed()) {
                try {
                    ss.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    /**
     * 群发消息（广播）
     * @param msg 消息内容
     * @param sender 发送者的Socket（用于排除自己），如果传null则发给所有人
     */
    public static void broadcast(String msg, Socket sender) {
        for (ClientSession session : users.values()) {
            // 排除发送者自己
            if (session.socket == sender) continue;
            session.send(msg);
        }
    }

    /**
     * 处理每个客户端连接的线程任务
     */
    static class ServerRunnable implements Runnable {

        private final Socket socket;
        private String userName;

        public ServerRunnable(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                // 这个 out 是用来给“当前连接”发消息的
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                out.println("=== 欢迎来到聊天室 ===");
                out.println("提示: 输入 @名字 消息 可以进行私聊");

                // 1. 读取名字并处理重名
                String name = in.readLine();
                while (Server.users.containsKey(name)) {
                    out.println("系统: 名字[" + name + "]已存在，自动重命名为 " + name + "_1");
                    name = name + "_1";
                }
                this.userName = name;

                // 2. 注册用户 Session
                ClientSession session = new ClientSession(socket);
                users.put(userName, session);

                // 3. 广播上线通知
                System.out.println("服务器日志: " + userName + " 上线了");
                broadcast("系统: " + userName + " 上线了", socket);

                // 4. 接收消息循环
                String str;
                while ((str = in.readLine()) != null) {
                    if ("bye".equals(str) || "exit".equals(str)) {
                        break;
                    }

                    // === 私聊逻辑开始 ===
                    if (str.startsWith("@")) {
                        int spaceIndex = str.indexOf(' '); // 寻找第一个空格
                        if (spaceIndex > 1) {
                            String targetName = str.substring(1, spaceIndex); // 提取名字
                            String privateMsg = str.substring(spaceIndex + 1); // 提取消息

                            ClientSession targetSession = users.get(targetName);
                            if (targetSession != null) {
                                // 1. 发给对方
                                targetSession.send("[私聊] " + userName + ": " + privateMsg);
                                // 2. 发给自己（回显）
                                session.send("[我 -> " + targetName + "]: " + privateMsg);
                            } else {
                                session.send("系统: 找不到用户 [" + targetName + "]");
                            }
                        } else {
                            session.send("系统: 私聊格式错误，请使用: @名字 消息内容");
                        }
                        // 【关键点】处理完私聊后，必须跳过，不然会继续执行下面的群发
                        continue;
                    }
                    // === 私聊逻辑结束 ===

                    // === 群发逻辑 ===
                    String msgToSend = userName + " 说: " + str;
                    broadcast(msgToSend, socket);
                }
            } catch (IOException ignored) {
                // 客户端异常断开
            } finally {
                // 5. 下线清理逻辑
                try {
                    if (userName != null) {
                        users.remove(userName);
                        System.out.println("服务器日志: " + userName + " 下线了");
                        broadcast("系统: " + userName + " 下线了", socket);
                    }
                    if (socket != null && !socket.isClosed()) {
                        socket.close();
                    }
                } catch (IOException ignored) {
                }
            }
        }
    }

    /**
     * 客户端会话包装类，用于线程安全地发送消息
     */
    static class ClientSession {
        public final Socket socket;
        public final PrintWriter out;

        ClientSession(Socket socket) throws IOException {
            this.socket = socket;
            this.out = new PrintWriter(socket.getOutputStream(), true);
        }

        // 加锁防止多线程并发写入导致消息乱码
        public void send(String msg) {
            synchronized (this) {
                out.println(msg);
            }
        }
    }
}