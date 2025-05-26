package com.lllllllhp.utils.socket;

import com.lllllllhp.data.MapRecord;
import com.lllllllhp.model.game.GameControllerModel;
import com.lllllllhp.model.game.GameModel;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetUtils {
    public final static int PORT = 8886;
    public final static String IP ;
    static {
        String ip1;
        try {
            ip1 = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            ip1 = "localhost";
            System.out.println(e.toString());
        }
        IP = ip1;
    }

    public static SocketServer server;
    public static SocketClient client;
    static ExecutorService executor = Executors.newSingleThreadExecutor();

    public static class ClientData {
        public static MapRecord spectatingMap;
        public static GameModel clientGame;
        public static GameControllerModel clientGameCon;
    }

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

    public static void startClient(String ip, int port) {
        executor.submit(() -> {
            NetUtils.client = new SocketClient(ip, port);
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
