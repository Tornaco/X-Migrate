package dev.nick.library.io;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
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
import dev.nick.library.io.procotol.DataHeader;

/**
 * Created by Tornaco on 2017/7/20.
 * Licensed with Apache.
 */

public class DataReader {

    private String sinkFileDir;

    public DataReader(String sinkFileDir) {
        this.sinkFileDir = sinkFileDir;
        Preconditions.checkArgument(!new File(sinkFileDir).exists()
                || new File(sinkFileDir).isDirectory(), "Only dir or no-exists file can be accepted");
    }

    public void readFrom(InputStream inputStream, ProgressListener progressListener)
            throws IOException {
        File sinkFile = new File(sinkFileDir);
        Preconditions.checkArgument(sinkFile.exists()
                || sinkFile.mkdirs(), "Fail mkdirs:" + sinkFileDir);

        Optional<ProgressListener> listenerOptional = Optional
                .fromNullable(progressListener);
        Closer closer = Closer.create();

        DataHeader dataHeader = DataHeader.readFrom(inputStream);
        Logger.d("Reader, file header:%s", dataHeader);
        String name = dataHeader.getName();
        long size = dataHeader.getSize();

        File targetFile = new File(sinkFileDir + File.separator + name);
        Logger.d("Target file in reader:%s", targetFile);
        ByteSink byteSink = Files.asByteSink(targetFile);
        OutputStream to = closer.register(byteSink.openStream());

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
        closer.close();
    }
}
