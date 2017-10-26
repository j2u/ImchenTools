package com.imchen.anhino;

import android.util.Log;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Created by imchen on 2017/10/26.
 */

public class test {

    private final static String TAG="test";

    public test() {

    }

    public void execJS(){
        Context rhino= Context.enter();
        Scriptable scope=rhino.initSafeStandardObjects();
        rhino.setOptimizationLevel(-1);
        String javaCallJs="function test(){" +
                "return 'hello world!'}";
        String jsCallJava="function callJ(){" +
                "return javaMethod('immammam')}";
        rhino.evaluateString(scope,readJS("sdcard/test.js"),"javaCallJs",1,null);
        Function function= (Function) scope.get("sum",scope);
        Object reuslt=function.call(rhino,scope,scope,new Object[]{});

        Log.d(TAG, "execJS: "+reuslt.toString());
//        ScriptableObject.putProperty(scope,javaCallJs,null);

    }

    public void javaMethod(String str){
        Log.d(TAG, "javaMethod: js call java method "+str);
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
}
