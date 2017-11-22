package com.imchen.j2va;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private  static String scriptFilePath= Environment.getExternalStorageDirectory()+"/test.js";

    private static final String TAG="MainActivity";
    public V8 runtime;
    private Context mContext;

    private TextView showPathTv;
    private EditText showScriptContentEt;
    private Button saveContentBtn;
    private Button executeContentBtn;

    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x0001:

            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();//init view
        setContentView(R.layout.activity_main);
        mContext=getApplicationContext();
        Log.d(TAG, "onCreate: "+scriptFilePath);
//        testJS();
    }

    public void testJS(){
        runtime=V8.createV8Runtime();
//        int result=runtime.executeIntegerScript("var a=0 \n" +
//                "var b=2 \n" +
//                "var c=3 \n"+
//                " a+b*c \n");
//        String js=readJS("sdcard/test.js");
//        Log.d(TAG, "testJS: "+js);
//        int result=runtime.executeIntegerScript(js);
//        Log.d(TAG, "testJS-----------------: "+result);

//        JavaCallback callback=new JavaCallback() {
//            @Override
//            public Object invoke(V8Object receiver, V8Array v8Array) {
//                if (v8Array.length()>0){
//                    receiver= (V8Object) v8Array.get(0);
//                    for (int i=1;i<v8Array.length();i++){
//                        Log.d(TAG, "invoke: "+v8Array.get(i));
//                    }
//                }
//                System.out.println(receiver.getString("first") + v8Array.get(0));
//                if (receiver instanceof Releasable){
//                    receiver.release();
//                }
//                return null;
//            }
//        };
//        runtime.registerJavaMethod(callback,"hello");
//        js="var array1 = [{first:'Ian'}, {first:'Jordi'}, {first:'Holger'}];\n" +
//                "for ( var i = 0; i < array1.length; i++ ) {\n" +
//                "  hello.call(array1[i], \" says Hi.\");\n" +
//                "}";
//        runtime.executeScript(js);
//        start();
//
        JavaCallback lunchAppCallback=new JavaCallback() {
            @Override
            public Object invoke(V8Object v8Object, V8Array v8Array) {
                Log.d(TAG, "lunchAppCallback: "+v8Array.get(0));
                lunchApp(v8Array.get(0).toString());
                return null;
            }
        };
        runtime.registerJavaMethod(lunchAppCallback,"lunchApp");
        runtime.executeScript(readJS("sdcard/test.js"));
        runtime.executeJSFunction("sum");
        runtime.release();
    }

    public void lunchApp(String packageName){
        PackageManager pm=getPackageManager();
        Intent lunchIntent=pm.getLaunchIntentForPackage(packageName);
        if (lunchIntent!=null){
            Toast.makeText(mContext,"lunch "+packageName,Toast.LENGTH_SHORT).show();
            startActivity(lunchIntent);
        }else{
            Toast.makeText(mContext,"Application not found!",Toast.LENGTH_SHORT).show();
        }
    }

    public String  readJS(String filePath){
        StringBuilder builder=new StringBuilder();
        try {
            FileInputStream in=new FileInputStream(filePath);
            BufferedReader buffer=new BufferedReader(new InputStreamReader(in,"UTF-8"));
            String line;

            while ((line=buffer.readLine())!=null){
                builder.append(line);
                builder.append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_save:
                break;
            case R.id.btn_exec:
                break;
        }
    }

    class Console{
        public void log(String str){
            System.out.println("log:"+str);
        }
        public void err(String str){
            System.out.println("err:"+str);
        }
    }
//reflect method register
    public void start(){
        Console console=new Console();
        V8Object v8obj=new V8Object(runtime);
        runtime.add("console",v8obj);
        v8obj.registerJavaMethod(console,"log","log",new Class<?>[]{String.class});
        v8obj.registerJavaMethod(console,"err","err",new Class<?>[]{String.class});
        v8obj.release();
        runtime.executeScript("console.log('hello,world!')");
    }

    public void initView(){
        showPathTv= (TextView) findViewById(R.id.tv_script_path);
        showScriptContentEt= (EditText) findViewById(R.id.et_script_content);
        saveContentBtn= (Button) findViewById(R.id.btn_save);
        executeContentBtn= (Button) findViewById(R.id.btn_exec);
    }

    public void updateView(){
        File scriptFile=new File(scriptFilePath);
        String info="";
        if (!scriptFile.exists()){
            info="script file not found , please create file in ExternalStorageDirectory!";
        }else{
            info="path:"+scriptFilePath;
        }
        Message msg=new Message();
        msg.what=0x0001;
        msg.obj=info;
        mHandler.sendMessage(msg);
    }

}
