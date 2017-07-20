package dev.nick.library.common;

/**
 * Created by Tornaco on 2017/7/19.
 * Licensed with Apache.
 */

public interface ProgressListener {
    /**
     * Max is 1, min is 0.
     */
    void onProgressUpdate(float progress);
}
