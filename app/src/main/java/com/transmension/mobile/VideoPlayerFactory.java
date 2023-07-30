package com.transmension.mobile;

import android.app.Activity;
import com.transmension.mobile.VideoPlayer;

/* loaded from: classes.dex */
public class VideoPlayerFactory {

    /* loaded from: classes.dex */
    public static class DefaultVideoPlayer implements VideoPlayer {
        @Override // com.transmension.mobile.VideoPlayer
        public boolean isAvailable() {
            return false;
        }

        @Override // com.transmension.mobile.VideoPlayer
        public String getName() {
            return "Default";
        }

        @Override // com.transmension.mobile.VideoPlayer
        public int getFeatures() {
            return 0;
        }

        @Override // com.transmension.mobile.VideoPlayer
        public boolean open(String uri) {
            return false;
        }

        @Override // com.transmension.mobile.VideoPlayer
        public boolean stop() {
            return false;
        }

        @Override // com.transmension.mobile.VideoPlayer
        public boolean close() {
            return false;
        }

        @Override // com.transmension.mobile.VideoPlayer
        public boolean pause() {
            return false;
        }

        @Override // com.transmension.mobile.VideoPlayer
        public void onStart() {
        }

        @Override // com.transmension.mobile.VideoPlayer
        public void onPause() {
        }

        @Override // com.transmension.mobile.VideoPlayer
        public void onResume() {
        }

        @Override // com.transmension.mobile.VideoPlayer
        public void onStop() {
        }

        @Override // com.transmension.mobile.VideoPlayer
        public void onDestroy() {
        }

        @Override // com.transmension.mobile.VideoPlayer
        public boolean play() {
            return false;
        }

        @Override // com.transmension.mobile.VideoPlayer
        public boolean seekTo(double time) {
            return false;
        }

        @Override // com.transmension.mobile.VideoPlayer
        public double getPosition() {
            return 0.0d;
        }

        @Override // com.transmension.mobile.VideoPlayer
        public double getDuration() {
            return 0.0d;
        }

        @Override // com.transmension.mobile.VideoPlayer
        public VideoPlayer.State getState() {
            return VideoPlayer.State.Stopped;
        }

        @Override // com.transmension.mobile.VideoPlayer
        public void setListener(VideoPlayer.Listener listener) {
        }

        @Override // com.transmension.mobile.VideoPlayer
        public int getScalingMode() {
            return 1;
        }

        @Override // com.transmension.mobile.VideoPlayer
        public boolean setScalingMode(int mode) {
            return false;
        }
    }

    public VideoPlayer create(Activity activity) {
        return new DefaultVideoPlayer();
    }
}