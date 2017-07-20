package dev.nick.library.loader;

import android.content.Context;
import android.support.annotation.NonNull;

import dev.nick.library.model.android.AndroidData;

/**
 * Created by Tornaco on 2017/7/18.
 * Licensed with Apache.
 */
public abstract class AndroidDataLoader implements Loader<AndroidData> {

    private LoaderSource loaderSource;
    private Context context;

    public AndroidDataLoader(LoaderSource loaderSource) {
        this.loaderSource = loaderSource;
    }

    public LoaderSource getLoaderSource() {
        return loaderSource;
    }

    @Override
    public void wireContext(@NonNull Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }
}
