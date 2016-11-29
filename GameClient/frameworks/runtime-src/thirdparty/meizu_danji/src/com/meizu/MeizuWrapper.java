package com.meizu;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import com.base.GameActivityBase;
import com.meizu.gamesdk.model.callback.MzPayListener;
import com.meizu.gamesdk.model.model.MzPayParams;
import com.meizu.gamesdk.model.model.PayResultCode;
import com.meizu.gamesdk.offline.core.MzGameCenterPlatform;
import com.meizu.gamesdk.utils.MD5Utils;
import com.rt.BASE64Decoder;
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
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;


public class MeizuWrapper implements ILogin,IPayable{
	private static final String TAG = MeizuWrapper.class.getSimpleName();
	private static Activity mActivity = (Activity) GameActivityBase.getInstance();
	private static ILoginListener mLoginListener=null;	
	private static IPaymentListener mPaymentListener=null;
    MzPayListener mzPayListener=new MzPayListener(){
		@Override
		public void onPayResult(int code, Bundle info, String errorMsg) {
			String orderid = null;
			if (info != null) {
				orderid = info.getString(MzPayParams.ORDER_KEY_ORDER_ID);
			}
			if (code == PayResultCode.PAY_SUCCESS) {
				appendMsg("支付成功:" + orderid);
				mPaymentListener.onPaymentFinished(PaymentManager.PayOrder_Result_OK);
			} else if (code == PayResultCode.PAY_USE_SMS) {
				appendMsg("短信支付:" + orderid);
				mPaymentListener.onPaymentFinished(PaymentManager.PayOrder_Result_OK);				
			} else if (code == PayResultCode.PAY_ERROR_CANCEL) {
				appendMsg("用户取消:" + orderid);
				mPaymentListener.onPaymentFinished(PaymentManager.PayOrder_Result_Cancelled);
			} else if (code == PayResultCode.PAY_ERROR_CODE_DUPLICATE_PAY) {
				appendMsg("重复支付:" + orderid);
				mPaymentListener.onPaymentFinished(PaymentManager.PayOrder_Result_Failed);
			} else if (code == PayResultCode.PAY_ERROR_GAME_VERIFY_ERROR) {
				appendMsg(errorMsg);
				mPaymentListener.onPaymentFinished(PaymentManager.PayOrder_Result_Failed);
			} else {
				appendMsg("支付失败:" + code + "," + errorMsg);
				mPaymentListener.onPaymentFinished(PaymentManager.PayOrder_Result_Failed);
			}
			Log.i("MzGameSDK", errorMsg + code);				
		}			
	};
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
		MzGameCenterPlatform.orderQueryConfirm(activity,mzPayListener);
	}   	
	
	@Override
	public void onCreate(Application app) {	
		  MzGameCenterPlatform.init(app, GameConstants.GAME_ID, GameConstants.GAME_KEY);
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
	public void pay(BuyInfo buyinfo,final IPaymentListener arg1) {		
		mPaymentListener=arg1;
		Bundle payInfo = generatePayInfo(buyinfo);
		if (payInfo == null) {
			return;
		}
		Log.d(TAG, payInfo.toString());
		MzGameCenterPlatform.singlePay(mActivity, payInfo, mzPayListener);
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
	
	
	private Bundle generatePayInfo(BuyInfo info) {
		Bundle payInfo = new Bundle();					
		//
		String GAME_SECRET="";					 
		try {
			BASE64Decoder base64 = new BASE64Decoder();	
			byte    mydata[]= base64.decodeBuffer(info.value);
			GAME_SECRET = new String(mydata,"UTF-8");
			Log.d(TAG,"value:"+GAME_SECRET);
		} catch (Exception e) {				
			e.printStackTrace();
		}	
		    //
			String cpUserInfo = "";
			String totalPrice = "0.01";//String.valueOf(info.productPrice);
			String orderId = info.orderId;
			String productId = "1";
			String productSubject = info.productName;
			String productBody = "";
			int payType = 0;
			long createTiem = System.currentTimeMillis();
			StringBuilder builder = new StringBuilder();
			final String equalStr = "=";
			final String andStr = "&";
			builder.append("app_id" + equalStr + GameConstants.GAME_ID + andStr);
			builder.append("cp_order_id" + equalStr + orderId + andStr);
			builder.append("create_time" + equalStr + createTiem + andStr);
			builder.append("pay_type" + equalStr + payType + andStr);
			builder.append("product_body" + equalStr + productBody + andStr);
			builder.append("product_id" + equalStr + productId + andStr);
			builder.append("product_subject" + equalStr + productSubject + andStr);
			builder.append("total_price" + equalStr + totalPrice + andStr);
			builder.append("user_info" + equalStr + cpUserInfo);
			builder.append(":" + GAME_SECRET);
			String sign = MD5Utils.sign(builder.toString());
			Log.d(TAG,"sign:"+sign);
			//sign = object.getString("sign");//
			String signType = "md5";
			
			// appid
			payInfo.putString(MzPayParams.ORDER_KEY_ORDER_APPID, GameConstants.GAME_ID);
			// cp自定义信息
			payInfo.putString("user_info", cpUserInfo);
			payInfo.putString(MzPayParams.ORDER_KEY_CP_INFO, cpUserInfo);
			// 金额
			payInfo.putString(MzPayParams.ORDER_KEY_AMOUNT, totalPrice);
			// 订单id
			payInfo.putString(MzPayParams.ORDER_KEY_ORDER_ID, orderId);
			// 产品id
			payInfo.putString(MzPayParams.ORDER_KEY_PRODUCT_BODY, productBody);
			// 产品id
			payInfo.putString(MzPayParams.ORDER_KEY_PRODUCT_ID, productId);
			// 产品描述
			payInfo.putString(MzPayParams.ORDER_KEY_PRODUCT_SUBJECT, productSubject);
			//支付方式，默认值：”0”（即定额支付）
			payInfo.putInt(MzPayParams.ORDER_KEY_PAY_TYPE, payType);
			// 签名
			payInfo.putString(MzPayParams.ORDER_KEY_SIGN, sign);
			// 签名类型
			payInfo.putString(MzPayParams.ORDER_KEY_SIGN_TYPE, signType);
			// 是否关闭短信支付,默认是打开状态
			payInfo.putBoolean(MzPayParams.ORDER_KEY_DISABLE_PAY_TYPE_SMS,	true);
			payInfo.putLong(MzPayParams.ORDER_KEY_CREATE_TIME,createTiem);
			// 优先支付渠道 0:支付宝 1:微信 2:银联 3:短信
			payInfo.putInt(MzPayParams.ORDER_KEY_PAY_CHANNEL, 0);
			
			
		
		return payInfo;
	}
	
	
	private void appendMsg(final String msg) {
		Log.d(TAG,msg);
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
