package cn.uc.gamesdk.jni;

import android.util.Log;
import cn.uc.gamesdk.open.UCCallbackListener;


public class FloatMenuListener implements UCCallbackListener<String> {
    private final static String TAG = "JNI_FloatMenuListener";
    
    private static FloatMenuListener _instance = null;
    
    public static FloatMenuListener getInstance() {
        if (_instance == null) {
            _instance = new FloatMenuListener();
        }
        return _instance;
    }


    @Override
    public void callback(int code, String msg) {
        Log.d(TAG, "Received float menu operation: code=" + code + ", msg=" + msg);

        //收到SDK界面打开或关闭消息
        try {
            Log.d(TAG, "callback float menu notification to C++, code=" + code + ", msg=" + msg);
            //JniCallback.nativeFloatMenuCallback(code, msg);
        } catch (Throwable e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }
 
}
