package com.jinli;


import com.base.GameActivityBase;
import com.gionee.game.offlinesdk.AppInfo;
import com.gionee.game.offlinesdk.GamePlatform;
import com.gionee.game.offlinesdk.InitPluginCallback;
import com.gionee.game.offlinesdk.MessagePayCallback;
import com.gionee.game.offlinesdk.OrderInfo;
import com.gionee.game.offlinesdk.PayCallback;
import com.thirdparty.login.ILogin;
import com.thirdparty.login.ILoginListener;
import com.thirdparty.login.ILogoutListener;
import com.thirdparty.login.ThirdPartyUserInfo;
import com.thirdparty.payment.BuyInfo;
import com.thirdparty.payment.IPayable;
import com.thirdparty.payment.IPaymentListener;
import com.thirdparty.payment.PaymentManager;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
import com.jinli.Constants;

public class JinliWrapper implements ILogin,IPayable{
	private static final String TAG = JinliWrapper.class.getSimpleName();
	private static Activity mActivity = (Activity) GameActivityBase.getInstance();
	private static ILoginListener mLoginListener=null;	
	private static IPaymentListener mPaymentListener=null;
	private boolean messagePay = false;
	
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
		 //调一下onActivityPause
	}
	
	@Override
	public void onResume(Activity activity) {
		 //调一下onActivityResume
	}	
	
	@Override
	public void onStop() {       
        Log.d(TAG, "onStop()");

    }
	
	
	@Override
	public void onCreate(Activity activity) {
		mActivity = activity;
		GamePlatform.getInstance().initPlugin(mActivity, mInitPluginCallback);		
	}   	
	
	@Override
	public void onCreate(Application app) {	
		 AppInfo appInfo = new AppInfo();
        appInfo.setApiKey(Constants.API_KEY);
        appInfo.setPrivateKey(Constants.PRIVATE_KEY);
        appInfo.setSpecificPayMode();
        GamePlatform.init(app, appInfo);
	}

	@Override
	public void onDestroy() {		
		System.out.println("============onDestroy=============");
		
	}
	
	@Override
    public void onTerminate() {             
    }
	
	@Override
	public boolean login(ILoginListener listener) {
		mLoginListener=listener;
		ThirdPartyUserInfo info=new ThirdPartyUserInfo();
		info.userName=getDeviceID();
		info.id=getDeviceID();
		info.key="";
		mLoginListener.onLoginFinished(true, info);
		return true;
	}

	@Override
	public void logout(ILogoutListener listener) {			
		listener.onLogoutFinished(true,"");
	}

	@Override
	public void pay(BuyInfo info,final IPaymentListener arg1) {		
		mPaymentListener=arg1;
		 OrderInfo orderInfo = createOrderInfo(info);

	        if(messagePay){
	            orderInfo.setMessagePayCallback(new MessagePayCallback() {
	                @Override
	                public void onCallback() {
	                    Toast.makeText(mActivity, "短信支付回调",	                            Toast.LENGTH_SHORT).show();
	                }
	            });
	        }

	        GamePlatform.getInstance().pay(mActivity, orderInfo, new PayCallback() {

	                @Override
	                public void onSuccess() {
	                    // 测试用，支付成功情况，请游戏更具实际情况处理
	                    //Toast.makeText(mActivity, "支付成功", Toast.LENGTH_SHORT).show();
	                	mPaymentListener.onPaymentFinished(PaymentManager.PayOrder_Result_OK);
	                }

	                @Override
	                public void onFail(String errCode, String errDescription) {
	                    // 测试用，支付失败情况，请游戏更具实际情况处理
	                    //Toast.makeText(mActivity, "支付失败：code：" + errCode + "， des：" + errDescription,	                            Toast.LENGTH_SHORT).show();
	                	mPaymentListener.onPaymentFinished(PaymentManager.PayOrder_Result_Failed);
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
	public void changeAccount() {
		// TODO Auto-generated method stub
		
	}		
	
	private InitPluginCallback mInitPluginCallback = new InitPluginCallback() {

        @Override
        public void onEvent(int event) {
            if (InitPluginCallback.EVENT_INIT_PLUGIN_FINISH == event) {
                // 插件初始化完成，请游戏根据实际情况处理
                //Toast.makeText(MainActivity.this, "插件初始化完毕", Toast.LENGTH_SHORT).show();
            }
        }
    };    

    private OrderInfo createOrderInfo(BuyInfo info) {
        String orderNum = info.orderId;
        String productName = info.productName;
        String totalFee = info.productPrice+"";
        String userId = info.playerId;

        // 设置订单信息
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCpOrderNum(orderNum);
        orderInfo.setProductName(productName);
        orderInfo.setTotalFee(totalFee);//
        orderInfo.setPayMethod(GamePlatform.PAY_METHOD_UNSPECIFIED);
        orderInfo.setUserId(userId);
        return orderInfo;
    }		
  /// 得到设备的DeviceID
    public String getDeviceID(){
		
        String dID = null;
		
		TelephonyManager tm = (TelephonyManager)mActivity.getSystemService(Context.TELEPHONY_SERVICE);
		
		dID = tm.getDeviceId();
		
		//// 如果IMEI没有获得，直接获得手机的机器地址
		if(null == dID || dID.length() == 0){
		
			WifiManager wm = (WifiManager)mActivity.getSystemService(Context.WIFI_SERVICE);
			dID = wm.getConnectionInfo().getMacAddress();
		}
		
//		Toast(dID);
		return dID;
	}
}
