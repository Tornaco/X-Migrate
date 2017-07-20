package dev.nick.library.common;

import com.google.common.base.Preconditions;

import java.io.File;

/**
 * Created by Tornaco on 2017/7/20.
 * Licensed with Apache.
 */

public abstract class Enforcer {

    public static void enforceFileExists(String path, String message) {
        Preconditions.checkArgument(new File(path).exists(), message);
    }

    public static void enforceFileReadable(String path, String message) {
        Preconditions.checkArgument(new File(path).canRead(), message);
    }
}
