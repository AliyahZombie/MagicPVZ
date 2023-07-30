package com.transmension.mobile;

import android.app.Activity;

/* loaded from: classes.dex */
public interface ImagePicker {
    public static final int FEATURE_NULL = 0;
    public static final int SOURCE_ALL = -1;
    public static final int SOURCE_CAMERA = 2;
    public static final int SOURCE_PHOTO_LIBRARY = 1;

    /* loaded from: classes.dex */
    public static class Config {
        public boolean cropEnabled = false;
        public int cropWidth = 0;
        public int cropHeight = 0;
        public int source = 1;
        public String path = "";
    }

    /* loaded from: classes.dex */
    public interface Listener {
        void onPickCancel(Activity activity, ImagePicker imagePicker);

        void onPickComplete(Activity activity, ImagePicker imagePicker, String str);

        void onPickError(Activity activity, ImagePicker imagePicker);
    }

    int getFeatures();

    String getName();

    void onDestroy();

    void onPause();

    void onResume();

    void onStart();

    void onStop();

    boolean pick(Config config);

    void setListener(Listener listener);
}