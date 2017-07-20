package dev.nick.library.io;

import android.support.annotation.NonNull;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.io.ByteSource;
import com.google.common.io.Closer;
import com.google.common.io.Files;

import org.newstand.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import dev.nick.library.common.Enforcer;
import dev.nick.library.common.ProgressListener;
import dev.nick.library.common.ProgressListenerAdapter;
import dev.nick.library.io.procotol.DataHeader;
import dev.nick.library.model.Category;

/**
 * Created by Tornaco on 2017/7/20.
 * Licensed with Apache.
 */

public class DateWriter {

    private ByteSource byteSource;
    private String name;
    private String extra;
    private Category category;

    public DateWriter(@NonNull String filePath) {
        Enforcer.enforceFileExists(filePath, "File not exists");
        File target = new File(filePath);
        this.byteSource = Files.asByteSource(target);
        this.name = target.getName();
    }

    public DateWriter(@NonNull ByteSource byteSource,
                      Category category,
                      String name, String extra) {
        this.byteSource = Preconditions.checkNotNull(byteSource);
        this.category = category;
        this.name = name;
        this.extra = extra;
    }

    public DateWriter(@NonNull ByteSource byteSource,
                      Category category,
                      String name) {
        this(byteSource, category, name, null);
    }

    public void writeTo(OutputStream outputStream,
                        ProgressListener progressListener) throws IOException {


        // Write file size first.
        long size = byteSource.size();
        DataHeader dataHeader = DataHeader.builder()
                .name(name).size(size).extra(extra)
                .category(category)
                .build();
        Logger.d("Writing file header:%s", dataHeader);
        DataHeader.writeTo(outputStream, dataHeader);

        Optional<ProgressListener> listenerOptional = Optional
                .fromNullable(progressListener);
        // Write content.
        Closer closer = Closer.create();
        OutputStream to = closer.register(outputStream);

        InputStream inputStream = byteSource.openStream();
        byte[] buf = Bytes.createBuffer();
        long total = 0;
        while (true) {
            int r = inputStream.read(buf);
            if (r == -1) {
                break;
            }
            to.write(buf, 0, r);
            total += r;
            float progress = (float) total / (float) size;
            listenerOptional.or(ProgressListenerAdapter.DUMMY).onProgressUpdate(progress);
        }
        closer.close(); // We should close anyway, or EOS will not be info.
    }
}
