package com.lllllllhp.utils.socket;

import com.lllllllhp.utils.socket.messageModel.Chat;
import com.lllllllhp.utils.socket.messageModel.Message;

import java.io.*;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer {
    ServerSocket serverSocket;
    final int PORT;
    final CopyOnWriteArrayList<ObjectOutputStream> clientOutputs = new CopyOnWriteArrayList<>();
    final ExecutorService pool = Executors.newFixedThreadPool(5);
    final ExecutorService broadcastPoll = Executors.newSingleThreadExecutor();
    volatile boolean isRunning = false;

    public SocketServer(int PORT) {
        this.PORT = PORT;
    }

    public void startServer() {
        try {
            serverSocket = new ServerSocket(PORT);
            isRunning = true;
            System.out.println("start serverSocket");
        } catch (BindException e) {
            System.out.println("PORT is occupied");
        } catch (IOException e) {
            System.out.println("create serverSocket fail");
            System.out.println(e.toString());
        }
        //连接客户端
        while (isRunning) {
            try {
                Socket client = serverSocket.accept();
                System.out.println("client connect: " + client.getRemoteSocketAddress());
                pool.submit(new ClientHandler(client));
            } catch (IOException e) {
                // 正常关闭时的异常，不需要打印
                if (!isRunning) break;
                System.out.println(e.toString());
            }
        }
    }

    public class ClientHandler implements Runnable {
        Socket client;
        ObjectOutputStream out;
        ObjectInputStream in;

        public ClientHandler(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            try {
                in = new ObjectInputStream(client.getInputStream());
                out = new ObjectOutputStream(client.getOutputStream());
                clientOutputs.add(out);

                while (client.isConnected()) {
                    Message msg = (Message) in.readObject();
                    broadcast(msg);
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println(e.toString());
                System.out.println(client.getRemoteSocketAddress() + "disconnected");
            } finally {
                try {
                    client.close();
                    clientOutputs.remove(out);
                } catch (IOException e) {
                    System.out.println(e.toString());
                }
            }
        }
    }

    public <T extends Message> void broadcast(T msg) throws IOException {
        for (ObjectOutputStream writer : clientOutputs) {
            writer.writeObject(msg);
            writer.flush();
        }
        //展示聊天
        if (msg instanceof Chat) msg.show();
    }

    public void closeServer() {
        try {
            if (serverSocket != null) {
                isRunning = false;
                serverSocket.close();
                System.out.println("close serverSocket succeed");
            } else System.out.println("close server fail: serverSocket is null");
            pool.shutdownNow();
        } catch (IOException e) {
            System.out.println(e.toString());
            throw new RuntimeException(e);
        }
    }

    public int getPORT() {
        return PORT;
    }

    public boolean isRunning() {
        return isRunning;
    }
}
