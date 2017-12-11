package com.imchen.imchentools;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    public static  Context mContext;
    private static TextView testView;

    public static Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x1234:
                    testView.setText("fasf");
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext=getApplicationContext();
        String json=getDisplayInfo().toString();
        Log.d("oncreate", "onCreate: "+json);
    }

    public static JSONObject getDisplayInfo() {
        JSONObject json=null;
        try {
            // 这个方法获取可能不是真实屏幕的高度

            // 获取屏幕密度（方法2）
            DisplayMetrics dm = new DisplayMetrics();
            dm = mContext.getResources().getDisplayMetrics();

            float density  = dm.density;        // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
            int densityDPI = dm.densityDpi;     // 屏幕密度（每寸像素：120/160/240/320）
            float xdpi = dm.xdpi;
            float ydpi = dm.ydpi;

            Log.d("display","  DisplayMetrics"+ " xdpi=" + xdpi + " ydpi=" + ydpi);
            Log.d("display","  DisplayMetrics density=" + density + "; densityDPI=" + densityDPI);
            int screenWidth  = dm.widthPixels;      // 屏幕宽（像素，如：480px）
            int screenHeight = dm.heightPixels;     // 屏幕高（像素，如：800px）
            int statusBarHeight=getStatusBarHeight();
            Log.d("display", "screenWidth:" + screenWidth + " screenHeight:" + screenHeight + " statusbar: " + statusBarHeight);
//				Hook.log("screenWidth: " + activityHeight + " width: " + activityWidth);
            json=new JSONObject();
            JSONObject displayJSON=new JSONObject();
            displayJSON.put("statusBarHeight", statusBarHeight);
            displayJSON.put("screenWidth", screenWidth);
            displayJSON.put("screenHeight", screenHeight);
            json.put("display", displayJSON);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;

    }

    public static int getStatusBarHeight() {
        int result = 0;
        int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = mContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
    class test implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            mContext.getPackageManager();
        }
    }

    class MyConnection implements ServiceConnection{
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
}
