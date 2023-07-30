package com.transmension.mobile;

import android.app.Activity;
import com.transmension.mobile.SocialManager;

/* loaded from: classes.dex */
public class SocialManagerFactory {
    public SocialManager create(Activity activity) {
        return new SocialManager() { // from class: com.transmension.mobile.SocialManagerFactory.1
            @Override // com.transmension.mobile.SocialManager
            public String getName() {
                return "Default";
            }

            @Override // com.transmension.mobile.SocialManager
            public int getFeatures() {
                return 0;
            }

            @Override // com.transmension.mobile.SocialManager
            public boolean sendMessage(String msg, String options) {
                return false;
            }

            @Override // com.transmension.mobile.SocialManager
            public boolean show(String what, String options) {
                return false;
            }

            @Override // com.transmension.mobile.SocialManager
            public boolean hide(String what, String options) {
                return false;
            }

            @Override // com.transmension.mobile.SocialManager
            public void setListener(SocialManager.Listener listener) {
            }

            @Override // com.transmension.mobile.SocialManager
            public void onStart() {
            }

            @Override // com.transmension.mobile.SocialManager
            public void onPause() {
            }

            @Override // com.transmension.mobile.SocialManager
            public void onResume() {
            }

            @Override // com.transmension.mobile.SocialManager
            public void onStop() {
            }

            @Override // com.transmension.mobile.SocialManager
            public void onDestroy() {
            }
        };
    }
}