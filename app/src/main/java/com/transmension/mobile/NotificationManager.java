package com.transmension.mobile;

import android.app.Activity;

/* loaded from: classes.dex */
public interface NotificationManager {
    public static final int FEATURE_ALIAS = 1;
    public static final int FEATURE_NULL = 0;
    public static final int FEATURE_TAGS = 2;

    void cancelAllNotifications();

    int getFeatures();

    String getName();

    void onDestroy();

    void onPause();

    void onResume();

    void onStart();

    void onStop();

    void registerActivity(Activity activity);

    boolean setAlias(String str);

    boolean setTags(String str);

    void unregisterActivity(Activity activity);
}