package dev.nick.library.nio;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import dev.nick.library.util.Closer;

/**
 * Created by Tornaco on 2017/7/19.
 * Licensed with Apache.
 */

public class Channel implements Closeable {

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

    @Override
    public void close() throws IOException {
        Closer.closeQuietly(getInputStream());
        Closer.closeQuietly(getOutputStream());
    }
}
