package com.lllllllhp.utils.socket;

import com.lllllllhp.utils.socket.messageModel.Message;

import java.io.IOException;
import java.net.Socket;
import java.util.function.Consumer;

public abstract class SocketBase {
    protected Consumer<Socket> connectCallback = socket -> {};
    protected Consumer<Message> receiveCallback = Message -> {};
    protected Consumer<IOException> errorCallback = IOException -> {};
    protected Socket connected;

    public void addConnectListener(Consumer<Socket> listener) {
        connectCallback = listener;
    }

    public void addMessageListener(Consumer<Message> listener) {
        receiveCallback = listener;
    }

    public void addErrorListener(Consumer<IOException> listener) {
        errorCallback = listener;
    }

}
