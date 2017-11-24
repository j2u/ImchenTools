package com.imchen.anhino;

import android.util.Log;

/**
 * Created by imchen on 2017/11/23.
 */

public class JFunction {
    private String TAG = "JFunction";

    public void log(String log) {
        Log.d(TAG, "log: ");
    }

    public void toast(String content) {
        Log.d(TAG, "toast: " + content);
    }

    public void alert(String title, String content) {
        Log.d(TAG, "alert: " + content);
    }

    public void launchApp(String packageName) {
        Log.d(TAG, "openApp: " + packageName);
    }

    public void reboot(String desc) {
        Log.d(TAG, "reboot: " + desc);
    }
}
