package com.transmension.mobile;

/* loaded from: classes.dex */
public interface UpdateManager {
    public static final int FEATURE_NULL = 0;
    public static final int FEATURE_UPDATE_APP = 1;
    public static final int FEATURE_UPDATE_APPDATA = 2;

    /* loaded from: classes.dex */
    public interface Listener {
        void onNewVersionAvailable();

        void onNoVersionAvailable();
    }

    boolean check();

    void disableFeature(int i);

    String getExternalDataPath();

    int getFeatures();

    Listener getListener();

    String getName();

    String getProperty(String str);

    boolean hasAppData();

    boolean hasFeature(int i);

    boolean hasProperty(String str);

    void onCreate();

    void onDestroy();

    void onPause();

    void onResume();

    void onStart();

    void onStop();

    void setListener(Listener listener);

    boolean setProperty(String str, String str2);

    boolean shouldUpdateApp();

    boolean shouldUpdateAppData();

    boolean updateApp();

    boolean updateAppData();
}