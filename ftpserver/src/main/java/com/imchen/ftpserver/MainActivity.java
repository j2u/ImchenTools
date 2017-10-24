package com.imchen.ftpserver;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    private static TextView mTipTv;
    private Button mStartBtn;
    private FtpServiceConnection conn = null;
    static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x0c1:
                    String result = (String) msg.obj;
                    mTipTv.setText(result);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
        initListener();
    }

    private void initListener() {
        mStartBtn = (Button) findViewById(R.id.btn_start_server);
        mTipTv = (TextView) findViewById(R.id.tv_tip);
        mStartBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_server:
                checkPermission();
                break;
        }
    }

    public void checkPermission() {

        ArrayList<String> permissionLists = new ArrayList<>();
        permissionLists.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissionLists.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (!PermissionUtil.checkPermission(mContext, Manifest.permission_group.STORAGE)) {
            PermissionUtil.requestPermission(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, PermissionUtil.STORAGE,
                    permissionLists, new PermissionUtil.onPermissionListener() {
                        @Override
                        public void onPermissionGranted() {
                           startFtpService();
                        }

                        @Override
                        public void onPermissionDenied() {
                            Toast.makeText(mContext,"Permission denied!can not start ftp server",Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private boolean startFtpService() {
        conn = new FtpServiceConnection();
        Intent intent = new Intent(MainActivity.this, FtpService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
        return false;
    }

    class FtpServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Toast.makeText(mContext, "bind service success!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Toast.makeText(mContext, "bind service failed!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.afterRequestPermission(requestCode,permissions,grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (conn != null) {
            unbindService(conn);
        }
    }
}
