package com.transmension.mobile;

/* loaded from: classes.dex */
public class NativeShareManager {
    public static native void onCancelNative(long j, ShareManager shareManager, int i);

    public static native void onCompleteNative(long j, ShareManager shareManager, int i);

    public static native void onErrorNative(long j, ShareManager shareManager, int i);
}