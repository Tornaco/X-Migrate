package dev.nick.library.loader;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.common.collect.Lists;

import java.util.List;

import dev.nick.library.model.android.AndroidData;

/**
 * Created by Tornaco on 2017/7/18.
 * Licensed with Apache.
 */

public class AppLoaderFile extends AndroidDataLoader {

    public AppLoaderFile(LoaderSource loaderSource) {
        super(loaderSource);
    }

    @NonNull
    @Override
    public List<AndroidData> load(@Nullable Filter<AndroidData> filter) {
        return Lists.newArrayList();
    }
}
