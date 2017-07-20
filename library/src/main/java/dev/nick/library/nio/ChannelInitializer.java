package dev.nick.library.nio;

import android.support.annotation.NonNull;

import java.io.IOException;

/**
 * Created by Tornaco on 2017/7/19.
 * Licensed with Apache.
 */

public interface ChannelInitializer {
    @NonNull
    Channel initialize() throws IOException;

    void teardown() throws IOException;
}
