package com.transmension.mobile;

import android.app.Activity;

/* loaded from: classes.dex */
public interface SocialManager {
    public static final int FEATURE_BBS = 1;
    public static final int FEATURE_NONE = 0;

    int getFeatures();

    String getName();

    boolean hide(String str, String str2);

    void onDestroy();

    void onPause();

    void onResume();

    void onStart();

    void onStop();

    boolean sendMessage(String str, String str2);

    void setListener(Listener listener);

    boolean show(String str, String str2);

    /* loaded from: classes.dex */
    public static abstract class Listener {
        public abstract void onEvent(Activity activity, SocialManager socialManager, String str, int i, String str2);

        public void onEvent(Activity activity, SocialManager extension, String event) {
            onEvent(activity, extension, event, 0, null);
        }
    }
}