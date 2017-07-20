package dev.nick.library;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;

import dev.nick.library.common.SharedExecutor;
import dev.nick.library.loader.Loader;
import dev.nick.library.loader.LoaderListener;
import dev.nick.library.loader.LoaderSource;
import dev.nick.library.model.Category;
import dev.nick.library.model.android.AndroidData;
import dev.nick.library.model.android.FileBasedData;

/**
 * Main class for this lib.
 */
public class Migrater {

    private Context context;

    private Migrater(Context context) {
        this.context = context;
    }

    public static Migrater with(Context context) {
        return new Migrater(context);
    }

    public LoadRequest.LoadRequestBuilder load(Category category) {
        return LoadRequest.builder(context, category);
    }

    public StreamRequest.StreamRequestBuilder stream(FileBasedData fileBasedData) {
        return StreamRequest.builder(fileBasedData);
    }

    static class LoadRequest {

        private Category category;
        private LoaderSource loaderSource;
        private Loader.Filter<AndroidData> filter;
        private LoaderListener<AndroidData> loaderListener;

        public LoadRequest(Category category, LoaderSource loaderSource, Loader.Filter<AndroidData> filter,
                           LoaderListener<AndroidData> loaderListener) {
            this.category = category;
            this.loaderSource = loaderSource;
            this.filter = filter;
            this.loaderListener = loaderListener;
        }

        public static LoadRequestBuilder builder(Context context, Category category) {
            return new LoadRequestBuilder(context, category);
        }

        public static class LoadRequestBuilder {

            private Category category;
            private LoaderSource loaderSource;
            private Loader.Filter<AndroidData> filter;
            private LoaderListener<AndroidData> loaderListener;
            private Context context;

            LoadRequestBuilder(Context context, Category category) {
                this.category = category;
                this.context = context;
            }

            public LoadRequest.LoadRequestBuilder source(LoaderSource loaderSource) {
                this.loaderSource = loaderSource;
                return this;
            }

            public LoadRequest.LoadRequestBuilder filter(Loader.Filter<AndroidData> filter) {
                this.filter = filter;
                return this;
            }

            public LoadRequest.LoadRequestBuilder loaderListener(LoaderListener<AndroidData> loaderListener) {
                this.loaderListener = loaderListener;
                return this;
            }

            public LoadRequestTask future() {
                LoadRequest loadRequest = new LoadRequest(category, loaderSource, filter, loaderListener);
                return new LoadRequestTask(loadRequest, context);
            }
        }

        public static class LoadRequestTask extends FutureTask<List<AndroidData>> {

            LoadRequestTask(final LoadRequest request, final Context context) {
                super(new Callable<List<AndroidData>>() {
                    @Override
                    public List<AndroidData> call() throws Exception {
                        if (request.loaderListener != null) {
                            request.loaderListener.onStartLoading();
                        }
                        Category c = request.category;
                        Loader<AndroidData> loader = c.getLoader(request.loaderSource);
                        loader.wireContext(context);
                        List<AndroidData> dataList = Lists.newArrayList();
                        try {
                            dataList = loader.load(request.filter);
                            if (request.loaderListener != null) {
                                request.loaderListener.onLoadingComplete(dataList);
                            }
                        } catch (Throwable e) {
                            if (request.loaderListener != null) {
                                request.loaderListener.onLoadingFailure(e);
                            }
                        }
                        return dataList;
                    }
                });
            }

            public LoadRequestTask execute(@NonNull Executor executor) {
                executor.execute(this);
                return this;
            }

            public LoadRequestTask execute() {
                execute(SharedExecutor.getService());
                return this;
            }

            public List<AndroidData> getSafety() {
                try {
                    return get();
                } catch (InterruptedException | ExecutionException e) {
                    return Lists.newArrayList();
                }
            }
        }


    }

    static class StreamRequest {
        private FileBasedData fileBasedData;

        static StreamRequestBuilder builder(FileBasedData fileBasedData) {
            return new StreamRequestBuilder(fileBasedData);
        }

        public static class StreamRequestBuilder {
            private FileBasedData fileBasedData;

            private StreamRequestBuilder(FileBasedData fileBasedData) {
                this.fileBasedData = fileBasedData;
            }

        }
    }


}
