package com.imchen.ftpserver;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by imchen on 2017/10/24.
 */

public class PermissionUtil {

    public  final static int CALENDAR=0x001;
    public final static int CAMERA=0x002;
    public final static int CONTACTS=0x003;
    public final static int LOCATION=0x004;
    public final static int MICROPHONE=0x005;
    public final static int PHONE=0x006;
    public final static int SENSORS=0x007;
    public final static int SMS=0x008;
    public final static int STORAGE=0x009;


    public  static ArrayList<String> deniedLists=new ArrayList<>();
    public static ArrayList<String> grantedLists=new ArrayList<>();
    public static ArrayList<String> mNeedPermissionLists;
    public static onPermissionListener permissionListener;
    public  static boolean requestResult=true;

    public static void requestPermission(Activity activity,String[] permissions,int requestCode,ArrayList<String> needPermissionLists ,onPermissionListener listener){
        ActivityCompat.requestPermissions(activity,permissions,requestCode);
        mNeedPermissionLists=needPermissionLists;
        permissionListener=listener;
    }

    public static boolean checkPermission(Context context,String permission){
        return ContextCompat.checkSelfPermission(context,permission)== PackageManager.PERMISSION_GRANTED;
    }

    public static void afterRequestPermission(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        for (int i=0;i<grantResults.length;i++){
            if (grantResults[i]==PackageManager.PERMISSION_GRANTED){
                grantedLists.add(permissions[i]);
            }else{
                deniedLists.add(permissions[i]);
                if (mNeedPermissionLists.contains(permissions[i])){
                    requestResult=false;
                }
            }
        }
        if (!requestResult){
            permissionListener.onPermissionDenied();
        }else if (grantedLists.size()==0){
            permissionListener.onPermissionDenied();
        }else if (grantedLists.size()!=0&&deniedLists.size()!=0){
            permissionListener.onPermissionGranted();
        }else{
            permissionListener.onPermissionGranted();
        }

    }

    /**
     * 显示提示对话框
     */
    public static void showTipsDialog(final Context context) {
        new AlertDialog.Builder(context)
                .setTitle("提示信息")
                .setMessage("当前应用缺少必要权限，该功能暂时无法使用。如若需要，请单击【确定】按钮前往设置中心进行权限授权。")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings(context);
                    }
                }).show();
    }

    /**
     * 启动当前应用设置页面
     */
    private static void startAppSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivity(intent);
    }

    public interface onPermissionListener{
        void onPermissionGranted();
        void onPermissionDenied();
    }
}
