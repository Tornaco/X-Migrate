package dev.nick.library.common;

/**
 * Created by Tornaco on 2017/7/19.
 * Licensed with Apache.
 */

public class ProgressListenerAdapter implements ProgressListener {

    public static final ProgressListener DUMMY = new ProgressListenerAdapter() {
        @Override
        public void onProgressUpdate(float progress) {
            super.onProgressUpdate(progress);
        }
    };

    @Override
    public void onProgressUpdate(float progress) {

    }
}
