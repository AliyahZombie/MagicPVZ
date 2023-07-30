package com.transmension.mobile;

import android.app.Activity;
import com.transmension.mobile.ImagePicker;

/* loaded from: classes.dex */
public class ImagePickerFactory {

    /* loaded from: classes.dex */
    public static class DefaultImagePicker implements ImagePicker {
        @Override // com.transmension.mobile.ImagePicker
        public String getName() {
            return "default";
        }

        @Override // com.transmension.mobile.ImagePicker
        public boolean pick(ImagePicker.Config config) {
            return false;
        }

        @Override // com.transmension.mobile.ImagePicker
        public int getFeatures() {
            return 0;
        }

        @Override // com.transmension.mobile.ImagePicker
        public void onStart() {
        }

        @Override // com.transmension.mobile.ImagePicker
        public void onPause() {
        }

        @Override // com.transmension.mobile.ImagePicker
        public void onResume() {
        }

        @Override // com.transmension.mobile.ImagePicker
        public void onStop() {
        }

        @Override // com.transmension.mobile.ImagePicker
        public void onDestroy() {
        }

        @Override // com.transmension.mobile.ImagePicker
        public void setListener(ImagePicker.Listener listener) {
        }
    }

    ImagePicker create(Activity activity) {
        return new DefaultImagePicker();
    }
}