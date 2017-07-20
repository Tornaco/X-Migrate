package dev.nick.library.nio;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Tornaco on 2017/7/19.
 * Licensed with Apache.
 */

public class ReceiverChannelInitializer implements ChannelInitializer {

    private String host;
    private int port;

    public static ReceiverChannelInitializer localhost() {
        return new ReceiverChannelInitializer("localhost", 8888);
    }

    public ReceiverChannelInitializer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @NonNull
    @Override
    public Channel initialize() throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(host, port));
        Socket socket = serverSocket.accept();
        Channel channel = new Channel();
        channel.setInputStream(socket.getInputStream());
        channel.setOutputStream(socket.getOutputStream());
        return channel;
    }
}
