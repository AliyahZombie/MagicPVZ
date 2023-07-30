package com.transmension.mobile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.FileObserver;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.util.Xml;
import android.view.Gravity;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.android.support.CkHomuraMenu;
import com.trans.pvz.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

public class EnhanceActivity extends MainActivity {


    //这几个按键事件是玩游戏需要的按键事件。用NativeInputManager.onKeyInputEventNative(mNativeHandle, null, keyEvent)就可以发送按键事件给游戏了。
    private final InputManager.KeyInputEvent enterEventDown = InputManager.KeyInputEvent.translate(new KeyEvent(0, 0, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BUTTON_A, 0, 0, 0, 0, 0, InputDevice.SOURCE_CLASS_BUTTON));
    private final InputManager.KeyInputEvent enterEventUp = InputManager.KeyInputEvent.translate(new KeyEvent(0, 0, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BUTTON_A, 0, 0, 0, 0, 0, InputDevice.SOURCE_CLASS_BUTTON));
    private final InputManager.KeyInputEvent backEventDown = InputManager.KeyInputEvent.translate(new KeyEvent(0, 0, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BUTTON_B, 0, 0, 0, 0, 0, InputDevice.SOURCE_CLASS_BUTTON));
    private final InputManager.KeyInputEvent backEventUp = InputManager.KeyInputEvent.translate(new KeyEvent(0, 0, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BUTTON_B, 0, 0, 0, 0, 0, InputDevice.SOURCE_CLASS_BUTTON));
    private final InputManager.KeyInputEvent shovelEventDown = InputManager.KeyInputEvent.translate(new KeyEvent(0, 0, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_1, 0, 0, 0, 0, 0, InputDevice.SOURCE_CLASS_BUTTON));
    private final InputManager.KeyInputEvent shovelEventUp = InputManager.KeyInputEvent.translate(new KeyEvent(0, 0, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_1, 0, 0, 0, 0, 0, InputDevice.SOURCE_CLASS_BUTTON));
    private final InputManager.KeyInputEvent hammerEventDown = InputManager.KeyInputEvent.translate(new KeyEvent(0, 0, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_2, 0, 0, 0, 0, 0, InputDevice.SOURCE_CLASS_BUTTON));
    private final InputManager.KeyInputEvent hammerEventUp = InputManager.KeyInputEvent.translate(new KeyEvent(0, 0, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_2, 0, 0, 0, 0, 0, InputDevice.SOURCE_CLASS_BUTTON));


    //四个方向键的按键事件（注意，游戏并不会识别方向键的抬起事件，只会识别方向键的按下事件。）
    private final InputManager.KeyInputEvent upEventDown = InputManager.KeyInputEvent.translate(new KeyEvent(0, 0, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_UP, 0, 0, 0, 0, 0, InputDevice.SOURCE_CLASS_BUTTON));
    private final InputManager.KeyInputEvent downEventDown = InputManager.KeyInputEvent.translate(new KeyEvent(0, 0, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_DOWN, 0, 0, 0, 0, 0, InputDevice.SOURCE_CLASS_BUTTON));
    private final InputManager.KeyInputEvent leftEventDown = InputManager.KeyInputEvent.translate(new KeyEvent(0, 0, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_LEFT, 0, 0, 0, 0, 0, InputDevice.SOURCE_CLASS_BUTTON));
    private final InputManager.KeyInputEvent rightEventDown = InputManager.KeyInputEvent.translate(new KeyEvent(0, 0, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_RIGHT, 0, 0, 0, 0, 0, InputDevice.SOURCE_CLASS_BUTTON));

    //LB键和RB键，用于商店翻页。
    private final InputManager.KeyInputEvent lbEventDown = InputManager.KeyInputEvent.translate(new KeyEvent(0, 0, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BUTTON_L1, 0, 0, 0, 0, 0, InputDevice.SOURCE_CLASS_BUTTON));
    private final InputManager.KeyInputEvent rbEventDown = InputManager.KeyInputEvent.translate(new KeyEvent(0, 0, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BUTTON_R1, 0, 0, 0, 0, 0, InputDevice.SOURCE_CLASS_BUTTON));


    //这些东西都是专门用在锤子键连点上的，长按锤子键即可触发每秒20次的锤子键连点。僵尸表示：NND，开挂是吧，举报了
    private boolean isHammerButtonPressed = false, isHammerHandlerRunning = false;
    private final Handler hammerHandler = new Handler();
    private final Runnable hammerRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isHammerButtonPressed) return;
            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, hammerEventDown);
            hammerHandler.postDelayed(this, 50);
        }
    };


    //fileObserver用于观测存放文件夹变动，并在存档更新时保留一份备份
    private boolean isFileObserverLaunched = false;
    private FileObserver fileObserver;

    //用于fileObserver备份存档的一些函数
    public static void copyDir(File srcDir, File destDir) throws IOException {
        if (srcDir.isDirectory()) {
            if (!destDir.exists()) {
                destDir.mkdirs();
            }

            String[] children = srcDir.list();
            if (children == null) return;
            for (String child : children) {
                File srcFile = new File(srcDir, child);
                File destFile = new File(destDir, child);
                copyDir(srcFile, destFile);
            }
        } else {
            FileInputStream inputStream = new FileInputStream(srcDir);
            FileOutputStream outputStream = new FileOutputStream(destDir);

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            outputStream.close();
        }
    }

    public static void deleteRecursive(File file) {
        if (file.isFile()) {
            file.delete();
        } else {
            // 如果是文件夹，递归删除
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File subFile : files) {
                    deleteRecursive(subFile);
                }
            }
            file.delete();
        }
    }

    public void checkAndDeleteOldBackups() {
        File file = getExternalFilesDir(null);
        if (file.exists() && file.isDirectory()) {

            File[] files = file.listFiles((dir1, name) -> {
                // 判断只要是数字开头的文件夹就符合要求
                return new File(dir1, name).isDirectory() && Character.isDigit(name.charAt(0));
            });
            if (files != null && files.length > 0) {
                // 对文件夹按照修改时间进行排序
                Arrays.sort(files, (f1, f2) -> Long.compare(f2.lastModified(), f1.lastModified()));
                // 如果文件夹数量超过20个，进行删除操作
                if (files.length > 20) {
                    // 遍历排序后的文件夹列表，删除20个之后的文件夹
                    for (int i = 20; i < files.length; i++) {
                        File oldFolder = files[i];
                        // 删除文件夹
                        deleteRecursive(oldFolder);
                    }
                }
            }
        }
    }


    private OrientationEventListener mOrientationListener;
    private boolean heavyWeaponAccel = false, useSmoothCursor = true;


    private ImageView enterButton, backButton, shovelButton, hammerButton, dpadButton, stopButton;
    private boolean isVisible;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //如果是初次启动，则载入assets文件夹中的data.xml
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences sharedPreferences = getSharedPreferences("data", 0);
        if (preferences.getBoolean("firstLaunch", true)) {
            try {
                InputStream inputStream = getAssets().open("data.xml");
                if (inputStream != null) {
                    XmlPullParser parser = Xml.newPullParser();
                    parser.setInput(inputStream, "utf-8");

                    int eventType = parser.getEventType();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_TAG) {
                            if ("boolean".equals(parser.getName())) {
                                String name = parser.getAttributeValue(null, "name");
                                boolean value = Boolean.parseBoolean(parser.getAttributeValue(null, "value"));
                                editor.putBoolean(name, value);
                            }
                        }
                        eventType = parser.next();
                    }
                    editor.apply();
                    inputStream.close();
                }
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }
            preferences.edit().putBoolean("firstLaunch", false).apply();
        }


        //这一段用于启动fileObserver，备份玩家存档
        isFileObserverLaunched = sharedPreferences.getBoolean("autoBackUp", true);
        if (isFileObserverLaunched) {
            checkAndDeleteOldBackups();
            File userdata = new File(getExternalFilesDir(null), "userdata");
            fileObserver = new FileObserver(userdata.getAbsolutePath(), FileObserver.CLOSE_WRITE) {
                @Override
                public void onEvent(int i, String s) {
                    try {
                        Calendar calendar = Calendar.getInstance();
                        File destDir = new File(getExternalFilesDir(null), String.format(Locale.getDefault(), "%d月%02d日%02d:%02d_userdata备份", calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));
                        copyDir(userdata, destDir);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            fileObserver.startWatching();
        }


        isVisible = sharedPreferences.getBoolean("isVisible", false);

        //为游戏添加ENTER按钮
        enterButton = new ImageView(this);
        enterButton.setFocusable(false);
        enterButton.setImageDrawable(getResources().getDrawable(R.drawable.button_a));
        enterButton.setAlpha(sharedPreferences.getInt("enterTran", 90) / 100f);
        if (!sharedPreferences.getBoolean("enterKeep", false))
            enterButton.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
        enterButton.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    animateScale(view, true);
                    NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventDown);
                    break;
                case MotionEvent.ACTION_UP:
                    animateScale(view, false);
                    NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventUp);
                    break;
            }
            return true;
        });
        int enterSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, sharedPreferences.getInt("enterSize", 100), getResources().getDisplayMetrics());
        FrameLayout.LayoutParams enterParams = new FrameLayout.LayoutParams(enterSize, enterSize, Gravity.CENTER);
        enterParams.leftMargin = sharedPreferences.getInt("enterX", 850);
        enterParams.topMargin = sharedPreferences.getInt("enterY", 350);
        enterButton.setLayoutParams(enterParams);


        //为游戏添加BACK按钮
        backButton = new ImageView(this);
        backButton.setFocusable(false);
        backButton.setImageDrawable(getResources().getDrawable(R.drawable.button_b));
        backButton.setAlpha(sharedPreferences.getInt("backTran", 90) / 100f);
        if (!sharedPreferences.getBoolean("backKeep", false))
            backButton.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
        backButton.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    animateScale(view, true);
                    NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventDown);
                    break;
                case MotionEvent.ACTION_UP:
                    animateScale(view, false);
                    NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventUp);
                    break;
            }
            return true;
        });
        int backSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, sharedPreferences.getInt("backSize", 75), getResources().getDisplayMetrics());
        FrameLayout.LayoutParams backParams = new FrameLayout.LayoutParams(backSize, backSize, Gravity.CENTER);
        backParams.leftMargin = sharedPreferences.getInt("backX", 500);
        backParams.topMargin = sharedPreferences.getInt("backY", 370);
        backButton.setLayoutParams(backParams);


        //为游戏添加方向按钮
        dpadButton = new CustomView(this);
        dpadButton.setFocusable(false);
        dpadButton.setImageDrawable(getResources().getDrawable(R.drawable.dpad));
        dpadButton.setAlpha(sharedPreferences.getInt("dpadTran", 90) / 100f);
        if (!sharedPreferences.getBoolean("dpadKeep", false))
            dpadButton.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
        int dpadSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, sharedPreferences.getInt("dpadSize", 200), getResources().getDisplayMetrics());
        FrameLayout.LayoutParams dpadParams = new FrameLayout.LayoutParams(dpadSize, dpadSize, Gravity.CENTER);
        dpadParams.leftMargin = sharedPreferences.getInt("dpadX", -680);
        dpadParams.topMargin = sharedPreferences.getInt("dpadY", 230);
        dpadButton.setLayoutParams(dpadParams);


        //为游戏添加SHOVEL按钮
        shovelButton = new ImageView(this);
        shovelButton.setFocusable(false);
        shovelButton.setImageDrawable(getResources().getDrawable(R.drawable.button_x));
        shovelButton.setAlpha(sharedPreferences.getInt("shovelTran", 90) / 100f);
        if (!sharedPreferences.getBoolean("shovelKeep", false))
            shovelButton.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
        shovelButton.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    animateScale(view, true);
                    NativeInputManager.onKeyInputEventNative(mNativeHandle, null, shovelEventDown);
                    break;
                case MotionEvent.ACTION_UP:
                    animateScale(view, false);
                    NativeInputManager.onKeyInputEventNative(mNativeHandle, null, shovelEventUp);
                    break;
            }
            return true;
        });
        int shovelSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, sharedPreferences.getInt("shovelSize", 75), getResources().getDisplayMetrics());
        FrameLayout.LayoutParams shovelParams = new FrameLayout.LayoutParams(shovelSize, shovelSize, Gravity.CENTER);
        shovelParams.leftMargin = sharedPreferences.getInt("shovelX", 580);
        shovelParams.topMargin = sharedPreferences.getInt("shovelY", 70);
        shovelButton.setLayoutParams(shovelParams);


        //为游戏添加HAMMER按钮(HAMMER长按可以触发连点，所以其点击事件和其他按钮不一样)
        hammerButton = new ImageView(this);
        hammerButton.setFocusable(false);
        hammerButton.setImageDrawable(getResources().getDrawable(R.drawable.button_y));
        hammerButton.setAlpha(sharedPreferences.getInt("hammerTran", 90) / 100f);
        if (!sharedPreferences.getBoolean("hammerKeep", false))
            hammerButton.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
        hammerButton.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    animateScale(view, true);
                    isHammerButtonPressed = true;
                    NativeInputManager.onKeyInputEventNative(mNativeHandle, null, hammerEventDown);
                    if (!isHammerHandlerRunning) {
                        isHammerHandlerRunning = true;
                        hammerHandler.postDelayed(hammerRunnable, 400);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    animateScale(view, false);
                    NativeInputManager.onKeyInputEventNative(mNativeHandle, null, hammerEventUp);
                    isHammerButtonPressed = false;
                    isHammerHandlerRunning = false;
                    hammerHandler.removeCallbacksAndMessages(null);
                    break;
            }
            return true;
        });
        int hammerSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, sharedPreferences.getInt("hammerSize", 75), getResources().getDisplayMetrics());
        FrameLayout.LayoutParams hammerParams = new FrameLayout.LayoutParams(hammerSize, hammerSize, Gravity.CENTER);
        hammerParams.leftMargin = sharedPreferences.getInt("hammerX", 900);
        hammerParams.topMargin = sharedPreferences.getInt("hammerY", 20);
        hammerButton.setLayoutParams(hammerParams);

        //为游戏添加STOP按钮
        stopButton = new ImageView(this);
        stopButton.setFocusable(false);
        stopButton.setImageDrawable(getResources().getDrawable(R.drawable.button_stop));
        stopButton.setAlpha(sharedPreferences.getInt("stopTran", 90) / 100f);
        if (!sharedPreferences.getBoolean("stopKeep", false))
            stopButton.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
        stopButton.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    animateScale(view, true);
                    NativeApp.onPauseNative(mNativeHandle);
                    break;
                case MotionEvent.ACTION_UP:
                    animateScale(view, false);
                    NativeApp.onResumeNative(mNativeHandle);
                    break;
            }
            return true;
        });
        int stopSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, sharedPreferences.getInt("stopSize", 75), getResources().getDisplayMetrics());
        FrameLayout.LayoutParams stopParams = new FrameLayout.LayoutParams(stopSize, stopSize, Gravity.CENTER);
        stopParams.leftMargin = sharedPreferences.getInt("stopX", 600);
        stopParams.topMargin = sharedPreferences.getInt("stopY", -270);
        stopButton.setLayoutParams(stopParams);


        container.addView(enterButton);
        container.addView(backButton);
        container.addView(dpadButton);
        container.addView(shovelButton);
        container.addView(hammerButton);
        container.addView(stopButton);


        //测量游戏边界,会在游戏窗口初次加载和每次窗口大小变动时触发测量。
        mNativeView.addOnLayoutChangeListener((view, i, i1, i2, i3, i4, i5, i6, i7) -> refreshNativeViewBorders(view));


        //重型武器重力感应用的东西
        mOrientationListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_GAME) {
            @Override
            public void onOrientationChanged(int i) {
                if (i == OrientationEventListener.ORIENTATION_UNKNOWN) return;
                nativeSetHeavyWeaponAngle(i > 180 ? i - 180 : i);
            }
        };


    }

    //用于按下、抬起按钮时的缩放动画
    public void animateScale(View view, boolean isDown) {
        ScaleAnimation a2 = isDown ? new ScaleAnimation(1f, 0.65f, 1f, 0.65f, view.getWidth() / 2f, view.getHeight() / 2f) : new ScaleAnimation(0.65f, 1f, 0.65f, 1f, view.getWidth() / 2f, view.getHeight() / 2f);
        a2.setDuration(100);
        a2.setFillAfter(true);
        if (!isDown) a2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(a2);
    }


    //方向键单独自定义一个类，这样方便我们自定义它
    public class CustomView extends ImageView implements View.OnTouchListener {

        private final Paint paint;      // 用于绘制的画笔
        private int cellWidth;          // 单元格宽度
        private int cellHeight;         // 单元格高度
        private int selectedRow = 1;    // 当前选中的行
        private int selectedCol = 1;    // 当前选中的列
        private final long longPressInterval; //长按方向键后触发连点的时间间隔


        //这些东西用于连点功能
        private boolean rowPressed = false, colPressed = false, isHandlerRunning = false;
        private final Handler dpadHandler = new Handler();
        private final Runnable dpadRunnable = new Runnable() {
            @Override
            public void run() {
                if (!rowPressed && !colPressed) return;
                if (rowPressed)
                    NativeInputManager.onKeyInputEventNative(mNativeHandle, null, selectedRow == 0 ? upEventDown : downEventDown);
                if (colPressed)
                    NativeInputManager.onKeyInputEventNative(mNativeHandle, null, selectedCol == 0 ? leftEventDown : rightEventDown);
                dpadHandler.postDelayed(this, longPressInterval);
            }
        };


        public CustomView(Context context) {
            super(context);
            longPressInterval = 1000 / context.getSharedPreferences("data", 0).getInt("longPress", 8);
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(0x80292929);
            setOnTouchListener(this);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldW, int oldH) {
            super.onSizeChanged(w, h, oldW, oldH);
            cellWidth = w / 3;
            cellHeight = h / 3;
        }


        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            // 绘制当前按下的单元格
            if (selectedRow == 1 && selectedCol == 1) return;
            int left = selectedCol * cellWidth;
            int top = selectedRow * cellHeight;
            int right = left + cellWidth;
            int bottom = top + cellHeight;

            canvas.drawCircle((left + right) / 2f, (top + bottom) / 2f, (right - left) / 3f, paint);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getActionMasked();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    int row = (int) (event.getY() / cellHeight);
                    int col = (int) (event.getX() / cellWidth);
                    if (row == selectedRow && col == selectedCol) return true;
                    if (row != selectedRow) {
                        selectedRow = row;
                        if (selectedRow != 1) {
                            rowPressed = true;
                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, selectedRow == 0 ? upEventDown : downEventDown);
                        }
                    }
                    if (col != selectedCol) {
                        selectedCol = col;
                        if (selectedCol != 1) {
                            colPressed = true;
                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, selectedCol == 0 ? leftEventDown : rightEventDown);
                        }
                    }

                    if ((selectedCol != 1 || selectedRow != 1) && !isHandlerRunning) {
                        isHandlerRunning = true;
                        dpadHandler.postDelayed(dpadRunnable, 400);
                    }
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    rowPressed = false;
                    colPressed = false;
                    selectedRow = 1;
                    selectedCol = 1;
                    dpadHandler.removeCallbacksAndMessages(null);
                    isHandlerRunning = false;
                    invalidate();
                    break;
            }
            return true;
        }
    }

    //这个枚举类型用于指示当前游戏内是什么界面
    enum currentBoardType {
        SEED_CHOOSE,//选择种子界面
        IMITATER_CHOOSE,//模仿者选择种子界面
        GRAVE_BOARD,//草坪（白天、黑夜）
        POOL_BOARD,//泳池（泳池、雾夜）
        ROOF_BOARD,//屋顶（屋顶、月夜）
        ZEN_GARDEN,//花园
        MUSHROOM_GARDEN,//蘑菇园
        QUARIUM_GARDEN,//水族馆花园
        WISDOM_TREE,//智慧树
        AWARD_SCREEN,//奖励物界面（也就是关卡结束后展示"下一关"的界面）
        VASE_BREAKER,//砸罐子
        HEAVY_WEAPON,//重型武器
        BEGHOULED_1,//宝石迷阵
        BEGHOULED_2,//宝石迷阵旋风
        CHALLENGE_SCREEN,//小游戏列表
        WHACK_ZOMBIE,//锤僵尸
        STORE_SCREEN,//戴夫商店
        RAINING_SEEDS,//种子雨
        SLOT_MACHINE,//老虎机
        ZOMBI_QUARIUM,//僵尸水族馆
        GRAVE_BOARD_SMOOTH,//草坪平滑光标模式
        POOL_BOARD_SMOOTH,//泳池平滑光标模式
        ROOF_BOARD_SMOOTH,//屋顶平滑光标模式
        RAINING_SEEDS_SMOOTH,//种子雨平滑光标
        VASE_BREAKER_SMOOTH,//砸罐子平滑光标
        SLOT_MACHINE_SMOOTH,//老虎机平滑光标
        VS_SETUP,//对战模式的手柄选择、种子选择界面
        VS_RESULT,//对战结算界面
        VS_BATTLE,//对战模式
        COOP_BATTLE,//结盟模式
        DO_NOTHING//什么都不做
    }

    private currentBoardType boardType = currentBoardType.DO_NOTHING;

    static native boolean nativeIsSeedChoosing();//返回当前是否为选择种子界面

    static native boolean nativeIsMainMenu();//返回当前是否为主界面

    static native int nativeNowScene();//返回当前的场景编号(白天，黑夜，泳池，雾夜，屋顶，月夜，花园，蘑菇园，水族馆，智慧树)

    static native int nativeGameState();//返回当前是是否正在SeedBank中选择植物种子。其可能的返回值为：    1：手上没拿种子，也不在SeedBank里选种子     6：正在SeedBank里选种子(我是僵尸模式也会生效哦)     7：已经选择完毕，手上正拿着种子

    static native void nativeSetGameState(int state);//设置当前是是否正在SeedBank中选择植物种子。

    static native boolean nativeIsDaveTalking();//返回当前是否存在戴夫和你交谈

    static native boolean nativeIsStartButtonEnabled();//返回当前选种子界面是否已经选满

    static native boolean nativeIsDialogExist();//返回当前是否存在对话框

    static native boolean nativeIsImitaterChooser();//返回当前是否为模仿者的选择目标模仿植物界面

    static native boolean nativeIsVaseBreakerMode();//返回当前是否为砸罐子模式

    static native void nativeRequestValidCobCanonCheck(int x, int y);//要求检查(x,y)处的植物是否为一个充能完毕的加农炮。如果是，此函数还会选取该加农炮。

    static native boolean nativeIsCobCannonSelected();//返回当前是否手持一个加农炮靶标。

    static native void nativeSetSeedBankPosition(int x);//要求将seedBank的光标定位到第x个卡片处。

    static native int nativeGetSeekBankPosition(); //获取当前seedBank的光标在第几个卡片处。

    static native boolean nativeIsAwardScreen();//返回当前是否在关卡结束的奖励物界面。

    static native int nativeMaxPlants();//获取当前seedBank中的植物卡的总数量。

    static native boolean nativeIsSeedChooserHas7Rows();//返回当前的SeedChooser界面是否有7行。

    static native boolean nativeIsShovelEnabled();//返回当前关卡是否显示铲子按钮。

    static native void nativeEnableConveyorBeltFast();//要求传送带秒传送。

    static native void nativeEnableManualCollect();//要求开启手动收集。

    static native void nativeDisableShop();//要求关闭道具栏。

    static native boolean nativeIsSurvivalRepick();//返回当前是否为生存模式打完第一面旗帜之后的带有“查看草坪”按钮的选卡界面。

    static native boolean nativeIsChallengeScreen();//返回当前是否为小游戏列表界面。

    static native void nativeSetChallengeScreenScrollTarget(int target, int offset);//设定小游戏列表界面的位置。target代表滚动了几格，为正就是向下滚动，反之向上滚动；offset是高亮选中四个小游戏中的第几个。

    static native boolean nativeIsStoreEnabled();//返回当前玩家是否解锁了商店。

    static native void nativeSetHeavyWeaponAngle(int i);//设定重型武器发射角度，i的值可以为0~180

    static native boolean nativeIsAlmanacEnabled();//返回当前玩家是否解锁了图鉴。

    static native boolean nativeIsImitaterEnabled();//返回当前是否拥有模仿者植物。

    static native boolean nativeIsWhackAZombieLevel();//返回当前是否为锤僵尸模式。

    static native int nativeGetCursorPositionX();//获取当前Board光标的X坐标。

    static native int nativeGetCursorPositionY();//获取当前Board光标的Y坐标。

    static native boolean nativeIsStoreScreen();//返回当前是否为戴夫商店界面。

    static native void nativeSetStoreSelectedSlot(int slot);//设定商店中选中的item的位置。

    static native int nativeGetSelectedStoreItem();//返回商店选中物品的代号。

    static native int nativeCursorType();//返回光标中的Item类型。

    static native void nativeSetZenGardenTool(int position);//设定花园工具栏的选中工具的位置

    static native void nativeSetBoardPositionByXY(float x, float y);//设置当前Board中的光标位置。用于平滑移动光标

    static native boolean nativeHasGoldenWaterCan();//返回游戏是否有金水壶。

    static native int nativeGameIndex();//返回小游戏索引。

    static native boolean nativeCanInteractWithStoreButtons();//返回是否可以和商店按钮交互（可以简单理解为商店界面是否有戴夫和你交谈）。

    static native void nativePickUpShovel(boolean pickUp);//设置当前1P是否要拿起铲子。

    static native void nativeShovelDown();//在1P当前光标位置使用铲子。

    static native void nativeSetSeedBankPosition2P(int x);//要求将2P的seedBank的光标定位到第x个卡片处。

    static native int nativeGetSeekBankPosition2P(); //获取当前2P的seedBank的光标在第几个卡片处。

    static native void nativeSetBoardPositionByXY2P(float x, float y);//设置当前Board中的2P光标位置。用于平滑移动2P光标

    static native void nativeSetGameState2P(int state);//设置当前2P是否正在SeedBank中选择植物种子。

    static native int nativeGameState2P();//返回2P是否正在seedBank中选择植物种子

    static native void native1PButtonDown(int code);//1P按下按键。用于对战和结盟中操作1P种植

    static native void native2PButtonDown(int code);//2P按下按键。用于对战和结盟中操作2P种植

    static native void native1PSelectPlantSeed(int x, int y);//1P在自定制战场中选择指定位置的植物种子

    static native void native2PSelectZombieSeed(int x, int y);//2P在自定制战场中选择指定位置的僵尸种子

    static native int nativeVSSetupState();//返回是否正在VSSetupMenu界面,以及在什么状态

    static native boolean nativeIsInVSResultMenu();//返回是否正在VSResultMenu界面

    private boolean isAddonWindowLoaded = false;
    private ImageView visibilityWindow;
    private WindowManager mWindowManager;


    private float viewWidth = 0, viewHeight = 0;

    //选择种子界面的各种边界
    private float seedChooseFrameTop, seedChooseFrameBottom, seedChooseFrameStart, seedChooseFrameEnd, seedChooseImitaterTop, seedChooseImitaterBottom, seedChooseImitaterStart, seedChooseImitaterEnd, seedChooseHelpBarTop, seedChooseHelpBarBottom, seedChooseFinishStart, seedChooseFinishEnd, seedChooseShopStart, seedChooseShopEnd, seedChooseAlmanacStart, seedChooseAlmanacEnd, seedChooseRepickViewLawnStart, seedChooseRepickViewLawnEnd, seedChooseRepickFinishStart, seedChooseRepickFinishEnd, seedChooseRepickShopStart, seedChooseRepickShopEnd, seedChooseRepickAlmanacStart, seedChooseRepickAlmanacEnd, seedChooseItemWidth, seedChooseItemHeight;

    //模仿者选择种子界面的各种边界
    private float imitaterChooseFrameTop, imitaterChooseFrameBottom, imitaterChooseFrameStart, imitaterChooseFrameEnd, imitaterChooseItemWidth, imitaterChooseItemHeight;

    //植物卡槽的各种边界
    private float seedBankFrameTop, seedBankFrameBottom, seedBankFrameStart, seedBankFrameEnd, shovelFrameStart, shovelFrameEnd, seedBankFrameItemLength;

    //草坪的各种边界
    private float graveBoardFrameTop, graveBoardFrameBottom, graveBoardFrameStart, graveBoardFrameEnd, graveBoardItemWidth, graveBoardHeavyWeaponItemWidth, graveBoardItemHeight;

    //泳池的各种边界
    private float poolBoardFrameTop, poolBoardFrameBottom, poolBoardFrameStart, poolBoardFrameEnd, poolBoardItemWidth, poolBoardItemHeight;

    //屋顶的各种边界（屋顶较为复杂，其前半段的地图是倾斜的，因此额外记录了屋顶前半段的斜率、斜率变为零的转折点的横坐标。）
    private float roofBoardFrameTop, roofBoardFrameBottom, roofBoardFrameStart, roofBoardFrameTurning, roofBoardFrameSlope, roofBoardFrameAddon, roofBoardFrameEnd, roofBoardItemWidth, roofBoardItemHeight;

    //小游戏列表的Item高度
    private float challengeScreenTop, challengeScreenBottom, challengeScreenStart, challengeScreenEnd, challengeScreenItemHeight;

    //商店的各种边界
    private float storeScreenFirstLineTop, storeScreenFirstLineBottom, storeScreenFirstLineStart, storeScreenFirstLineEnd, storeScreenSecondLineTop, storeScreenSecondLineBottom, storeScreenSecondLineStart, storeScreenSecondLineEnd, storeScreenPageSwitchTop, storeScreenPageSwitchBottom, storeScreenPageLeftStart, storeScreenPageLeftEnd, storeScreenPageRightStart, storeScreenPageRightEnd, storeScreenBackTop, storeScreenBackBottom, storeScreenBackStart, storeScreenBackEnd, storeScreenItemWidth, storeScreenItemHeight;

    //花园的各种边界
    private float zenGardenToolBarTop, zenGardenToolBarBottom, zenGardenToolBarStart, zenGardenToolBarEnd, zenGardenToolBarItemWidth;


    //通用Board光标移动边界
    private float boardCursorTop, boardCursorBottom, boardCursorStart, boardCursorEnd, boardCursorAspectRatioX, boardCursorAspectRatioY;

    //对战模式中的双方战场和植物栏的边界
    private float seedBankVSFrameTop, seedBankVSFrameBottom, seedBankVSPlantFrameStart, seedBankVSPlantFrameEnd, shovelVSPlantFrameStart, shovelVSPlantFrameEnd, seedBankVSZombieFrameStart, seedBankVSZombieFrameEnd, seedBankFrameVSItemLength, boardCursorDivide;

    //对战前的选择种子界面边界（自定制战场的界面边界）
    private float plantChooserVSFrameTop, plantChooserVSFrameBottom, plantChooserVSFrameStart, plantChooserVSFrameEnd, plantChooserVSItemLength, plantChooserVSItemHeight, zombieChooserVSFrameTop, zombieChooserVSFrameBottom, zombieChooserVSFrameStart, zombieChooserVSFrameEnd, zombieChooserVSItemLength, zombieChooserVSItemHeight;

    //对战的战前准备的界面中三个按钮的位置
    private float buttonVSFrameTop, buttonVSFrameBottom, buttonVSFrameStart, buttonVSFrameEnd, buttonVSItemLength;

    //对战结束后的结算界面中两个按钮的位置
    private float buttonVSResultFrameTop, buttonVSResultFrameBottom, buttonVSResultFrameStart, buttonVSResultFrameEnd;

    //测量游戏边界，我测！
    public void refreshNativeViewBorders(View view) {

        viewWidth = view.getWidth() / 1920f;
        viewHeight = view.getHeight() / 1080f;

        //选择种子界面的各种边界
        seedChooseFrameTop = viewHeight * 276;
        seedChooseFrameBottom = viewHeight * 900;
        seedChooseFrameStart = viewWidth * 392;
        seedChooseFrameEnd = viewWidth * 1022;

        seedChooseImitaterTop = viewHeight * 827;
        seedChooseImitaterBottom = viewHeight * 937;
        seedChooseImitaterStart = viewWidth * 1055;
        seedChooseImitaterEnd = viewWidth * 1130;
        seedChooseHelpBarTop = viewHeight * 915;
        seedChooseHelpBarBottom = viewHeight * 980;

        seedChooseShopStart = viewWidth * 437;
        seedChooseShopEnd = viewWidth * 545;

        seedChooseFinishStart = viewWidth * 620;
        seedChooseFinishEnd = viewWidth * 790;

        seedChooseAlmanacStart = viewWidth * 868;
        seedChooseAlmanacEnd = viewWidth * 975;

        seedChooseRepickViewLawnStart = viewWidth * 392;
        seedChooseRepickViewLawnEnd = viewWidth * 560;

        seedChooseRepickShopStart = viewWidth * 592;
        seedChooseRepickShopEnd = viewWidth * 691;

        seedChooseRepickFinishStart = viewWidth * 721;
        seedChooseRepickFinishEnd = viewWidth * 890;

        seedChooseRepickAlmanacStart = viewWidth * 920;
        seedChooseRepickAlmanacEnd = viewWidth * 1022;

        seedChooseItemWidth = (seedChooseFrameEnd - seedChooseFrameStart) / 8;
        seedChooseItemHeight = (seedChooseFrameBottom - seedChooseFrameTop) / 6;

        //模仿者选择种子界面的各种边界
        imitaterChooseFrameTop = viewHeight * 278;
        imitaterChooseFrameBottom = viewHeight * 805;
        imitaterChooseFrameStart = viewWidth * 643;
        imitaterChooseFrameEnd = viewWidth * 1252;
        imitaterChooseItemWidth = (imitaterChooseFrameEnd - imitaterChooseFrameStart) / 8;
        imitaterChooseItemHeight = (imitaterChooseFrameBottom - imitaterChooseFrameTop) / 5;

        //植物卡槽的各种边界
        seedBankFrameTop = viewHeight * 90;
        seedBankFrameBottom = viewHeight * 220;
        seedBankFrameStart = viewWidth * 480;
        seedBankFrameEnd = viewWidth * 1260;
        seedBankFrameItemLength = viewWidth * 78;

        //草坪的各种边界
        graveBoardFrameTop = viewHeight * 220;
        graveBoardFrameBottom = viewHeight * 954;
        graveBoardFrameStart = viewWidth * 410;
        graveBoardFrameEnd = viewWidth * 1490;
        graveBoardItemWidth = (graveBoardFrameEnd - graveBoardFrameStart) / 9;
        graveBoardHeavyWeaponItemWidth = graveBoardItemWidth / 2;
        graveBoardItemHeight = (graveBoardFrameBottom - graveBoardFrameTop) / 5;

        //泳池的各种边界
        poolBoardFrameTop = viewHeight * 220;
        poolBoardFrameBottom = viewHeight * 1004;
        poolBoardFrameStart = viewWidth * 410;
        poolBoardFrameEnd = viewWidth * 1490;
        poolBoardItemWidth = (poolBoardFrameEnd - poolBoardFrameStart) / 9;
        poolBoardItemHeight = (poolBoardFrameBottom - poolBoardFrameTop) / 6;

        //屋顶的各种边界（屋顶较为复杂，其前半段的地图是倾斜的，因此额外记录了屋顶前半段的斜率、斜率变为零的转折点的横坐标。）
        roofBoardFrameTop = viewHeight * 220;
        roofBoardFrameBottom = viewHeight * 850;
        roofBoardFrameStart = viewWidth * 418;
        roofBoardFrameTurning = viewWidth * 1018;
        roofBoardFrameSlope = (viewHeight * -132) / (viewWidth * 600);
        roofBoardFrameAddon = viewHeight * 18;
        roofBoardFrameEnd = viewWidth * 1498;
        roofBoardItemWidth = (roofBoardFrameEnd - roofBoardFrameStart) / 9;
        roofBoardItemHeight = (roofBoardFrameBottom - roofBoardFrameTop) / 5;

        challengeScreenTop = viewHeight * 210;
        challengeScreenBottom = viewHeight * 923;
        challengeScreenStart = viewWidth * 404;
        challengeScreenEnd = viewWidth * 1483;
        challengeScreenItemHeight = (challengeScreenBottom - challengeScreenTop) / 4;

        //商店
        storeScreenFirstLineTop = viewHeight * 345;
        storeScreenFirstLineBottom = viewHeight * 505;
        storeScreenFirstLineStart = viewWidth * 940;
        storeScreenFirstLineEnd = viewWidth * 1400;
        storeScreenSecondLineTop = storeScreenFirstLineBottom;
        storeScreenSecondLineBottom = viewHeight * 670;
        storeScreenSecondLineStart = viewWidth * 846;
        storeScreenSecondLineEnd = viewWidth * 1306;
        storeScreenPageSwitchTop = storeScreenSecondLineBottom;
        storeScreenPageSwitchBottom = viewHeight * 805;
        storeScreenPageLeftStart = viewWidth * 639;
        storeScreenPageLeftEnd = viewWidth * 797;
        storeScreenPageRightStart = viewWidth * 1260;
        storeScreenPageRightEnd = viewWidth * 1470;
        storeScreenBackTop = viewHeight * 878;
        storeScreenBackBottom = viewHeight * 1014;
        storeScreenBackStart = viewWidth * 846;
        storeScreenBackEnd = viewWidth * 1098;
        storeScreenItemWidth = (storeScreenFirstLineEnd - storeScreenFirstLineStart) / 4;
        storeScreenItemHeight = storeScreenFirstLineBottom - storeScreenFirstLineTop;

        //花园工具栏
        zenGardenToolBarTop = viewHeight * 90;
        zenGardenToolBarBottom = viewHeight * 197;
        zenGardenToolBarStart = viewWidth * 405;
        zenGardenToolBarEnd = viewWidth * 1560;
        zenGardenToolBarItemWidth = (zenGardenToolBarEnd - zenGardenToolBarStart) / 11;

        //平滑光标模式下通用的移动边界
        boardCursorTop = viewHeight * 230;
        boardCursorBottom = viewHeight * 985;
        boardCursorStart = viewWidth * 420;
        boardCursorEnd = viewWidth * 1515;
        boardCursorAspectRatioX = 730 / (boardCursorEnd - boardCursorStart);
        boardCursorAspectRatioY = 500 / (boardCursorBottom - boardCursorTop);


        //对战模式下的植物方和僵尸方的seedBank
        seedBankVSFrameTop = viewHeight * 90;
        seedBankVSFrameBottom = viewHeight * 206;
        seedBankVSPlantFrameStart = viewWidth * 460;
        seedBankVSPlantFrameEnd = viewWidth * 950;
        shovelVSPlantFrameStart = viewWidth * 370;
        shovelVSPlantFrameEnd = viewWidth * 460;
        seedBankVSZombieFrameStart = viewWidth * 970;
        seedBankVSZombieFrameEnd = viewWidth * 1460;
        seedBankFrameVSItemLength = (seedBankVSZombieFrameEnd - seedBankVSZombieFrameStart) / 6;
        boardCursorDivide = viewWidth * 1144;


        //自定制战场的边界
        plantChooserVSFrameTop = viewHeight * 288;
        plantChooserVSFrameBottom = viewHeight * 828;
        plantChooserVSFrameStart = viewWidth * 415;
        plantChooserVSFrameEnd = viewWidth * 1044;
        plantChooserVSItemLength = (plantChooserVSFrameEnd - plantChooserVSFrameStart) / 8;
        plantChooserVSItemHeight = (plantChooserVSFrameBottom - plantChooserVSFrameTop) / 5;
        zombieChooserVSFrameTop = viewHeight * 288;
        zombieChooserVSFrameBottom = viewHeight * 718;
        zombieChooserVSFrameStart = viewWidth * 1097;
        zombieChooserVSFrameEnd = viewWidth * 1489;
        zombieChooserVSItemLength = (zombieChooserVSFrameEnd - zombieChooserVSFrameStart) / 5;
        zombieChooserVSItemHeight = (zombieChooserVSFrameBottom - zombieChooserVSFrameTop) / 4;

        //对战的战前准备的界面中三个按钮的位置
        buttonVSFrameTop = viewHeight * 825;
        buttonVSFrameBottom = viewHeight * 962;
        buttonVSFrameStart = viewWidth * 483;
        buttonVSFrameEnd = viewWidth * 1449;
        buttonVSItemLength = (buttonVSFrameEnd - buttonVSFrameStart) / 3;

        //对战结束后的结算界面中两个按钮的位置
        buttonVSResultFrameTop = viewHeight * 803;
        buttonVSResultFrameBottom = viewHeight * 945;
        buttonVSResultFrameStart = viewWidth * 635;
        buttonVSResultFrameEnd = viewWidth * 1300;

    }

    private void refreshBoardType() {
        if (nativeIsDaveTalking()) {  //戴夫对话的时候肯定需要发送确认键，所以一旦有戴夫存在，就直接发
            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventDown);
            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventUp);
        }
        //下面这些场景判断的先后顺序很重要，因为这些条件很可能同时成立（比如既在选种子界面，又存在对话框）。这个时候条件们的优先级就很关键了（比如对话框优先级应该高于选种子界面）。
        if (nativeIsImitaterChooser()) boardType = currentBoardType.IMITATER_CHOOSE;
        else if (nativeIsDialogExist()) boardType = currentBoardType.DO_NOTHING;
        else if (nativeIsInVSResultMenu()) boardType = currentBoardType.VS_RESULT;
        else if (nativeVSSetupState() >= 0) boardType = currentBoardType.VS_SETUP;
        else if (nativeIsStoreScreen()) boardType = currentBoardType.STORE_SCREEN;
        else if (nativeIsAwardScreen()) boardType = currentBoardType.AWARD_SCREEN;
        else if (nativeIsMainMenu()) boardType = currentBoardType.DO_NOTHING;
        else if (nativeIsSeedChoosing()) boardType = currentBoardType.SEED_CHOOSE;
        else if (nativeIsChallengeScreen())
            boardType = currentBoardType.CHALLENGE_SCREEN;
        else {
            switch (nativeGameIndex()) {
                case 19:
                    boardType = useSmoothCursor ? currentBoardType.SLOT_MACHINE_SMOOTH : currentBoardType.SLOT_MACHINE;
                    break;
                case 20:
                    boardType = currentBoardType.HEAVY_WEAPON;
                    break;
                case 21:
                    boardType = currentBoardType.BEGHOULED_1;
                    break;
                case 24:
                    boardType = currentBoardType.ZOMBI_QUARIUM;
                    break;
                case 25:
                    boardType = currentBoardType.BEGHOULED_2;
                    break;
                case 76:
                    boardType = currentBoardType.VS_BATTLE;
                    break;
                case 79:
                case 80:
                case 81:
                case 82:
                case 83:
                case 84:
                case 85:
                case 86:
                case 87:
                case 88:
                case 89:
                    boardType = currentBoardType.COOP_BATTLE;
                    break;
                case 90:
                    boardType = useSmoothCursor ? currentBoardType.RAINING_SEEDS_SMOOTH : currentBoardType.RAINING_SEEDS;
                    break;
                default:
                    switch (nativeNowScene()) {
                        case 0:
                            boardType = useSmoothCursor ? currentBoardType.GRAVE_BOARD_SMOOTH : currentBoardType.GRAVE_BOARD;
                            break;
                        case 1:
                            boardType = nativeIsVaseBreakerMode() ? (useSmoothCursor ? currentBoardType.VASE_BREAKER_SMOOTH : currentBoardType.VASE_BREAKER) : nativeIsWhackAZombieLevel() ? currentBoardType.WHACK_ZOMBIE : useSmoothCursor ? currentBoardType.GRAVE_BOARD_SMOOTH : currentBoardType.GRAVE_BOARD;
                            break;
                        case 2:
                        case 3:
                            boardType = useSmoothCursor ? currentBoardType.POOL_BOARD_SMOOTH : currentBoardType.POOL_BOARD;
                            break;
                        case 4:
                        case 5:
                            boardType = useSmoothCursor ? currentBoardType.ROOF_BOARD_SMOOTH : currentBoardType.ROOF_BOARD;
                            break;
                        case 6:
                            boardType = currentBoardType.MUSHROOM_GARDEN;
                            break;
                        case 7:
                            boardType = currentBoardType.ZEN_GARDEN;
                            break;
                        case 8:
                            boardType = currentBoardType.QUARIUM_GARDEN;
                            break;
                        case 9:
                            boardType = currentBoardType.WISDOM_TREE;
                            break;
                        default:
                            boardType = currentBoardType.DO_NOTHING;
                            break;
                    }
                    break;
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        if (!isAddonWindowLoaded) {
            isAddonWindowLoaded = true;

            //Hook，启动！
            System.loadLibrary("Homura");

            SharedPreferences sharedPreferences = getSharedPreferences("data", 0);

            //读取设置中的“开启菜单修改器”设置项，决定是否开启菜单修改器
            if (sharedPreferences.getBoolean("useMenu", true))
                try {
                    CkHomuraMenu menu = new CkHomuraMenu(this);
                    menu.SetWindowManagerActivity();
                    menu.ShowMenu();
                } catch (NoClassDefFoundError ignored) {
                }

            //读取设置中的“开启传送带秒传送”设置项，决定是否开启秒传送
            if (sharedPreferences.getBoolean("enableConveyorBeltFast", true))
                nativeEnableConveyorBeltFast();

            //读取设置中的“手动收集阳光金币”设置项，决定是否开启手动收集
            if (sharedPreferences.getBoolean("enableManualCollect", false))
                nativeEnableManualCollect();

            //读取设置中的“关闭道具栏”设置项，决定是否关闭道具栏
            if (sharedPreferences.getBoolean("disableShop", false))
                nativeDisableShop();

            //读取设置中的“重型武器重力感应”设置项，决定是否开启重力感应
            heavyWeaponAccel = sharedPreferences.getBoolean("heavyWeaponAccel", false);
            if (heavyWeaponAccel) mOrientationListener.enable();

            //平滑光标
            useSmoothCursor = sharedPreferences.getBoolean("useSmoothCursor", true);

            //触控实现的核心逻辑就在这里！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
            mNativeView.setOnTouchListener(new View.OnTouchListener() {
                private float lastX = 0, lastY = 0;//记录Board中的当前光标左上角的位置（并非游戏内的坐标系，而是游戏显示窗口里的坐标，也就是屏幕坐标）。这两个数据的变化是离散的，比如从（78.5, 0）变成（78.5, 130.5），意味着Board光标下移了一格。
                private int seedBankPosition = 0, seedBankMaxPlants = 0;//记录SeedBank中的当前选中的植物是第几个。他的取值可能为为整数的0,1,2,3,4,5,6,7,8,9。
                private boolean isTouchPressedInSeedBankFrame = false, isTouchPressedInPlantsBoard = false, isTouchPressedInSeedChooseFrame = false, isTouchPressedInShovelFrame = false, cobCannonSelected = false, isCursorMoved = false, isGoldWateringCan = false, shovelPicked = false;
                //用于当前触控的一些判断，比如是在SeedBank还是在Board之类的

                private int plantVSTouchPointId = -1, zombieVSTouchPointId = -1;

                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {


                    //在用户按下手指时，使用JNI判断当前场景，从而确定要使用的触控类型。这个判断极快，一秒可以判断20万次，因此不必担心性能。感谢PvzMod
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                        refreshBoardType();

                    //获取触控的坐标信息。再往下就都是Java层的触控逻辑了
                    final float touchX = motionEvent.getX();
                    final float touchY = motionEvent.getY();

                    switch (boardType) {
                        case SEED_CHOOSE:
                            //当前为选种子界面
                            switch (motionEvent.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    if (nativeIsSeedChooserHas7Rows()) {
                                        seedChooseFrameTop = viewHeight * 276;
                                        seedChooseFrameBottom = viewHeight * 900;
                                        seedChooseItemHeight = (seedChooseFrameBottom - seedChooseFrameTop) / 6;
                                    } else {
                                        seedChooseFrameTop = viewHeight * 282;
                                        seedChooseFrameBottom = viewHeight * 822;
                                        seedChooseItemHeight = (seedChooseFrameBottom - seedChooseFrameTop) / 5;
                                    }
                                    if (touchX > seedChooseFrameStart && touchX < seedChooseFrameEnd && touchY < seedChooseFrameBottom && touchY > seedChooseFrameTop) {
                                        //触控落点在在7x8的选种子界面中。无论当前光标在哪里，我们给游戏发送7个上、7个左之后，光标必定位于（1，1）。然后再根据用户触控落点坐标发送X个右、Y个下，即可实现精确坐标定位。
                                        isTouchPressedInSeedChooseFrame = true;
                                        for (int i = 0; i < 7; i++)
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, upEventDown);
                                        for (int i = 0; i < 7; i++)
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, leftEventDown);
                                        final int boardCursorPositionX = (int) ((touchX - seedChooseFrameStart) / seedChooseItemWidth);
                                        for (int i = 0; i < boardCursorPositionX; i++)
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, rightEventDown);
                                        final int boardCursorPositionY = (int) ((touchY - seedChooseFrameTop) / seedChooseItemHeight);
                                        for (int i = 0; i < boardCursorPositionY; i++)
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, downEventDown);
                                        lastX = seedChooseFrameStart + boardCursorPositionX * seedChooseItemWidth;
                                        lastY = seedChooseFrameTop + boardCursorPositionY * seedChooseItemHeight;
                                    } else if (touchX > seedChooseImitaterStart && touchX < seedChooseImitaterEnd && touchY < seedChooseImitaterBottom && touchY > seedChooseImitaterTop) {
                                        //触控落点在“模仿者”按钮上。我们给游戏发送7个上、7个左、5个下、8个右、一个ENTER，这样无论当前光标在哪，做完之后必定可以点击“模仿者”按钮。
                                        if (!nativeIsImitaterEnabled()) return true;

                                        for (int i = 0; i < 7; i++)
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, upEventDown);
                                        for (int i = 0; i < 7; i++)
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, leftEventDown);
                                        for (int i = 0; i < 5; i++)
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, downEventDown);
                                        for (int i = 0; i < 8; i++)
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, rightEventDown);
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventDown);
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventUp);
                                    } else if (touchY < seedChooseHelpBarBottom && touchY > seedChooseHelpBarTop) {
                                        if (nativeIsSurvivalRepick()) {
                                            if (touchX > seedChooseRepickViewLawnStart && touchX < seedChooseRepickViewLawnEnd) {
                                                //触控落点在“查看草坪”按钮上。我们给游戏发送7个上、7个左、6个下、一个ENTER，这样无论当前光标在哪，做完之后必定可以点击“查看草坪”按钮。
                                                for (int i = 0; i < 7; i++)
                                                    NativeInputManager.onKeyInputEventNative(mNativeHandle, null, upEventDown);
                                                for (int i = 0; i < 7; i++)
                                                    NativeInputManager.onKeyInputEventNative(mNativeHandle, null, leftEventDown);
                                                for (int i = 0; i < 6; i++)
                                                    NativeInputManager.onKeyInputEventNative(mNativeHandle, null, downEventDown);
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventDown);
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventUp);
                                            } else if (touchX > seedChooseRepickShopStart && touchX < seedChooseRepickShopEnd) {
                                                if (!nativeIsStoreEnabled()) return true;
                                                //触控落点在“商店”按钮上。我们给游戏发送7个上、7个左、6个下、一个右、一个ENTER，这样无论当前光标在哪，做完之后必定可以点击“商店”按钮。
                                                for (int i = 0; i < 7; i++)
                                                    NativeInputManager.onKeyInputEventNative(mNativeHandle, null, upEventDown);
                                                for (int i = 0; i < 7; i++)
                                                    NativeInputManager.onKeyInputEventNative(mNativeHandle, null, leftEventDown);
                                                for (int i = 0; i < 6; i++)
                                                    NativeInputManager.onKeyInputEventNative(mNativeHandle, null, downEventDown);
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, rightEventDown);
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventDown);
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventUp);
                                            } else if (touchX > seedChooseRepickFinishStart && touchX < seedChooseRepickFinishEnd) {
                                                //触控落点在“开始游戏”的按钮上。我们给游戏发送7个上、7个左、6个下、2个右、一个ENTER，这样无论当前光标在哪，做完之后必定可以点击“开始游戏”按钮。请叫我天才！

                                                if (!nativeIsStartButtonEnabled()) return true;
                                                for (int i = 0; i < 7; i++)
                                                    NativeInputManager.onKeyInputEventNative(mNativeHandle, null, upEventDown);
                                                for (int i = 0; i < 7; i++)
                                                    NativeInputManager.onKeyInputEventNative(mNativeHandle, null, leftEventDown);
                                                for (int i = 0; i < 6; i++)
                                                    NativeInputManager.onKeyInputEventNative(mNativeHandle, null, downEventDown);
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, rightEventDown);
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, rightEventDown);
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventDown);
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventUp);
                                            } else if (touchX > seedChooseRepickAlmanacStart && touchX < seedChooseRepickAlmanacEnd) {
                                                //触控落点在“图鉴”按钮上。我们给游戏发送7个上、7个右、6个下、一个ENTER，这样无论当前光标在哪，做完之后必定可以点击“图鉴”按钮。
                                                if (!nativeIsAlmanacEnabled()) return true;
                                                for (int i = 0; i < 7; i++)
                                                    NativeInputManager.onKeyInputEventNative(mNativeHandle, null, upEventDown);
                                                for (int i = 0; i < 7; i++)
                                                    NativeInputManager.onKeyInputEventNative(mNativeHandle, null, rightEventDown);
                                                for (int i = 0; i < 6; i++)
                                                    NativeInputManager.onKeyInputEventNative(mNativeHandle, null, downEventDown);
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventDown);
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventUp);
                                            }
                                        } else {
                                            if (touchX > seedChooseShopStart && touchX < seedChooseShopEnd) {
                                                if (!nativeIsStoreEnabled()) return true;
                                                //触控落点在“商店”按钮上。我们给游戏发送7个上、7个左、6个下、一个ENTER，这样无论当前光标在哪，做完之后必定可以点击“商店”按钮。
                                                for (int i = 0; i < 7; i++)
                                                    NativeInputManager.onKeyInputEventNative(mNativeHandle, null, upEventDown);
                                                for (int i = 0; i < 7; i++)
                                                    NativeInputManager.onKeyInputEventNative(mNativeHandle, null, leftEventDown);
                                                for (int i = 0; i < 6; i++)
                                                    NativeInputManager.onKeyInputEventNative(mNativeHandle, null, downEventDown);
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventDown);
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventUp);
                                            } else if (touchX > seedChooseFinishStart && touchX < seedChooseFinishEnd) {
                                                //触控落点在“开始游戏”的按钮上。我们给游戏发送7个上、7个左、6个下、1个右、一个ENTER，这样无论当前光标在哪，做完之后必定可以点击“开始游戏”按钮。请叫我天才！

                                                if (!nativeIsStartButtonEnabled()) return true;
                                                for (int i = 0; i < 7; i++)
                                                    NativeInputManager.onKeyInputEventNative(mNativeHandle, null, upEventDown);
                                                for (int i = 0; i < 7; i++)
                                                    NativeInputManager.onKeyInputEventNative(mNativeHandle, null, leftEventDown);
                                                for (int i = 0; i < 6; i++)
                                                    NativeInputManager.onKeyInputEventNative(mNativeHandle, null, downEventDown);
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, rightEventDown);
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventDown);
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventUp);
                                            } else if (touchX > seedChooseAlmanacStart && touchX < seedChooseAlmanacEnd) {
                                                if (!nativeIsAlmanacEnabled()) return true;
                                                //触控落点在“图鉴”按钮上。我们给游戏发送7个上、7个右、6个下、一个ENTER，这样无论当前光标在哪，做完之后必定可以点击“图鉴”按钮。
                                                for (int i = 0; i < 7; i++)
                                                    NativeInputManager.onKeyInputEventNative(mNativeHandle, null, upEventDown);
                                                for (int i = 0; i < 7; i++)
                                                    NativeInputManager.onKeyInputEventNative(mNativeHandle, null, rightEventDown);
                                                for (int i = 0; i < 6; i++)
                                                    NativeInputManager.onKeyInputEventNative(mNativeHandle, null, downEventDown);
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventDown);
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventUp);
                                            }
                                        }

                                    }
                                    break;
                                case MotionEvent.ACTION_MOVE:
                                    //将触控的移动转换为光标的移动。其核心逻辑是，lastX表示当前光标的X坐标，如果touchX小于lastX，立即将光标左移一格；如果touchX大于(lastX + seedChooseItemWidth), 立即将光标右移一格。每次移动后，更新lastX的值。
                                    if (isTouchPressedInSeedChooseFrame) {
                                        if (touchX < seedChooseFrameStart || touchX > seedChooseFrameEnd || touchY < seedChooseFrameTop || touchY > seedChooseFrameBottom)
                                            break;
                                        if (touchX < lastX) {
                                            lastX -= seedChooseItemWidth;
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, leftEventDown);
                                        } else if (touchX > lastX + seedChooseItemWidth) {
                                            lastX += seedChooseItemWidth;
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, rightEventDown);
                                        }

                                        if (touchY < lastY) {
                                            lastY -= seedChooseItemHeight;
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, upEventDown);
                                        } else if (touchY > lastY + seedChooseItemHeight) {
                                            lastY += seedChooseItemHeight;
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, downEventDown);
                                        }
                                    }
                                    break;
                                case MotionEvent.ACTION_UP:
                                    if (isTouchPressedInSeedChooseFrame) {
                                        //在抬起手指时，发送ENTER键
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventDown);
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventUp);
                                    }
                                    isTouchPressedInSeedChooseFrame = false;
                                    lastX = 0;
                                    lastY = 0;
                                    break;
                            }
                            break;
                        case IMITATER_CHOOSE:
                            //模仿者的模仿目标的选择界面
                            switch (motionEvent.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    if (touchX > imitaterChooseFrameStart && touchX < imitaterChooseFrameEnd && touchY < imitaterChooseFrameBottom && touchY > imitaterChooseFrameTop) {
                                        //触控落点在在5x8的模仿者选种子界面中。无论当前光标在哪里，我们给游戏发送4个上、7个左之后，光标必定位于（1，1）。然后再根据用户触控落点坐标发送X个右、Y个下，即可实现精确坐标定位。
                                        isTouchPressedInSeedChooseFrame = true;
                                        for (int i = 0; i < 4; i++)
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, upEventDown);
                                        for (int i = 0; i < 7; i++)
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, leftEventDown);
                                        final int boardCursorPositionX = (int) ((touchX - imitaterChooseFrameStart) / imitaterChooseItemWidth);
                                        for (int i = 0; i < boardCursorPositionX; i++)
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, rightEventDown);
                                        final int boardCursorPositionY = (int) ((touchY - imitaterChooseFrameTop) / imitaterChooseItemHeight);
                                        for (int i = 0; i < boardCursorPositionY; i++)
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, downEventDown);
                                        lastX = imitaterChooseFrameStart + boardCursorPositionX * imitaterChooseItemWidth;
                                        lastY = imitaterChooseFrameTop + boardCursorPositionY * imitaterChooseItemHeight;
                                    }
                                    break;
                                case MotionEvent.ACTION_MOVE:
                                    //将触控的移动转换为光标的移动，同样是精确定位。其核心逻辑是，lastX表示当前光标的X坐标，如果touchX小于lastX，立即将光标左移一格；如果touchX大于(lastX + seedChooseItemWidth), 立即将光标右移一格。每次移动后，更新lastX的值。
                                    if (isTouchPressedInSeedChooseFrame) {
                                        if (touchX < imitaterChooseFrameStart || touchX > imitaterChooseFrameEnd || touchY < imitaterChooseFrameTop || touchY > imitaterChooseFrameBottom)
                                            break;
                                        if (touchX < lastX) {
                                            lastX -= imitaterChooseItemWidth;
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, leftEventDown);
                                        } else if (touchX > lastX + imitaterChooseItemWidth) {
                                            lastX += imitaterChooseItemWidth;
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, rightEventDown);
                                        }
                                        if (touchY < lastY) {
                                            lastY -= imitaterChooseItemHeight;
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, upEventDown);
                                        } else if (touchY > lastY + imitaterChooseItemHeight) {
                                            lastY += imitaterChooseItemHeight;
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, downEventDown);
                                        }
                                    }
                                    break;
                                case MotionEvent.ACTION_UP:
                                    if (isTouchPressedInSeedChooseFrame) {
                                        //在抬起手指时，发送ENTER键
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventDown);
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventUp);
                                    }
                                    isTouchPressedInSeedChooseFrame = false;
                                    lastX = 0;
                                    lastY = 0;
                                    break;
                            }
                            break;
                        case GRAVE_BOARD:
                            //草坪战斗界面（白天、黑夜）
                            switch (motionEvent.getAction()) {
                                case MotionEvent.ACTION_DOWN:

                                    if (touchY < seedBankFrameBottom && touchY > seedBankFrameTop && touchX > seedBankFrameStart) {
                                        seedBankMaxPlants = nativeMaxPlants();
                                        switch (seedBankMaxPlants) {
                                            case 7:
                                                seedBankFrameEnd = 1100 * viewWidth;
                                                seedBankFrameItemLength = (seedBankFrameEnd - seedBankFrameStart) / 7;
                                                break;
                                            case 8:
                                                seedBankFrameEnd = 1124 * viewWidth;
                                                seedBankFrameItemLength = (seedBankFrameEnd - seedBankFrameStart) / 8;
                                                break;
                                            case 9:
                                                seedBankFrameEnd = 1178 * viewWidth;
                                                seedBankFrameItemLength = (seedBankFrameEnd - seedBankFrameStart) / 9;
                                                break;
                                            case 10:
                                                seedBankFrameEnd = 1238 * viewWidth;
                                                seedBankFrameItemLength = (seedBankFrameEnd - seedBankFrameStart) / 10;
                                                break;
                                            default:
                                                seedBankFrameEnd = 1010 * viewWidth;
                                                seedBankFrameItemLength = (seedBankFrameEnd - seedBankFrameStart) / 6;
                                                break;
                                        }

                                        if (nativeIsShovelEnabled()) {
                                            shovelFrameStart = seedBankFrameEnd + 35 * viewWidth;
                                            shovelFrameEnd = shovelFrameStart + 105 * viewWidth;
                                        } else {
                                            shovelFrameStart = 0;
                                            shovelFrameEnd = 0;
                                        }


                                        if (touchX < seedBankFrameEnd) {
                                            //如果用户手指落点位于SeedBank区域中，则设定SeedBank的光标位置为用户手指落点位置。
                                            isTouchPressedInSeedBankFrame = true;
                                            seedBankPosition = (int) ((touchX - seedBankFrameStart) / seedBankFrameItemLength);
                                            if (seedBankPosition != nativeGetSeekBankPosition()) {
                                                isCursorMoved = true;
                                                nativeSetSeedBankPosition(seedBankPosition);
                                            }
                                            lastX = seedBankFrameStart + seedBankPosition * seedBankFrameItemLength;
                                            if (nativeIsCobCannonSelected()) {
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventDown);
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventUp);
                                            }
                                        } else if (touchX > shovelFrameStart && touchX < shovelFrameEnd) {
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, shovelEventDown);
                                            return true;
                                        }


                                    } else if (touchX > graveBoardFrameStart && touchX < graveBoardFrameEnd && touchY < graveBoardFrameBottom && touchY > graveBoardFrameTop) {
                                        //如果触控落点在Board区域中
                                        isTouchPressedInPlantsBoard = true;

                                        if (nativeGameState() == 6) {
                                            //如果在选植物卡，则取消选取植物卡.
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventDown);
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventUp);
                                        }

                                        //将光标移动至用户手指落点处
                                        final int boardCursorPositionX = (int) ((touchX - graveBoardFrameStart) / graveBoardItemWidth);
                                        final int diffX = nativeGetCursorPositionX() - boardCursorPositionX;
                                        if (diffX > 0) {
                                            for (int i = 0; i < diffX; i++)
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, leftEventDown);
                                        } else if (diffX < 0) {
                                            for (int i = diffX; i < 0; i++)
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, rightEventDown);
                                        }

                                        final int boardCursorPositionY = (int) ((touchY - graveBoardFrameTop) / graveBoardItemHeight);
                                        final int diffY = nativeGetCursorPositionY() - boardCursorPositionY;
                                        if (diffY > 0) {
                                            for (int i = 0; i < diffY; i++)
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, upEventDown);
                                        } else if (diffY < 0) {
                                            for (int i = diffY; i < 0; i++)
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, downEventDown);
                                        }

                                        lastX = graveBoardFrameStart + boardCursorPositionX * graveBoardItemWidth;
                                        lastY = graveBoardFrameTop + boardCursorPositionY * graveBoardItemHeight;
                                        //移动光标完毕。


                                        //检查当前光标处是否为一个充能完毕的加农炮。
                                        cobCannonSelected = nativeIsCobCannonSelected();
                                        if (!cobCannonSelected && nativeGameState() != 6) {
                                            nativeRequestValidCobCanonCheck(boardCursorPositionX, boardCursorPositionY);
                                        }
                                    }
                                    break;
                                case MotionEvent.ACTION_MOVE:
                                    if (isTouchPressedInSeedBankFrame) {

                                        //用户触控落点为SeedBank区域。
                                        if (touchX > graveBoardFrameStart && touchX < graveBoardFrameEnd && touchY < graveBoardFrameBottom && touchY > graveBoardFrameTop) {
                                            //在用户触控落点为SeedBank区域的前提下，如果用户将手指移动至Board区域了，则我们结束SeedBank中的植物选取过程，并将Board光标移动至用户手指处。
                                            isTouchPressedInSeedBankFrame = false;
                                            isTouchPressedInPlantsBoard = true;
                                            nativeSetGameState(7);
                                            final int boardCursorPositionX = (int) ((touchX - graveBoardFrameStart) / graveBoardItemWidth);
                                            final int diffX = nativeGetCursorPositionX() - boardCursorPositionX;
                                            if (diffX > 0) {
                                                for (int i = 0; i < diffX; i++)
                                                    NativeInputManager.onKeyInputEventNative(mNativeHandle, null, leftEventDown);
                                            } else if (diffX < 0) {
                                                for (int i = diffX; i < 0; i++)
                                                    NativeInputManager.onKeyInputEventNative(mNativeHandle, null, rightEventDown);
                                            }

                                            final int boardCursorPositionY = (int) ((touchY - graveBoardFrameTop) / graveBoardItemHeight);
                                            final int diffY = nativeGetCursorPositionY() - boardCursorPositionY;
                                            if (diffY > 0) {
                                                for (int i = 0; i < diffY; i++)
                                                    NativeInputManager.onKeyInputEventNative(mNativeHandle, null, upEventDown);
                                            } else if (diffY < 0) {
                                                for (int i = diffY; i < 0; i++)
                                                    NativeInputManager.onKeyInputEventNative(mNativeHandle, null, downEventDown);
                                            }

                                            lastX = graveBoardFrameStart + boardCursorPositionX * graveBoardItemWidth;
                                            lastY = graveBoardFrameTop + boardCursorPositionY * graveBoardItemHeight;

                                        } else {

                                            //用户手指在SeeBank中滑动。我们根据用户手指位置来设定SeedBank的光标位置。
                                            if (touchX < lastX) {
                                                if (seedBankPosition > 0) {
                                                    seedBankPosition--;
                                                    isCursorMoved = true;
                                                    nativeSetSeedBankPosition(seedBankPosition);
                                                    lastX -= seedBankFrameItemLength;
                                                }
                                            } else if (touchX > lastX + seedBankFrameItemLength) {
                                                if (seedBankPosition < seedBankMaxPlants - 1) {
                                                    seedBankPosition++;
                                                    isCursorMoved = true;
                                                    nativeSetSeedBankPosition(seedBankPosition);
                                                    lastX += seedBankFrameItemLength;
                                                }
                                            }
                                        }
                                    } else if (isTouchPressedInPlantsBoard) {
                                        //用户手指落点在Board中。
                                        if (touchY < seedBankFrameBottom && touchY > seedBankFrameTop && touchX > seedBankFrameStart && touchX < seedBankFrameEnd) {
                                            //如果用户手指移动进SeedBank区域且手上拿着植物或加农炮靶标，则取消选取当前植物或加农炮靶标。
                                            if (nativeGameState() == 7 || nativeIsCobCannonSelected()) {
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventDown);
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventUp);
                                                cobCannonSelected = false;
                                            }
                                            isTouchPressedInPlantsBoard = false;
                                            return true;
                                        }

                                        //如果用户手指移出了Board区域，就不再继续处理。
                                        if (touchX < graveBoardFrameStart || touchX > graveBoardFrameEnd || touchY < graveBoardFrameTop || touchY > graveBoardFrameBottom)
                                            break;


                                        //让Board光标跟随用户手指移动。
                                        if (touchX < lastX) {
                                            isCursorMoved = true;
                                            lastX -= graveBoardItemWidth;
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, leftEventDown);
                                        } else if (touchX > lastX + graveBoardItemWidth) {
                                            isCursorMoved = true;
                                            lastX += graveBoardItemWidth;
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, rightEventDown);
                                        }
                                        if (touchY < lastY) {
                                            isCursorMoved = true;
                                            lastY -= graveBoardItemHeight;
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, upEventDown);
                                        } else if (touchY > lastY + graveBoardItemHeight) {
                                            isCursorMoved = true;
                                            lastY += graveBoardItemHeight;
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, downEventDown);
                                        }
                                    }
                                    break;
                                case MotionEvent.ACTION_UP:

                                    if (isTouchPressedInSeedBankFrame && !isCursorMoved) {
                                        nativeSetGameState(nativeGameState() == 7 ? 1 : 7);
                                    }

                                    //根据一些条件来确定是否在抬起手指时发送确认键
                                    if (nativeGameState() == 7 && isTouchPressedInPlantsBoard || isCursorMoved && nativeIsCobCannonSelected() || cobCannonSelected) {
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventDown);
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventUp);
                                    }

                                    cobCannonSelected = false;
                                    isCursorMoved = false;
                                    isTouchPressedInSeedBankFrame = false;
                                    isTouchPressedInPlantsBoard = false;
                                    lastX = 0;
                                    lastY = 0;
                                    break;
                            }
                            break;
                        case POOL_BOARD:
                            //泳池战斗界面（泳池，雾夜）
                            switch (motionEvent.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    if (touchY < seedBankFrameBottom && touchY > seedBankFrameTop && touchX > seedBankFrameStart) {
                                        seedBankMaxPlants = nativeMaxPlants();
                                        switch (seedBankMaxPlants) {
                                            case 7:
                                                seedBankFrameEnd = 1100 * viewWidth;
                                                seedBankFrameItemLength = (seedBankFrameEnd - seedBankFrameStart) / 7;
                                                break;
                                            case 8:
                                                seedBankFrameEnd = 1124 * viewWidth;
                                                seedBankFrameItemLength = (seedBankFrameEnd - seedBankFrameStart) / 8;
                                                break;
                                            case 9:
                                                seedBankFrameEnd = 1178 * viewWidth;
                                                seedBankFrameItemLength = (seedBankFrameEnd - seedBankFrameStart) / 9;
                                                break;
                                            case 10:
                                                seedBankFrameEnd = 1238 * viewWidth;
                                                seedBankFrameItemLength = (seedBankFrameEnd - seedBankFrameStart) / 10;
                                                break;
                                            default:
                                                seedBankFrameEnd = 1010 * viewWidth;
                                                seedBankFrameItemLength = (seedBankFrameEnd - seedBankFrameStart) / 6;
                                                break;
                                        }

                                        if (nativeIsShovelEnabled()) {
                                            shovelFrameStart = seedBankFrameEnd + 35 * viewWidth;
                                            shovelFrameEnd = shovelFrameStart + 105 * viewWidth;
                                        } else {
                                            shovelFrameStart = 0;
                                            shovelFrameEnd = 0;
                                        }


                                        if (touchX < seedBankFrameEnd) {
                                            //如果用户手指落点位于SeedBank区域中，则设定SeedBank的光标位置为用户手指落点位置。
                                            isTouchPressedInSeedBankFrame = true;
                                            seedBankPosition = (int) ((touchX - seedBankFrameStart) / seedBankFrameItemLength);
                                            if (seedBankPosition != nativeGetSeekBankPosition()) {
                                                isCursorMoved = true;
                                                nativeSetSeedBankPosition(seedBankPosition);
                                            }
                                            lastX = seedBankFrameStart + seedBankPosition * seedBankFrameItemLength;
                                            if (nativeIsCobCannonSelected()) {
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventDown);
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventUp);
                                            }
                                        } else if (touchX > shovelFrameStart && touchX < shovelFrameEnd) {
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, shovelEventDown);
                                            return true;
                                        }


                                    } else if (touchX > poolBoardFrameStart && touchX < poolBoardFrameEnd && touchY < poolBoardFrameBottom && touchY > poolBoardFrameTop) {
                                        //如果触控落点在Board区域中
                                        if (nativeGameState() == 6) {
                                            //如果在选植物卡，则取消选取植物卡.
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventDown);
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventUp);
                                        }

                                        //将光标移动至用户手指落点处
                                        isTouchPressedInPlantsBoard = true;
                                        final int boardCursorPositionX = (int) ((touchX - poolBoardFrameStart) / poolBoardItemWidth);
                                        final int diffX = nativeGetCursorPositionX() - boardCursorPositionX;
                                        if (diffX > 0) {
                                            for (int i = 0; i < diffX; i++)
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, leftEventDown);
                                        } else if (diffX < 0) {
                                            for (int i = diffX; i < 0; i++)
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, rightEventDown);
                                        }

                                        final int boardCursorPositionY = (int) ((touchY - poolBoardFrameTop) / poolBoardItemHeight);
                                        final int diffY = nativeGetCursorPositionY() - boardCursorPositionY;
                                        if (diffY > 0) {
                                            for (int i = 0; i < diffY; i++)
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, upEventDown);
                                        } else if (diffY < 0) {
                                            for (int i = diffY; i < 0; i++)
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, downEventDown);
                                        }

                                        lastX = poolBoardFrameStart + boardCursorPositionX * poolBoardItemWidth;
                                        lastY = poolBoardFrameTop + boardCursorPositionY * poolBoardItemHeight;
                                        //移动光标完毕。

                                        //检查当前光标处是否为一个充能完毕的加农炮。
                                        cobCannonSelected = nativeIsCobCannonSelected();
                                        if (!cobCannonSelected && nativeGameState() != 6) {
                                            nativeRequestValidCobCanonCheck(boardCursorPositionX, boardCursorPositionY);
                                        }
                                    }
                                    break;
                                case MotionEvent.ACTION_MOVE:
                                    if (isTouchPressedInSeedBankFrame)
                                        //用户触控落点为SeedBank区域。
                                        if (touchX > poolBoardFrameStart && touchX < poolBoardFrameEnd && touchY < poolBoardFrameBottom && touchY > poolBoardFrameTop) {
                                            //在用户触控落点为SeedBank区域的前提下，如果用户将手指移动至Board区域了，则我们结束SeedBank中的植物选取过程，并将Board光标移动至用户手指处。
                                            isTouchPressedInSeedBankFrame = false;
                                            isTouchPressedInPlantsBoard = true;
                                            nativeSetGameState(7);
                                            final int boardCursorPositionX = (int) ((touchX - poolBoardFrameStart) / poolBoardItemWidth);
                                            final int diffX = nativeGetCursorPositionX() - boardCursorPositionX;
                                            if (diffX > 0) {
                                                for (int i = 0; i < diffX; i++)
                                                    NativeInputManager.onKeyInputEventNative(mNativeHandle, null, leftEventDown);
                                            } else if (diffX < 0) {
                                                for (int i = diffX; i < 0; i++)
                                                    NativeInputManager.onKeyInputEventNative(mNativeHandle, null, rightEventDown);
                                            }

                                            final int boardCursorPositionY = (int) ((touchY - poolBoardFrameTop) / poolBoardItemHeight);
                                            final int diffY = nativeGetCursorPositionY() - boardCursorPositionY;
                                            if (diffY > 0) {
                                                for (int i = 0; i < diffY; i++)
                                                    NativeInputManager.onKeyInputEventNative(mNativeHandle, null, upEventDown);
                                            } else if (diffY < 0) {
                                                for (int i = diffY; i < 0; i++)
                                                    NativeInputManager.onKeyInputEventNative(mNativeHandle, null, downEventDown);
                                            }
                                            lastX = poolBoardFrameStart + boardCursorPositionX * poolBoardItemWidth;
                                            lastY = poolBoardFrameTop + boardCursorPositionY * poolBoardItemHeight;

                                        } else {

                                            //用户手指在SeeBank中滑动。我们根据用户手指位置来设定SeedBank的光标位置。
                                            if (touchX < lastX) {
                                                if (seedBankPosition > 0) {
                                                    seedBankPosition--;
                                                    isCursorMoved = true;
                                                    nativeSetSeedBankPosition(seedBankPosition);
                                                    lastX -= seedBankFrameItemLength;
                                                }
                                            } else if (touchX > lastX + seedBankFrameItemLength) {
                                                if (seedBankPosition < seedBankMaxPlants - 1) {
                                                    seedBankPosition++;
                                                    isCursorMoved = true;
                                                    nativeSetSeedBankPosition(seedBankPosition);
                                                    lastX += seedBankFrameItemLength;
                                                }
                                            }
                                        }

                                    else if (isTouchPressedInPlantsBoard) {

                                        if (touchY < seedBankFrameBottom && touchY > seedBankFrameTop && touchX > seedBankFrameStart && touchX < seedBankFrameEnd) {
                                            //如果用户手指移动进SeedBank区域且手上拿着植物或加农炮炮弹，则取消当前植物种植或加农炮发射。
                                            if (nativeGameState() == 7 || nativeIsCobCannonSelected()) {
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventDown);
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventUp);
                                                cobCannonSelected = false;
                                            }
                                            isTouchPressedInPlantsBoard = false;
                                            return true;
                                        }


                                        //如果用户手指移出了Board区域，就不再继续处理。
                                        if (touchX < poolBoardFrameStart || touchX > poolBoardFrameEnd || touchY < poolBoardFrameTop || touchY > poolBoardFrameBottom)
                                            break;


                                        //让Board光标跟随用户手指移动。
                                        if (touchX < lastX) {
                                            isCursorMoved = true;
                                            lastX -= poolBoardItemWidth;
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, leftEventDown);
                                        } else if (touchX > lastX + poolBoardItemWidth) {
                                            isCursorMoved = true;
                                            lastX += poolBoardItemWidth;
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, rightEventDown);
                                        }
                                        if (touchY < lastY) {
                                            isCursorMoved = true;
                                            lastY -= poolBoardItemHeight;
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, upEventDown);
                                        } else if (touchY > lastY + poolBoardItemHeight) {
                                            isCursorMoved = true;
                                            lastY += poolBoardItemHeight;
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, downEventDown);
                                        }

                                    }
                                    break;
                                case MotionEvent.ACTION_UP:
                                    if (isTouchPressedInSeedBankFrame && !isCursorMoved) {
                                        nativeSetGameState(nativeGameState() == 7 ? 1 : 7);
                                    }
                                    //根据一些条件来确定是否在抬起手指时发送确认键
                                    if (nativeGameState() == 7 && isTouchPressedInPlantsBoard || isCursorMoved && nativeIsCobCannonSelected() || cobCannonSelected) {
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventDown);
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventUp);
                                    }
                                    isCursorMoved = false;
                                    cobCannonSelected = false;
                                    isTouchPressedInSeedBankFrame = false;
                                    isTouchPressedInPlantsBoard = false;
                                    lastX = 0;
                                    lastY = 0;
                                    break;
                            }
                            break;
                        case ROOF_BOARD:
                            //屋顶战斗界面（屋顶、月夜）。屋顶前半段地图是倾斜的，所以我将触控Y坐标换算为统一的Y坐标，这就是projectionY。
                            final float projectionY = touchX < roofBoardFrameTurning ? touchY + roofBoardFrameSlope * (roofBoardFrameTurning - touchX) - roofBoardFrameAddon : touchY;
                            switch (motionEvent.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    if (touchY < seedBankFrameBottom && touchY > seedBankFrameTop && touchX > seedBankFrameStart) {
                                        seedBankMaxPlants = nativeMaxPlants();
                                        switch (seedBankMaxPlants) {
                                            case 7:
                                                seedBankFrameEnd = 1100 * viewWidth;
                                                seedBankFrameItemLength = (seedBankFrameEnd - seedBankFrameStart) / 7;
                                                break;
                                            case 8:
                                                seedBankFrameEnd = 1124 * viewWidth;
                                                seedBankFrameItemLength = (seedBankFrameEnd - seedBankFrameStart) / 8;
                                                break;
                                            case 9:
                                                seedBankFrameEnd = 1178 * viewWidth;
                                                seedBankFrameItemLength = (seedBankFrameEnd - seedBankFrameStart) / 9;
                                                break;
                                            case 10:
                                                seedBankFrameEnd = 1238 * viewWidth;
                                                seedBankFrameItemLength = (seedBankFrameEnd - seedBankFrameStart) / 10;
                                                break;
                                            default:
                                                seedBankFrameEnd = 1010 * viewWidth;
                                                seedBankFrameItemLength = (seedBankFrameEnd - seedBankFrameStart) / 6;
                                                break;
                                        }

                                        if (nativeIsShovelEnabled()) {
                                            shovelFrameStart = seedBankFrameEnd + 35 * viewWidth;
                                            shovelFrameEnd = shovelFrameStart + 105 * viewWidth;
                                        } else {
                                            shovelFrameStart = 0;
                                            shovelFrameEnd = 0;
                                        }


                                        if (touchX < seedBankFrameEnd) {
                                            //如果用户手指落点位于SeedBank区域中，则设定SeedBank的光标位置为用户手指落点位置。
                                            isTouchPressedInSeedBankFrame = true;
                                            seedBankPosition = (int) ((touchX - seedBankFrameStart) / seedBankFrameItemLength);
                                            if (seedBankPosition != nativeGetSeekBankPosition()) {
                                                isCursorMoved = true;
                                                nativeSetSeedBankPosition(seedBankPosition);
                                            }
                                            lastX = seedBankFrameStart + seedBankPosition * seedBankFrameItemLength;
                                            if (nativeIsCobCannonSelected()) {
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventDown);
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventUp);
                                            }
                                        } else if (touchX > shovelFrameStart && touchX < shovelFrameEnd) {
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, shovelEventDown);
                                            return true;
                                        }


                                    } else if (touchX > roofBoardFrameStart && touchX < roofBoardFrameEnd && projectionY < roofBoardFrameBottom && projectionY > roofBoardFrameTop) {
                                        //如果触控落点在Board区域中
                                        if (nativeGameState() == 6) {
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventDown);
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventUp);
                                        }

                                        //将光标移动至用户手指落点处
                                        isTouchPressedInPlantsBoard = true;
                                        final int boardCursorPositionX = (int) ((touchX - roofBoardFrameStart) / roofBoardItemWidth);
                                        final int diffX = nativeGetCursorPositionX() - boardCursorPositionX;
                                        if (diffX > 0) {
                                            for (int i = 0; i < diffX; i++)
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, leftEventDown);
                                        } else if (diffX < 0) {
                                            for (int i = diffX; i < 0; i++)
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, rightEventDown);
                                        }

                                        final int boardCursorPositionY = (int) ((projectionY - roofBoardFrameTop) / roofBoardItemHeight);
                                        final int diffY = nativeGetCursorPositionY() - boardCursorPositionY;
                                        if (diffY > 0) {
                                            for (int i = 0; i < diffY; i++)
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, upEventDown);
                                        } else if (diffY < 0) {
                                            for (int i = diffY; i < 0; i++)
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, downEventDown);
                                        }
                                        lastX = roofBoardFrameStart + boardCursorPositionX * roofBoardItemWidth;
                                        lastY = roofBoardFrameTop + boardCursorPositionY * roofBoardItemHeight;
                                        //移动光标完毕。

                                        //检查当前光标处是否为一个充能完毕的加农炮。
                                        cobCannonSelected = nativeIsCobCannonSelected();
                                        if (!cobCannonSelected && nativeGameState() != 6) {
                                            nativeRequestValidCobCanonCheck(boardCursorPositionX, boardCursorPositionY);
                                        }
                                    }
                                    break;
                                case MotionEvent.ACTION_MOVE:
                                    if (isTouchPressedInSeedBankFrame)
                                        //用户触控落点为SeedBank区域。
                                        if (touchX > roofBoardFrameStart && touchX < roofBoardFrameEnd && projectionY < roofBoardFrameBottom && projectionY > roofBoardFrameTop) {
                                            //在用户触控落点为SeedBank区域的前提下，如果用户将手指移动至Board区域了，则我们结束SeedBank中的植物选取过程，并将Board光标移动至用户手指处。
                                            isTouchPressedInSeedBankFrame = false;
                                            isTouchPressedInPlantsBoard = true;
                                            nativeSetGameState(7);
                                            final int boardCursorPositionX = (int) ((touchX - roofBoardFrameStart) / roofBoardItemWidth);
                                            final int diffX = nativeGetCursorPositionX() - boardCursorPositionX;
                                            if (diffX > 0) {
                                                for (int i = 0; i < diffX; i++)
                                                    NativeInputManager.onKeyInputEventNative(mNativeHandle, null, leftEventDown);
                                            } else if (diffX < 0) {
                                                for (int i = diffX; i < 0; i++)
                                                    NativeInputManager.onKeyInputEventNative(mNativeHandle, null, rightEventDown);
                                            }

                                            final int boardCursorPositionY = (int) ((touchY - roofBoardFrameTop) / roofBoardItemHeight);
                                            final int diffY = nativeGetCursorPositionY() - boardCursorPositionY;
                                            if (diffY > 0) {
                                                for (int i = 0; i < diffY; i++)
                                                    NativeInputManager.onKeyInputEventNative(mNativeHandle, null, upEventDown);
                                            } else if (diffY < 0) {
                                                for (int i = diffY; i < 0; i++)
                                                    NativeInputManager.onKeyInputEventNative(mNativeHandle, null, downEventDown);
                                            }
                                            lastX = roofBoardFrameStart + boardCursorPositionX * roofBoardItemWidth;
                                            lastY = roofBoardFrameTop + boardCursorPositionY * roofBoardItemHeight;

                                        } else {

                                            //用户手指在SeeBank中滑动。我们根据用户手指位置来设定SeedBank的光标位置。
                                            if (touchX < lastX) {
                                                if (seedBankPosition > 0) {
                                                    seedBankPosition--;
                                                    isCursorMoved = true;
                                                    nativeSetSeedBankPosition(seedBankPosition);
                                                    lastX -= seedBankFrameItemLength;
                                                }
                                            } else if (touchX > lastX + seedBankFrameItemLength) {
                                                if (seedBankPosition < seedBankMaxPlants - 1) {
                                                    seedBankPosition++;
                                                    isCursorMoved = true;
                                                    nativeSetSeedBankPosition(seedBankPosition);
                                                    lastX += seedBankFrameItemLength;
                                                }
                                            }
                                        }

                                    else if (isTouchPressedInPlantsBoard) {
                                        if (touchY < seedBankFrameBottom && touchY > seedBankFrameTop && touchX > seedBankFrameStart && touchX < seedBankFrameEnd) {
                                            //如果用户手指移动进SeedBank区域且手上拿着植物或加农炮炮弹，则取消当前植物种植或加农炮发射。
                                            if (nativeGameState() == 7 || nativeIsCobCannonSelected()) {
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventDown);
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventUp);
                                                cobCannonSelected = false;
                                            }
                                            isTouchPressedInPlantsBoard = false;
                                            return true;
                                        }


                                        //如果用户手指移出了Board区域，就不再继续处理。
                                        if (touchX < roofBoardFrameStart || touchX > roofBoardFrameEnd || projectionY < roofBoardFrameTop || projectionY > roofBoardFrameBottom)
                                            break;


                                        //让Board光标跟随用户手指移动。
                                        if (touchX < lastX) {
                                            isCursorMoved = true;
                                            lastX -= roofBoardItemWidth;
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, leftEventDown);
                                        } else if (touchX > lastX + roofBoardItemWidth) {
                                            isCursorMoved = true;
                                            lastX += roofBoardItemWidth;
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, rightEventDown);
                                        }
                                        if (projectionY < lastY) {
                                            isCursorMoved = true;
                                            lastY -= roofBoardItemHeight;
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, upEventDown);
                                        } else if (projectionY > lastY + roofBoardItemHeight) {
                                            isCursorMoved = true;
                                            lastY += roofBoardItemHeight;
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, downEventDown);
                                        }

                                    }
                                    break;
                                case MotionEvent.ACTION_UP:
                                    if (isTouchPressedInSeedBankFrame && !isCursorMoved) {
                                        nativeSetGameState(nativeGameState() == 7 ? 1 : 7);
                                    }
                                    //根据一些条件来确定是否在抬起手指时发送确认键
                                    if (nativeGameState() == 7 && isTouchPressedInPlantsBoard || isCursorMoved && nativeIsCobCannonSelected() || cobCannonSelected) {
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventDown);
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventUp);
                                    }
                                    isCursorMoved = false;
                                    cobCannonSelected = false;
                                    isTouchPressedInSeedBankFrame = false;
                                    isTouchPressedInPlantsBoard = false;
                                    lastX = 0;
                                    lastY = 0;
                                    break;
                            }
                            break;
                        case AWARD_SCREEN:
                            //奖励界面，将点击转换为确定键即可。
                            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventDown);
                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventUp);
                            }
                            break;
                        case CHALLENGE_SCREEN:
                            //小游戏列表界面。包含了滑动和点击的识别
                            switch (motionEvent.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    if (touchX > challengeScreenStart && touchX < challengeScreenEnd && touchY < challengeScreenBottom && touchY > challengeScreenTop) {
                                        isTouchPressedInPlantsBoard = true;
                                        lastY = touchY;
                                        isCursorMoved = false;
                                        nativeSetChallengeScreenScrollTarget(0, (int) ((touchY - challengeScreenTop) / challengeScreenItemHeight));
                                    }

                                    break;
                                case MotionEvent.ACTION_MOVE:
                                    if (isTouchPressedInPlantsBoard) {
                                        if (touchY < lastY) {
                                            isCursorMoved = true;
                                            lastY -= challengeScreenItemHeight;
                                            nativeSetChallengeScreenScrollTarget(1, 0);
                                        } else if (touchY > lastY + challengeScreenItemHeight) {
                                            isCursorMoved = true;
                                            lastY += challengeScreenItemHeight;
                                            nativeSetChallengeScreenScrollTarget(-1, 0);
                                        }
                                    }

                                    break;
                                case MotionEvent.ACTION_UP:
                                    if (isTouchPressedInPlantsBoard && !isCursorMoved) {
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventDown);
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventUp);
                                    }
                                    isTouchPressedInPlantsBoard = false;
                                    break;
                            }
                            break;
                        case STORE_SCREEN:
                            //戴夫商店。目前存在一个问题：戴夫商店中存在戴夫对话时，Hook框架无法识别。在冒险模式中，有三次关卡结束后会进入戴夫商店且存在戴夫对话，目前只能用内置游戏键盘点确认来解决。
                            switch (motionEvent.getAction()) {

                                case MotionEvent.ACTION_DOWN:
                                    if (!nativeCanInteractWithStoreButtons()) {
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventDown);
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventUp);
                                        return true;
                                    }
                                    if (touchX > storeScreenFirstLineStart && touchX < storeScreenFirstLineEnd && touchY < storeScreenFirstLineBottom && touchY > storeScreenFirstLineTop) {
                                        isTouchPressedInPlantsBoard = true;
                                        lastX = touchX;
                                        lastY = touchY;
                                        seedBankPosition = nativeGetSelectedStoreItem();
                                        nativeSetStoreSelectedSlot((int) ((touchX - storeScreenFirstLineStart) / storeScreenItemWidth));
                                    } else if (touchX > storeScreenSecondLineStart && touchX < storeScreenSecondLineEnd && touchY < storeScreenSecondLineBottom && touchY > storeScreenSecondLineTop) {
                                        isTouchPressedInPlantsBoard = true;
                                        lastX = touchX;
                                        lastY = touchY;
                                        seedBankPosition = nativeGetSelectedStoreItem();
                                        nativeSetStoreSelectedSlot((int) ((touchX - storeScreenSecondLineStart) / storeScreenItemWidth) + 4);
                                    } else if (touchY < storeScreenPageSwitchBottom && touchY > storeScreenPageSwitchTop) {
                                        if (touchX > storeScreenPageLeftStart && touchX < storeScreenPageLeftEnd) {
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, lbEventDown);
                                        } else if (touchX > storeScreenPageRightStart && touchX < storeScreenPageRightEnd) {
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, rbEventDown);
                                        }
                                    } else if (touchX > storeScreenBackStart && touchX < storeScreenBackEnd && touchY < storeScreenBackBottom && touchY > storeScreenBackTop) {
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventDown);
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventUp);
                                    }
                                    break;
                                case MotionEvent.ACTION_MOVE:
                                    if (isTouchPressedInPlantsBoard) {
                                        if (Math.abs(touchX - lastX) > storeScreenItemWidth || Math.abs(touchY - lastY) > storeScreenItemHeight) {
                                            isTouchPressedInPlantsBoard = false;
                                        }
                                    }
                                    break;
                                case MotionEvent.ACTION_UP:
                                    if (isTouchPressedInPlantsBoard) {
                                        if (seedBankPosition == nativeGetSelectedStoreItem()) {
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventDown);
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventUp);
                                        }
                                    }
                                    isTouchPressedInPlantsBoard = false;
                                    lastX = 0;
                                    lastY = 0;
                                    break;
                            }
                            break;
                        case VASE_BREAKER:
                            //砸罐子模式的触控方式完全不同。因此单独为砸罐子模式写一个触控逻辑
                            switch (motionEvent.getAction()) {
                                case MotionEvent.ACTION_DOWN:

                                    if (touchY < seedBankFrameBottom && touchY > seedBankFrameTop && touchX > 1045 * viewWidth && touchX < 1150 * viewWidth) {
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, shovelEventDown);
                                        return true;
                                    }

                                    if (touchX > graveBoardFrameStart && touchX < graveBoardFrameEnd && touchY < graveBoardFrameBottom && touchY > graveBoardFrameTop) {
                                        //如果触控落点在Board区域中
                                        isTouchPressedInPlantsBoard = true;
                                        //将光标移动至用户手指落点处

                                        final int boardCursorPositionX = (int) ((touchX - graveBoardFrameStart) / graveBoardItemWidth);
                                        final int diffX = nativeGetCursorPositionX() - boardCursorPositionX;
                                        if (diffX > 0) {
                                            for (int i = 0; i < diffX; i++)
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, leftEventDown);
                                        } else if (diffX < 0) {
                                            for (int i = diffX; i < 0; i++)
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, rightEventDown);
                                        }

                                        final int boardCursorPositionY = (int) ((touchY - graveBoardFrameTop) / graveBoardItemHeight);
                                        final int diffY = nativeGetCursorPositionY() - boardCursorPositionY;
                                        if (diffY > 0) {
                                            for (int i = 0; i < diffY; i++)
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, upEventDown);
                                        } else if (diffY < 0) {
                                            for (int i = diffY; i < 0; i++)
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, downEventDown);
                                        }

                                        lastX = graveBoardFrameStart + boardCursorPositionX * graveBoardItemWidth;
                                        lastY = graveBoardFrameTop + boardCursorPositionY * graveBoardItemHeight;
                                        //移动光标完毕。


                                        //在此时额外发送一个锤子键，这样就可以做到点击砸罐子。
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, hammerEventDown);
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, hammerEventUp);
                                        cobCannonSelected = nativeCursorType() == 2;
                                    }
                                    break;
                                case MotionEvent.ACTION_MOVE:
                                    if (isTouchPressedInPlantsBoard) {
                                        //用户手指落点在Board中。

                                        //如果用户手指移出了Board区域，就不再继续处理。
                                        if (touchX < graveBoardFrameStart || touchX > graveBoardFrameEnd || touchY < graveBoardFrameTop || touchY > graveBoardFrameBottom)
                                            break;

                                        //让Board光标跟随用户手指移动。
                                        if (touchX < lastX) {
                                            lastX -= graveBoardItemWidth;
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, leftEventDown);
                                        } else if (touchX > lastX + graveBoardItemWidth) {
                                            lastX += graveBoardItemWidth;
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, rightEventDown);
                                        }
                                        if (touchY < lastY) {
                                            lastY -= graveBoardItemHeight;
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, upEventDown);
                                        } else if (touchY > lastY + graveBoardItemHeight) {
                                            lastY += graveBoardItemHeight;
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, downEventDown);
                                        }
                                    }
                                    break;
                                case MotionEvent.ACTION_UP:
                                    if (cobCannonSelected) {
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventDown);
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventUp);
                                    }
                                    cobCannonSelected = false;
                                    isTouchPressedInPlantsBoard = false;
                                    lastX = 0;
                                    lastY = 0;
                                    break;
                            }
                            break;
                        case HEAVY_WEAPON:
                            //重型武器模式的触控方式完全不同。因此单独为重型武器模式写一个触控逻辑
                            switch (motionEvent.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    if (touchY < seedBankFrameBottom && touchY > seedBankFrameTop && touchX > seedBankFrameStart) {
                                        seedBankMaxPlants = 6;
                                        seedBankFrameEnd = 1010 * viewWidth;
                                        seedBankFrameItemLength = (seedBankFrameEnd - seedBankFrameStart) / 6;

                                        if (touchX < seedBankFrameEnd) {
                                            //如果用户手指落点位于SeedBank区域中，则设定SeedBank的光标位置为用户手指落点位置。
                                            isTouchPressedInSeedBankFrame = true;
                                            seedBankPosition = (int) ((touchX - seedBankFrameStart) / seedBankFrameItemLength);
                                            nativeSetSeedBankPosition(seedBankPosition);
                                            nativeSetGameState(6);
                                            lastX = seedBankFrameStart + seedBankPosition * seedBankFrameItemLength;
                                        }

                                    } else {
                                        //如果触控落点在Board区域中
                                        isTouchPressedInPlantsBoard = true;
                                        lastX = touchX;

                                        if (nativeGameState() == 6) {
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventDown);
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventUp);
                                        }

                                    }
                                    break;
                                case MotionEvent.ACTION_MOVE:
                                    if (isTouchPressedInPlantsBoard) {
                                        //用户手指落点在Board中。

                                        //让Board光标跟随用户手指移动。
                                        if (touchX < lastX) {
                                            lastX -= graveBoardHeavyWeaponItemWidth;
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, leftEventDown);
                                        } else if (touchX > lastX + graveBoardHeavyWeaponItemWidth) {
                                            lastX += graveBoardHeavyWeaponItemWidth;
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, rightEventDown);
                                        }
                                    } else if (isTouchPressedInSeedBankFrame) {
                                        //用户手指在SeeBank中滑动。我们根据用户手指位置来设定SeedBank的光标位置。
                                        if (touchX < lastX) {
                                            if (seedBankPosition > 0) {
                                                seedBankPosition--;
                                                nativeSetSeedBankPosition(seedBankPosition);
                                                nativeSetGameState(6);
                                                lastX -= seedBankFrameItemLength;
                                            }
                                        } else if (touchX > lastX + seedBankFrameItemLength) {
                                            if (seedBankPosition < 5) {
                                                seedBankPosition++;
                                                nativeSetSeedBankPosition(seedBankPosition);
                                                nativeSetGameState(6);
                                                lastX += seedBankFrameItemLength;
                                            }
                                        }
                                    }
                                    break;
                                case MotionEvent.ACTION_UP:
                                    if (isTouchPressedInSeedBankFrame) {
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventDown);
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventUp);
                                    }
                                    isTouchPressedInSeedBankFrame = false;
                                    isTouchPressedInPlantsBoard = false;
                                    lastX = 0;
                                    break;
                            }
                            break;
                        case BEGHOULED_1:
                            //宝石迷阵模式的触控方式完全不同。因此单独为宝石迷阵模式写一个触控逻辑
                            switch (motionEvent.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    if (touchY < seedBankFrameBottom && touchY > seedBankFrameTop && touchX > seedBankFrameStart) {
                                        seedBankMaxPlants = nativeMaxPlants();
                                        seedBankFrameEnd = 1010 * viewWidth;
                                        seedBankFrameItemLength = (seedBankFrameEnd - seedBankFrameStart) / 6;

                                        if (touchX < seedBankFrameEnd) {
                                            //如果用户手指落点位于SeedBank区域中，则设定SeedBank的光标位置为用户手指落点位置。
                                            isTouchPressedInSeedBankFrame = true;
                                            seedBankPosition = (int) ((touchX - seedBankFrameStart) / seedBankFrameItemLength);
                                            nativeSetSeedBankPosition(seedBankPosition);
                                            nativeSetGameState(6);
                                            lastX = seedBankFrameStart + seedBankPosition * seedBankFrameItemLength;
                                        }


                                    } else if (touchX > graveBoardFrameStart && touchX < graveBoardFrameEnd && touchY < graveBoardFrameBottom && touchY > graveBoardFrameTop) {
                                        //如果触控落点在Board区域中
                                        isTouchPressedInPlantsBoard = true;
                                        if (nativeGameState() == 6) {
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventDown);
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventUp);
                                        }
                                        //将光标移动至用户手指落点处
                                        final int boardCursorPositionX = (int) ((touchX - graveBoardFrameStart) / graveBoardItemWidth);
                                        final int diffX = nativeGetCursorPositionX() - boardCursorPositionX;
                                        if (diffX > 0) {
                                            for (int i = 0; i < diffX; i++)
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, leftEventDown);
                                        } else if (diffX < 0) {
                                            for (int i = diffX; i < 0; i++)
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, rightEventDown);
                                        }

                                        final int boardCursorPositionY = (int) ((touchY - graveBoardFrameTop) / graveBoardItemHeight);
                                        final int diffY = nativeGetCursorPositionY() - boardCursorPositionY;
                                        if (diffY > 0) {
                                            for (int i = 0; i < diffY; i++)
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, upEventDown);
                                        } else if (diffY < 0) {
                                            for (int i = diffY; i < 0; i++)
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, downEventDown);
                                        }

                                        //移动光标完毕。
                                        lastX = graveBoardFrameStart + boardCursorPositionX * graveBoardItemWidth;
                                        lastY = graveBoardFrameTop + boardCursorPositionY * graveBoardItemHeight;

                                    }
                                    break;
                                case MotionEvent.ACTION_MOVE:
                                    if (isTouchPressedInPlantsBoard && !isCursorMoved) {
                                        //用户手指落点在Board中。

                                        //如果用户手指移出了Board区域，就不再继续处理。
                                        if (touchX < graveBoardFrameStart || touchX > graveBoardFrameEnd || touchY < graveBoardFrameTop || touchY > graveBoardFrameBottom)
                                            break;

                                        //让Board光标跟随用户手指移动。
                                        if (touchX < lastX) {
                                            isCursorMoved = true;
                                            //在此时额外发送一个锤子键，这样就可以做到交换。
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, hammerEventDown);
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, hammerEventUp);
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, leftEventDown);
                                        } else if (touchX > lastX + graveBoardItemWidth) {
                                            isCursorMoved = true;
                                            //在此时额外发送一个锤子键，这样就可以做到交换。
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, hammerEventDown);
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, hammerEventUp);
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, rightEventDown);
                                        }
                                        if (touchY < lastY) {
                                            isCursorMoved = true;
                                            //在此时额外发送一个锤子键，这样就可以做到交换。
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, hammerEventDown);
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, hammerEventUp);
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, upEventDown);
                                        } else if (touchY > lastY + graveBoardItemHeight) {
                                            isCursorMoved = true;
                                            //在此时额外发送一个锤子键，这样就可以做到交换。
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, hammerEventDown);
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, hammerEventUp);
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, downEventDown);
                                        }
                                    } else if (isTouchPressedInSeedBankFrame) {
                                        //用户手指在SeeBank中滑动。我们根据用户手指位置来设定SeedBank的光标位置。
                                        if (touchX < lastX) {
                                            if (seedBankPosition > 0) {
                                                seedBankPosition--;
                                                nativeSetSeedBankPosition(seedBankPosition);
                                                nativeSetGameState(6);
                                                lastX -= seedBankFrameItemLength;
                                            }
                                        } else if (touchX > lastX + seedBankFrameItemLength) {
                                            if (seedBankPosition < seedBankMaxPlants - 1) {
                                                seedBankPosition++;
                                                nativeSetSeedBankPosition(seedBankPosition);
                                                nativeSetGameState(6);
                                                lastX += seedBankFrameItemLength;
                                            }
                                        }

                                    }
                                    break;
                                case MotionEvent.ACTION_UP:
                                    if (isTouchPressedInSeedBankFrame && nativeGameState() == 6) {
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventDown);
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventUp);
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventDown);
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventUp);
                                    }
                                    isCursorMoved = false;
                                    isTouchPressedInSeedBankFrame = false;
                                    isTouchPressedInPlantsBoard = false;
                                    lastX = 0;
                                    lastY = 0;
                                    break;
                            }
                            break;
                        case BEGHOULED_2:
                            //宝石迷阵旋风模式的触控方式完全不同。因此单独为宝石迷阵旋风模式写一个触控逻辑
                            switch (motionEvent.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    if (touchY < seedBankFrameBottom && touchY > seedBankFrameTop && touchX > seedBankFrameStart) {
                                        seedBankMaxPlants = nativeMaxPlants();
                                        seedBankFrameEnd = 1010 * viewWidth;
                                        seedBankFrameItemLength = (seedBankFrameEnd - seedBankFrameStart) / 6;

                                        if (touchX < seedBankFrameEnd) {
                                            //如果用户手指落点位于SeedBank区域中，则设定SeedBank的光标位置为用户手指落点位置。
                                            isTouchPressedInSeedBankFrame = true;
                                            seedBankPosition = (int) ((touchX - seedBankFrameStart) / seedBankFrameItemLength);
                                            nativeSetSeedBankPosition(seedBankPosition);
                                            nativeSetGameState(6);
                                            lastX = seedBankFrameStart + seedBankPosition * seedBankFrameItemLength;
                                        }


                                    } else if (touchX > graveBoardFrameStart && touchX < graveBoardFrameEnd && touchY < graveBoardFrameBottom && touchY > graveBoardFrameTop) {
                                        //如果触控落点在Board区域中
                                        isTouchPressedInPlantsBoard = true;
                                        if (nativeGameState() == 6) {
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventDown);
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventUp);
                                        }
                                        //将光标移动至用户手指落点处
                                        final int boardCursorPositionX = (int) (-0.5 + (touchX - graveBoardFrameStart) / graveBoardItemWidth);
                                        final int diffX = nativeGetCursorPositionX() - boardCursorPositionX;
                                        if (diffX > 0) {
                                            for (int i = 0; i < diffX; i++)
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, leftEventDown);
                                        } else if (diffX < 0) {
                                            for (int i = diffX; i < 0; i++)
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, rightEventDown);
                                        }
                                        final int boardCursorPositionY = (int) (-0.5 + (touchY - graveBoardFrameTop) / graveBoardItemHeight);
                                        final int diffY = nativeGetCursorPositionY() - boardCursorPositionY;
                                        if (diffY > 0) {
                                            for (int i = 0; i < diffY; i++)
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, upEventDown);
                                        } else if (diffY < 0) {
                                            for (int i = diffY; i < 0; i++)
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, downEventDown);
                                        }

                                        //移动光标完毕。
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, hammerEventDown);
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, hammerEventUp);
                                    }
                                    break;
                                case MotionEvent.ACTION_MOVE:
                                    if (isTouchPressedInSeedBankFrame) {
                                        //用户手指在SeeBank中滑动。我们根据用户手指位置来设定SeedBank的光标位置。
                                        if (touchX < lastX) {
                                            if (seedBankPosition > 0) {
                                                seedBankPosition--;
                                                nativeSetSeedBankPosition(seedBankPosition);
                                                nativeSetGameState(6);
                                                lastX -= seedBankFrameItemLength;
                                            }

                                        } else if (touchX > lastX + seedBankFrameItemLength) {
                                            if (seedBankPosition < seedBankMaxPlants - 1) {
                                                seedBankPosition++;
                                                nativeSetSeedBankPosition(seedBankPosition);
                                                nativeSetGameState(6);
                                                lastX += seedBankFrameItemLength;
                                            }

                                        }
                                    }
                                    break;
                                case MotionEvent.ACTION_UP:
                                    if (isTouchPressedInSeedBankFrame && nativeGameState() == 6) {
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventDown);
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventUp);
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventDown);
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventUp);
                                    }
                                    isTouchPressedInSeedBankFrame = false;
                                    isTouchPressedInPlantsBoard = false;
                                    lastX = 0;
                                    break;
                            }
                            break;
                        case WHACK_ZOMBIE:
                            //锤僵尸模式的触控方式完全不同。因此单独为锤僵尸模式写一个触控逻辑
                            switch (motionEvent.getAction()) {
                                case MotionEvent.ACTION_DOWN:

                                    if (touchY < seedBankFrameBottom && touchY > seedBankFrameTop && touchX > seedBankFrameStart) {
                                        seedBankMaxPlants = 3;
                                        seedBankFrameEnd = 1010 * viewWidth;
                                        seedBankFrameItemLength = (seedBankFrameEnd - seedBankFrameStart) / 6;
                                        shovelFrameStart = seedBankFrameEnd + 35 * viewWidth;
                                        shovelFrameEnd = shovelFrameStart + 105 * viewWidth;

                                        if (touchX < seedBankFrameEnd) {
                                            //如果用户手指落点位于SeedBank区域中，则设定SeedBank的光标位置为用户手指落点位置。
                                            isTouchPressedInSeedBankFrame = true;
                                            seedBankPosition = (int) ((touchX - seedBankFrameStart) / seedBankFrameItemLength);
                                            if (seedBankPosition != nativeGetSeekBankPosition()) {
                                                isCursorMoved = true;
                                                nativeSetSeedBankPosition(seedBankPosition);
                                            }
                                            lastX = seedBankFrameStart + seedBankPosition * seedBankFrameItemLength;
                                        } else if (touchX > shovelFrameStart && touchX < shovelFrameEnd) {
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, shovelEventDown);
                                            return true;
                                        }


                                    } else if (touchX > graveBoardFrameStart && touchX < graveBoardFrameEnd && touchY < graveBoardFrameBottom && touchY > graveBoardFrameTop) {
                                        //如果触控落点在Board区域中
                                        isTouchPressedInPlantsBoard = true;

                                        final int gameState = nativeGameState();
                                        if (gameState == 6) {
                                            //如果在选植物卡，则取消选取植物卡.
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventDown);
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventUp);
                                        }

                                        //将光标移动至用户手指落点处
                                        final int boardCursorPositionX = (int) ((touchX - graveBoardFrameStart) / graveBoardItemWidth);
                                        final int diffX = nativeGetCursorPositionX() - boardCursorPositionX;
                                        if (diffX > 0) {
                                            for (int i = 0; i < diffX; i++)
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, leftEventDown);
                                        } else if (diffX < 0) {
                                            for (int i = diffX; i < 0; i++)
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, rightEventDown);
                                        }

                                        final int boardCursorPositionY = (int) ((touchY - graveBoardFrameTop) / graveBoardItemHeight);
                                        final int diffY = nativeGetCursorPositionY() - boardCursorPositionY;
                                        if (diffY > 0) {
                                            for (int i = 0; i < diffY; i++)
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, upEventDown);
                                        } else if (diffY < 0) {
                                            for (int i = diffY; i < 0; i++)
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, downEventDown);
                                        }

                                        lastX = graveBoardFrameStart + boardCursorPositionX * graveBoardItemWidth;
                                        lastY = graveBoardFrameTop + boardCursorPositionY * graveBoardItemHeight;
                                        //移动光标完毕。

                                        if (gameState != 6) {
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, hammerEventDown);
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, hammerEventUp);
                                        }
                                    }
                                    break;
                                case MotionEvent.ACTION_MOVE:
                                    if (isTouchPressedInSeedBankFrame) {

                                        //用户触控落点为SeedBank区域。
                                        if (touchX > graveBoardFrameStart && touchX < graveBoardFrameEnd && touchY < graveBoardFrameBottom && touchY > graveBoardFrameTop) {
                                            //在用户触控落点为SeedBank区域的前提下，如果用户将手指移动至Board区域了，则我们结束SeedBank中的植物选取过程，并将Board光标移动至用户手指处。
                                            isTouchPressedInSeedBankFrame = false;
                                            isTouchPressedInPlantsBoard = true;
                                            nativeSetGameState(7);
                                            final int boardCursorPositionX = (int) ((touchX - graveBoardFrameStart) / graveBoardItemWidth);
                                            final int diffX = nativeGetCursorPositionX() - boardCursorPositionX;
                                            if (diffX > 0) {
                                                for (int i = 0; i < diffX; i++)
                                                    NativeInputManager.onKeyInputEventNative(mNativeHandle, null, leftEventDown);
                                            } else if (diffX < 0) {
                                                for (int i = diffX; i < 0; i++)
                                                    NativeInputManager.onKeyInputEventNative(mNativeHandle, null, rightEventDown);
                                            }

                                            final int boardCursorPositionY = (int) ((touchY - graveBoardFrameTop) / graveBoardItemHeight);
                                            final int diffY = nativeGetCursorPositionY() - boardCursorPositionY;
                                            if (diffY > 0) {
                                                for (int i = 0; i < diffY; i++)
                                                    NativeInputManager.onKeyInputEventNative(mNativeHandle, null, upEventDown);
                                            } else if (diffY < 0) {
                                                for (int i = diffY; i < 0; i++)
                                                    NativeInputManager.onKeyInputEventNative(mNativeHandle, null, downEventDown);
                                            }

                                            lastX = graveBoardFrameStart + boardCursorPositionX * graveBoardItemWidth;
                                            lastY = graveBoardFrameTop + boardCursorPositionY * graveBoardItemHeight;
                                        } else {
                                            //用户手指在SeeBank中滑动。我们根据用户手指位置来设定SeedBank的光标位置。
                                            if (touchX < lastX) {
                                                if (seedBankPosition > 0) {
                                                    seedBankPosition--;
                                                    isCursorMoved = true;
                                                    nativeSetSeedBankPosition(seedBankPosition);
                                                    lastX -= seedBankFrameItemLength;
                                                }
                                            } else if (touchX > lastX + seedBankFrameItemLength) {
                                                if (seedBankPosition < 2) {
                                                    seedBankPosition++;
                                                    isCursorMoved = true;
                                                    nativeSetSeedBankPosition(seedBankPosition);
                                                    lastX += seedBankFrameItemLength;
                                                }
                                            }
                                        }
                                    } else if (isTouchPressedInPlantsBoard) {
                                        //用户手指落点在Board中。
                                        if (touchX > seedBankFrameStart && touchX < seedBankFrameEnd && touchY < seedBankFrameBottom && touchY > seedBankFrameTop) {
                                            //如果用户手指移动进SeedBank区域且手上拿着植物或加农炮，则取消当前植物种植或加农炮发射。
                                            if (nativeGameState() == 7) {
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventDown);
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventUp);
                                            }
                                            isTouchPressedInPlantsBoard = false;
                                            return true;
                                        }

                                        //如果用户手指移出了Board区域，就不再继续处理。
                                        if (touchX < graveBoardFrameStart || touchX > graveBoardFrameEnd || touchY < graveBoardFrameTop || touchY > graveBoardFrameBottom)
                                            break;

                                        //让Board光标跟随用户手指移动。
                                        if (touchX < lastX) {
                                            lastX -= graveBoardItemWidth;
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, leftEventDown);
                                        } else if (touchX > lastX + graveBoardItemWidth) {
                                            lastX += graveBoardItemWidth;
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, rightEventDown);
                                        }
                                        if (touchY < lastY) {
                                            lastY -= graveBoardItemHeight;
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, upEventDown);
                                        } else if (touchY > lastY + graveBoardItemHeight) {
                                            lastY += graveBoardItemHeight;
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, downEventDown);
                                        }
                                    }
                                    break;
                                case MotionEvent.ACTION_UP:

                                    if (isTouchPressedInSeedBankFrame && !isCursorMoved) {
                                        nativeSetGameState(nativeGameState() == 7 ? 1 : 7);
                                    }

                                    //根据一些条件来确定是否在抬起手指时发送确认键
                                    if (isTouchPressedInPlantsBoard && nativeGameState() == 7) {
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventDown);
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventUp);
                                    }

                                    isCursorMoved = false;
                                    isTouchPressedInSeedBankFrame = false;
                                    isTouchPressedInPlantsBoard = false;
                                    lastX = 0;
                                    lastY = 0;
                                    break;
                            }
                            break;
                        case RAINING_SEEDS:
                            //种子雨模式的触控方式完全不同。因此单独为种子雨模式写一个触控逻辑
                            switch (motionEvent.getAction()) {
                                case MotionEvent.ACTION_DOWN:

                                    if (touchY < seedBankFrameBottom && touchY > seedBankFrameTop && touchX > 1045 * viewWidth && touchX < 1150 * viewWidth) {
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, shovelEventDown);
                                        return true;
                                    }

                                    if (touchX > poolBoardFrameStart && touchX < poolBoardFrameEnd && touchY < poolBoardFrameBottom && touchY > poolBoardFrameTop) {
                                        //如果触控落点在Board区域中
                                        isTouchPressedInPlantsBoard = true;
                                        //将光标移动至用户手指落点处

                                        final int boardCursorPositionX = (int) ((touchX - poolBoardFrameStart) / poolBoardItemWidth);
                                        final int diffX = nativeGetCursorPositionX() - boardCursorPositionX;
                                        if (diffX > 0) {
                                            for (int i = 0; i < diffX; i++)
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, leftEventDown);
                                        } else if (diffX < 0) {
                                            for (int i = diffX; i < 0; i++)
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, rightEventDown);
                                        }

                                        final int boardCursorPositionY = (int) ((touchY - poolBoardFrameTop) / poolBoardItemHeight);
                                        final int diffY = nativeGetCursorPositionY() - boardCursorPositionY;
                                        if (diffY > 0) {
                                            for (int i = 0; i < diffY; i++)
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, upEventDown);
                                        } else if (diffY < 0) {
                                            for (int i = diffY; i < 0; i++)
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, downEventDown);
                                        }

                                        lastX = poolBoardFrameStart + boardCursorPositionX * poolBoardItemWidth;
                                        lastY = poolBoardFrameTop + boardCursorPositionY * poolBoardItemHeight;
                                        //移动光标完毕。
                                        cobCannonSelected = nativeCursorType() == 2;
                                    }
                                    break;
                                case MotionEvent.ACTION_MOVE:
                                    if (isTouchPressedInPlantsBoard) {
                                        //用户手指落点在Board中。

                                        //如果用户手指移出了Board区域，就退掉手中的种子，且不再处理。
                                        if (touchX < poolBoardFrameStart || touchX > poolBoardFrameEnd || touchY < poolBoardFrameTop || touchY > poolBoardFrameBottom) {
                                            if (nativeCursorType() == 2) {
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventDown);
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventUp);
                                            }
                                            break;
                                        }

                                        //让Board光标跟随用户手指移动。
                                        if (touchX < lastX) {
                                            lastX -= poolBoardItemWidth;
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, leftEventDown);
                                        } else if (touchX > lastX + poolBoardItemWidth) {
                                            lastX += poolBoardItemWidth;
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, rightEventDown);
                                        }
                                        if (touchY < lastY) {
                                            lastY -= poolBoardItemHeight;
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, upEventDown);
                                        } else if (touchY > lastY + poolBoardItemHeight) {
                                            lastY += poolBoardItemHeight;
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, downEventDown);
                                        }
                                    }
                                    break;
                                case MotionEvent.ACTION_UP:
                                    if (cobCannonSelected) {
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventDown);
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventUp);
                                    }
                                    cobCannonSelected = false;
                                    isTouchPressedInPlantsBoard = false;
                                    lastX = 0;
                                    lastY = 0;
                                    break;
                            }
                            break;
                        case SLOT_MACHINE:
                            //老虎机模式的触控方式完全不同。因此单独为老虎机模式写一个触控逻辑
                            switch (motionEvent.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    if (touchX > seedBankFrameStart && touchY < seedBankFrameBottom && touchY > seedBankFrameTop) {

                                        if (touchX < 1260 * viewWidth) {
                                            //在此时额外发送一个锤子键，这样就可以做到点击启动老虎机。
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, hammerEventDown);
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, hammerEventUp);
                                            return true;
                                        } else if (touchX < 1365 * viewWidth) {
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, shovelEventDown);
                                            return true;
                                        }
                                    }

                                    if (touchX > graveBoardFrameStart && touchX < graveBoardFrameEnd && touchY < graveBoardFrameBottom && touchY > graveBoardFrameTop) {
                                        //如果触控落点在Board区域中
                                        isTouchPressedInPlantsBoard = true;

                                        if (nativeGameState() == 6) {
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventDown);
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventUp);
                                        }

                                        //将光标移动至用户手指落点处
                                        final int boardCursorPositionX = (int) ((touchX - graveBoardFrameStart) / graveBoardItemWidth);
                                        final int diffX = nativeGetCursorPositionX() - boardCursorPositionX;
                                        if (diffX > 0) {
                                            for (int i = 0; i < diffX; i++)
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, leftEventDown);
                                        } else if (diffX < 0) {
                                            for (int i = diffX; i < 0; i++)
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, rightEventDown);
                                        }

                                        final int boardCursorPositionY = (int) ((touchY - graveBoardFrameTop) / graveBoardItemHeight);
                                        final int diffY = nativeGetCursorPositionY() - boardCursorPositionY;
                                        if (diffY > 0) {
                                            for (int i = 0; i < diffY; i++)
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, upEventDown);
                                        } else if (diffY < 0) {
                                            for (int i = diffY; i < 0; i++)
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, downEventDown);
                                        }

                                        lastX = graveBoardFrameStart + boardCursorPositionX * graveBoardItemWidth;
                                        lastY = graveBoardFrameTop + boardCursorPositionY * graveBoardItemHeight;
                                        //移动光标完毕。

                                        cobCannonSelected = nativeCursorType() == 2;
                                    }
                                    break;
                                case MotionEvent.ACTION_MOVE:
                                    if (isTouchPressedInPlantsBoard) {
                                        //用户手指落点在Board中。

                                        //如果用户手指移出了Board区域，就退掉手上的种子，不再继续处理。
                                        if (touchX < graveBoardFrameStart || touchX > graveBoardFrameEnd || touchY < graveBoardFrameTop || touchY > graveBoardFrameBottom) {
                                            break;
                                        }

                                        //让Board光标跟随用户手指移动。
                                        if (touchX < lastX) {
                                            lastX -= graveBoardItemWidth;
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, leftEventDown);
                                        } else if (touchX > lastX + graveBoardItemWidth) {
                                            lastX += graveBoardItemWidth;
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, rightEventDown);
                                        }
                                        if (touchY < lastY) {
                                            lastY -= graveBoardItemHeight;
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, upEventDown);
                                        } else if (touchY > lastY + graveBoardItemHeight) {
                                            lastY += graveBoardItemHeight;
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, downEventDown);
                                        }
                                    }
                                    break;
                                case MotionEvent.ACTION_UP:
                                    if (cobCannonSelected) {
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventDown);
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventUp);
                                    }
                                    cobCannonSelected = false;
                                    isTouchPressedInPlantsBoard = false;
                                    lastX = 0;
                                    lastY = 0;
                                    break;
                            }
                            break;
                        case ZOMBI_QUARIUM:
                            //僵尸水族馆模式的触控方式完全不同。因此单独为僵尸水族馆模式写一个触控逻辑
                            switch (motionEvent.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    if (touchX > seedBankFrameStart && touchY < seedBankFrameBottom && touchY > seedBankFrameTop) {
                                        seedBankMaxPlants = 2;
                                        seedBankFrameEnd = 1010 * viewWidth;
                                        seedBankFrameItemLength = (seedBankFrameEnd - seedBankFrameStart) / 6;

                                        if (touchX < seedBankFrameEnd) {
                                            //如果用户手指落点位于SeedBank区域中，则设定SeedBank的光标位置为用户手指落点位置。
                                            isTouchPressedInSeedBankFrame = true;
                                            seedBankPosition = (int) ((touchX - seedBankFrameStart) / seedBankFrameItemLength);
                                            nativeSetSeedBankPosition(seedBankPosition);
                                            nativeSetGameState(6);
                                            lastX = seedBankFrameStart + seedBankPosition * seedBankFrameItemLength;
                                        }


                                    } else if (touchX > graveBoardFrameStart && touchX < graveBoardFrameEnd && touchY < graveBoardFrameBottom && touchY > graveBoardFrameTop) {
                                        //如果触控落点在Board区域中
                                        isTouchPressedInPlantsBoard = true;
                                        if (nativeGameState() == 6) {
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventDown);
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventUp);
                                        }
                                        //将光标移动至用户手指落点处
                                        final int boardCursorPositionX = (int) ((touchX - graveBoardFrameStart) / graveBoardItemWidth);
                                        final int diffX = nativeGetCursorPositionX() - boardCursorPositionX;
                                        if (diffX > 0) {
                                            for (int i = 0; i < diffX; i++)
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, leftEventDown);
                                        } else if (diffX < 0) {
                                            for (int i = diffX; i < 0; i++)
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, rightEventDown);
                                        }

                                        final int boardCursorPositionY = (int) ((touchY - graveBoardFrameTop) / graveBoardItemHeight);
                                        final int diffY = nativeGetCursorPositionY() - boardCursorPositionY;
                                        if (diffY > 0) {
                                            for (int i = 0; i < diffY; i++)
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, upEventDown);
                                        } else if (diffY < 0) {
                                            for (int i = diffY; i < 0; i++)
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, downEventDown);
                                        }

                                        //移动光标完毕。
                                        lastX = graveBoardFrameStart + boardCursorPositionX * graveBoardItemWidth;
                                        lastY = graveBoardFrameTop + boardCursorPositionY * graveBoardItemHeight;

                                        //在此时额外发送一个锤子键，这样就可以做到点击砸罐子。
                                        if (nativeGameState() != 6) {
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, hammerEventDown);
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, hammerEventUp);
                                        }

                                    }
                                    break;
                                case MotionEvent.ACTION_MOVE:
                                    if (isTouchPressedInPlantsBoard && !isCursorMoved) {
                                        //用户手指落点在Board中。

                                        //如果用户手指移出了Board区域，就不再继续处理。
                                        if (touchX < graveBoardFrameStart || touchX > graveBoardFrameEnd || touchY < graveBoardFrameTop || touchY > graveBoardFrameBottom)
                                            break;

                                        //让Board光标跟随用户手指移动。
                                        if (touchX < lastX) {
                                            lastX -= graveBoardItemWidth;
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, leftEventDown);
                                        } else if (touchX > lastX + graveBoardItemWidth) {
                                            lastX += graveBoardItemWidth;
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, rightEventDown);
                                        }
                                        if (touchY < lastY) {
                                            lastY -= graveBoardItemHeight;
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, upEventDown);
                                        } else if (touchY > lastY + graveBoardItemHeight) {
                                            lastY += graveBoardItemHeight;
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, downEventDown);
                                        }
                                    } else if (isTouchPressedInSeedBankFrame) {
                                        //用户手指在SeeBank中滑动。我们根据用户手指位置来设定SeedBank的光标位置。
                                        if (touchX < lastX) {
                                            if (seedBankPosition > 0) {
                                                seedBankPosition--;
                                                nativeSetSeedBankPosition(seedBankPosition);
                                                nativeSetGameState(6);
                                                lastX -= seedBankFrameItemLength;
                                            }

                                        } else if (touchX > lastX + seedBankFrameItemLength) {
                                            if (seedBankPosition < 1) {
                                                seedBankPosition++;
                                                nativeSetSeedBankPosition(seedBankPosition);
                                                nativeSetGameState(6);
                                                lastX += seedBankFrameItemLength;
                                            }

                                        }

                                    }
                                    break;
                                case MotionEvent.ACTION_UP:
                                    if (isTouchPressedInSeedBankFrame && nativeGameState() == 6) {
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventDown);
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventUp);
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventDown);
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventUp);
                                    }
                                    isTouchPressedInSeedBankFrame = false;
                                    isTouchPressedInPlantsBoard = false;
                                    lastX = 0;
                                    lastY = 0;
                                    break;
                            }
                            break;
                        case ZEN_GARDEN:
                        case QUARIUM_GARDEN:
                        case MUSHROOM_GARDEN:
                        case WISDOM_TREE:
                            switch (motionEvent.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    if (touchX > zenGardenToolBarStart && touchX < zenGardenToolBarEnd && touchY > zenGardenToolBarTop && touchY < zenGardenToolBarBottom) {
                                        isTouchPressedInSeedBankFrame = true;
                                        nativeSetZenGardenTool((int) ((touchX - zenGardenToolBarStart) / zenGardenToolBarItemWidth));
                                    } else if (touchX > boardCursorStart && touchX < boardCursorEnd && touchY > boardCursorTop && touchY < boardCursorBottom) {
                                        isTouchPressedInPlantsBoard = true;
                                        isGoldWateringCan = nativeCursorType() == 9 && nativeHasGoldenWaterCan();
                                        nativeSetBoardPositionByXY((touchX - boardCursorStart) * boardCursorAspectRatioX + (isGoldWateringCan ? 20 : 40), (touchY - boardCursorTop) * boardCursorAspectRatioY + (isGoldWateringCan ? 40 : 80));
                                    }
                                    break;
                                case MotionEvent.ACTION_MOVE:
                                    if (isTouchPressedInPlantsBoard) {
                                        nativeSetBoardPositionByXY((touchX - boardCursorStart) * boardCursorAspectRatioX + (isGoldWateringCan ? 20 : 40), (touchY - boardCursorTop) * boardCursorAspectRatioY + (isGoldWateringCan ? 40 : 80));
                                    }
                                    break;
                                case MotionEvent.ACTION_UP:
                                    if (isTouchPressedInSeedBankFrame) {
                                        final int cursorType = nativeCursorType();
                                        if (cursorType >= 18 && cursorType <= 21) {
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventDown);
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventUp);
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventDown);
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventUp);
                                        }
                                    } else if (isTouchPressedInPlantsBoard) {
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventDown);
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventUp);
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventDown);
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventUp);
                                    }
                                    isGoldWateringCan = false;
                                    isTouchPressedInSeedBankFrame = false;
                                    isTouchPressedInPlantsBoard = false;
                                    break;
                            }
                            break;
                        case GRAVE_BOARD_SMOOTH:
                            switch (motionEvent.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    if (touchY < seedBankFrameBottom && touchY > seedBankFrameTop && touchX > seedBankFrameStart) {
                                        seedBankMaxPlants = nativeMaxPlants();
                                        switch (seedBankMaxPlants) {
                                            case 7:
                                                seedBankFrameEnd = 1100 * viewWidth;
                                                seedBankFrameItemLength = (seedBankFrameEnd - seedBankFrameStart) / 7;
                                                break;
                                            case 8:
                                                seedBankFrameEnd = 1124 * viewWidth;
                                                seedBankFrameItemLength = (seedBankFrameEnd - seedBankFrameStart) / 8;
                                                break;
                                            case 9:
                                                seedBankFrameEnd = 1178 * viewWidth;
                                                seedBankFrameItemLength = (seedBankFrameEnd - seedBankFrameStart) / 9;
                                                break;
                                            case 10:
                                                seedBankFrameEnd = 1238 * viewWidth;
                                                seedBankFrameItemLength = (seedBankFrameEnd - seedBankFrameStart) / 10;
                                                break;
                                            default:
                                                seedBankFrameEnd = 1010 * viewWidth;
                                                seedBankFrameItemLength = (seedBankFrameEnd - seedBankFrameStart) / 6;
                                                break;
                                        }
                                        if (nativeIsShovelEnabled()) {
                                            shovelFrameStart = seedBankFrameEnd + 35 * viewWidth;
                                            shovelFrameEnd = shovelFrameStart + 105 * viewWidth;
                                        } else {
                                            shovelFrameStart = 0;
                                            shovelFrameEnd = 0;
                                        }
                                        if (touchX < seedBankFrameEnd) {
                                            //如果用户手指落点位于SeedBank区域中，则设定SeedBank的光标位置为用户手指落点位置。
                                            isTouchPressedInSeedBankFrame = true;
                                            seedBankPosition = (int) ((touchX - seedBankFrameStart) / seedBankFrameItemLength);
                                            if (seedBankPosition != nativeGetSeekBankPosition()) {
                                                isCursorMoved = true;
                                                nativeSetSeedBankPosition(seedBankPosition);
                                            }
                                            lastX = seedBankFrameStart + seedBankPosition * seedBankFrameItemLength;
                                            if (nativeIsCobCannonSelected()) {
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventDown);
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventUp);
                                            }
                                        } else if (touchX > shovelFrameStart && touchX < shovelFrameEnd) {
//                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, shovelEventDown);
                                            isTouchPressedInShovelFrame = true;
                                        }


                                    } else if (touchX > boardCursorStart && touchX < boardCursorEnd && touchY > boardCursorTop && touchY < boardCursorBottom) {
                                        //如果触控落点在Board区域中
                                        if (nativeGameState() == 6) {
                                            //如果在选植物卡，则取消选取植物卡.
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventDown);
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventUp);
                                        }

                                        //将光标移动至用户手指落点处
                                        isTouchPressedInPlantsBoard = true;
                                        final int boardCursorPositionX = (int) ((touchX - graveBoardFrameStart) / graveBoardItemWidth);
                                        final int boardCursorPositionY = (int) ((touchY - graveBoardFrameTop) / graveBoardItemHeight);

                                        nativeSetBoardPositionByXY((touchX - boardCursorStart) * boardCursorAspectRatioX + 40, (touchY - boardCursorTop) * boardCursorAspectRatioY + 80);
                                        //移动光标完毕。

                                        //检查当前光标处是否为一个充能完毕的加农炮。
                                        cobCannonSelected = nativeIsCobCannonSelected();
                                        if (!cobCannonSelected && nativeGameState() != 6) {
                                            nativeRequestValidCobCanonCheck(boardCursorPositionX, boardCursorPositionY);
                                        }
                                        shovelPicked = nativeCursorType() == 6;
                                    }

                                    break;
                                case MotionEvent.ACTION_MOVE:
                                    if (isTouchPressedInSeedBankFrame) {

                                        //用户触控落点为SeedBank区域。
                                        if (touchX > graveBoardFrameStart && touchX < graveBoardFrameEnd && touchY < graveBoardFrameBottom && touchY > graveBoardFrameTop) {
                                            //在用户触控落点为SeedBank区域的前提下，如果用户将手指移动至Board区域了，则我们结束SeedBank中的植物选取过程，并将Board光标移动至用户手指处。
                                            isTouchPressedInSeedBankFrame = false;
                                            isTouchPressedInPlantsBoard = true;
                                            nativeSetGameState(7);
                                            nativeSetBoardPositionByXY((touchX - boardCursorStart) * boardCursorAspectRatioX + 40, (touchY - boardCursorTop) * boardCursorAspectRatioY + 80);
                                        } else {

                                            //用户手指在SeeBank中滑动。我们根据用户手指位置来设定SeedBank的光标位置。
                                            if (touchX < lastX) {
                                                if (seedBankPosition > 0) {
                                                    seedBankPosition--;
                                                    isCursorMoved = true;
                                                    nativeSetSeedBankPosition(seedBankPosition);
                                                    lastX -= seedBankFrameItemLength;
                                                }
                                            } else if (touchX > lastX + seedBankFrameItemLength) {
                                                if (seedBankPosition < seedBankMaxPlants - 1) {
                                                    seedBankPosition++;
                                                    isCursorMoved = true;
                                                    nativeSetSeedBankPosition(seedBankPosition);
                                                    lastX += seedBankFrameItemLength;
                                                }
                                            }
                                        }
                                    } else if (isTouchPressedInPlantsBoard) {
                                        //用户手指落点在Board中。
                                        if (touchY < seedBankFrameBottom && touchY > seedBankFrameTop && touchX > seedBankFrameStart && touchX < seedBankFrameEnd) {
                                            //如果用户手指移动进SeedBank区域且手上拿着植物或加农炮，则取消当前植物种植或加农炮发射。
                                            if (nativeGameState() == 7 || nativeIsCobCannonSelected()) {
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventDown);
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventUp);
                                                cobCannonSelected = false;
                                            }
                                            isTouchPressedInPlantsBoard = false;
                                            return true;
                                        }


                                        //让Board光标跟随用户手指移动。
                                        nativeSetBoardPositionByXY((touchX - boardCursorStart) * boardCursorAspectRatioX + 40, (touchY - boardCursorTop) * boardCursorAspectRatioY + 80);
                                        if (touchX < lastX) {
                                            isCursorMoved = true;
                                        } else if (touchX > lastX + graveBoardItemWidth) {
                                            isCursorMoved = true;
                                        }
                                        if (touchY < lastY) {
                                            isCursorMoved = true;
                                        } else if (touchY > lastY + graveBoardItemHeight) {
                                            isCursorMoved = true;
                                        }

                                    } else if (isTouchPressedInShovelFrame) {
                                        if (touchY > graveBoardFrameTop) {
                                            isTouchPressedInShovelFrame = false;
                                            isTouchPressedInPlantsBoard = true;
                                            nativePickUpShovel(true);
                                            nativeSetBoardPositionByXY((touchX - boardCursorStart) * boardCursorAspectRatioX + 40, (touchY - boardCursorTop) * boardCursorAspectRatioY + 80);
                                            shovelPicked = true;
                                        }
                                    }
                                    break;
                                case MotionEvent.ACTION_UP:
                                    if (isTouchPressedInSeedBankFrame && !isCursorMoved) {
                                        nativeSetGameState(nativeGameState() == 7 ? 1 : 7);
                                    }

                                    if (isTouchPressedInShovelFrame) {
                                        nativePickUpShovel(nativeCursorType() != 6);
                                    } else if (isTouchPressedInPlantsBoard && shovelPicked) {
                                        nativeShovelDown();
                                    } else if (nativeGameState() == 7 && isTouchPressedInPlantsBoard || isCursorMoved && nativeIsCobCannonSelected() || cobCannonSelected) {
                                        //根据一些条件来确定是否在抬起手指时发送确认键
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventDown);
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventUp);
                                    }
                                    isCursorMoved = false;
                                    cobCannonSelected = false;
                                    isTouchPressedInShovelFrame = false;
                                    isTouchPressedInSeedBankFrame = false;
                                    isTouchPressedInPlantsBoard = false;
                                    lastX = 0;
                                    lastY = 0;
                                    break;
                            }
                            break;
                        case POOL_BOARD_SMOOTH:
                            switch (motionEvent.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    if (touchY < seedBankFrameBottom && touchY > seedBankFrameTop && touchX > seedBankFrameStart) {
                                        seedBankMaxPlants = nativeMaxPlants();
                                        switch (seedBankMaxPlants) {
                                            case 7:
                                                seedBankFrameEnd = 1100 * viewWidth;
                                                seedBankFrameItemLength = (seedBankFrameEnd - seedBankFrameStart) / 7;
                                                break;
                                            case 8:
                                                seedBankFrameEnd = 1124 * viewWidth;
                                                seedBankFrameItemLength = (seedBankFrameEnd - seedBankFrameStart) / 8;
                                                break;
                                            case 9:
                                                seedBankFrameEnd = 1178 * viewWidth;
                                                seedBankFrameItemLength = (seedBankFrameEnd - seedBankFrameStart) / 9;
                                                break;
                                            case 10:
                                                seedBankFrameEnd = 1238 * viewWidth;
                                                seedBankFrameItemLength = (seedBankFrameEnd - seedBankFrameStart) / 10;
                                                break;
                                            default:
                                                seedBankFrameEnd = 1010 * viewWidth;
                                                seedBankFrameItemLength = (seedBankFrameEnd - seedBankFrameStart) / 6;
                                                break;
                                        }

                                        if (nativeIsShovelEnabled()) {
                                            shovelFrameStart = seedBankFrameEnd + 35 * viewWidth;
                                            shovelFrameEnd = shovelFrameStart + 105 * viewWidth;
                                        } else {
                                            shovelFrameStart = 0;
                                            shovelFrameEnd = 0;
                                        }


                                        if (touchX < seedBankFrameEnd) {
                                            //如果用户手指落点位于SeedBank区域中，则设定SeedBank的光标位置为用户手指落点位置。
                                            isTouchPressedInSeedBankFrame = true;
                                            seedBankPosition = (int) ((touchX - seedBankFrameStart) / seedBankFrameItemLength);
                                            if (seedBankPosition != nativeGetSeekBankPosition()) {
                                                isCursorMoved = true;
                                                nativeSetSeedBankPosition(seedBankPosition);
                                            }
                                            lastX = seedBankFrameStart + seedBankPosition * seedBankFrameItemLength;
                                            if (nativeIsCobCannonSelected()) {
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventDown);
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventUp);
                                            }
                                        } else if (touchX > shovelFrameStart && touchX < shovelFrameEnd) {
                                            // NativeInputManager.onKeyInputEventNative(mNativeHandle, null, shovelEventDown);
                                            isTouchPressedInShovelFrame = true;
                                        }


                                    } else if (touchX > boardCursorStart && touchX < boardCursorEnd && touchY > boardCursorTop && touchY < boardCursorBottom) {
                                        //如果触控落点在Board区域中
                                        if (nativeGameState() == 6) {
                                            //如果在选植物卡，则取消选取植物卡.
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventDown);
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventUp);
                                        }

                                        //将光标移动至用户手指落点处
                                        isTouchPressedInPlantsBoard = true;
                                        final int boardCursorPositionX = (int) ((touchX - poolBoardFrameStart) / poolBoardItemWidth);
                                        final int boardCursorPositionY = (int) ((touchY - poolBoardFrameTop) / poolBoardItemHeight);

                                        nativeSetBoardPositionByXY((touchX - boardCursorStart) * boardCursorAspectRatioX + 40, (touchY - boardCursorTop) * boardCursorAspectRatioY + 80);
                                        //移动光标完毕。

                                        //检查当前光标处是否为一个充能完毕的加农炮。
                                        cobCannonSelected = nativeIsCobCannonSelected();
                                        if (!cobCannonSelected && nativeGameState() != 6) {
                                            nativeRequestValidCobCanonCheck(boardCursorPositionX, boardCursorPositionY);
                                        }
                                        shovelPicked = nativeCursorType() == 6;
                                    }
                                    break;
                                case MotionEvent.ACTION_MOVE:
                                    if (isTouchPressedInSeedBankFrame) {

                                        //用户触控落点为SeedBank区域。
                                        if (touchX > poolBoardFrameStart && touchX < poolBoardFrameEnd && touchY < poolBoardFrameBottom && touchY > poolBoardFrameTop) {
                                            //在用户触控落点为SeedBank区域的前提下，如果用户将手指移动至Board区域了，则我们结束SeedBank中的植物选取过程，并将Board光标移动至用户手指处。
                                            isTouchPressedInSeedBankFrame = false;
                                            isTouchPressedInPlantsBoard = true;
                                            nativeSetGameState(7);
                                            nativeSetBoardPositionByXY((touchX - boardCursorStart) * boardCursorAspectRatioX + 40, (touchY - boardCursorTop) * boardCursorAspectRatioY + 80);

                                        } else {

                                            //用户手指在SeeBank中滑动。我们根据用户手指位置来设定SeedBank的光标位置。
                                            if (touchX < lastX) {
                                                if (seedBankPosition > 0) {
                                                    seedBankPosition--;
                                                    isCursorMoved = true;
                                                    nativeSetSeedBankPosition(seedBankPosition);
                                                    lastX -= seedBankFrameItemLength;
                                                }
                                            } else if (touchX > lastX + seedBankFrameItemLength) {
                                                if (seedBankPosition < seedBankMaxPlants - 1) {
                                                    seedBankPosition++;
                                                    isCursorMoved = true;
                                                    nativeSetSeedBankPosition(seedBankPosition);
                                                    lastX += seedBankFrameItemLength;
                                                }
                                            }
                                        }
                                    } else if (isTouchPressedInPlantsBoard) {
                                        //用户手指落点在Board中。
                                        if (touchY < seedBankFrameBottom && touchY > seedBankFrameTop && touchX > seedBankFrameStart && touchX < seedBankFrameEnd) {
                                            //如果用户手指移动进SeedBank区域且手上拿着植物或加农炮，则取消当前植物种植或加农炮发射。
                                            if (nativeGameState() == 7 || nativeIsCobCannonSelected()) {
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventDown);
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventUp);
                                                cobCannonSelected = false;
                                            }
                                            isTouchPressedInPlantsBoard = false;
                                            return true;
                                        }


                                        //让Board光标跟随用户手指移动。
                                        nativeSetBoardPositionByXY((touchX - boardCursorStart) * boardCursorAspectRatioX + 40, (touchY - boardCursorTop) * boardCursorAspectRatioY + 80);
                                        if (touchX < lastX) {
                                            isCursorMoved = true;
                                        } else if (touchX > lastX + poolBoardItemWidth) {
                                            isCursorMoved = true;
                                        }
                                        if (touchY < lastY) {
                                            isCursorMoved = true;
                                        } else if (touchY > lastY + poolBoardItemHeight) {
                                            isCursorMoved = true;
                                        }

                                    } else if (isTouchPressedInShovelFrame) {
                                        if (touchY > poolBoardFrameTop) {
                                            isTouchPressedInShovelFrame = false;
                                            isTouchPressedInPlantsBoard = true;
                                            nativePickUpShovel(true);
                                            nativeSetBoardPositionByXY((touchX - boardCursorStart) * boardCursorAspectRatioX + 40, (touchY - boardCursorTop) * boardCursorAspectRatioY + 80);
                                            shovelPicked = true;
                                        }
                                    }
                                    break;
                                case MotionEvent.ACTION_UP:
                                    if (isTouchPressedInSeedBankFrame && !isCursorMoved) {
                                        nativeSetGameState(nativeGameState() == 7 ? 1 : 7);
                                    }
                                    if (isTouchPressedInShovelFrame) {
                                        nativePickUpShovel(nativeCursorType() != 6);
                                    } else if (isTouchPressedInPlantsBoard && shovelPicked) {
                                        nativeShovelDown();
                                    } else if (nativeGameState() == 7 && isTouchPressedInPlantsBoard || isCursorMoved && nativeIsCobCannonSelected() || cobCannonSelected) {
                                        //根据一些条件来确定是否在抬起手指时发送确认键
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventDown);
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventUp);
                                    }
                                    isCursorMoved = false;
                                    cobCannonSelected = false;
                                    isTouchPressedInShovelFrame = false;
                                    isTouchPressedInSeedBankFrame = false;
                                    isTouchPressedInPlantsBoard = false;
                                    lastX = 0;
                                    lastY = 0;
                                    break;
                            }
                            break;
                        case ROOF_BOARD_SMOOTH:
                            final float projectionY1 = touchX < roofBoardFrameTurning ? touchY + roofBoardFrameSlope * (roofBoardFrameTurning - touchX) - roofBoardFrameAddon : touchY;
                            switch (motionEvent.getAction()) {
                                case MotionEvent.ACTION_DOWN:

                                    if (touchY < seedBankFrameBottom && touchY > seedBankFrameTop && touchX > seedBankFrameStart) {
                                        seedBankMaxPlants = nativeMaxPlants();
                                        switch (seedBankMaxPlants) {
                                            case 7:
                                                seedBankFrameEnd = 1100 * viewWidth;
                                                seedBankFrameItemLength = (seedBankFrameEnd - seedBankFrameStart) / 7;
                                                break;
                                            case 8:
                                                seedBankFrameEnd = 1124 * viewWidth;
                                                seedBankFrameItemLength = (seedBankFrameEnd - seedBankFrameStart) / 8;
                                                break;
                                            case 9:
                                                seedBankFrameEnd = 1178 * viewWidth;
                                                seedBankFrameItemLength = (seedBankFrameEnd - seedBankFrameStart) / 9;
                                                break;
                                            case 10:
                                                seedBankFrameEnd = 1238 * viewWidth;
                                                seedBankFrameItemLength = (seedBankFrameEnd - seedBankFrameStart) / 10;
                                                break;
                                            default:
                                                seedBankFrameEnd = 1010 * viewWidth;
                                                seedBankFrameItemLength = (seedBankFrameEnd - seedBankFrameStart) / 6;
                                                break;
                                        }

                                        if (nativeIsShovelEnabled()) {
                                            shovelFrameStart = seedBankFrameEnd + 35 * viewWidth;
                                            shovelFrameEnd = shovelFrameStart + 105 * viewWidth;
                                        } else {
                                            shovelFrameStart = 0;
                                            shovelFrameEnd = 0;
                                        }


                                        if (touchX < seedBankFrameEnd) {
                                            //如果用户手指落点位于SeedBank区域中，则设定SeedBank的光标位置为用户手指落点位置。
                                            isTouchPressedInSeedBankFrame = true;
                                            seedBankPosition = (int) ((touchX - seedBankFrameStart) / seedBankFrameItemLength);
                                            if (seedBankPosition != nativeGetSeekBankPosition()) {
                                                isCursorMoved = true;
                                                nativeSetSeedBankPosition(seedBankPosition);
                                            }
                                            lastX = seedBankFrameStart + seedBankPosition * seedBankFrameItemLength;
                                            if (nativeIsCobCannonSelected()) {
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventDown);
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventUp);
                                            }
                                        } else if (touchX > shovelFrameStart && touchX < shovelFrameEnd) {
                                            //NativeInputManager.onKeyInputEventNative(mNativeHandle, null, shovelEventDown);
                                            isTouchPressedInShovelFrame = true;
                                        }


                                    } else if (touchX > boardCursorStart && touchX < boardCursorEnd && touchY > boardCursorTop && touchY < boardCursorBottom) {
                                        //如果触控落点在Board区域中
                                        if (nativeGameState() == 6) {
                                            //如果在选植物卡，则取消选取植物卡.
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventDown);
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventUp);
                                        }

                                        //将光标移动至用户手指落点处
                                        isTouchPressedInPlantsBoard = true;
                                        final int boardCursorPositionX = (int) ((touchX - roofBoardFrameStart) / roofBoardItemWidth);
                                        final int boardCursorPositionY = (int) ((projectionY1 - roofBoardFrameTop) / roofBoardItemHeight);

                                        nativeSetBoardPositionByXY((touchX - boardCursorStart) * boardCursorAspectRatioX + 40, (touchY - boardCursorTop) * boardCursorAspectRatioY + 80);
                                        //移动光标完毕。

                                        //检查当前光标处是否为一个充能完毕的加农炮。
                                        cobCannonSelected = nativeIsCobCannonSelected();
                                        if (!cobCannonSelected && nativeGameState() != 6) {
                                            nativeRequestValidCobCanonCheck(boardCursorPositionX, boardCursorPositionY);
                                        }
                                        shovelPicked = nativeCursorType() == 6;
                                    }
                                    break;
                                case MotionEvent.ACTION_MOVE:
                                    if (isTouchPressedInSeedBankFrame) {

                                        //用户触控落点为SeedBank区域。
                                        if (touchX > roofBoardFrameStart && touchX < roofBoardFrameEnd && projectionY1 < roofBoardFrameBottom && projectionY1 > roofBoardFrameTop) {
                                            //在用户触控落点为SeedBank区域的前提下，如果用户将手指移动至Board区域了，则我们结束SeedBank中的植物选取过程，并将Board光标移动至用户手指处。
                                            isTouchPressedInSeedBankFrame = false;
                                            isTouchPressedInPlantsBoard = true;
                                            nativeSetGameState(7);
                                            nativeSetBoardPositionByXY((touchX - boardCursorStart) * boardCursorAspectRatioX + 40, (touchY - boardCursorTop) * boardCursorAspectRatioY + 80);

                                        } else {

                                            //用户手指在SeeBank中滑动。我们根据用户手指位置来设定SeedBank的光标位置。
                                            if (touchX < lastX) {
                                                if (seedBankPosition > 0) {
                                                    seedBankPosition--;
                                                    isCursorMoved = true;
                                                    nativeSetSeedBankPosition(seedBankPosition);
                                                    lastX -= seedBankFrameItemLength;
                                                }
                                            } else if (touchX > lastX + seedBankFrameItemLength) {
                                                if (seedBankPosition < seedBankMaxPlants - 1) {
                                                    seedBankPosition++;
                                                    isCursorMoved = true;
                                                    nativeSetSeedBankPosition(seedBankPosition);
                                                    lastX += seedBankFrameItemLength;
                                                }
                                            }
                                        }
                                    } else if (isTouchPressedInPlantsBoard) {
                                        //用户手指落点在Board中。
                                        if (touchY < seedBankFrameBottom && touchY > seedBankFrameTop && touchX > seedBankFrameStart && touchX < seedBankFrameEnd) {
                                            //如果用户手指移动进SeedBank区域且手上拿着植物或加农炮，则取消当前植物种植或加农炮发射。
                                            if (nativeGameState() == 7 || nativeIsCobCannonSelected()) {
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventDown);
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventUp);
                                                cobCannonSelected = false;
                                            }
                                            isTouchPressedInPlantsBoard = false;
                                            return true;
                                        }


                                        //让Board光标跟随用户手指移动。
                                        nativeSetBoardPositionByXY((touchX - boardCursorStart) * boardCursorAspectRatioX + 40, (touchY - boardCursorTop) * boardCursorAspectRatioY + 80);
                                        if (touchX < lastX) {
                                            isCursorMoved = true;
                                        } else if (touchX > lastX + roofBoardItemWidth) {
                                            isCursorMoved = true;
                                        }
                                        if (projectionY1 < lastY) {
                                            isCursorMoved = true;
                                        } else if (projectionY1 > lastY + roofBoardItemHeight) {
                                            isCursorMoved = true;
                                        }

                                    } else if (isTouchPressedInShovelFrame) {
                                        if (projectionY1 > roofBoardFrameTop) {
                                            isTouchPressedInShovelFrame = false;
                                            isTouchPressedInPlantsBoard = true;
                                            nativePickUpShovel(true);
                                            nativeSetBoardPositionByXY((touchX - boardCursorStart) * boardCursorAspectRatioX + 40, (touchY - boardCursorTop) * boardCursorAspectRatioY + 80);
                                            shovelPicked = true;
                                        }
                                    }
                                    break;
                                case MotionEvent.ACTION_UP:
                                    if (isTouchPressedInSeedBankFrame && !isCursorMoved) {
                                        nativeSetGameState(nativeGameState() == 7 ? 1 : 7);
                                    }

                                    //根据一些条件来确定是否在抬起手指时发送确认键
                                    if (isTouchPressedInShovelFrame) {
                                        nativePickUpShovel(nativeCursorType() != 6);
                                    } else if (isTouchPressedInPlantsBoard && shovelPicked) {
                                        nativeShovelDown();
                                    } else if (nativeGameState() == 7 && isTouchPressedInPlantsBoard || isCursorMoved && nativeIsCobCannonSelected() || cobCannonSelected) {
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventDown);
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventUp);
                                    }
                                    isCursorMoved = false;
                                    cobCannonSelected = false;
                                    isTouchPressedInShovelFrame = false;
                                    isTouchPressedInSeedBankFrame = false;
                                    isTouchPressedInPlantsBoard = false;
                                    lastX = 0;
                                    lastY = 0;
                                    break;
                            }
                            break;
                        case RAINING_SEEDS_SMOOTH:
                            //种子雨模式的触控方式完全不同。因此单独为种子雨模式写一个触控逻辑
                            switch (motionEvent.getAction()) {
                                case MotionEvent.ACTION_DOWN:

                                    if (touchY < seedBankFrameBottom && touchY > seedBankFrameTop && touchX > 1045 * viewWidth && touchX < 1150 * viewWidth) {
                                        //NativeInputManager.onKeyInputEventNative(mNativeHandle, null, shovelEventDown);
                                        isTouchPressedInShovelFrame = true;
                                    }

                                    if (touchX > poolBoardFrameStart && touchX < poolBoardFrameEnd && touchY < poolBoardFrameBottom && touchY > poolBoardFrameTop) {
                                        //如果触控落点在Board区域中
                                        isTouchPressedInPlantsBoard = true;
                                        //将光标移动至用户手指落点处
                                        nativeSetBoardPositionByXY((touchX - boardCursorStart) * boardCursorAspectRatioX + 40, (touchY - boardCursorTop) * boardCursorAspectRatioY + 80);
                                        //移动光标完毕。
                                        cobCannonSelected = nativeCursorType() == 2;
                                        shovelPicked = nativeCursorType() == 6;
                                    }
                                    break;
                                case MotionEvent.ACTION_MOVE:
                                    if (isTouchPressedInPlantsBoard) {
                                        //用户手指落点在Board中。

                                        //如果用户手指移出了Board区域，就退掉手中的种子，且不再处理。
                                        if (touchX < poolBoardFrameStart || touchX > poolBoardFrameEnd || touchY < poolBoardFrameTop || touchY > poolBoardFrameBottom) {
                                            if (nativeCursorType() == 2) {
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventDown);
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventUp);
                                            }
                                            break;
                                        }

                                        //让Board光标跟随用户手指移动。
                                        nativeSetBoardPositionByXY((touchX - boardCursorStart) * boardCursorAspectRatioX + 40, (touchY - boardCursorTop) * boardCursorAspectRatioY + 80);

                                    } else if (isTouchPressedInShovelFrame) {
                                        if (touchY > poolBoardFrameTop) {
                                            isTouchPressedInShovelFrame = false;
                                            isTouchPressedInPlantsBoard = true;
                                            nativePickUpShovel(true);
                                            nativeSetBoardPositionByXY((touchX - boardCursorStart) * boardCursorAspectRatioX + 40, (touchY - boardCursorTop) * boardCursorAspectRatioY + 80);
                                            shovelPicked = true;
                                        }
                                    }
                                    break;
                                case MotionEvent.ACTION_UP:
                                    if (isTouchPressedInShovelFrame) {
                                        nativePickUpShovel(nativeCursorType() != 6);
                                    } else if (isTouchPressedInPlantsBoard && shovelPicked) {
                                        nativeShovelDown();
                                    } else if (cobCannonSelected) {
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventDown);
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventUp);
                                    }
                                    cobCannonSelected = false;
                                    isTouchPressedInShovelFrame = false;
                                    isTouchPressedInPlantsBoard = false;
                                    break;
                            }
                            break;
                        case SLOT_MACHINE_SMOOTH:
                            //老虎机模式的触控方式完全不同。因此单独为老虎机模式写一个触控逻辑
                            switch (motionEvent.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    if (touchY < seedBankFrameBottom && touchY > seedBankFrameTop && touchX > seedBankFrameStart) {
                                        if (touchX < 1260 * viewWidth) {
                                            //发送一个锤子键，这样就可以做到点击启动老虎机。
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, hammerEventDown);
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, hammerEventUp);
                                            return true;
                                        } else if (touchX < 1365 * viewWidth) {
                                            //发送铲子键
                                            //NativeInputManager.onKeyInputEventNative(mNativeHandle, null, shovelEventDown);
                                            isTouchPressedInShovelFrame = true;
                                        }
                                    }

                                    if (touchX > graveBoardFrameStart && touchX < graveBoardFrameEnd && touchY < graveBoardFrameBottom && touchY > graveBoardFrameTop) {
                                        //如果触控落点在Board区域中
                                        isTouchPressedInPlantsBoard = true;

                                        if (nativeGameState() == 6) {
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventDown);
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, backEventUp);
                                        }
                                        nativeSetBoardPositionByXY((touchX - boardCursorStart) * boardCursorAspectRatioX + 40, (touchY - boardCursorTop) * boardCursorAspectRatioY + 80);
                                        cobCannonSelected = nativeCursorType() == 2;
                                        shovelPicked = nativeCursorType() == 6;
                                    }
                                    break;
                                case MotionEvent.ACTION_MOVE:
                                    if (isTouchPressedInPlantsBoard) {
                                        //用户手指落点在Board中。
                                        nativeSetBoardPositionByXY((touchX - boardCursorStart) * boardCursorAspectRatioX + 40, (touchY - boardCursorTop) * boardCursorAspectRatioY + 80);
                                    } else if (isTouchPressedInShovelFrame) {
                                        if (touchY > graveBoardFrameTop) {
                                            isTouchPressedInShovelFrame = false;
                                            isTouchPressedInPlantsBoard = true;
                                            nativePickUpShovel(true);
                                            nativeSetBoardPositionByXY((touchX - boardCursorStart) * boardCursorAspectRatioX + 40, (touchY - boardCursorTop) * boardCursorAspectRatioY + 80);
                                            shovelPicked = true;
                                        }
                                    }
                                    break;
                                case MotionEvent.ACTION_UP:
                                    if (isTouchPressedInShovelFrame) {
                                        nativePickUpShovel(nativeCursorType() != 6);
                                    } else if (isTouchPressedInPlantsBoard && shovelPicked) {
                                        nativeShovelDown();
                                    } else if (cobCannonSelected) {
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventDown);
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventUp);
                                    }
                                    cobCannonSelected = false;
                                    isTouchPressedInShovelFrame = false;
                                    isTouchPressedInPlantsBoard = false;
                                    break;
                            }
                            break;
                        case VASE_BREAKER_SMOOTH:
                            //砸罐子模式的触控方式完全不同。因此单独为砸罐子模式写一个触控逻辑
                            switch (motionEvent.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    if (touchY < seedBankFrameBottom && touchY > seedBankFrameTop && touchX > 1045 * viewWidth && touchX < 1150 * viewWidth) {
                                        // NativeInputManager.onKeyInputEventNative(mNativeHandle, null, shovelEventDown);
                                        isTouchPressedInShovelFrame = true;
                                    }

                                    if (touchX > graveBoardFrameStart && touchX < graveBoardFrameEnd && touchY < graveBoardFrameBottom && touchY > graveBoardFrameTop) {
                                        //如果触控落点在Board区域中
                                        isTouchPressedInPlantsBoard = true;
                                        //将光标移动至用户手指落点处

                                        final int boardCursorPositionX = (int) ((touchX - graveBoardFrameStart) / graveBoardItemWidth);
                                        final int diffX = nativeGetCursorPositionX() - boardCursorPositionX;
                                        if (diffX > 0) {
                                            for (int i = 0; i < diffX; i++)
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, leftEventDown);
                                        } else if (diffX < 0) {
                                            for (int i = diffX; i < 0; i++)
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, rightEventDown);
                                        }

                                        final int boardCursorPositionY = (int) ((touchY - graveBoardFrameTop) / graveBoardItemHeight);
                                        final int diffY = nativeGetCursorPositionY() - boardCursorPositionY;
                                        if (diffY > 0) {
                                            for (int i = 0; i < diffY; i++)
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, upEventDown);
                                        } else if (diffY < 0) {
                                            for (int i = diffY; i < 0; i++)
                                                NativeInputManager.onKeyInputEventNative(mNativeHandle, null, downEventDown);
                                        }
                                        //移动光标完毕。


                                        //在此时额外发送一个锤子键，这样就可以做到点击砸罐子。
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, hammerEventDown);
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, hammerEventUp);
                                        cobCannonSelected = nativeCursorType() == 2;
                                        shovelPicked = nativeCursorType() == 6;
                                    }
                                    break;
                                case MotionEvent.ACTION_MOVE:
                                    if (isTouchPressedInPlantsBoard) {
                                        //用户手指落点在Board中。
                                        nativeSetBoardPositionByXY((touchX - boardCursorStart) * boardCursorAspectRatioX + 40, (touchY - boardCursorTop) * boardCursorAspectRatioY + 80);
                                    } else if (isTouchPressedInShovelFrame) {
                                        if (touchY > graveBoardFrameTop) {
                                            isTouchPressedInShovelFrame = false;
                                            isTouchPressedInPlantsBoard = true;
                                            nativePickUpShovel(true);
                                            nativeSetBoardPositionByXY((touchX - boardCursorStart) * boardCursorAspectRatioX + 40, (touchY - boardCursorTop) * boardCursorAspectRatioY + 80);
                                            shovelPicked = true;
                                        }
                                    }
                                    break;
                                case MotionEvent.ACTION_UP:
                                    if (isTouchPressedInShovelFrame) {
                                        nativePickUpShovel(nativeCursorType() != 6);
                                    } else if (isTouchPressedInPlantsBoard && shovelPicked) {
                                        nativeShovelDown();
                                    } else if (cobCannonSelected) {
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventDown);
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventUp);
                                    }
                                    cobCannonSelected = false;
                                    isTouchPressedInShovelFrame = false;
                                    isTouchPressedInPlantsBoard = false;
                                    lastX = 0;
                                    lastY = 0;
                                    break;
                            }
                            break;
                        case VS_BATTLE:
                            switch (motionEvent.getActionMasked()) {
                                case MotionEvent.ACTION_DOWN:
                                case MotionEvent.ACTION_POINTER_DOWN:

                                    final int pointerIndex = motionEvent.getActionIndex();
                                    final int pointerId = motionEvent.getPointerId(pointerIndex);
                                    float x = motionEvent.getX(pointerIndex);
                                    float y = motionEvent.getY(pointerIndex);

                                    if (y < seedBankVSFrameBottom && y > seedBankVSFrameTop) {
                                        if (x > seedBankVSPlantFrameStart && x < seedBankVSPlantFrameEnd) {
                                            int position = (int) ((x - seedBankVSPlantFrameStart) / seedBankFrameVSItemLength);
                                            if (position != nativeGetSeekBankPosition()) {
                                                isCursorMoved = true;
                                                nativeSetSeedBankPosition(position);
                                            } else {
                                                nativeSetGameState(nativeGameState() == 7 ? 1 : 7);
                                            }
                                        } else if (x > shovelVSPlantFrameStart && x < shovelVSPlantFrameEnd) {
                                            nativePickUpShovel(nativeCursorType() != 6);
                                            isTouchPressedInShovelFrame = true;
                                        } else if (x > seedBankVSZombieFrameStart && x < seedBankVSZombieFrameEnd) {
                                            int position = (int) ((x - seedBankVSZombieFrameStart) / seedBankFrameVSItemLength);
                                            if (position != nativeGetSeekBankPosition2P()) {
                                                isCursorMoved = true;
                                                nativeSetSeedBankPosition2P(position);
                                            } else {
                                                nativeSetGameState2P(nativeGameState2P() == 7 ? 1 : 7);
                                            }
                                        }
                                    } else if (x > boardCursorStart && x < boardCursorEnd && y > boardCursorTop && y < boardCursorBottom) {
                                        if (x < boardCursorDivide) {
                                            if (plantVSTouchPointId == -1) {
                                                plantVSTouchPointId = pointerId;
                                                nativeSetBoardPositionByXY((x - boardCursorStart) * boardCursorAspectRatioX + 40, (y - boardCursorTop) * boardCursorAspectRatioY + 80);
                                                shovelPicked = nativeCursorType() == 6;
                                            }
                                        } else {
                                            if (zombieVSTouchPointId == -1) {
                                                zombieVSTouchPointId = pointerId;
                                                nativeSetBoardPositionByXY2P((x - boardCursorStart) * boardCursorAspectRatioX + 40, (y - boardCursorTop) * boardCursorAspectRatioY + 80);
                                            }
                                        }
                                    }
                                    break;

                                case MotionEvent.ACTION_MOVE:
                                    if (plantVSTouchPointId != -1) {
                                        int plantPointerIndex = motionEvent.findPointerIndex(plantVSTouchPointId);
                                        nativeSetBoardPositionByXY((motionEvent.getX(plantPointerIndex) - boardCursorStart) * boardCursorAspectRatioX + 40, (motionEvent.getY(plantPointerIndex) - boardCursorTop) * boardCursorAspectRatioY + 80);
                                    }
                                    if (zombieVSTouchPointId != -1) {
                                        int zombiePointerIndex = motionEvent.findPointerIndex(zombieVSTouchPointId);
                                        nativeSetBoardPositionByXY2P((motionEvent.getX(zombiePointerIndex) - boardCursorStart) * boardCursorAspectRatioX + 40, (motionEvent.getY(zombiePointerIndex) - boardCursorTop) * boardCursorAspectRatioY + 80);
                                    }
                                    break;

                                case MotionEvent.ACTION_POINTER_UP:
                                case MotionEvent.ACTION_UP:

                                    final int pointerIndex1 = motionEvent.getActionIndex();
                                    final int pointerId1 = motionEvent.getPointerId(pointerIndex1);

                                    if (plantVSTouchPointId == pointerId1) {
                                        plantVSTouchPointId = -1;
                                        if (nativeGameState() == 7) {
                                            native1PButtonDown(6);
                                        }
                                        if (shovelPicked) {
                                            nativeShovelDown();
                                            shovelPicked = false;
                                        }
                                    } else if (zombieVSTouchPointId == pointerId1) {
                                        zombieVSTouchPointId = -1;
                                        if (nativeGameState2P() == 7) {
                                            native2PButtonDown(6);
                                        }
                                    }
                                    break;
                            }
                            break;
                        case VS_SETUP:
                            switch (motionEvent.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    switch (nativeVSSetupState()) {
                                        case 0:
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventDown);
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventUp);
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventDown);
                                            NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventUp);
                                            break;
                                        case 1:
                                            break;
                                        case 2:
                                            if (touchX > buttonVSFrameStart && touchX < buttonVSFrameEnd && touchY < buttonVSFrameBottom && touchY > buttonVSFrameTop) {
                                                isTouchPressedInSeedChooseFrame = true;
                                                final int x = (int) ((touchX - buttonVSFrameStart) / buttonVSItemLength);
                                                for (int i = 0; i < 2; i++)
                                                    NativeInputManager.onKeyInputEventNative(mNativeHandle, null, leftEventDown);
                                                for (int i = 0; i < x; i++)
                                                    NativeInputManager.onKeyInputEventNative(mNativeHandle, null, rightEventDown);
                                            }
                                            break;
                                        case 3:
                                            if (touchX > plantChooserVSFrameStart && touchX < plantChooserVSFrameEnd && touchY < plantChooserVSFrameBottom && touchY > plantChooserVSFrameTop) {
                                                native1PSelectPlantSeed((int) ((touchX - plantChooserVSFrameStart) / plantChooserVSItemLength), (int) ((touchY - plantChooserVSFrameTop) / plantChooserVSItemHeight));
                                            } else if (touchX > zombieChooserVSFrameStart && touchX < zombieChooserVSFrameEnd && touchY < zombieChooserVSFrameBottom && touchY > zombieChooserVSFrameTop) {
                                                final int y = (int) ((touchY - zombieChooserVSFrameTop) / zombieChooserVSItemHeight);
                                                native2PSelectZombieSeed(y == 3 ? (int) (-0.5 + (touchX - zombieChooserVSFrameStart) / zombieChooserVSItemLength) : (int) ((touchX - zombieChooserVSFrameStart) / zombieChooserVSItemLength), y);
                                            }
                                            break;
                                    }
                                    break;
                                case MotionEvent.ACTION_UP:
                                    if (isTouchPressedInSeedChooseFrame) {
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventDown);
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventUp);
                                    }
                                    isTouchPressedInSeedChooseFrame = false;
                                    break;
                            }
                            break;
                        case VS_RESULT:
                            switch (motionEvent.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    if (touchX > buttonVSResultFrameStart && touchX < buttonVSResultFrameEnd && touchY < buttonVSResultFrameBottom && touchY > buttonVSResultFrameTop) {
                                        isTouchPressedInSeedChooseFrame = true;
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, touchX > (buttonVSResultFrameStart + buttonVSResultFrameEnd) / 2 ? rightEventDown : leftEventDown);
                                    }
                                    break;
                                case MotionEvent.ACTION_UP:
                                    if (isTouchPressedInSeedChooseFrame) {
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventDown);
                                        NativeInputManager.onKeyInputEventNative(mNativeHandle, null, enterEventUp);
                                    }
                                    isTouchPressedInSeedChooseFrame = false;
                                    break;
                            }
                            break;
                        case DO_NOTHING:
                            //如果当前是主界面或存在对话框，则将触控传递给Native层。因为游戏本就支持处理主界面和对话框的触控
                            if ((motionEvent.getSource() & 16) != 0)
                                NativeInputManager.onJoystickEventNative(mNativeHandle, null, InputManager.JoystickEvent.translate(motionEvent));
                            else if ((motionEvent.getSource() & 4) == 0)
                                NativeInputManager.onTouchEventNative(mNativeHandle, null, InputManager.PointerEvent.translate(motionEvent));
                            break;
                    }
                    return true;
                }
            });

            //显示眼睛按钮，点击眼睛可以显示\隐藏游戏键盘
            mWindowManager = getWindowManager();
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
                mWindowManager.getDefaultDisplay().getRealMetrics(metrics);
            final int SCREEN_WIDTH = metrics.widthPixels;
            final int SCREEN_HEIGHT = metrics.heightPixels;
            final int visibilitySize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, sharedPreferences.getInt("visibilitySize", 40), getResources().getDisplayMetrics());
            final int visibilityX = sharedPreferences.getInt("visibilityX", -200);
            final int visibilityY = sharedPreferences.getInt("visibilityY", -200);
            final boolean isVisibilityLockPosition = sharedPreferences.getBoolean("isVisibilityLockPosition", false);
            WindowManager.LayoutParams visibilityParams = new WindowManager.LayoutParams(visibilitySize, visibilitySize, visibilityX, visibilityY, WindowManager.LayoutParams.TYPE_APPLICATION_PANEL, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSPARENT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                visibilityParams.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            visibilityParams.gravity = Gravity.CENTER;
            visibilityParams.alpha = sharedPreferences.getInt("visibilityTran", 90) / 100f;
            visibilityWindow = new ImageView(this);
            visibilityWindow.setImageDrawable(getResources().getDrawable(isVisible ? R.drawable.button_visible : R.drawable.button_invisible));
            final View[] views = new View[]{enterButton, backButton, shovelButton, hammerButton, dpadButton, stopButton};
            final boolean[] viewsKeep = {sharedPreferences.getBoolean("enterKeep", false), sharedPreferences.getBoolean("backKeep", false), sharedPreferences.getBoolean("shovelKeep", false), sharedPreferences.getBoolean("hammerKeep", false), sharedPreferences.getBoolean("dpadKeep", false), sharedPreferences.getBoolean("stopKeep", false)};
            visibilityWindow.setOnTouchListener(new View.OnTouchListener() {
                float lastX = 0, lastY = 0;
                long downTime;
                boolean moved;

                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {

                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            moved = false;
                            downTime = System.currentTimeMillis();
                            lastX = motionEvent.getRawX();
                            lastY = motionEvent.getRawY();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            float rawX = motionEvent.getRawX();
                            float rawY = motionEvent.getRawY();
                            int dx = Math.round(rawX - lastX);
                            int dy = Math.round(rawY - lastY);
                            if (Math.abs(dx) > 5 || Math.abs(dy) > 5) moved = true;
                            if (isVisibilityLockPosition) break;
                            lastX += dx;
                            lastY += dy;
                            visibilityParams.x += dx;
                            visibilityParams.y += dy;
                            mWindowManager.updateViewLayout(view, visibilityParams);
                            break;
                        case MotionEvent.ACTION_UP:
                            if (!moved) {
                                for (int i = 0; i < views.length; i++) {
                                    if (!viewsKeep[i]) {
                                        views[i].setVisibility(isVisible ? View.INVISIBLE : View.VISIBLE);
                                    }
                                }
                                visibilityWindow.setImageDrawable(getResources().getDrawable(isVisible ? R.drawable.button_invisible : R.drawable.button_visible));
                                isVisible = !isVisible;
                                sharedPreferences.edit().putBoolean("isVisible", isVisible).apply();
                            }

                            //自动贴边
                            visibilityParams.x = Math.min(Math.max(visibilityParams.x, -SCREEN_WIDTH / 2), SCREEN_WIDTH / 2);
                            visibilityParams.y = Math.min(Math.max(visibilityParams.y, -SCREEN_HEIGHT / 2), SCREEN_HEIGHT / 2);
                            mWindowManager.updateViewLayout(view, visibilityParams);

                            //存储悬浮球位置
                            sharedPreferences.edit().putInt("visibilityX", visibilityParams.x).putInt("visibilityY", visibilityParams.y).apply();
                    }
                    return false;
                }
            });
            mWindowManager.addView(visibilityWindow, visibilityParams);
        }
        super.onWindowFocusChanged(hasFocus);
    }


    @Override
    public void onDestroy() {
        if (isFileObserverLaunched) fileObserver.stopWatching();
        if (isAddonWindowLoaded) mWindowManager.removeViewImmediate(visibilityWindow);
        if (heavyWeaponAccel) mOrientationListener.disable();
        super.onDestroy();
    }

}
