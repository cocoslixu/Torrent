package com.niux;

import org.json.JSONObject;
import org.json.JSONTokener;
import com.base.GameActivityBase;
import com.thirdparty.ISubmitExtendData;
import com.thirdparty.exit.IExitListener;
import com.thirdparty.exit.IExitable;
import com.thirdparty.login.ILogin;
import com.thirdparty.login.ILoginListener;
import com.thirdparty.login.ILogoutListener;
import com.thirdparty.payment.BuyInfo;
import com.thirdparty.payment.IPayable;
import com.thirdparty.payment.IPaymentListener;
import com.xunlei.niux.mobilegame.sdk.listener.NiuxMobileGameListener;
import com.xunlei.niux.mobilegame.sdk.platform.NiuxMobileGame;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.util.Log;

public class NiuxWrapper implements ILogin,IExitable,IPayable,ISubmitExtendData{
	private static final String TAG = NiuxWrapper.class.getSimpleName();

	private static Activity mActivity = (Activity) GameActivityBase.getInstance();

	private NiuxMobileGameListener mListener;
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
		NiuxMobileGame.getInstance().createFloatView(activity);
	}	
	
	@Override
	public void onStop() {       
        Log.d(TAG, "onStop()");
        NiuxMobileGame.getInstance().destoryFloatView(mActivity);
    }
	
	@Override
    public void onExit(IExitListener listener){
        Log.d(TAG, "onExit()");
        if(NiuxMobileGame.getInstance().isLogin()){
            NiuxMobileGame.getInstance().showExitDialog(mActivity);          
        }
    }
	@Override
	public void onCreate(Activity activity) {
		mActivity = activity;	
		this.mListener = new SDKListener(activity);
		
	    String gameID = "050478";
        String channelID = "xunlei";
        String appKey = "uoEQJQbzfya69zrCgQUDUkAVOPTXSYHC";
        String gameName = "电玩城捕鱼";
        double exchangeRate = 1000;   //your exchangeRate
        String exchangeUnit = "金币";  //your exchangeUnit
        NiuxMobileGame.getInstance().initMobileGame(gameID, gameName, exchangeRate, exchangeUnit, channelID, appKey, this.mListener, activity); //必须要设置SDK,否则后面的方法就没法调用的
	}   
	
	@Override
	public void onCreate(Application app) {		
		NiuxMobileGame.getInstance().appInit(app);
	}

	@Override
	public void onDestroy() {		
		
	}
	
	@Override
    public void onTerminate() {      
        NiuxMobileGame.getInstance().appUnInit();
    }
	
	@Override
	public boolean login(ILoginListener listener) {
		mLoginListener=listener;
		if(NiuxMobileGame.getInstance().isLogin()){
			NiuxMobileGame.getInstance().logout(mActivity);	
		}
		NiuxMobileGame.getInstance().login(mActivity);	
		return true;
	}

	@Override
	public void logout(ILogoutListener listener) {	
		listener.onLogoutFinished(true,"");
		//NiuxMobileGame.getInstance().logout(mActivity);	
	}
	@Override
	public boolean isCanExit() {	
		return NiuxMobileGame.getInstance().isLogin();
	}
	@Override
	public void pay(BuyInfo info,final IPaymentListener arg1) {
		mPaymentListener=arg1;
		NiuxMobileGame.getInstance().chargeCenter(mActivity, info.playerId, "1",(int)info.productPrice,info.orderId);		
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
						
			String roleID=jsonExData.getString("roleId"); 
			String roleName=jsonExData.getString("roleName"); 
			String serverID=jsonExData.getString("zoneId");
			String submitType=jsonExData.getString("submitType");			
			Log.d(TAG,"submitExtendData:"+roleID+"-"+roleName+"-"+serverID+"-"+submitType);
			if(submitType.equals("login")){							
				NiuxMobileGame.getInstance().enterGame(mActivity,roleID, roleName, serverID);										
			}
			else if(submitType.equals("createrole")){				
				NiuxMobileGame.getInstance().createRole(mActivity,roleID, roleName, serverID);											
			}
		} catch (Exception e) {
			e.printStackTrace();
		}				
	}	
	
	@Override
	public void changeAccount()
	{
		
	}
}
