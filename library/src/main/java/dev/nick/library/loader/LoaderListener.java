package dev.nick.library.loader;

import android.support.annotation.NonNull;

import java.util.List;

import dev.nick.library.model.Data;

/**
 * Created by Tornaco on 2017/7/18.
 * Licensed with Apache.
 */

public interface LoaderListener<T extends Data> {
    void onStartLoading();

    void onLoadingComplete(@NonNull List<T> dataList);

    void onLoadingFailure(Throwable err);
}
