package cn.uc.gamesdk.jni;

import android.util.Log;
import cn.uc.gamesdk.open.UCCallbackListener;
import cn.uc.gamesdk.open.UCGameSdkStatusCode;

public class UserCenterListener implements UCCallbackListener<String> {
    private final static String TAG = "JNI_UserCenterListener";

    private static UserCenterListener _instance = null;

    public static UserCenterListener getInstance() {
        if (_instance == null) {
            _instance = new UserCenterListener();
        }
        return _instance;
    }


    @Override
    public void callback(int code, String data) {
        Log.d(TAG, "Received user center notification: code=" + code + ", data=" + data);
        String msg = null;
        switch (code) {
        case UCGameSdkStatusCode.SUCCESS:
            msg = "九游社区正常退出";
            break;

        case UCGameSdkStatusCode.NO_INIT:
            msg = "调用九游社区失败，没有初始化";
            break;
        case UCGameSdkStatusCode.NO_LOGIN:
            msg = "调用九游社区失败，没有登录";
            break;
        default:
            msg = "";
            break;
        }

        try {
            Log.d(TAG, "callback user center notification to C++, code=" + code + ", msg=" + msg);
            //JniCallback.nativeUserCenterCallback(code, msg);
        } catch (Throwable e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

}