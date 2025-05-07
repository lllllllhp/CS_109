package com.lllllllhp.utils.socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

public class SocketServer extends SocketBase {
    ServerSocket serverSocket;
    private final int PORT;
    private static final CopyOnWriteArrayList<ObjectOutputStream> clientOutputs = new CopyOnWriteArrayList<>();

    public SocketServer(int PORT) {
        this.PORT = PORT;
    }

    public void startServer() throws IOException {
        serverSocket = new ServerSocket(PORT);
        System.out.println("服务器启动");

        while (true) {
            Socket client = serverSocket.accept();
            System.out.println("客户端连接：" + client.getRemoteSocketAddress());
            //每个客户端生成新的接收线程
            new Thread(new ClientHandler(client)).start();
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

                while (true) {
                    Message msg = (Message) in.readObject();
                    broadcast(msg);
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println(e.toString());
                System.out.println(client.getRemoteSocketAddress() + "断开连接");
            }finally {
                try {
                    client.close();
                } catch (IOException e) {
                    System.out.println(e.toString());
                }
            }
        }

        public void broadcast(Message msg) throws IOException {
            for (ObjectOutputStream writer : clientOutputs) {
                writer.writeObject(msg);
                writer.flush();
            }
            System.out.println(msg);
        }
    }


    public void closeServer() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.out.println(e.toString());
            throw new RuntimeException(e);
        }
    }

    public int getPORT() {
        return PORT;
    }
}
