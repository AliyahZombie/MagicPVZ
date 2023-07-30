package com.transmension.mobile;

/* loaded from: classes.dex */
public class NativeImagePicker {
    public static native void onCancelNative(long j, ImagePicker imagePicker);

    public static native void onCompleteNative(long j, ImagePicker imagePicker, String str);

    public static native void onErrorNative(long j, ImagePicker imagePicker);
}