package dev.nick.library.model.android;

import android.support.annotation.NonNull;

import com.google.common.io.ByteSource;
import com.google.common.io.Files;

import java.io.File;

import dev.nick.library.io.ByteSourcable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by Tornaco on 2017/7/19.
 * Licensed with Apache.
 */
@Getter
@Setter
@ToString
public abstract class FileBasedData extends AndroidData implements ByteSourcable {

    private String filePath;
    private long fileSize;

    @NonNull
    @Override
    public ByteSource asByteSource() {
        return Files.asByteSource(new File(getFilePath()));
    }
}
