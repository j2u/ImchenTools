package com.imchen.anhino;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.imchen.anhino.utils.FileUtil;
import com.imchen.anhino.utils.PermissionUtil;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static String scriptFilePath = Environment.getExternalStorageDirectory() + "/test.js";
    private static Context mContext;
    private TextView showPathTv;
    private EditText showScriptContentEt;
    private Button saveContentBtn;
    private Button executeContentBtn;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x0001:
                    Bundle bundle = msg.getData();
                    String info = bundle.getString("info");
                    String scriptContent = bundle.getString("content");
                    showPathTv.setText(info);
                    showScriptContentEt.setText(scriptContent);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
        initView();
        updateView();
        JSEngine.getInstance();
        JSEngine.javaToJS();
        JSEngine.execJS(new File(scriptFilePath));
        JSEngine.execJS(new File(Environment.getExternalStorageDirectory()+"/app.js"));
//        test test=new test();
//        test.execJS();
    }

    public void initView() {
        showPathTv = (TextView) findViewById(R.id.tv_script_path);
        showScriptContentEt = (EditText) findViewById(R.id.et_script_content);
        saveContentBtn = (Button) findViewById(R.id.btn_save);
        executeContentBtn = (Button) findViewById(R.id.btn_exec);
        saveContentBtn.setOnClickListener(this);
        executeContentBtn.setOnClickListener(this);
    }

    public void updateView() {
        File scriptFile = new File(scriptFilePath);
        String info = "";
        String scriptContent = null;
        if (!scriptFile.exists()) {
            info = "script file not found , please create file in ExternalStorageDirectory!";
        } else {
            info = "path:" + scriptFilePath;
            scriptContent = FileUtil.readFile(scriptFilePath);
        }
        Message msg = new Message();
        msg.what = 0x0001;
        Bundle bundle = new Bundle();
        bundle.putString("info", info);
        bundle.putString("content", scriptContent);
        msg.setData(bundle);
        mHandler.sendMessage(msg);
        new Thread(new Runnable() {
            @Override
            public void run() {
                PermissionUtil.requestPermission(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PermissionUtil.STORAGE
                        , new PermissionUtil.onPermissionListener() {
                            @Override
                            public void onGrant() {
                                generalScript(scriptFilePath);
                            }

                            @Override
                            public void onDenied() {
                                toast("Permission denied !!! Fuck");
                            }
                        }, new int[0]);
            }
        }).start();
    }

    public void generalScript(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            String parentPath = file.getParent();
            if (!new File(parentPath).exists()) {
                new File(parentPath).mkdirs();
            }
        }
        if (FileUtil.readFile(scriptFilePath)!=null){
            return;
        }
        String testJS="\n" +
                "module.exports = function(){\n" +
                "var launchApp=function(i){\n" +
                "jf.launchApp()\n" +
                "}\n" +
                "var log=function(i){\n" +
                "jf.toast(i)\n" +
                "}\n"+
                "}\n" +
                "\n";
        String script = "importPackage(java.lang)\n" +
                "var a=1\n" +
                "var b=3\n" +
                "request("+testJS+")" +
                "function sum(){\n" +
                "var c=a+b\n" +
                "java.lang.System.out.println(c)\n" +
                "toast(c)\n" +
                "}\n";
        FileUtil.writeFile(scriptFilePath, script);


//        FileUtil.writeFile(Environment.getExternalStorageDirectory()+"/app.js",testJS);
    }

    public static void toast(String content) {
        Toast.makeText(mContext, content, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //call permission util
        PermissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_save:
                int result=FileUtil.writeFile(scriptFilePath,showScriptContentEt.getText().toString());
                if (result==0){
                    toast("Save content success!");
                }
                break;
            case R.id.btn_exec:
                JSEngine.execJS(showScriptContentEt.getText().toString());
                break;
        }
    }
}
