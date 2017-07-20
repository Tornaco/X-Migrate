package dev.nick.library.loader;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.common.collect.Lists;
import com.google.common.io.Files;

import org.newstand.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import dev.nick.library.model.android.AndroidData;
import dev.nick.library.model.android.AppData;
import dev.nick.library.util.ApkUtil;
import dev.nick.library.util.BitmapUtils;
import dev.nick.library.util.Closer;

/**
 * Created by Tornaco on 2017/7/18.
 * Licensed with Apache.
 */

public class AppLoaderSystem extends AndroidDataLoader {

    public AppLoaderSystem(LoaderSource loaderSource) {
        super(loaderSource);
    }

    @NonNull
    @Override
    public List<AndroidData> load(@Nullable Filter<AndroidData> filter) {

        Logger.i("loading app with filter:%s", filter);

        List<AndroidData> androidDataList = Lists.newArrayList();

        PackageManager pm = getContext().getPackageManager();
        List<PackageInfo> packages;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            packages = pm.getInstalledPackages(PackageManager.MATCH_UNINSTALLED_PACKAGES);
        } else {
            packages = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        }

        for (PackageInfo packageInfo : packages) {

            AppData appRecord = new AppData();

            String name = packageInfo.applicationInfo.loadLabel(pm).toString();
            if (!TextUtils.isEmpty(name)) {
                name = name.replace(" ", "");
            } else {
                Logger.w("Ignored app with empty name:%s", packageInfo);
                continue;
            }
            appRecord.setDisplayName(name);
            appRecord.setPkgName(packageInfo.packageName);
            appRecord.setFilePath(packageInfo.applicationInfo.publicSourceDir);
            appRecord.setVersionName(packageInfo.versionName);

            // Ignore our self.
            if (getContext().getPackageName().equals(appRecord.getPkgName())) {
                continue;
            }

            boolean isSystemApp = (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;

            if (ignoreSystemApp() && isSystemApp) {
                continue;
            }

            if (!isSystemApp && ignoreUserApp()) {
                continue;
            }

            try {
                long size = Files.asByteSource(new File(appRecord.getFilePath())).size();
                appRecord.setFileSize(size);
            } catch (IOException e) {
                Logger.e("Failed to query size for:%s", appRecord);
            }

            Bitmap bitmap;
            OutputStream os = null;
            // Cache icon if we have an cache dir.
            if (getIconCacheDir() != null) try {
                Drawable icon = ApkUtil.loadIconByPkgName(getContext(), packageInfo.packageName);
                bitmap = BitmapUtils.getBitmap(getContext(), icon);
                String iconUrl = getIconCacheDir() + File.separator + appRecord.getPkgName();
                appRecord.setIconPath(iconUrl);
                File iconFile = new File(iconUrl);
                Files.createParentDirs(iconFile);
                if (!iconFile.exists() && bitmap != null) {
                    os = Files.asByteSink(iconFile).openStream();
                    try {
                        if (!bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)) {
                            Logger.w("Fail compress bitmap");
                        }
                    } catch (Exception e) {
                        Logger.e(e, "Fail compress bitmap");
                    }
                }
            } catch (IOException e) {
                Logger.e(e, "Fail save icon");
            } finally {
                // Fix recycled bitmap can not be compressed issue.
                Closer.closeQuietly(os);
            }

            androidDataList.add(appRecord);
        }

        return androidDataList;
    }

    @Nullable
    protected String getIconCacheDir() {
        File externalCacheFile = getContext().getExternalCacheDir();
        if (externalCacheFile == null) return null;
        String cacheDir = externalCacheFile.getPath();
        return cacheDir + "icon_cache";
    }

    protected boolean ignoreSystemApp() {
        return true;
    }

    protected boolean ignoreUserApp() {
        return false;
    }
}
