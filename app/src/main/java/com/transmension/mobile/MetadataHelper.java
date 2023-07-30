package com.transmension.mobile;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import java.util.Set;

/* loaded from: classes.dex */
public class MetadataHelper {
    public static String[] bundleToStringPairs(Bundle bundle) {
        if (bundle == null || bundle.size() == 0) {
            return null;
        }
        String[] result = new String[bundle.size() * 2];
        int i = 0;
        Set<String> keys = bundle.keySet();
        for (String key : keys) {
            Object value = bundle.get(key);
            String strValue = value.toString();
            int i2 = i + 1;
            result[i] = new String(key);
            i = i2 + 1;
            result[i2] = new String(strValue);
        }
        return result;
    }

    public static Bundle getAppMetaDataBundle(Context context) {
        ApplicationInfo appinfo = null;
        try {
            appinfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
        } catch (PackageManager.NameNotFoundException e) {
        }
        if (appinfo == null) {
            return null;
        }
        return appinfo.metaData;
    }

    public static String[] getAllAppMetaData(Context context) {
        Bundle metaData = getAppMetaDataBundle(context);
        return bundleToStringPairs(metaData);
    }

    public static String getAppMetaData(Context context, String key, String defaultValue) {
        Bundle metaData = getAppMetaDataBundle(context);
        return (metaData == null || !metaData.containsKey(key)) ? defaultValue : metaData.get(key).toString();
    }

    public static int getAppMetaData(Context context, String key, int defaultValue) {
        Bundle metaData = getAppMetaDataBundle(context);
        return (metaData == null || !metaData.containsKey(key)) ? defaultValue : metaData.getInt(key);
    }

    public static boolean getAppMetaData(Context context, String key, boolean defaultValue) {
        Bundle metaData = getAppMetaDataBundle(context);
        return (metaData == null || !metaData.containsKey(key)) ? defaultValue : metaData.getBoolean(key);
    }

    public static Bundle getMetaDataBundle(Activity context) {
        ActivityInfo ai = null;
        try {
            ai = context.getPackageManager().getActivityInfo(context.getComponentName(), 128);
        } catch (PackageManager.NameNotFoundException e) {
        }
        if (ai == null) {
            return null;
        }
        return ai.metaData;
    }

    public static String getMetaData(Activity context, String key) {
        Bundle metaData = getMetaDataBundle(context);
        if (metaData == null || !metaData.containsKey(key)) {
            return null;
        }
        return metaData.get(key).toString();
    }

    public static String getMetaData(Activity context, String key, String defaultValue) {
        Bundle metaData = getMetaDataBundle(context);
        return (metaData == null || !metaData.containsKey(key)) ? defaultValue : metaData.get(key).toString();
    }

    public static int getMetaData(Activity context, String key, int defaultValue) {
        Bundle metaData = getMetaDataBundle(context);
        return (metaData == null || !metaData.containsKey(key)) ? defaultValue : metaData.getInt(key);
    }

    public static boolean getMetaData(Activity context, String key, boolean defaultValue) {
        Bundle metaData = getMetaDataBundle(context);
        return (metaData == null || !metaData.containsKey(key)) ? defaultValue : metaData.getBoolean(key);
    }

    public String getVersionName(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }

    public int getVersionCode(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return 0;
        }
    }
}