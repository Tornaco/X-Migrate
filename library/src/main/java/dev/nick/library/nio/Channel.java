package dev.nick.library.nio;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Tornaco on 2017/7/19.
 * Licensed with Apache.
 */

public class Channel {

    private InputStream inputStream;
    private OutputStream outputStream;

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }
}
