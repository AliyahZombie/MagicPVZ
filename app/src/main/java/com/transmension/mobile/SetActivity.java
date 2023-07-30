package com.transmension.mobile;

import android.animation.LayoutTransition;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Pair;
import android.util.TypedValue;
import android.util.Xml;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class SetActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //设置主题、导航栏与操作栏透明，UI会好看些
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            setTheme(android.R.style.Theme_DeviceDefault);
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            window.setNavigationBarContrastEnforced(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

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

        setTitle("PvZ TV版附加设置");
        ScrollView scrollView = new ScrollView(this);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        TextView textView = new TextView(this);
        textView.setGravity(Gravity.CENTER);
        textView.setText("\n默认画面长宽比为16:9，您可在此处任意调节。\n");
        textView.setTextSize(18f);

        CheckBox checkBox = new CheckBox(this);
        checkBox.setText("全屏显示");
        checkBox.setTextSize(18f);
        checkBox.setChecked(!sharedPreferences.getBoolean("shiLiuBiJiu", true));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        checkBox.setLayoutParams(params);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 1;
        LinearLayout linearLayout1 = new LinearLayout(this);
        linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
        NumberPicker numberPicker = new NumberPicker(this);
        numberPicker.setMaxValue(20);
        numberPicker.setMinValue(1);
        numberPicker.setLayoutParams(layoutParams);
        numberPicker.setValue(sharedPreferences.getInt("width", 16));
        NumberPicker numberPicker1 = new NumberPicker(this);
        numberPicker1.setMaxValue(20);
        numberPicker1.setMinValue(1);
        numberPicker1.setLayoutParams(layoutParams);
        numberPicker1.setValue(sharedPreferences.getInt("height", 9));
        linearLayout1.addView(numberPicker);
        linearLayout1.addView(numberPicker1);
        linearLayout1.setVisibility(checkBox.isChecked() ? View.INVISIBLE : View.VISIBLE);
        checkBox.setOnCheckedChangeListener((compoundButton, b) -> linearLayout1.setVisibility(b ? View.INVISIBLE : View.VISIBLE));
        Button button = new Button(this);
        button.setText("保存画面比例");
        button.setOnClickListener(view -> {
            sharedPreferences.edit().putInt("width", numberPicker.getValue()).putInt("height", numberPicker1.getValue()).putBoolean("shiLiuBiJiu", !checkBox.isChecked()).apply();
            Toast.makeText(SetActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
        });
        Button buttonButton = new Button(this);
        buttonButton.setText("点我设置内置游戏键盘->");
        buttonButton.setOnClickListener(view -> {
            startActivity(new Intent(SetActivity.this, ButtonSetActivity.class));
            finish();
        });
        final Button buttonImportFromExternal = new Button(this);
        buttonImportFromExternal.setText("长按此处导入全通关存档");
        buttonImportFromExternal.setOnClickListener(view -> Toast.makeText(this, "现有存档将被覆盖。\n长按确认导入", Toast.LENGTH_SHORT).show());
        buttonImportFromExternal.setOnLongClickListener(view -> {
            try {
                InputStream inputStream = getAssets().open("userdata.zip");
                if (inputStream != null) {
                    File userdata_import = new File(getExternalFilesDir(null), "userdata");

                    if (!userdata_import.exists()) userdata_import.mkdir();
                    ZipInputStream zipInputStream = new ZipInputStream(inputStream);
                    // 读取ZipEntry对象
                    ZipEntry zipEntry = zipInputStream.getNextEntry();
                    // 遍历ZipEntry对象并解压缩文件
                    byte[] buffer = new byte[1024];
                    while (zipEntry != null) {
                        // 提取zip文件中的唯一一个目录结构下的所有文件
                        if (!zipEntry.isDirectory()) {
                            // 获取文件名并创建新文件。
                            String fileName = new File(zipEntry.getName()).getName();
                            File newFile = new File(userdata_import, fileName);
                            // 写出文件流
                            FileOutputStream fileOutputStream = new FileOutputStream(newFile);
                            int readLength;
                            while ((readLength = zipInputStream.read(buffer)) > 0) {
                                fileOutputStream.write(buffer, 0, readLength);
                            }
                            fileOutputStream.close();
                        }
                        // 关闭当前ZipEntry，并移到下一个ZipEntry。
                        zipInputStream.closeEntry();
                        zipEntry = zipInputStream.getNextEntry();
                    }
                    // 关闭ZipInputStream对象
                    zipInputStream.close();
                    inputStream.close();
                }
                Toast.makeText(SetActivity.this, "成功导入全通关存档！", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(SetActivity.this, "导入全通关存档失败", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            return true;
        });
        final Button buttonImport = new Button(this);
        buttonImport.setText("从外部导入存档");

        buttonImport.setOnClickListener(view -> {
            Toast.makeText(SetActivity.this, "请选择zip格式的存档文件", Toast.LENGTH_SHORT).show();
            //通过系统的文件浏览器选择一个文件
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            //筛选，只显示可以“打开”的结果，如文件(而不是联系人或时区列表)
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("application/zip");
            startActivityForResult(intent, 99);
        });
        final Button buttonImportFromBackup = new Button(this);
        buttonImportFromBackup.setText("从备份恢复存档");
        buttonImportFromBackup.setOnClickListener(view -> {
            PopFragment popFragment = new PopFragment();
            popFragment.show(getFragmentManager(), "pop");
        });

        final Button buttonExport = new Button(this);
        buttonExport.setText("导出当前存档");
        buttonExport.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            // 文件类型
            intent.setType("application/zip");
            // 文件名称
            intent.putExtra(Intent.EXTRA_TITLE, "TV版存档备份.zip");
            startActivityForResult(intent, 66);
        });

        final Button buttonAbout = new Button(this);
        buttonAbout.setText("版本介绍和问题反馈");
        buttonAbout.setOnClickListener(view -> new AlertDialog.Builder(this)
                .setTitle("版本介绍和问题反馈")
                .setMessage("\n感谢一众热爱PvZ的技术大佬们的协助，我才得以做出如今的TV触控版。\n\n当然，现在的触控还不够完善，主要体现在：\n1.花园工具使用后会自动变回水壶；(触控的缺陷)\n" +
                        "\n" +
                        "2.路灯花照不透罐子；(游戏本身的缺陷)\n" +
                        "\n" +
                        "3.模仿者变的植物不是灰色；(游戏本身的缺陷)\n" +
                        "\n" +
                        "4.小游戏“坚不可摧”无法在开始游戏前的准备时期种植物；(游戏本身的缺陷)\n" +
                        "\n" +
                        "5.图鉴界面最后一行的触控判定位置偏上；且图鉴界面的\"查看植物\"和\"查看僵尸\"按钮会持续起作用 (游戏本身的缺陷)\n\n目前TV触控主要由我一人维护，如果您希望向我反馈BUG或建议，请前往访问我的Bili主页。")
                .setNeutralButton("版本介绍", (dialogInterface, i) -> new AlertDialog.Builder(SetActivity.this)
                        .setTitle("版本介绍")
                        .setMessage("1.几乎所有界面都可触控\n\n2.逆向并重写了游戏的整个Java层源代码，去除全部权限，TargetAPI 33\n\n3.为游戏加入菜单修改器、游戏键盘、附加设置\n\n4.补齐全场景BGM、解锁隐藏小游戏、破解道具栏内购、去除网络错误弹窗、点击\"制作人员\"可播放MV\n")
                        .setNegativeButton("明白",null)
                        .show())
                .setPositiveButton("Bili主页", (dialogInterface, i1) -> {
                    String url = "https://space.bilibili.com/480240783";
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                })
                .show());
        final CheckBox checkBox1 = new CheckBox(this);
        checkBox1.setText("自动备份存档(保存最近20次的存档)");
        checkBox1.setChecked(sharedPreferences.getBoolean("autoBackUp", true));
        checkBox1.setOnCheckedChangeListener((compoundButton, bool) -> sharedPreferences.edit().putBoolean("autoBackUp", bool).apply());
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params1.gravity = Gravity.CENTER;
        checkBox1.setLayoutParams(params1);
        final CheckBox checkBox3 = new CheckBox(this);
        checkBox3.setText("开启传送带秒传送(用于传送带触控)");
        checkBox3.setChecked(sharedPreferences.getBoolean("enableConveyorBeltFast", true));
        checkBox3.setOnCheckedChangeListener((compoundButton, bool) -> sharedPreferences.edit().putBoolean("enableConveyorBeltFast", bool).apply());
        checkBox3.setLayoutParams(params1);
        final CheckBox checkBox4 = new CheckBox(this);
        checkBox4.setText("手动收集阳光金币");
        checkBox4.setChecked(sharedPreferences.getBoolean("enableManualCollect", false));
        checkBox4.setOnCheckedChangeListener((compoundButton, bool) -> sharedPreferences.edit().putBoolean("enableManualCollect", bool).apply());
        checkBox4.setLayoutParams(params1);
        final CheckBox checkBox5 = new CheckBox(this);
        checkBox5.setText("关闭游戏内道具栏");
        checkBox5.setChecked(sharedPreferences.getBoolean("disableShop", false));
        checkBox5.setOnCheckedChangeListener((compoundButton, bool) -> sharedPreferences.edit().putBoolean("disableShop", bool).apply());
        checkBox5.setLayoutParams(params1);
        final CheckBox checkBox6 = new CheckBox(this);
        checkBox6.setText("重型武器重力感应");
        checkBox6.setChecked(sharedPreferences.getBoolean("heavyWeaponAccel", false));
        checkBox6.setOnCheckedChangeListener((compoundButton, bool) -> sharedPreferences.edit().putBoolean("heavyWeaponAccel", bool).apply());
        checkBox6.setLayoutParams(params1);
        final CheckBox checkBox7 = new CheckBox(this);
        checkBox7.setText("使用平滑光标");
        checkBox7.setChecked(sharedPreferences.getBoolean("useSmoothCursor", true));
        checkBox7.setOnCheckedChangeListener((compoundButton, bool) -> sharedPreferences.edit().putBoolean("useSmoothCursor", bool).apply());
        checkBox7.setLayoutParams(params1);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(params);
        linearLayout.addView(textView);
        linearLayout.addView(checkBox);
        linearLayout.addView(linearLayout1);
        linearLayout.addView(button);
        linearLayout.addView(buttonButton);
        linearLayout.addView(buttonImportFromExternal);
        linearLayout.addView(buttonImport);
        linearLayout.addView(buttonImportFromBackup);
        linearLayout.addView(buttonExport);
        linearLayout.addView(buttonAbout);
        linearLayout.addView(layout);
        layout.addView(checkBox1);
        layout.addView(checkBox3);
        layout.addView(checkBox4);
        layout.addView(checkBox5);
        layout.addView(checkBox6);
        layout.addView(checkBox7);
        scrollView.addView(linearLayout);
        setContentView(scrollView);

        try {
            Class.forName("com.android.support.CkHomuraMenu");
            final CheckBox checkBox2 = new CheckBox(this);
            checkBox2.setText("开启菜单修改器");
            checkBox2.setChecked(sharedPreferences.getBoolean("useMenu", true));
            checkBox2.setOnCheckedChangeListener((compoundButton, bool) -> sharedPreferences.edit().putBoolean("useMenu", bool).apply());
            checkBox2.setLayoutParams(params1);
            layout.addView(checkBox2);
        } catch (ClassNotFoundException ignored) {
        }
    }

    public static class PopFragment extends DialogFragment {


        @Override
        public void onActivityCreated(Bundle savedInstanceState) {

            Window window = getDialog().getWindow();
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            super.onActivityCreated(savedInstanceState);
        }

        @Override
        public void onStart() {
            Window window = getDialog().getWindow();
            window.setLayout(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? getResources().getDisplayMetrics().heightPixels : getResources().getDisplayMetrics().widthPixels - 150, -2);
            super.onStart();
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Dialog dialog = new Dialog(getActivity());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                dialog.requestWindowFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCanceledOnTouchOutside(true);
            return dialog;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            List<Pair<String, Long>> backupsList = findNumDirs(getActivity().getExternalFilesDir(null));
            ListView listView = new ListView(getActivity());
            if (backupsList.isEmpty()) {
                Toast.makeText(getActivity(), "您还没有任何备份的存档哦", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(this::dismiss, 1000);
                return listView;
            }
            float density = getResources().getDisplayMetrics().density;
            ShapeDrawable oval = new ShapeDrawable(new RoundRectShape(new float[]{20 * density, 20 * density, 20 * density, 20 * density, 20 * density, 20 * density, 20 * density, 20 * density}, null, null));
            oval.getPaint().setColor(0xff424c50);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                listView.setBackground(oval);


            listView.setAdapter(new BaseAdapter() {
                @Override
                public int getCount() {
                    return backupsList.size();
                }

                @Override
                public Object getItem(int i) {
                    return backupsList.get(i);
                }

                @Override
                public long getItemId(int i) {
                    return i;
                }

                @Override
                public View getView(int i, View view, ViewGroup viewGroup) {
                    view = new TextView(getActivity());
                    ((TextView) view).setText(backupsList.get(i).first);
                    ((TextView) view).setGravity(Gravity.CENTER);
                    ((TextView) view).setHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getActivity().getResources().getDisplayMetrics()));
                    ((TextView) view).setTextSize(16f);
                    ((TextView) view).setTextColor(Color.WHITE);
                    view.setOnClickListener(view1 -> Toast.makeText(getActivity(), "长按即可恢复这个存档", Toast.LENGTH_SHORT).show());
                    view.setOnLongClickListener(view12 -> {
                        String backupFile = backupsList.get(i).first;
                        File file = new File(getActivity().getExternalFilesDir(null), backupFile);
                        File destFile = new File(getActivity().getExternalFilesDir(null), "userdata");
                        deleteRecursive(destFile);
                        try {
                            copyDir(file, destFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(getActivity(), "恢复存档成功", Toast.LENGTH_SHORT).show();
                        return true;
                    });
                    return view;
                }
            });
            listView.setLayoutTransition(new LayoutTransition());
            listView.setOnItemClickListener((parent, view, position, id) -> dismiss());
            return listView;
        }
    }

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

    public static List<Pair<String, Long>> findNumDirs(File dir) {
        List<Pair<String, Long>> list = new ArrayList<>();
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles((dir1, name) -> {
                // 判断只要是数字开头的文件夹就符合要求
                return new File(dir1, name).isDirectory() && Character.isDigit(name.charAt(0));
            });
            if (files != null && files.length > 0) {
                // 将文件夹的名称和修改时间存入Pair中，再将Pair存入list中
                for (File file : files) {
                    String dirName = file.getName();
                    long modifiedTime = file.lastModified();
                    Pair<String, Long> pair = new Pair<>(dirName, modifiedTime);
                    list.add(pair);
                }
                // 按照修改时间排序
                Collections.sort(list, (o1, o2) -> o2.second.compareTo(o1.second));
            }
        }
        return list;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 66 && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            if (uri != null) {
                try {
                    OutputStream outputStream = getContentResolver().openOutputStream(uri);
                    if (outputStream != null) {
                        File userdata = new File(getExternalFilesDir(null), "userdata");
                        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
                        addFolderToZip(userdata, zipOutputStream);
                        zipOutputStream.close();
                        outputStream.close();
                    }
                    Toast.makeText(this, "成功导出当前存档！", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(this, "导出当前存档失败", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        }

        if (requestCode == 99 && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            if (uri != null) {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(uri);
                    if (inputStream != null) {
                        File userdata_import = new File(getExternalFilesDir(null), "userdata");
                        deleteRecursive(userdata_import);
                        if (!userdata_import.exists()) userdata_import.mkdir();
                        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
                        // 读取ZipEntry对象
                        ZipEntry zipEntry = zipInputStream.getNextEntry();
                        // 遍历ZipEntry对象并解压缩文件
                        byte[] buffer = new byte[1024];
                        while (zipEntry != null) {
                            // 提取zip文件中的唯一一个目录结构下的所有文件
                            if (!zipEntry.isDirectory()) {
                                // 获取文件名并创建新文件。
                                String fileName = new File(zipEntry.getName()).getName();
                                File newFile = new File(userdata_import, fileName);
                                // 写出文件流
                                FileOutputStream fileOutputStream = new FileOutputStream(newFile);
                                int readLength;
                                while ((readLength = zipInputStream.read(buffer)) > 0) {
                                    fileOutputStream.write(buffer, 0, readLength);
                                }
                                fileOutputStream.close();
                            }
                            // 关闭当前ZipEntry，并移到下一个ZipEntry。
                            zipInputStream.closeEntry();
                            zipEntry = zipInputStream.getNextEntry();
                        }
                        // 关闭ZipInputStream对象
                        zipInputStream.close();
                        inputStream.close();
                    }
                    Toast.makeText(this, "成功导入存档！", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(this, "导入存档失败", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        }
    }


    public static void addFolderToZip(File folder, ZipOutputStream zipOutputStream) throws IOException {
        File[] files = folder.listFiles();
        if (files == null) return;
        for (File file : files) {
            if (file.isDirectory()) {
                addFolderToZip(file, zipOutputStream);
            } else {
                byte[] buffer = new byte[1024];
                FileInputStream fileInputStream = new FileInputStream(file);
                zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
                int length;
                while ((length = fileInputStream.read(buffer)) > 0) {
                    zipOutputStream.write(buffer, 0, length);
                }
                fileInputStream.close();
            }
        }
    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item1 = menu.add(0, 1, 1, "隐藏桌面图标");
        item1.setCheckable(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(1).setChecked(getPackageManager().getComponentEnabledSetting(new ComponentName(getPackageName(), SetActivityEntrance.class.getName())) == PackageManager.COMPONENT_ENABLED_STATE_DISABLED);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemSelected(int i, MenuItem menuItem) {
        new AlertDialog.Builder(this)
                .setTitle("隐藏附加设置桌面图标")
                .setMessage("隐藏后，您依然可以长按游戏桌面图标打开附加设置。\n\n确定隐藏吗？")
                .setNegativeButton("返回",null)
                .setPositiveButton("确定", (dialogInterface, i1) -> {
                    PackageManager packageManager = getPackageManager();
                    if (packageManager.getComponentEnabledSetting(new ComponentName(getPackageName(), SetActivityEntrance.class.getName())) == PackageManager.COMPONENT_ENABLED_STATE_DISABLED)
                        packageManager.setComponentEnabledSetting(new ComponentName(getPackageName(), SetActivityEntrance.class.getName()), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, 0);
                    else
                        packageManager.setComponentEnabledSetting(new ComponentName(getPackageName(), SetActivityEntrance.class.getName()), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, 0);

                })
                .show();
        return super.onMenuItemSelected(i, menuItem);
    }

}