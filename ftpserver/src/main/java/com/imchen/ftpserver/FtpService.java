package com.imchen.ftpserver;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FtpService extends Service {
    private final static String TAG ="FtpService";
    private Context mContext;
    FtpServer server=null;
    ListenerFactory listenerFactory=null;
    private String homeDir= Environment.getExternalStorageDirectory().getAbsolutePath();
    public FtpService() {
    }

    @Override
    public void onCreate() {
        mContext=getApplicationContext();
        boolean result=startFtpServer();
        Log.d(TAG, "onCreate: ftp server start "+result);
        updateUI(result);
    }

    @Override
    public void onDestroy() {
        boolean result=stopFtpServer();
        Log.d(TAG, "onCreate: ftp server start "+result);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new MyBinder();
    }

    private boolean startFtpServer() {
        try {
            FtpServerFactory serverFactory = new FtpServerFactory();
            listenerFactory = new ListenerFactory();
            listenerFactory.setPort(3838);

            serverFactory.addListener("default", listenerFactory.createListener());
            BaseUser user=new BaseUser();
            user.setName("anonymous");
            user.setHomeDirectory(homeDir);
            List<Authority> authorities=new ArrayList<Authority>();
            authorities.add(new WritePermission());
            user.setAuthorities(authorities);
            serverFactory.getUserManager().save(user);
            server = serverFactory.createServer();
            server.start();
        } catch (FtpException e) {
            e.printStackTrace();
        }
        return !server.isStopped();
    }

    private boolean stopFtpServer(){
        boolean result=false;
        if (server!=null&&!server.isStopped()){
            server.stop();
            result=server.isStopped();
            server=null;
        }
        return result;
    }

    private void updateUI(boolean isStartFtp){
        Message msg=new Message();
        msg.what=0x0c1;
        if (isStartFtp){
            msg.obj="Ftp server start success! please visit: ftp://"+getIPAddress()+":"+listenerFactory.getPort();
        }else{
            msg.obj="Ftp server start failed!";
        }
        MainActivity.mHandler.sendMessage(msg);
    }

    private String getIPAddress(){
        WifiManager wm = (WifiManager) mContext.getSystemService(WIFI_SERVICE);
        int ipAddressInt = wm.getConnectionInfo().getIpAddress();
        String ipAddress = String.format(Locale.getDefault(), "%d.%d.%d.%d", (ipAddressInt & 0xff), (ipAddressInt >> 8 & 0xff), (ipAddressInt >> 16 & 0xff), (ipAddressInt >> 24 & 0xff));
        return  ipAddress;
    }

    class MyBinder extends Binder {
    }
}
