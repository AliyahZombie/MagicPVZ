package com.transmension.mobile;

import android.app.Activity;
import android.view.View;

/* loaded from: classes.dex */
public class TextInputManagerFactory {
    TextInputManager create(Activity activity, View view) {
        return new AndroidTextInputManager(activity, view);
    }
}