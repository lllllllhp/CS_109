package com.lllllllhp.utils.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Scanner;

public class SocketClient extends SocketBase {
    private Socket client;
    private final String SERVER_IP;
    private final int PORT;

    ObjectOutputStream out;
    ObjectInputStream in;
    Scanner scanner = new Scanner(System.in);

    private String userName;

    public SocketClient(String SERVER_IP, int PORT) {
        this.SERVER_IP = SERVER_IP;
        this.PORT = PORT;
    }

    public void startClient() throws IOException {
        Socket client = new Socket(SERVER_IP, PORT);
        this.client = client;
        System.out.println("连接到服务器");

        System.out.println("请输入你的名字");
        userName = scanner.nextLine();
        System.out.println("设置成功，你的名字：" + userName);

        out = new ObjectOutputStream(client.getOutputStream());
        in = new ObjectInputStream(client.getInputStream());

        //接受server广播
        new Thread(new ServerListener()).start();

        //输入消息
        while (true) {
            String content = scanner.nextLine();
            Message msg = new Message(Message.Type.Message, userName, content, LocalDateTime.now());
            out.writeObject(msg);
            out.flush();
        }
    }

    public class ServerListener implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    Message msg = (Message) in.readObject();
                    System.out.println(msg);
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println(e.toString());
            } finally {
                closeClient();
                System.out.println("与服务器断开连接");
            }
        }
    }

    public void closeClient() {
        try {
            client.close();
        } catch (IOException e) {
            System.out.println(e.toString());
            throw new RuntimeException(e);
        }
    }

    public String getSERVER_IP() {
        return SERVER_IP;
    }

    public int getPORT() {
        return PORT;
    }

    public Socket getClient() {
        return client;
    }
}
