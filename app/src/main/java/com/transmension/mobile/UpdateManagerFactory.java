package com.transmension.mobile;

import android.app.Activity;
import com.transmension.mobile.UpdateManager;

/* loaded from: classes.dex */
public class UpdateManagerFactory {

    /* loaded from: classes.dex */
    public static class DefaultUpdateManager implements UpdateManager {
        protected UpdateManager.Listener mListener;

        @Override // com.transmension.mobile.UpdateManager
        public String getName() {
            return "Default";
        }

        @Override // com.transmension.mobile.UpdateManager
        public int getFeatures() {
            return 0;
        }

        @Override // com.transmension.mobile.UpdateManager
        public void disableFeature(int feature) {
        }

        @Override // com.transmension.mobile.UpdateManager
        public boolean hasFeature(int feature) {
            return (getFeatures() & feature) == feature;
        }

        @Override // com.transmension.mobile.UpdateManager
        public boolean check() {
            return false;
        }

        @Override // com.transmension.mobile.UpdateManager
        public boolean shouldUpdateApp() {
            return false;
        }

        @Override // com.transmension.mobile.UpdateManager
        public boolean updateApp() {
            return false;
        }

        @Override // com.transmension.mobile.UpdateManager
        public boolean shouldUpdateAppData() {
            return false;
        }

        @Override // com.transmension.mobile.UpdateManager
        public boolean updateAppData() {
            return false;
        }

        @Override // com.transmension.mobile.UpdateManager
        public boolean hasAppData() {
            return !shouldUpdateAppData();
        }

        @Override // com.transmension.mobile.UpdateManager
        public String getExternalDataPath() {
            return null;
        }

        @Override // com.transmension.mobile.UpdateManager
        public boolean setProperty(String key, String value) {
            return false;
        }

        @Override // com.transmension.mobile.UpdateManager
        public String getProperty(String key) {
            return "";
        }

        @Override // com.transmension.mobile.UpdateManager
        public boolean hasProperty(String key) {
            return false;
        }

        @Override // com.transmension.mobile.UpdateManager
        public void onCreate() {
        }

        @Override // com.transmension.mobile.UpdateManager
        public void onStart() {
        }

        @Override // com.transmension.mobile.UpdateManager
        public void onPause() {
        }

        @Override // com.transmension.mobile.UpdateManager
        public void onResume() {
        }

        @Override // com.transmension.mobile.UpdateManager
        public void onStop() {
        }

        @Override // com.transmension.mobile.UpdateManager
        public void onDestroy() {
        }

        @Override // com.transmension.mobile.UpdateManager
        public void setListener(UpdateManager.Listener listener) {
            this.mListener = listener;
        }

        @Override // com.transmension.mobile.UpdateManager
        public UpdateManager.Listener getListener() {
            return this.mListener;
        }
    }

    UpdateManager create(Activity activity) {
        return new DefaultUpdateManager();
    }
}