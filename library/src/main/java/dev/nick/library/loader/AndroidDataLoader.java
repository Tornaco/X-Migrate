package dev.nick.library.loader;

import dev.nick.library.model.android.AndroidData;

/**
 * Created by Tornaco on 2017/7/18.
 * Licensed with Apache.
 */
public abstract class AndroidDataLoader implements Loader<AndroidData> {
    private LoaderSource loaderSource;

    public AndroidDataLoader(LoaderSource loaderSource) {
        this.loaderSource = loaderSource;
    }

    public LoaderSource getLoaderSource() {
        return loaderSource;
    }
}
