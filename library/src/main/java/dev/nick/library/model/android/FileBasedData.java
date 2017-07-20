package dev.nick.library.model.android;

import android.support.annotation.NonNull;

import com.google.common.io.ByteSource;
import com.google.common.io.Files;

import java.io.File;

import dev.nick.library.io.ByteSourcable;

/**
 * Created by Tornaco on 2017/7/19.
 * Licensed with Apache.
 */

public class FileBasedData extends AndroidData implements ByteSourcable {

    private String filePath;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @NonNull
    @Override
    public ByteSource asByteSource() {
        return Files.asByteSource(new File(getFilePath()));
    }
}
