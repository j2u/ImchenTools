package com.imchen.anhino;

import android.os.Environment;
import android.util.Log;

import com.imchen.anhino.utils.FileUtil;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.annotations.JSFunction;

import java.io.File;

/**
 * Created by imchen on 2017/11/23.
 */

public class JSEngine {

    private static String TAG="JSEngine";
    public static Context rhino;
    public static Scriptable scope;
    private  final static String REPORT_RESULT_PATH= Environment.getExternalStorageDirectory()+"/script.log";
    private static JSEngine JSEngine;

    private  JSEngine() {
        rhino = Context.enter();
//        scope = rhino.initSafeStandardObjects();
        ImporterTopLevel importerTopLevel=new ImporterTopLevel();
        importerTopLevel.initStandardObjects(rhino,false);
        scope=importerTopLevel;
        rhino.setOptimizationLevel(-1);
    }

    public static JSEngine getInstance(){
        if (JSEngine==null){
            JSEngine=new JSEngine();
        }
        return JSEngine;
    }

    public static boolean execJS(String script,String name) {
//        Log.d(TAG, "execJS: "+script);
        Object obj=rhino.evaluateString(scope,script,"<"+name+">",1,null);
        Log.d(TAG, "execJS: "+rhino.toString(obj));
        return false;
    }

    public static void execJS(String script){
        execJS(script,"cmd");
    }

    public static void execJS(File filePath) {
        String js = FileUtil.readFile(filePath.getPath());
        execJS(js,filePath.getName());
    }

    public static void javaToJS(){
        Object outObj=Context.javaToJS(System.out,scope);
        ScriptableObject.putProperty(scope,"out",outObj);
//        Object logObj=Context.javaToJS(,scope);
//        ScriptableObject.putProperty(scope,"f",logObj);
    }
}
