package com.transmension.mobile;

import android.content.Context;

/* loaded from: classes.dex */
public interface ActivityEventListener {
    void onCreate(Context context);

    void onDestroy(Context context);

    void onPause(Context context);

    void onResume(Context context);

    void onStart(Context context);

    void onStop(Context context);
}