package dev.nick.library.loader;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import dev.nick.library.common.ContextWirable;
import dev.nick.library.model.Data;

/**
 * Created by Tornaco on 2017/7/18.
 * Licensed with Apache.
 */

public interface Loader<T extends Data> extends ContextWirable {
    @NonNull
    List<T> load( @Nullable Filter<T> filter);

    interface Filter<T extends Data> {
        boolean ignore(@NonNull T t);
    }
}
