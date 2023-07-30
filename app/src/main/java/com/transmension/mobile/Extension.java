package com.transmension.mobile;

import android.app.Activity;

/* loaded from: classes.dex */
public interface Extension {
    public static final String EXTENSION_TYPE_ANALYTICS = "ANALYTICS";
    public static final String EXTENSION_TYPE_DEFAULT = "DEFAULT";
    public static final String EXTENSION_TYPE_SCREEN_RECORDER = "SCREEN_RECORDER";

    String getExtensionType();

    int getFeatures();

    int getId();

    String getName();

    boolean hide(String str, String str2);

    void onDestroy();

    void onPause();

    void onResume();

    void onStart();

    void onStop();

    boolean sendMessage(String str, String str2);

    void setId(int i);

    void setListener(Listener listener);

    boolean show(String str, String str2);

    /* loaded from: classes.dex */
    public static abstract class Listener {
        public abstract void onEvent(Activity activity, Extension extension, String str, int i, String str2);

        public void onEvent(Activity activity, Extension extension, String event) {
            onEvent(activity, extension, event, 0, null);
        }
    }
}