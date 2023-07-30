package com.transmension.mobile;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;


/* loaded from: classes.dex */
public class ExtensionManager {
    private MainActivity mActivity;
    private final String TAG = "ExtensionManager";
    private Map<Integer, Extension> mExtensions = new HashMap();
    private int mNextId = new Random().nextInt(10000) + GameCenter.FEATURE_MESSAGE;

    public ExtensionManager(MainActivity activity) {
        this.mActivity = activity;
    }

    public void loadExtensions() {
        String extensions = MetadataHelper.getMetaData(this.mActivity, "Extensions");
        if (!TextUtils.isEmpty(extensions)) {
            String[] factories = extensions.split(";");
            for (String factoryName : factories) {
                loadExtension(factoryName);
            }
        }
    }

    private int getNextId() {
        int i = this.mNextId;
        this.mNextId = i + 1;
        return i;
    }

    public Extension loadExtension(String factoryName) {
        Extension extension = createExtension(factoryName);
        if (extension != null) {
            extension.setId(getNextId());
            this.mExtensions.put(extension.getId(), extension);
        }
        return extension;
    }

    public int[] getExtensionIds() {
        int[] result = new int[this.mExtensions.size()];
        int i = 0;
        for (Extension ext : this.mExtensions.values()) {
            result[i] = ext.getId();
            i++;
        }
        return result;
    }

    public int getExtensionIdByName(String name) {
        Extension extension = getExtensionByName(name);
        if (extension != null) {
            return extension.getId();
        }
        return -1;
    }

    public Extension getExtensionByName(String name) {
        if (name == null) {
            return null;
        }
        for (Extension extension : this.mExtensions.values()) {
            if (extension.getName().equals(name)) {
                return extension;
            }
        }
        return null;
    }

    public int getExtensionIdByType(String name) {
        Extension extension = getExtensionByType(name);
        if (extension != null) {
            return extension.getId();
        }
        return -1;
    }

    public Extension getExtensionByType(String name) {
        if (name == null) {
            return null;
        }
        for (Extension extension : this.mExtensions.values()) {
            if (extension.getExtensionType().equals(name)) {
                return extension;
            }
        }
        return null;
    }

    private Extension createExtension(String factoryName) {
        try {
            Class<?> clazz = Class.forName(factoryName);
            try {
                ExtensionFactory factory = (ExtensionFactory) clazz.newInstance();
                Extension extension = factory.create(this.mActivity);
                extension.setListener(new Extension.Listener() { // from class: com.transmension.mobile.ExtensionManager.1
                    @Override // com.transmension.mobile.Extension.Listener
                    public void onEvent(Activity activity, Extension extension2, String event, int status, String extras) {
                        Log.i("ExtensionManager", "Extension event: " + event + " extras: " + extras);
                        NativeExtensionManager.onEventNative(ExtensionManager.this.mActivity.mNativeHandle, extension2, extension2.getId(), event, status, extras);
                    }
                });
                Log.i("ExtensionManager", "Created extension: " + extension.getName());
                return extension;
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

    public int getExtensionCount() {
        return this.mExtensions.size();
    }

    public int[] getAllExtensionIds() {
        Set<Integer> keys = this.mExtensions.keySet();
        int[] result = new int[keys.size()];
        int i = 0;
        for (Integer id : keys) {
            result[i] = id;
            i++;
        }
        return result;
    }

    public Collection<Extension> getAllExtensions() {
        return this.mExtensions.values();
    }

    public String getName(int id) {
        Extension extension = this.mExtensions.get(id);
        if (extension == null) {
            return null;
        }
        return extension.getName();
    }

    public String getExtensionType(int id) {
        Extension extension = this.mExtensions.get(id);
        if (extension == null) {
            return null;
        }
        return extension.getExtensionType();
    }

    public int getFeatures(int id) {
        Extension extension = this.mExtensions.get(id);
        if (extension == null) {
            return 0;
        }
        return extension.getFeatures();
    }

    public boolean sendMessage(int id, String msg, String options) {
        Extension extension = this.mExtensions.get(id);
        if (extension == null) {
            return false;
        }
        return extension.sendMessage(msg, options);
    }

    public boolean show(int id, String what, String options) {
        Extension extension = this.mExtensions.get(id);
        if (extension == null) {
            return false;
        }
        return extension.show(what, options);
    }

    public boolean hide(int id, String what, String options) {
        Extension extension = this.mExtensions.get(id);
        if (extension == null) {
            return false;
        }
        return extension.hide(what, options);
    }

    public void onStart() {
        for (Extension extension : this.mExtensions.values()) {
            try {
                extension.onStart();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public void onPause() {
        for (Extension extension : this.mExtensions.values()) {
            try {
                extension.onPause();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public void onResume() {
        for (Extension extension : this.mExtensions.values()) {
            try {
                extension.onResume();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public void onStop() {
        for (Extension extension : this.mExtensions.values()) {
            try {
                extension.onStop();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public void onDestroy() {
        for (Extension extension : this.mExtensions.values()) {
            try {
                extension.onDestroy();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        this.mExtensions.clear();
    }
}