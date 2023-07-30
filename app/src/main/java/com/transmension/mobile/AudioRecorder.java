package com.transmension.mobile;

import java.nio.ByteBuffer;

/* loaded from: classes.dex */
public interface AudioRecorder {
    public static final int FEATURE_FEEDBACK = 4;
    public static final int FEATURE_LOW_LATENCY = 2;
    public static final int FEATURE_NONE = 0;
    public static final int FEATURE_REMOTE = 1;

    int getFeatures();

    String getName();

    boolean init(int i, int i2, int i3, int i4);

    boolean isAvailable();

    boolean isRecording();

    void onDestroy();

    void onPause();

    void onResume();

    void onStart();

    void onStop();

    int read(ByteBuffer byteBuffer, int i);

    boolean release();

    boolean start();

    boolean stop();
}