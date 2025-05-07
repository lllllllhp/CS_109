package com.lllllllhp.utils.socket;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetUtils {
    final static int PORT = 8886;
    final static String IP = "localhost";
    static SocketServer server;
    static SocketClient client;
    static ExecutorService executor = Executors.newSingleThreadExecutor();

    public static void startServer() {
        executor.submit(() -> {
            NetUtils.server = new SocketServer(PORT);
            server.startServer();
        });
    }

    public static void endServer() {
        if (server != null) {
            server.closeServer();
        } else System.out.println("close fail");
        server = null;
    }

    public static void startClient() {
        executor.submit(() -> {
            NetUtils.client = new SocketClient(IP, PORT);
            client.startClient();
        });
    }

    public static void endClient() {
        if (client != null)
            client.closeClient();
        client = null;
    }

    public static boolean hasServer() {
        return server != null;
    }

    public static boolean hasClient() {
        return client != null;
    }
}
