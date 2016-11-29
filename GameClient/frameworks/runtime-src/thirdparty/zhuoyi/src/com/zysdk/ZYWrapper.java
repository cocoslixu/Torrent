package com.zysdk;


import com.base.GameActivityBase;
import com.thirdparty.ISubmitExtendData;
import com.thirdparty.exit.IExitListener;
import com.thirdparty.exit.IExitable;
import com.thirdparty.login.ILogin;
import com.thirdparty.login.ILoginListener;
import com.thirdparty.login.ILogoutListener;
import com.thirdparty.login.LoginManager;
import com.thirdparty.login.ThirdPartyUserInfo;
import com.thirdparty.payment.BuyInfo;
import com.thirdparty.payment.IPayable;
import com.thirdparty.payment.IPaymentListener;
import com.thirdparty.payment.PaymentManager;
import com.zhuoyou.pay.sdk.ZYGameManager;
import com.zhuoyou.pay.sdk.account.UserInfo;
import com.zhuoyou.pay.sdk.entity.PayParams;
import com.zhuoyou.pay.sdk.listener.IZYLoginCheckListener;
import com.zhuoyou.pay.sdk.listener.ZYInitListener;
import com.zhuoyou.pay.sdk.listener.ZYLoginListener;
import com.zhuoyou.pay.sdk.listener.ZYRechargeListener;


import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class ZYWrapper implements ILogin,IExitable,IPayable,ISubmitExtendData{
	private static final String TAG = ZYWrapper.class.getSimpleName();
	private static Activity mActivity = (Activity) GameActivityBase.getInstance();
	private static ILoginListener mLoginListener=null;	
	private static IPaymentListener mPaymentListener=null;
	
	 private UserInfo mUserInfo = null;
	
	public static ILoginListener getLoginListener(){		
		return mLoginListener;
	}
	
	public static IPaymentListener getPaymentListener(){		
		return mPaymentListener;
	}	
	
	@Override
	public void configDeveloperInfo(String paramStr) {		
	}	

	
	@Override
	public void onPause() {
	}
	
	@Override
	public void onResume(Activity activity) {

	}	
	
	@Override
	public void onStop() {       
        Log.d(TAG, "onStop()");

    }
	
	@Override
    public void onExit(IExitListener listener){    
		Log.d(TAG, "onExit()");
//		GameSdk.exitSDK();
    }
	
	@Override
	public void onCreate(Activity activity) {
		mActivity = activity;		
		
	}   
	
	@Override
	public void onCreate(Application app) {	

	}

	@Override
	public void onDestroy() {		
		System.out.println("============onDestroy=============");
//		  ZYGameManager.onDestroy(mActivity);
	}
	
	@Override
    public void onTerminate() {             
    }
	
	@Override
	public boolean login(ILoginListener listener) {
		mLoginListener=listener;	
		
		//点击登陆
		ZYGameManager.login(mActivity, new ZYLoginListener() {

            @Override
            public void logout() {
//                Toast.makeText(mActivity, "帐号已登出，游戏回到主界面！！！！", 1000).show();
//            	 ZYGameManager.onDestroy(mActivity);
            	LoginManager.logout();
            }
            @Override
            public void login() {
                init();
            }
        }, ZYGameManager.LOIGN_THEME_LANDSCAPE);
        
		return true;
	}

	@Override
	public void logout(ILogoutListener listener) {	
		//登出调用
		ZYGameManager.onDestroy(mActivity);
		listener.onLogoutFinished(true,"");
		
	}
	@Override
	public boolean isCanExit() {
		return false;
	}
	
	@Override
	public void pay(BuyInfo info,final IPaymentListener arg1) {
		mPaymentListener=arg1;
		
		//初始化支付参数
		
        PayParams params=new PayParams();
        params.setAmount(info.productPrice);
        params.setPropsName(info.productName);
        params.setOrderId(info.orderId);
        params.setExtraParam("extra");

        ZYGameManager.pay(params, mActivity,new ZYRechargeListener() {

            @Override
            public void success(PayParams params, String zyOrderId) {
//                Toast.makeText(MainActivity.this, "QAQ支付成功 - "+zyOrderId, 1).show();
            	mPaymentListener.onPaymentFinished(PaymentManager.PayOrder_Result_OK);
            }

            @Override
            public void fail(PayParams params, String erroMsg) {
//                Toast.makeText(MainActivity.this, "QAQ支付失败"+erroMsg, 1).show();
            }

        });
		
	}	

	@Override
	public void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRestart() {
		// TODO Auto-generated method stub
		
	}	
	
	@Override
	public void changeAccount()
	{
		Log.i(TAG, "===doSwitchUser is called");

	}

	@Override
	public void submitExtendData(String data) {			
	}	
		
	
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        ZYGameManager.onConfigurationChanged(mActivity);
//    }

	/*---------------------此分界线以上实现接口逻辑，以下实现具体方法以及sdk回调----------------------*/

    private void init() {
        ZYGameManager.init(mActivity,new ZYInitListener() {
            @Override
            public void iniSuccess(UserInfo userInfo) {
//                Toast.makeText(mActivity, "初始化成功", Toast.LENGTH_SHORT).show();
                mUserInfo = userInfo;
                if (null != mUserInfo) {
                    ZYGameManager.loginCheck(mActivity, mUserInfo.getOpenId(), mUserInfo.getAccessToken(),
                            new IZYLoginCheckListener() {
                        @Override
                        public void checkResult(String code, String message) {
//                            Toast.makeText(MainActivity.this,
//                                    "登录验证结果 code ： " + code + " ,\n Message : " + message, 1000).show();
                            if (!TextUtils.isEmpty(code) && code.equals("0")) {
                                // 登录验证通过，进入游戏。。。。。
                                ThirdPartyUserInfo info=new ThirdPartyUserInfo();
              	            	info.id = mUserInfo.getOpenId()+"";
              	            	info.userName = mUserInfo.getNickName();
              	            	info.key = mUserInfo.getAccessToken();
              	            	mLoginListener.onLoginFinished(true, info);
                            }
                            else{
                            	mLoginListener.onLoginFinished(false, null);
                            }
                        }
                    });
                } else {
//                    Toast.makeText(mActivity, "请先初始化登录  ！！！", 1000).show();
                } 
            }

            @Override
            public void iniFail(String msg) {
//                Toast.makeText(mActivity, "初始化失败 " + msg, 1000).show();
            }
        });
    }
	

}


