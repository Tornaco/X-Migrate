package dev.nick.library.io.procotol;

import com.google.common.primitives.Longs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Tornaco on 2017/7/19.
 * Licensed with Apache.
 */

public class FileHeader {

    public static long sizeOf(InputStream in) throws IOException {
        byte[] sizeBuffer = new byte[Longs.BYTES];
        if (sizeBuffer.length != in.read(sizeBuffer)) {
            throw new IOException("Invalid file header.");
        }
        return Longs.fromByteArray(sizeBuffer);
    }

    public static void sizeTo(OutputStream outputStream, long size) throws IOException {
        byte[] bytes = Longs.toByteArray(size);
        outputStream.write(bytes);
    }
}
