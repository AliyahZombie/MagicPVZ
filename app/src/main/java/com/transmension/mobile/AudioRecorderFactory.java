package com.transmension.mobile;

import android.app.Activity;
import java.nio.ByteBuffer;

/* loaded from: classes.dex */
public class AudioRecorderFactory {

    /* loaded from: classes.dex */
    public static class DefaultAudioRecorder implements AudioRecorder {
        @Override // com.transmension.mobile.AudioRecorder
        public boolean isAvailable() {
            return false;
        }

        @Override // com.transmension.mobile.AudioRecorder
        public String getName() {
            return "Default";
        }

        @Override // com.transmension.mobile.AudioRecorder
        public int getFeatures() {
            return 0;
        }

        @Override // com.transmension.mobile.AudioRecorder
        public boolean init(int source, int sampleRate, int channel, int format) {
            return false;
        }

        @Override // com.transmension.mobile.AudioRecorder
        public boolean isRecording() {
            return false;
        }

        @Override // com.transmension.mobile.AudioRecorder
        public boolean start() {
            return false;
        }

        @Override // com.transmension.mobile.AudioRecorder
        public boolean stop() {
            return false;
        }

        @Override // com.transmension.mobile.AudioRecorder
        public boolean release() {
            return false;
        }

        @Override // com.transmension.mobile.AudioRecorder
        public int read(ByteBuffer audioBuffer, int sizeInBytes) {
            return 0;
        }

        @Override // com.transmension.mobile.AudioRecorder
        public void onStart() {
        }

        @Override // com.transmension.mobile.AudioRecorder
        public void onPause() {
        }

        @Override // com.transmension.mobile.AudioRecorder
        public void onResume() {
        }

        @Override // com.transmension.mobile.AudioRecorder
        public void onStop() {
        }

        @Override // com.transmension.mobile.AudioRecorder
        public void onDestroy() {
        }
    }

    public AudioRecorder create(Activity activity) {
        return new DefaultAudioRecorder();
    }
}