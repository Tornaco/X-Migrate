package dev.nick.library.io;

import android.support.annotation.NonNull;

import com.google.common.io.ByteSource;

/**
 * Created by Tornaco on 2017/7/19.
 * Licensed with Apache.
 */

public interface ByteSourcable {
    @NonNull
    ByteSource asByteSource();
}
