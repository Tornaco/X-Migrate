package dev.nick.library.loader;

import android.support.annotation.NonNull;

import dev.nick.library.model.Data;

/**
 * Created by Tornaco on 2017/7/18.
 * Licensed with Apache.
 */

public interface LoaderProvider<T extends Data> {
    Loader<T> getLoader(@NonNull LoaderSource loaderSource);
}
