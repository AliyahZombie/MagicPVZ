package com.transmension.mobile;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;

/* loaded from: classes.dex */
public interface ShareManager {
    public static final int FEATURE_IMAGE = 2;
    public static final int FEATURE_IMAGEURL = 4;
    public static final int FEATURE_MUSIC = 8;
    public static final int FEATURE_NULL = 0;
    public static final int FEATURE_TEXT = 1;

    /* loaded from: classes.dex */
    public interface Listener {
        void onShareCancel(Activity activity, ShareManager shareManager, int i);

        void onShareComplete(Activity activity, ShareManager shareManager, int i);

        void onShareError(Activity activity, ShareManager shareManager, int i);
    }

    /* loaded from: classes.dex */
    public static class Post {
        public int features = 0;
        public String title = "";
        public String titleURL = "";
        public String content = "";
        public String image = "";
        public String targetURL = "";
        public String siteURL = "";
        public Bundle extras = null;
        public String music = "";
    }

    int getFeatures();

    String getName();

    void onDestroy();

    void onPause();

    void onResume();

    void onStart();

    void onStop();

    void setAppIcon(int i);

    void setAppName(int i);

    void setListener(Listener listener);

    int share(Post post);

    int share(String str, Bitmap bitmap);

    int share(String str, String str2);
}