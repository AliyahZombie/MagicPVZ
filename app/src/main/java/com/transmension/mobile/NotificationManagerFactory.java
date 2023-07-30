package com.transmension.mobile;

import android.app.Activity;
import android.content.Context;

/* loaded from: classes.dex */
public class NotificationManagerFactory {

    /* loaded from: classes.dex */
    public static class DefaultPushNotification implements NotificationManager {
        @Override // com.transmension.mobile.NotificationManager
        public String getName() {
            return "default";
        }

        @Override // com.transmension.mobile.NotificationManager
        public int getFeatures() {
            return 0;
        }

        @Override // com.transmension.mobile.NotificationManager
        public void cancelAllNotifications() {
        }

        @Override // com.transmension.mobile.NotificationManager
        public void onStart() {
        }

        @Override // com.transmension.mobile.NotificationManager
        public void onPause() {
        }

        @Override // com.transmension.mobile.NotificationManager
        public void onResume() {
        }

        @Override // com.transmension.mobile.NotificationManager
        public void onStop() {
        }

        @Override // com.transmension.mobile.NotificationManager
        public void onDestroy() {
        }

        @Override // com.transmension.mobile.NotificationManager
        public boolean setAlias(String alias) {
            return false;
        }

        @Override // com.transmension.mobile.NotificationManager
        public boolean setTags(String tags) {
            return false;
        }

        @Override // com.transmension.mobile.NotificationManager
        public void registerActivity(Activity activity) {
        }

        @Override // com.transmension.mobile.NotificationManager
        public void unregisterActivity(Activity activity) {
        }
    }

    NotificationManager create(Context context) {
        return new DefaultPushNotification();
    }
}