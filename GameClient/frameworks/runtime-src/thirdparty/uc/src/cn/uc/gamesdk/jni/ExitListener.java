package cn.uc.gamesdk.jni;

import com.base.GameActivityBase;

import android.app.Activity;
import android.util.Log;
import cn.uc.gamesdk.open.UCCallbackListener;
import cn.uc.gamesdk.open.UCGameSdkStatusCode;

public class ExitListener implements UCCallbackListener<String> {
    private final static String TAG = "JNI_ExitListener";
    private static ExitListener _instance = null;

    public static ExitListener getInstance() {
        if (_instance == null) {
            _instance = new ExitListener();
        }
        return _instance;
    }

    @Override
    public void callback(int code, String msg) {
        Log.d(TAG, "UCGameSDK exit: code=" + code + ", msg=" + msg);

        switch (code) {
        case UCGameSdkStatusCode.SDK_EXIT:
        	GameSdk.destroyFloatButton();//销毁悬浮按钮                 	
	        System.exit(0);
            break;
        case UCGameSdkStatusCode.SDK_EXIT_CONTINUE:
            break;
        }

//        try {
//            Log.d(TAG, "callback exit to C++, code=" + code + ", msg=" + msg);
            //JniCallback.nativeExitCallback(code, msg);
//        } catch (Throwable e) {
//            Log.e(TAG, e.getMessage(), e);
//        }
    }
}