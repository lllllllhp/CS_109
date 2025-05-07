package com.lllllllhp.utils.socket;

import com.lllllllhp.utils.socket.messageModel.Message;

import java.io.*;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer extends SocketBase {
    ServerSocket serverSocket;
    final int PORT;
    final CopyOnWriteArrayList<ObjectOutputStream> clientOutputs = new CopyOnWriteArrayList<>();
    final ExecutorService pool = Executors.newCachedThreadPool();
    boolean isRunning = false;

    public SocketServer(int PORT) {
        this.PORT = PORT;
    }

    public void startServer() {
        try {
            serverSocket = new ServerSocket(PORT);
            isRunning = true;
            System.out.println("start serverSocket");

            while (isRunning) {
                Socket client = serverSocket.accept();
                System.out.println("client connect：" + client.getRemoteSocketAddress());
                //每个客户端生成新的接收线程
                pool.submit(new ClientHandler(client));
            }
        } catch (BindException e) {
            System.out.println("PORT is occupied");
        } catch (IOException e) {
            System.out.println("create serverSocket fail");
            System.out.println(e.toString());
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
}
