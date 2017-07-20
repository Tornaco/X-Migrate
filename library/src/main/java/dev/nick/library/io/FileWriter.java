package dev.nick.library.io;

import com.google.common.base.Optional;
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
import dev.nick.library.io.procotol.FileHeader;

/**
 * Created by Tornaco on 2017/7/20.
 * Licensed with Apache.
 */

public class FileWriter {

    private String filePath;

    public FileWriter(String filePath) {
        this.filePath = filePath;
        Enforcer.enforceFileExists(filePath, "File not exists");
    }

    public void writeTo(OutputStream outputStream,
                        ProgressListener progressListener) throws IOException {

        File target = new File(filePath);

        ByteSource byteSource = Files.asByteSource(target);

        // Write file size first.
        long size = byteSource.size();
        FileHeader.sizeTo(outputStream, size);

        Logger.d("File size in write:%s", size);

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
    }
}
