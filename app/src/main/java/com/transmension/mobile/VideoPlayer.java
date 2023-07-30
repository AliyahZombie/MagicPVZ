package com.transmension.mobile;

import android.app.Activity;

/* loaded from: classes.dex */
public interface VideoPlayer {
    public static final int EVENT_COMPLETED = 2;
    public static final int EVENT_STARTED = 1;
    public static final int FEATURE_NONE = 0;
    public static final int SCALING_MODE_CROP = 2;
    public static final int SCALING_MODE_SCALE_TO_FIT = 1;

    /* loaded from: classes.dex */
    public enum State {
        Stopped,
        Paused,
        Playing,
        Seeking;

        /* JADX DEBUG: Replace access to removed values field (ENUM$VALUES) with 'values()' method */
        /* renamed from: values  reason: to resolve conflict with enum method */
        public static State[] valuesCustom() {
            State[] valuesCustom = values();
            int length = valuesCustom.length;
            State[] stateArr = new State[length];
            System.arraycopy(valuesCustom, 0, stateArr, 0, length);
            return stateArr;
        }
    }

    boolean close();

    double getDuration();

    int getFeatures();

    String getName();

    double getPosition();

    int getScalingMode();

    State getState();

    boolean isAvailable();

    void onDestroy();

    void onPause();

    void onResume();

    void onStart();

    void onStop();

    boolean open(String str);

    boolean pause();

    boolean play();

    boolean seekTo(double d);

    void setListener(Listener listener);

    boolean setScalingMode(int i);

    boolean stop();

    /* loaded from: classes.dex */
    public static abstract class Listener {
        public abstract void onEvent(Activity activity, VideoPlayer videoPlayer, int i, int i2, String str);

        public void onEvent(Activity activity, VideoPlayer player, int event) {
            onEvent(activity, player, event, 0, null);
        }

        public void onStarted(Activity activity, VideoPlayer player) {
            onEvent(activity, player, 1, 0, null);
        }

        public void onComplete(Activity activity, VideoPlayer player) {
            onEvent(activity, player, 2, 0, null);
        }
    }
}