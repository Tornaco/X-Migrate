package dev.nick.library.loader;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.List;

import dev.nick.library.model.Data;

/**
 * Created by Tornaco on 2017/7/18.
 * Licensed with Apache.
 */

public class LoaderListenerMainThreadDelegate<T extends Data> extends LoaderListenerAdapter<T> {

    private Handler handler = new Handler(Looper.getMainLooper());

    private LoaderListener<T> loaderListener;

    public LoaderListenerMainThreadDelegate(LoaderListener<T> loaderListener) {
        this.loaderListener = loaderListener;
    }

    @Override
    public void onStartLoading() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                loaderListener.onStartLoading();
            }
        });
    }

    @Override
    public void onLoadingComplete(@NonNull final List<T> dataList) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                loaderListener.onLoadingComplete(dataList);
            }
        });
    }

    @Override
    public void onLoadingFailure(final Throwable err) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                loaderListener.onLoadingFailure(err);
            }
        });
    }
}
