package cn.uc.gamesdk.jni;

import com.thirdparty.ThirdPartyManager;
import android.util.Log;
import cn.uc.gamesdk.open.UCCallbackListener;
import cn.uc.gamesdk.open.UCGameSdkStatusCode;

public class InitResultListener implements UCCallbackListener<String> {
    private final static String TAG = "JNI_InitResultListener";

    private static InitResultListener _instance = null;

    public static InitResultListener getInstance() {
        if (_instance == null) {
            _instance = new InitResultListener();
        }
        return _instance;
    }


    @Override
    public void callback(int code, String msg) {
        Log.d(TAG, "UCGameSDK init result: code=" + code + ", msg=" + msg + "");
        switch (code) {
        case UCGameSdkStatusCode.SUCCESS:
            Log.d(TAG, "UCGameSDK init result: 初始化成功");          
            ThirdPartyManager.initSDKOver();
            GameSdk.createFloatButton();
            break;
        case UCGameSdkStatusCode.INIT_FAIL:
        	 Log.d(TAG, "UCGameSDK init result: 初始化失败");
            break;
        case UCGameSdkStatusCode.LOGIN_EXIT:
            //addOutputResult("login-exit", String.valueOf(code));
            break;
        default:
            //addOutputResult("login-fail", String.valueOf(code));
            break;
        }

//        try {
//            Log.d(TAG, "callback init notification to C++, code=" + code + ", msg=" + msg);
            //JniCallback.nativeInitResultCallback(code, msg);
//        } catch (Throwable e) {
//            Log.e(TAG, e.getMessage(), e);
//        }
    }

}