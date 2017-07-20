package dev.nick.library.loader;

import android.support.annotation.NonNull;

import java.util.List;

import dev.nick.library.model.Data;

/**
 * Created by Tornaco on 2017/7/18.
 * Licensed with Apache.
 */

public class LoaderListenerAdapter<T extends Data> implements LoaderListener<T> {
    @Override
    public void onStartLoading() {

    }

    @Override
    public void onLoadingComplete(@NonNull List<T> dataList) {

    }

    @Override
    public void onLoadingFailure(Throwable err) {

    }
}
