package com.lin.childclient.control;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.DataOutputStream;
import java.lang.reflect.Method;

/**
 * Created by xinlyun on 16-1-18.
 */
public class PhoneControl {

    public static boolean execCmd(String command) {
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command+"\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if(process != null) {
                    process.destroy();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }


    public static void shutdown(Context context){
//        Intent.ACTION_ASSIST
        Intent intent = new Intent("android.intent.action.ACTION_REQUEST_SHUTDOWN");
        intent.putExtra("android.intent.extra.KEY_CONFIRM", false);
        //其中false换成true,会弹出是否关机的确认窗口
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    public static void shutOff(){
        try {

            //获得ServiceManager类
            Class ServiceManager = Class
                    .forName("android.os.ServiceManager");

            //获得ServiceManager的getService方法
            Method getService = ServiceManager.getMethod("getService", java.lang.String.class);

            //调用getService获取RemoteService
            Object oRemoteService = getService.invoke(null,Context.POWER_SERVICE);

            //获得IPowerManager.Stub类
            Class cStub = Class
                    .forName("android.os.IPowerManager$Stub");
            //获得asInterface方法
            Method asInterface = cStub.getMethod("asInterface", android.os.IBinder.class);
            //调用asInterface方法获取IPowerManager对象
            Object oIPowerManager = asInterface.invoke(null, oRemoteService);
            //获得shutdown()方法
            Method shutdown = oIPowerManager.getClass().getMethod("shutdown",boolean.class,boolean.class);
            //调用shutdown()方法
            shutdown.invoke(oIPowerManager,false,true);

        } catch (Exception e) {
            Log.e("shutOff", e.toString(), e);
        }
    }
}
