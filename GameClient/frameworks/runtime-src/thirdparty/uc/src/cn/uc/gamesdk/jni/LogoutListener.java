package cn.uc.gamesdk.jni;

import android.util.Log;
import cn.uc.gamesdk.open.UCCallbackListener;


public class LogoutListener implements UCCallbackListener<String> {
    private final static String TAG = "JNI_LogoutListener";

    private static LogoutListener _instance = null;

    public static LogoutListener getInstance() {
        if (_instance == null) {
            _instance = new LogoutListener();
        }
        return _instance;
    }


    @Override
    public void callback(int code, String msg) {
        Log.d(TAG, "received logout notification: msg=" + msg);

//        try {
//            Log.d(TAG, "callback logout notification to C++, code=" + code + ", msg=" + msg);
            //JniCallback.nativeLogoutCallback(code, msg);
//        } catch (Throwable e) {
//            Log.e(TAG, e.getMessage(), e);
//        }    	
        GameSdk.showFloatButton(0, 0, false);
    }

}
