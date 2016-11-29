package cn.uc.gamesdk.jni;

import android.util.Log;
import cn.uc.gamesdk.open.UCCallbackListener;
import cn.uc.gamesdk.open.UCGameSdkStatusCode;

public class EnterUIListener implements UCCallbackListener<String> {
    private final static String TAG = "JNI_EnterUIListener";
    
    private static EnterUIListener _instance = null;
    
    public static EnterUIListener getInstance() {
        if (_instance == null) {
            _instance = new EnterUIListener();
        }
        return _instance;
    }

    
    @Override
    public void callback(int code, String data) {
        Log.d(TAG, "Received sdk-ui notification: code=" + code + ", data=" + data);
        String msg = null;
        switch (code) {
        case UCGameSdkStatusCode.SUCCESS:
            msg = "SDK界面正常退出";
            break;

        case UCGameSdkStatusCode.NO_INIT:
            msg = "打开SDK界面失败，没有初始化";
            break;
        case UCGameSdkStatusCode.NO_LOGIN:
            msg = "打开SDK界面失败，没有登录";
            break;
        default:
            msg = "";
            break;
        }
        
        try{
            Log.d(TAG, "callback enterUI notification to C++, code=" + code + ", msg=" + msg);
            //JniCallback.nativeEnterUICallback(code, msg);
        } catch (Throwable e) {
            Log.e(TAG, "", e);
        }
    }
    
}