package dev.nick.library.nio;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Tornaco on 2017/7/19.
 * Licensed with Apache.
 */

public class SenderChannelInitializer implements ChannelInitializer {

    private String host;
    private int port;

    public static SenderChannelInitializer localhost() {
        return new SenderChannelInitializer("localhost", 8888);
    }

    public SenderChannelInitializer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @NonNull
    @Override
    public Channel initialize() throws IOException {
        Socket socket = new Socket(host, port);
        Channel channel = new Channel();
        channel.setInputStream(socket.getInputStream());
        channel.setOutputStream(socket.getOutputStream());
        return channel;
    }
}
