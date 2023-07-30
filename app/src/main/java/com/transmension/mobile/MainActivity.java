package com.transmension.mobile;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager;

/* loaded from: classes.dex */
public class MainActivity extends NativeActivity {
    private static final String TAG = "MainActivity";
    private AudioRecorder mAudioRecorder;
    private GameCenter mGameCenter;
    private ImagePicker mImagePicker;
    private InputManager mInputManager;
    private NotificationManager mNotificationManager;
    private ShareManager mShareManager;
    private SocialManager mSocialManager;
    private UpdateManager mUpdateManager;
    private VideoPlayer mVideoPlayer;
    private String mGameCenterFactoryName = GameCenterFactory.class.getName();
    private String mShareManagerFactoryName = ShareManagerFactory.class.getName();
    private String mNotificationManagerFactoryName = NotificationManagerFactory.class.getName();
    private String mInputManagerFactoryName = InputManagerFactory.class.getName();
    private String mUpdateManagerFactoryName = UpdateManagerFactory.class.getName();
    private String mAudioRecorderFactoryName = AudioRecorderFactory.class.getName();
    private String mTextInputManagerFactoryName = TextInputManagerFactory.class.getName();
    private String mImagePickerFactoryName = AndroidImagePickerFactory.class.getName();
    private final AudioOutput mAudioOutput = new AudioOutput(this);
    private final ExtensionManager mExtensionManager = new ExtensionManager(this);
    private String mSocialManagerFactoryName = SocialManagerFactory.class.getName();
    private String mVideoPlayerFactoryName = AndroidVideoPlayerFactory.class.getName();


    public void hideSystemNagvigationBar() {
        if (Build.VERSION.SDK_INT >= 14) {
            getWindow().getDecorView().setSystemUiVisibility(2);
        }
    }

    @Override // com.transmension.mobile.NativeActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        UpdateManager updateManager = createUpdateManager();
//        if (updateManager != null && !isFinishing()) {
//            GameCenter gameCenter = createGameCenter();
//            if (gameCenter != null) {
//                if ((gameCenter.getFeatures() & 8) == 8) {
//                    updateManager.disableFeature(1);
//                }
//                String channel = gameCenter.getProperty("Channel");
//                if (!TextUtils.isEmpty(channel)) {
//                    updateManager.setProperty("Channel", channel);
//                }
//            }
//            updateManager.onCreate();
//        }
//        Application app = getApplication();
//        if (app instanceof IMainApplication) {
//            IMainApplication mainApp = (IMainApplication) app;
//            NotificationManager manager = mainApp.getNotificationManager();
//            if (manager != null) {
//                manager.registerActivity(this);
//            }
//        }
//        this.mExtensionManager.loadExtensions();
    }

    @Override // com.transmension.mobile.NativeActivity, android.app.Activity
    public void onDestroy() {
//        Application app = getApplication();
//        if (app instanceof IMainApplication) {
//            IMainApplication mainApp = (IMainApplication) app;
//            NotificationManager manager = mainApp.getNotificationManager();
//            if (manager != null) {
//                manager.onDestroy();
//                manager.unregisterActivity(this);
//            }
//        }
        if (this.mVideoPlayer != null) {
            this.mVideoPlayer.onDestroy();
        }
        if (this.mSocialManager != null) {
            this.mSocialManager.onDestroy();
        }
        this.mExtensionManager.onDestroy();
        if (this.mInputManager != null) {
            this.mInputManager.onDestroy();
        }
        if (this.mGameCenter != null) {
            this.mGameCenter.onDestroy();
        }
        if (this.mShareManager != null) {
            this.mShareManager.onDestroy();
        }
        if (this.mNotificationManager != null) {
            this.mNotificationManager.onDestroy();
        }
        if (this.mAudioRecorder != null) {
            this.mAudioRecorder.onDestroy();
        }
        if (this.mUpdateManager != null) {
            this.mUpdateManager.onDestroy();
        }
        super.onDestroy();
    }

    @Override // com.transmension.mobile.NativeActivity, android.app.Activity
    public void onStart() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        NotificationManager manager = getAppNotificationManager();
//        if (manager != null) {
//            manager.onStart();
//        }
//        if (this.mVideoPlayer != null) {
//            this.mVideoPlayer.onStart();
//        }
//        if (this.mSocialManager != null) {
//            this.mSocialManager.onStart();
//        }
//        this.mExtensionManager.onStart();
//        if (this.mInputManager != null) {
//            this.mInputManager.onStart();
//        }
//        if (this.mGameCenter != null) {
//            this.mGameCenter.onStart();
//        }
//        if (this.mShareManager != null) {
//            this.mShareManager.onStart();
//        }
//        if (this.mNotificationManager != null && this.mNotificationManager != manager) {
//            this.mNotificationManager.onStart();
//        }
//        if (this.mAudioRecorder != null) {
//            this.mAudioRecorder.onStart();
//        }
//        if (this.mUpdateManager != null) {
//            this.mUpdateManager.onStart();
//        }
        super.onStart();
    }

    @Override // com.transmension.mobile.NativeActivity, android.app.Activity
    public void onStop() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        NotificationManager manager = getAppNotificationManager();
//        if (manager != null) {
//            manager.onStop();
//        }
//        if (this.mVideoPlayer != null) {
//            this.mVideoPlayer.onStop();
//        }
//        if (this.mSocialManager != null) {
//            this.mSocialManager.onStop();
//        }
//        this.mExtensionManager.onStop();
//        if (this.mInputManager != null) {
//            this.mInputManager.onStop();
//        }
//        if (this.mGameCenter != null) {
//            this.mGameCenter.onStop();
//        }
//        if (this.mShareManager != null) {
//            this.mShareManager.onStop();
//        }
//        if (this.mNotificationManager != null && this.mNotificationManager != manager) {
//            this.mNotificationManager.onStop();
//        }
//        if (this.mAudioRecorder != null) {
//            this.mAudioRecorder.onStop();
//        }
//        if (this.mUpdateManager != null) {
//            this.mUpdateManager.onStop();
//        }
        super.onStop();
    }

//    @Override // com.transmension.mobile.NativeActivity, android.app.Activity
//    public void onPause() {
//        super.onPause();
//        NotificationManager manager = getAppNotificationManager();
//        if (manager != null) {
//            manager.onPause();
//        }
//        if (this.mVideoPlayer != null) {
//            this.mVideoPlayer.onPause();
//        }
//        if (this.mSocialManager != null) {
//            this.mSocialManager.onPause();
//        }
//        this.mExtensionManager.onPause();
//        if (this.mInputManager != null) {
//            this.mInputManager.onPause();
//        }
//        if (this.mGameCenter != null) {
//            this.mGameCenter.onPause();
//        }
//        if (this.mShareManager != null) {
//            this.mShareManager.onPause();
//        }
//        if (this.mNotificationManager != null && this.mNotificationManager != manager) {
//            this.mNotificationManager.onPause();
//        }
//        if (this.mAudioRecorder != null) {
//            this.mAudioRecorder.onPause();
//        }
//        if (this.mAudioOutput != null) {
//            this.mAudioOutput.onPause();
//        }
//        if (this.mUpdateManager != null) {
//            this.mUpdateManager.onPause();
//        }
//    }

//    @Override // com.transmension.mobile.NativeActivity, android.app.Activity
//    public void onResume() {
//        super.onResume();
//        NotificationManager manager = getAppNotificationManager();
//        if (manager != null) {
//            manager.onResume();
//        }
//        if (this.mVideoPlayer != null) {
//            this.mVideoPlayer.onResume();
//        }
//        if (this.mSocialManager != null) {
//            this.mSocialManager.onResume();
//        }
//        this.mExtensionManager.onResume();
//        if (this.mInputManager != null) {
//            this.mInputManager.onResume();
//        }
//        if (this.mGameCenter != null) {
//            this.mGameCenter.onResume();
//        }
//        if (this.mShareManager != null) {
//            this.mShareManager.onResume();
//        }
//        if (this.mNotificationManager != null && this.mNotificationManager != manager) {
//            this.mNotificationManager.onResume();
//        }
//        if (this.mAudioRecorder != null) {
//            this.mAudioRecorder.onResume();
//        }
//        if (this.mAudioOutput != null) {
//            this.mAudioOutput.onResume();
//        }
//        if (this.mUpdateManager != null) {
//            this.mUpdateManager.onResume();
//        }
//    }

    public ExtensionManager getExtensionManager() {
        return this.mExtensionManager;
    }

    public String getGameCenterFactoryName() {
        return this.mGameCenterFactoryName;
    }

    public void setGameCenterFactoryName(String name) {
        this.mGameCenterFactoryName = name;
    }

    public GameCenter createGameCenter() {
        if (this.mGameCenter != null) {
            return this.mGameCenter;
        }
        try {
            Class<?> clazz = Class.forName(this.mGameCenterFactoryName);
            try {
                GameCenterFactory factory = (GameCenterFactory) clazz.newInstance();
                this.mGameCenter = factory.create(this);
                this.mGameCenter.setListener(new GameCenter.Listener() { // from class: com.transmension.mobile.MainActivity.1

                    @Override // com.transmension.mobile.GameCenter.Listener
                    public void onEvent(Activity activity, GameCenter gameCenter, int event, int status, String extras) {
                        NativeGameCenter.onEventNative(MainActivity.this.mNativeHandle, gameCenter, event, status, extras);
                    }
                });
                Log.i(TAG, "GameCenter: " + this.mGameCenter.getName());
                onGameCenterCreated();
                return this.mGameCenter;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return null;
            } catch (InstantiationException e2) {
                e2.printStackTrace();
                return null;
            }
        } catch (ClassNotFoundException e3) {
            e3.printStackTrace();
            return null;
        }
    }

    public boolean hasGameCenter() {
        return this.mGameCenter != null;
    }

    public GameCenter getGameCenter() {
        return this.mGameCenter;
    }

    public void onGameCenterCreated() {
    }

    public String getShareManagerFactoryName() {
        return this.mShareManagerFactoryName;
    }

    public void setShareManagerFactoryName(String name) {
        this.mShareManagerFactoryName = name;
    }

    public ShareManager createShareManager() {
        if (this.mShareManager != null) {
            return this.mShareManager;
        }
        try {
            Class<?> clazz = Class.forName(this.mShareManagerFactoryName);
            try {
                ShareManagerFactory factory = (ShareManagerFactory) clazz.newInstance();
                this.mShareManager = factory.create(this);
                Log.i(TAG, "ShareManager: " + this.mShareManager.getName());
                this.mShareManager.setListener(new ShareManager.Listener() { // from class: com.transmension.mobile.MainActivity.2

                    @Override // com.transmension.mobile.ShareManager.Listener
                    public void onShareComplete(Activity activity, ShareManager shareManager, int id) {
                        NativeShareManager.onCompleteNative(MainActivity.this.mNativeHandle, shareManager, id);
                    }

                    @Override // com.transmension.mobile.ShareManager.Listener
                    public void onShareError(Activity activity, ShareManager shareManager, int id) {
                        NativeShareManager.onErrorNative(MainActivity.this.mNativeHandle, shareManager, id);
                    }

                    @Override // com.transmension.mobile.ShareManager.Listener
                    public void onShareCancel(Activity activity, ShareManager shareManager, int id) {
                        NativeShareManager.onCancelNative(MainActivity.this.mNativeHandle, shareManager, id);
                    }
                });
                onShareManagerCreated();
                return this.mShareManager;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return null;
            } catch (InstantiationException e2) {
                e2.printStackTrace();
                return null;
            }
        } catch (ClassNotFoundException e3) {
            e3.printStackTrace();
            return null;
        }
    }

    public boolean hasShareManager() {
        return this.mShareManager != null;
    }

    public ShareManager getShareManager() {
        return this.mShareManager;
    }

    public void onShareManagerCreated() {
        ShareManager shareManager = getShareManager();
        int app_name = getApplicationInfo().labelRes;
        int icon = getApplicationInfo().icon;
        shareManager.setAppName(app_name);
        shareManager.setAppIcon(icon);
    }

    public String getNotificationManagerFactoryName() {
        return this.mNotificationManagerFactoryName;
    }

    public void setNotificationManagerFactoryName(String name) {
        this.mNotificationManagerFactoryName = name;
    }

    public NotificationManager createNotificationManager() {
        NotificationManager manager = getAppNotificationManager();
        if (manager == null || manager.getFeatures() == 0) {
            if (this.mNotificationManager != null) {
                return this.mNotificationManager;
            }
            try {
                Class<?> clazz = Class.forName(this.mNotificationManagerFactoryName);
                try {
                    NotificationManagerFactory factory = (NotificationManagerFactory) clazz.newInstance();
                    this.mNotificationManager = factory.create(this);
                    Log.i(TAG, "NotificationManager: " + this.mNotificationManager.getName());
                    onNotificationManagerCreated();
                    return this.mNotificationManager;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return null;
                } catch (InstantiationException e2) {
                    e2.printStackTrace();
                    return null;
                }
            } catch (ClassNotFoundException e3) {
                e3.printStackTrace();
                return null;
            }
        }
        return manager;
    }

    public boolean hasNotificationManager() {
        NotificationManager manager = getAppNotificationManager();
        return (manager != null && manager.getFeatures() != 0) || this.mNotificationManager != null;
    }

    public NotificationManager getNotificationManager() {
        NotificationManager manager = getAppNotificationManager();
        return (manager == null || manager.getFeatures() == 0) ? this.mNotificationManager : manager;
    }

    public void onNotificationManagerCreated() {
    }

    public String getAudioRecorderFactoryName() {
        return this.mAudioRecorderFactoryName;
    }

    public void setAudioRecorderFactoryName(String name) {
        this.mAudioRecorderFactoryName = name;
    }

    public AudioRecorder createAudioRecorder() {
        if (this.mAudioRecorder != null) {
            return this.mAudioRecorder;
        }
        try {
            Class<?> clazz = Class.forName(this.mAudioRecorderFactoryName);
            try {
                AudioRecorderFactory factory = (AudioRecorderFactory) clazz.newInstance();
                this.mAudioRecorder = factory.create(this);
                if (this.mAudioRecorder != null) {
                    Log.i(TAG, "AudioRecorder: " + this.mAudioRecorder.getName());
                    onNotificationManagerCreated();
                }
                return this.mAudioRecorder;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return null;
            } catch (InstantiationException e2) {
                e2.printStackTrace();
                return null;
            }
        } catch (ClassNotFoundException e3) {
            e3.printStackTrace();
            return null;
        }
    }

    public boolean hasAudioRecorder() {
        return this.mAudioRecorder != null;
    }

    public AudioRecorder getAudioRecorder() {
        return this.mAudioRecorder;
    }

    public void onAudioRecorderCreated() {
    }

    public AudioOutput getAudioOutput() {
        return this.mAudioOutput;
    }

    public String getInputManagerFactoryName() {
        return this.mInputManagerFactoryName;
    }

    public void setInputManagerFactoryName(String name) {
        this.mInputManagerFactoryName = name;
    }

    public InputManager createInputManager() {
        if (this.mInputManager != null) {
            return this.mInputManager;
        }
        try {
            Class<?> clazz = Class.forName(this.mInputManagerFactoryName);
            try {
                InputManagerFactory factory = (InputManagerFactory) clazz.newInstance();
                this.mInputManager = factory.create(this);
                Log.i(TAG, "InputManager: " + this.mInputManager.getName());
                onInputManagerCreated();
                return this.mInputManager;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return null;
            } catch (InstantiationException e2) {
                e2.printStackTrace();
                return null;
            }
        } catch (ClassNotFoundException e3) {
            e3.printStackTrace();
            return null;
        }
    }

    public boolean hasInputManager() {
        return this.mInputManager != null;
    }

    public InputManager getInputManager() {
        return this.mInputManager;
    }

    public void onInputManagerCreated() {
    }

    public String getImagePickerFactoryName() {
        return this.mImagePickerFactoryName;
    }

    public void setImagePickerFactoryName(String name) {
        this.mImagePickerFactoryName = name;
    }

    public ImagePicker createImagePicker() {
        if (this.mImagePicker != null) {
            return this.mImagePicker;
        }
        try {
            Class<?> clazz = Class.forName(this.mImagePickerFactoryName);
            try {
                ImagePickerFactory factory = (ImagePickerFactory) clazz.newInstance();
                this.mImagePicker = factory.create(this);
                Log.i(TAG, "ImagePicker: " + this.mImagePicker.getName());
                this.mImagePicker.setListener(new ImagePicker.Listener() { // from class: com.transmension.mobile.MainActivity.3

                    @Override // com.transmension.mobile.ImagePicker.Listener
                    public void onPickComplete(Activity activity, ImagePicker picker, String path) {
                        NativeImagePicker.onCompleteNative(MainActivity.this.mNativeHandle, picker, path);
                    }

                    @Override // com.transmension.mobile.ImagePicker.Listener
                    public void onPickError(Activity activity, ImagePicker picker) {
                        NativeImagePicker.onErrorNative(MainActivity.this.mNativeHandle, picker);
                    }

                    @Override // com.transmension.mobile.ImagePicker.Listener
                    public void onPickCancel(Activity activity, ImagePicker picker) {
                        NativeImagePicker.onCancelNative(MainActivity.this.mNativeHandle, picker);
                    }
                });
                onImagePickerCreated();
                return this.mImagePicker;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return null;
            } catch (InstantiationException e2) {
                e2.printStackTrace();
                return null;
            }
        } catch (ClassNotFoundException e3) {
            e3.printStackTrace();
            return null;
        }
    }

    public boolean hasImagePicker() {
        return this.mImagePicker != null;
    }

    public ImagePicker getImagePicker() {
        return this.mImagePicker;
    }

    public void onImagePickerCreated() {
    }

    public String getUpdateManagerFactoryName() {
        return this.mUpdateManagerFactoryName;
    }

    public void setUpdateManagerFactoryName(String name) {
        this.mUpdateManagerFactoryName = name;
    }

    public UpdateManager createUpdateManager() {
        if (this.mUpdateManager != null) {
            return this.mUpdateManager;
        }
        try {
            Class<?> clazz = Class.forName(this.mUpdateManagerFactoryName);
            try {
                UpdateManagerFactory factory = (UpdateManagerFactory) clazz.newInstance();
                this.mUpdateManager = factory.create(this);
                Log.i(TAG, "UpdateManager: " + this.mUpdateManager.getName());
                onUpdateManagerCreated();
                return this.mUpdateManager;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return null;
            } catch (InstantiationException e2) {
                e2.printStackTrace();
                return null;
            }
        } catch (ClassNotFoundException e3) {
            e3.printStackTrace();
            return null;
        }
    }

    public boolean hasUpdateManager() {
        return this.mUpdateManager != null;
    }

    public UpdateManager getUpdateManager() {
        return this.mUpdateManager;
    }

    public void onUpdateManagerCreated() {
    }

    public String getTextInputManagerFactoryName() {
        return this.mTextInputManagerFactoryName;
    }

    public void setTextInputManagerFactoryName(String name) {
        this.mTextInputManagerFactoryName = name;
    }

    @Override // com.transmension.mobile.NativeActivity
    public TextInputManager createTextInputManager(NativeView view) {
        try {
            Class<?> clazz = Class.forName(this.mTextInputManagerFactoryName);
            try {
                TextInputManagerFactory factory = (TextInputManagerFactory) clazz.newInstance();
                return factory.create(this, view);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return null;
            } catch (InstantiationException e2) {
                e2.printStackTrace();
                return null;
            }
        } catch (ClassNotFoundException e3) {
            e3.printStackTrace();
            return null;
        }
    }

    public String getSocialManagerFactoryName() {
        return this.mSocialManagerFactoryName;
    }

    public void setSocialManagerFactoryName(String name) {
        this.mSocialManagerFactoryName = name;
    }

    public SocialManager createSocialManager() {
        if (this.mSocialManager != null) {
            return this.mSocialManager;
        }
        try {
            Class<?> clazz = Class.forName(this.mSocialManagerFactoryName);
            try {
                SocialManagerFactory factory = (SocialManagerFactory) clazz.newInstance();
                this.mSocialManager = factory.create(this);
                Log.i(TAG, "SocialManager: " + this.mSocialManager.getName());
                this.mSocialManager.setListener(new SocialManager.Listener() { // from class: com.transmension.mobile.MainActivity.4

                    @Override // com.transmension.mobile.SocialManager.Listener
                    public void onEvent(Activity activity, SocialManager extension, String event, int status, String extras) {
                        NativeSocialManager.onEventNative(MainActivity.this.mNativeHandle, MainActivity.this.mSocialManager, event, status, extras);
                    }
                });
                onSocialManagerCreated();
                return this.mSocialManager;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return null;
            } catch (InstantiationException e2) {
                e2.printStackTrace();
                return null;
            }
        } catch (ClassNotFoundException e3) {
            e3.printStackTrace();
            return null;
        }
    }

    public boolean hasSocialManager() {
        return this.mSocialManager != null;
    }

    public SocialManager getSocialManager() {
        return this.mSocialManager;
    }

    public void onSocialManagerCreated() {
    }

    public String getVideoPlayerFactoryName() {
        return this.mVideoPlayerFactoryName;
    }

    public void setVideoPlayerFactoryName(String name) {
        this.mVideoPlayerFactoryName = name;
    }

    public VideoPlayer createVideoPlayer() {
        if (this.mVideoPlayer != null) {
            return this.mVideoPlayer;
        }
        try {
            Class<?> clazz = Class.forName(this.mVideoPlayerFactoryName);
            try {
                VideoPlayerFactory factory = (VideoPlayerFactory) clazz.newInstance();
                this.mVideoPlayer = factory.create(this);
                Log.i(TAG, "VideoPlayer: " + this.mVideoPlayer.getName());
                this.mVideoPlayer.setListener(new VideoPlayer.Listener() { // from class: com.transmension.mobile.MainActivity.5

                    @Override // com.transmension.mobile.VideoPlayer.Listener
                    public void onEvent(Activity activity, VideoPlayer player, int event, int status, String extras) {
                        NativeVideoPlayer.onEventNative(MainActivity.this.mNativeHandle, MainActivity.this.mVideoPlayer, event, status, extras);
                    }
                });
                onVideoPlayerCreated();
                return this.mVideoPlayer;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return null;
            } catch (InstantiationException e2) {
                e2.printStackTrace();
                return null;
            }
        } catch (ClassNotFoundException e3) {
            e3.printStackTrace();
            return null;
        }
    }

    public boolean hasVideoPlayer() {
        return this.mVideoPlayer != null;
    }

    public VideoPlayer getVideoPlayer() {
        return this.mVideoPlayer;
    }

    public void onVideoPlayerCreated() {
    }

    @Override // com.transmension.mobile.NativeActivity
    public void onNativeKeyEvent(KeyEvent event) {
        if (this.mInputManager == null) {
            createInputManager();
        }
        if (this.mInputManager == null) {
            super.onNativeKeyEvent(event);
        } else {
            this.mInputManager.onKeyEvent(event);
        }
    }

    @Override // com.transmension.mobile.NativeActivity
    public void onNativeMotionEvent(MotionEvent event) {
        if (this.mInputManager == null) {
            createInputManager();
        }
        if (this.mInputManager == null) {
            super.onNativeMotionEvent(event);
        } else {
            this.mInputManager.onMotionEvent(event);
        }
    }

    public NotificationManager getAppNotificationManager() {
        Application app = getApplication();
        if (app instanceof IMainApplication) {
            IMainApplication mainApp = (IMainApplication) app;
            return mainApp.getNotificationManager();
        }
        return null;
    }
}