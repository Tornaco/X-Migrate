package dev.nick.library.io;

/**
 * Created by Tornaco on 2017/7/19.
 * Licensed with Apache.
 */

public class Bytes {
    /**
     * Creates a new byte array for buffering reads or writes.
     */
    public static byte[] createBuffer() {
        return new byte[8192];
    }
}
