package cn.uc.gamesdk.jni;

import org.json.JSONException;
import org.json.JSONObject;
import com.libcore.utils.UIThread;
import com.thirdparty.login.ILoginListener;
import com.thirdparty.login.ThirdPartyUserInfo;
import com.uc.UCWrapper;

import android.util.Log;
import cn.uc.gamesdk.open.UCCallbackListener;
import cn.uc.gamesdk.open.UCGameSdkStatusCode;

public class LoginResultListener implements UCCallbackListener<String> {
    private final static String TAG = "JNI_LoginResultListener";

    private static LoginResultListener _instance = null;
    
    public static LoginResultListener getInstance() {
        if (_instance == null) {
            _instance = new LoginResultListener();
        }
        return _instance;
    }


    @Override
    public void callback(int code, String msg) {
        Log.d(TAG, "UCGameSDK login result: code=" + code + ", msg=" + msg);
        switch (code) {
        case UCGameSdkStatusCode.SUCCESS:
        	String sid = GameSdk.getSid();
        	Log.d(TAG, "UCGameSDK login result: 登陆成功"+sid);      
            
           
            GameSdk.showFloatButton(0, 80, true);
            ILoginListener listener=UCWrapper.getLoginListener();
            ThirdPartyUserInfo info=new ThirdPartyUserInfo();
            info.id=sid;            
            listener.onLoginFinished(true, info);
            break;
        case UCGameSdkStatusCode.NO_INIT:
        	Log.d(TAG, "UCGameSDK login result: 登陆失败，没初始化");
        	reLogin();
            break;
        case UCGameSdkStatusCode.LOGIN_EXIT:
            break;
        case UCGameSdkStatusCode.NO_LOGIN:
            Log.d(TAG, "未登录成功，需要游戏重新调用登录方法");           
            break;
        default:
            break;
        }

//        try {
//            Log.d(TAG, "callback login result to C++, code=" + code + ", msg=" + msg);
            //JniCallback.nativeLoginResultCallback(code, msg);
//        } catch (Throwable e) {
//            Log.e(TAG, e.getMessage(), e);
//        }
    }
    //登陆失败时重新登陆
    public void reLogin(){    
    	 final Runnable runnable = new Runnable() {
    	        @Override
    	        public void run() {
    	            GameSdk.login();
    	        }};
    	 UIThread.runDelayed(runnable, 100);    	
    }
}