import com.lllllllhp.utils.socket.SocketServer;

import java.io.IOException;

public class ServerTest {
    public static void main(String[] args) {
        final int PORT = 8888;
        SocketServer socketServer = new SocketServer(PORT);

        try {
            socketServer.startServer();
        } catch (IOException e) {
            System.out.println(e.toString());
        }

        socketServer.closeServer();
    }
}
