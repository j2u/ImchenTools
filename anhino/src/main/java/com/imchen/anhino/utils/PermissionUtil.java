package com.imchen.anhino.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;

/**
 * Created by imchen on 2017/11/22.
 */

public class PermissionUtil {

    public final static int CALENDAR = 0x001;
    public final static int CAMERA = 0x002;
    public final static int CONTACTS = 0x003;
    public final static int LOCATION = 0x004;
    public final static int MICROPHONE = 0x005;
    public final static int PHONE = 0x006;
    public final static int SENSORS = 0x007;
    public final static int SMS = 0x008;
    public final static int STORAGE = 0x009;

    public static onPermissionListener permissionListener;

    private static int[] neededPermission;

    public interface onPermissionListener {
        void onGrant();

        void onDenied();
    }

    public static void requestPermission(Activity activity, String[] permissions, int requestCode, onPermissionListener listener, int[] needed) {
        ArrayList<String> deniedPermission = new ArrayList<>();
        int[] grantResults=new int[permissions.length];
        if (listener != null) {
            permissionListener = listener;
        }
        if (needed.length != 0) {
            neededPermission = needed;
        }
        for (int i = 0; i < permissions.length; i++) {
            if (ActivityCompat.checkSelfPermission(activity, permissions[i]) == PackageManager.PERMISSION_DENIED) {
                 deniedPermission.add(permissions[i]);
            }else{
                grantResults[i]=PackageManager.PERMISSION_GRANTED;
            }

        }
        if (deniedPermission.size() > 0) {
            ActivityCompat.requestPermissions(activity, deniedPermission.toArray(new String[deniedPermission.size()]), requestCode);
        }else{
            onRequestPermissionsResult(requestCode,permissions,grantResults);
        }

    }

    public static void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean requestResult = false;
        ArrayList<String> deniedList = new ArrayList();
        int GRANT = PackageManager.PERMISSION_GRANTED;
        int DENIED = PackageManager.PERMISSION_DENIED;
        for (int i = 0; i < permissions.length; i++) {
            if (permissions.length == 1) {
                if (grantResults[i] == GRANT) {
                    permissionListener.onGrant();
                } else {
                    permissionListener.onDenied();
                }
            } else {
                if (grantResults[i] == DENIED) {
                    deniedList.add(permissions[i]);
                    permissionListener.onDenied();
                    return;
                } else {
                    requestResult = true;
                }
            }
        }
        if (requestResult) {
            permissionListener.onGrant();
        }
    }
}
