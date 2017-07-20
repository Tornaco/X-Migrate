package dev.nick.library;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.newstand.logger.Logger;
import org.newstand.logger.Settings;

import java.io.File;
import java.io.IOException;
import java.util.List;

import dev.nick.library.common.SharedExecutor;
import dev.nick.library.io.DataReader;
import dev.nick.library.io.DateWriter;
import dev.nick.library.loader.Loader;
import dev.nick.library.loader.LoaderListener;
import dev.nick.library.model.Category;
import dev.nick.library.model.android.AndroidData;
import dev.nick.library.model.android.FileBasedData;
import dev.nick.library.model.android.SystemLoaderSource;
import dev.nick.library.nio.Channel;
import dev.nick.library.nio.ChannelInitializer;
import dev.nick.library.nio.ReceiverChannelInitializer;
import dev.nick.library.nio.SenderChannelInitializer;

import static junit.framework.Assert.fail;

/**
 * Created by Tornaco on 2017/7/18.
 * Licensed with Apache.
 */
@RunWith(AndroidJUnit4.class)
public class MigraterTest {

    @BeforeClass
    public static void setup() throws IOException {
        Logger.config(Settings.builder().logLevel(Logger.LogLevel.ALL).tag("MigraterTest").build());
        String command = String.format("pm grant %s %s", InstrumentationRegistry.getTargetContext()
                        .getPackageName()
                , android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
                .executeShellCommand(command);
    }

    @Test
    public void testApi() throws IOException {
        List<AndroidData> list = Migrater.with(InstrumentationRegistry.getContext())
                .load(Category.App)
                .source(new SystemLoaderSource())
                .filter(new Loader.Filter<AndroidData>() {
                    @Override
                    public boolean ignore(@NonNull AndroidData androidData) {
                        return false;
                    }
                })
                .loaderListener(new LoaderListener<AndroidData>() {
                    @Override
                    public void onStartLoading() {

                    }

                    @Override
                    public void onLoadingComplete(@NonNull List<AndroidData> dataList) {
                        Logger.d("Load complete.");

                        for (AndroidData data : dataList) {
                            try {
                                stream((FileBasedData) data);
                            } catch (IOException e) {
                                fail(e.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onLoadingFailure(Throwable err) {
                        Logger.e(err, "Load onLoadingFailure");
                    }
                })
                .future()
                .execute()
                .getSafety();

        Logger.d("List:%s", list);
    }

    private void stream(final FileBasedData fileBasedData) throws IOException {

        final ChannelInitializer serverChannelInitializer = ReceiverChannelInitializer.localhost();

        SharedExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Channel source = serverChannelInitializer.initialize();
                    Logger.d("Server initialized");
                    new DateWriter(fileBasedData.asByteSource(),
                            fileBasedData.getCategory(),
                            fileBasedData.getDisplayName(),
                            "Hello world!")
                            .writeTo(source.getOutputStream(), null);
                    Logger.d("Server write");
                    serverChannelInitializer.teardown();
                } catch (IOException e) {
                    fail(e.getMessage());
                }
            }
        });

        Logger.d("startSink");
        String sinkPath = Environment.getExternalStorageDirectory().getPath() + File.separator + "testapps";
        ChannelInitializer socketChannelInitializer = SenderChannelInitializer.localhost();
        Channel sink = socketChannelInitializer.initialize();
        Logger.d("sink initialized");
        new DataReader(sinkPath).readFrom(sink.getInputStream(), null);
        Logger.d("sink read");
        socketChannelInitializer.teardown();
    }

}