import com.lllllllhp.utils.socket.SocketClient;

import java.io.IOException;

public class ClientTest {
    public static void main(String[] args) {
        final String serverIp = "localhost";
        final int PORT = 8888;
        SocketClient client = new SocketClient(serverIp, PORT);

        try {
            client.startClient();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        client.closeClient();
    }
}
