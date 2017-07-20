package dev.nick.library.model;

import android.support.annotation.NonNull;

import dev.nick.library.model.android.AndroidData;
import dev.nick.library.model.android.SystemLoaderSource;
import dev.nick.library.loader.AppLoaderFile;
import dev.nick.library.loader.AppLoaderSystem;
import dev.nick.library.loader.Loader;
import dev.nick.library.loader.LoaderProvider;
import dev.nick.library.loader.LoaderSource;

/**
 * Created by Tornaco on 2017/7/18.
 * Licensed with Apache.
 */

public enum Category implements LoaderProvider<AndroidData> {

    App {
        @Override
        public Loader<AndroidData> getLoader(@NonNull LoaderSource loaderSource) {
            if (loaderSource.getClass() == SystemLoaderSource.class) {
                return new AppLoaderSystem(loaderSource);
            }
            return new AppLoaderFile(loaderSource);
        }
    };

}
