package dev.nick.library.model.android;

import android.support.annotation.NonNull;

import com.google.common.io.ByteSource;

import dev.nick.library.model.Category;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by Tornaco on 2017/7/19.
 * Licensed with Apache.
 */
@Getter
@Setter
@ToString
public class AppData extends FileBasedData {

    private String pkgName;
    private String versionName;
    private String iconPath;

    @NonNull
    @Override
    public ByteSource asByteSource() {
        return super.asByteSource();
    }

    @Override
    public Category getCategory() {
        return Category.App;
    }
}
