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
import dev.nick.library.io.FileReader;
import dev.nick.library.io.FileWriter;
import dev.nick.library.loader.Loader;
import dev.nick.library.loader.LoaderListenerAdapter;
import dev.nick.library.loader.LoaderListenerMainThreadDelegate;
import dev.nick.library.model.Category;
import dev.nick.library.model.android.AndroidData;
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
                .loaderListener(new LoaderListenerMainThreadDelegate<>(new LoaderListenerAdapter<AndroidData>() {
                    @Override
                    public void onLoadingComplete(@NonNull List<AndroidData> dataList) {
                        super.onLoadingComplete(dataList);
                        Logger.d("Load complete.");
                    }
                }))
                .future()
                .execute()
                .getSafety();

        Logger.d("List:%s", list);


        final String sourcePath = Environment.getExternalStorageDirectory().getPath() + File.separator + "test.mp4";


        final ChannelInitializer serverChannelInitializer = ReceiverChannelInitializer.localhost();

        SharedExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Channel source = serverChannelInitializer.initialize();
                    Logger.d("Server initialized");
                    new FileWriter(sourcePath).writeTo(source.getOutputStream(), null);
                    Logger.d("Server write");
                } catch (IOException e) {
                    fail(e.getMessage());
                }
            }
        });

        startSink();

        while (true) {
        }
    }

    private void startSink() throws IOException {
        Logger.d("startSink");
        String sinkPath = Environment.getExternalStorageDirectory().getPath() + File.separator + "test2.mp4";
        ChannelInitializer socketChannelInitializer = SenderChannelInitializer.localhost();
        Channel sink = socketChannelInitializer.initialize();
        Logger.d("sink initialized");
        new FileReader(sinkPath).readFrom(sink.getInputStream(), null);
        Logger.d("sink read");
    }

}