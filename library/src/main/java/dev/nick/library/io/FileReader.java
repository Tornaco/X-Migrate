package dev.nick.library.io;

import com.google.common.base.Optional;
import com.google.common.io.ByteSink;
import com.google.common.io.Closer;
import com.google.common.io.Files;

import org.newstand.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import dev.nick.library.common.ProgressListener;
import dev.nick.library.common.ProgressListenerAdapter;
import dev.nick.library.io.procotol.FileHeader;

/**
 * Created by Tornaco on 2017/7/20.
 * Licensed with Apache.
 */

public class FileReader {

    private String sinkFilePath;

    public FileReader(String sinkFilePath) {
        this.sinkFilePath = sinkFilePath;
    }

    public void readFrom(InputStream inputStream, ProgressListener progressListener) throws IOException {
        // Create parent files.
        File sinkFile = new File(sinkFilePath);
        Files.createParentDirs(sinkFile);

        Optional<ProgressListener> listenerOptional = Optional
                .fromNullable(progressListener);
        Closer closer = Closer.create();

        ByteSink byteSink = Files.asByteSink(sinkFile);
        OutputStream to = closer.register(byteSink.openStream());

        // Read file size first.
        long size = FileHeader.sizeOf(inputStream);

        Logger.d("File size in read:%s", size);

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
