package com.transmension.mobile;

import android.content.Context;

/* loaded from: classes.dex */
public interface IMainApplication {

    /* loaded from: classes.dex */
    public interface Listener {
        void onNotificationManagerCreated();
    }

    void LoadNativeModule(String str);

    NotificationManager createNotificationManager();

    NotificationManager getNotificationManager();

    String getNotificationManagerFactoryName();

    boolean hasNotificationManager();

    void onAttachedToBaseContext(Context context);

    void onCreate();

    void onPrecreate();

    void onTerminate();

    void setNotificationManagerFactoryName(String str);
}