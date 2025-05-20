package com.lllllllhp.utils.socket;

import com.lllllllhp.data.MapRecord;
import com.lllllllhp.model.game.GameControllerModel;
import com.lllllllhp.model.game.GameModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetUtils {
    final static int PORT = 8886;
    final static String IP = "localhost";
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
