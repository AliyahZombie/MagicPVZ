package com.transmension.mobile;

import android.app.Activity;
import android.graphics.Bitmap;
import com.transmension.mobile.ShareManager;

/* loaded from: classes.dex */
public class ShareManagerFactory {

    /* loaded from: classes.dex */
    public static class DefaultShareManager implements ShareManager {
        @Override // com.transmension.mobile.ShareManager
        public String getName() {
            return "default";
        }

        @Override // com.transmension.mobile.ShareManager
        public int share(String content, Bitmap bitmap) {
            return -1;
        }

        @Override // com.transmension.mobile.ShareManager
        public int share(String content, String bitmapPath) {
            return -1;
        }

        @Override // com.transmension.mobile.ShareManager
        public int share(ShareManager.Post post) {
            return share(post.content, post.image);
        }

        @Override // com.transmension.mobile.ShareManager
        public int getFeatures() {
            return 0;
        }

        @Override // com.transmension.mobile.ShareManager
        public void onStart() {
        }

        @Override // com.transmension.mobile.ShareManager
        public void onPause() {
        }

        @Override // com.transmension.mobile.ShareManager
        public void onResume() {
        }

        @Override // com.transmension.mobile.ShareManager
        public void onStop() {
        }

        @Override // com.transmension.mobile.ShareManager
        public void onDestroy() {
        }

        @Override // com.transmension.mobile.ShareManager
        public void setAppName(int resId) {
        }

        @Override // com.transmension.mobile.ShareManager
        public void setAppIcon(int resId) {
        }

        @Override // com.transmension.mobile.ShareManager
        public void setListener(ShareManager.Listener listener) {
        }
    }

    ShareManager create(Activity activity) {
        return new DefaultShareManager();
    }
}