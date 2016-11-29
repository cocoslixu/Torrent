package com.uc;


import org.json.JSONObject;
import org.json.JSONTokener;

import cn.uc.gamesdk.jni.GameSdk;

import com.base.GameActivityBase;
import com.thirdparty.IInitThirdPartySDK;
import com.thirdparty.ISubmitExtendData;
import com.thirdparty.exit.IExitListener;
import com.thirdparty.exit.IExitable;
import com.thirdparty.login.ILogin;
import com.thirdparty.login.ILoginListener;
import com.thirdparty.login.ILogoutListener;
import com.thirdparty.payment.BuyInfo;
import com.thirdparty.payment.IPayable;
import com.thirdparty.payment.IPaymentListener;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.util.Log;

public class UCWrapper implements ILogin,IExitable,IPayable,ISubmitExtendData,IInitThirdPartySDK{
	private static final String TAG = UCWrapper.class.getSimpleName();
	private static Activity mActivity = (Activity) GameActivityBase.getInstance();
	private static ILoginListener mLoginListener=null;	
	private static IPaymentListener mPaymentListener=null;
	
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
		GameSdk.exitSDK();
    }
	
	@Override
	public void onCreate(Activity activity) {
		mActivity = activity;
		GameSdk.setCurrentActivity(activity);				
		//GameSdk.initSDK(false, 2, 623700,0,true,true,1);						
	}   
	@Override
	public boolean initThirdPartySDK(){				
		GameSdk.initSDK(false, 2, 623700,0,true,true,1);	
		return true;
	}
	@Override
	public void onCreate(Application app) {	
	}

	@Override
	public void onDestroy() {		
		System.out.println("============onDestroy=============");
        GameSdk.destroyFloatButton();//销毁悬浮按钮                        
	}
	
	@Override
    public void onTerminate() {             
    }
	
	@Override
	public boolean login(ILoginListener listener) {
		mLoginListener=listener;
		//
		GameSdk.login();
		return true;
	}

	@Override
	public void logout(ILogoutListener listener) {			
		GameSdk.logout();	
		listener.onLogoutFinished(true,"");
	}
	@Override
	public boolean isCanExit() {
		return true;
	}
	
	@Override
	public void pay(BuyInfo info,final IPaymentListener arg1) {
		mPaymentListener=arg1;
		GameSdk.pay(true,info.productPrice, 0, info.playerId, info.playerName, "0", info.orderId, info.notifyurl, info.orderId);
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
	public void submitExtendData(String data) {	
		try {
			JSONTokener jsonParser = new JSONTokener(data);
			JSONObject jsonExData = (JSONObject) jsonParser.nextValue();	
			String submitType=jsonExData.getString("submitType");	
			if(submitType.equals("login")||submitType.equals("levelup")){
				GameSdk.submitExtendData("loginGameRole", jsonExData);	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	@Override
	public void changeAccount() {
		// TODO Auto-generated method stub
		
	}		
}
