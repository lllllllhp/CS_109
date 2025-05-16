package com.lllllllhp.utils.socket;

import com.lllllllhp.utils.socket.messageModel.Chat;
import com.lllllllhp.utils.socket.messageModel.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Scanner;

public class SocketClient {
    private Socket client;
    private final String SERVER_IP;
    private final int PORT;

    ObjectOutputStream out;
    ObjectInputStream in;
    Scanner scanner = new Scanner(System.in);

    String userName;
    volatile boolean connected = false;

    public SocketClient(String SERVER_IP, int PORT) {
        this.SERVER_IP = SERVER_IP;
        this.PORT = PORT;
    }

    public void startClient() {
        try {
            Socket client = new Socket(SERVER_IP, PORT);
            this.client = client;
            connected = true;
            System.out.println("connect to server");

            System.out.println("Please enter your name: ");
            userName = scanner.nextLine();
            System.out.println("succeed, your name: " + userName);

            out = new ObjectOutputStream(client.getOutputStream());
            in = new ObjectInputStream(client.getInputStream());

            //接受server广播
            new Thread(new ServerListener()).start();

            //输入消息
            while (client.isConnected()) {
                String content = scanner.nextLine();
                Message msg = new Chat(userName, content, LocalDateTime.now());
                out.writeObject(msg);
                out.flush();
            }
        } catch (BindException e) {
            System.out.println("PORT is occupied");
        } catch (IOException e) {
            System.out.println("connect fail");
            System.out.println(e.toString());
        }
    }

    public class ServerListener implements Runnable {
        @Override
        public void run() {
            try {
                while (client.isConnected()) {
                    Message msg = (Message) in.readObject();
                    System.out.println(msg);
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println(e.toString());
            } finally {
                closeClient();
                System.out.println("disconnected with server");
            }
        }
    }

    public void closeClient() {
        try {
            connected = false;
            client.close();
        } catch (IOException e) {
            System.out.println(e.toString());
            throw new RuntimeException(e);
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
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
