package com.meizu;


import java.util.HashMap;

import org.json.JSONObject;



import com.meizu.GameConstants;
import com.meizu.UrlRequest;
import com.meizu.gamecenter.sdk.LoginResultCode;
import com.meizu.gamecenter.sdk.MzAccountInfo;
import com.meizu.gamecenter.sdk.MzBuyInfo;
import com.meizu.gamecenter.sdk.MzGameBarPlatform;
import com.meizu.gamecenter.sdk.MzGameCenterPlatform;
import com.meizu.gamecenter.sdk.MzLoginListener;
import com.meizu.gamecenter.sdk.MzPayListener;
import com.meizu.gamecenter.sdk.PayResultCode;
import com.base.GameActivityBase;
import com.thirdparty.login.ILogin;
import com.thirdparty.login.ILoginListener;
import com.thirdparty.login.ILogoutListener;
import com.thirdparty.login.ThirdPartyUserInfo;
import com.thirdparty.payment.BuyInfo;
import com.thirdparty.payment.IPayable;
import com.thirdparty.payment.IPaymentListener;
import com.thirdparty.payment.PaymentManager;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


public class MeizuWrapper implements ILogin,IPayable{
	private static final String TAG = MeizuWrapper.class.getSimpleName();
	private static Activity mActivity = (Activity) GameActivityBase.getInstance();
	private static ILoginListener mLoginListener=null;	
	private static IPaymentListener mPaymentListener=null;
	private String mUid="";
    //gamebar操作实例,不需要悬浮窗的可以不用
    MzGameBarPlatform mzGameBarPlatform;
    BuyInfo _info=null;
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
        mzGameBarPlatform.onActivityPause();
	}
	
	@Override
	public void onResume(Activity activity) {
		 //调一下onActivityResume
        mzGameBarPlatform.onActivityResume();
	}	
	
	@Override
	public void onStop() {       
        Log.d(TAG, "onStop()");

    }
	
	
	@Override
	public void onCreate(Activity activity) {
		mActivity = activity;
		
		//初始化，可以指定gamebar第一次显示的位置，在游戏退出时会记住用户操作的最后一次位置，再次启动时使用上一次的位置
        //第一次显示的位置可以指定四个方向，左上，左下，右上，右下
        //    public static final int GRAVITY_LEFT_TOP = 1;
        //    public static final int GRAVITY_LEFT_BOTTOM = 2;
        //    public static final int GRAVITY_RIGHT_TOP = 3;
        //    public static final int GRAVITY_RIGHT_BOTTOM = 4;
		//　游戏登录后不能显示悬浮窗问题应检查下系统是否允许魅族游戏框架使用悬浮窗权限（在MIUI中可能会遇到）,具体可查看接入文档
        mzGameBarPlatform = new MzGameBarPlatform(activity, MzGameBarPlatform.GRAVITY_RIGHT_BOTTOM);
	}   	
	
	@Override
	public void onCreate(Application app) {	
		// TODO 游戏初始化时，初始化参数
		MzGameCenterPlatform.init(app, GameConstants.GAME_ID, GameConstants.GAME_KEY);
	}

	@Override
	public void onDestroy() {		
		System.out.println("============onDestroy=============");
		// TODO 游戏退出调用该方法
				MzGameCenterPlatform.logout(mActivity);
				 //调一下onActivityDestroy
				//如果游戏用Sysetm.exit(0)或者killProcess方式退出，需在退出前主动调用mzGameBarPlatform.onActivityPause()和mzGameBarPlatform.onActivityDestroy();
				//否则可能会出现游戏退出后悬浮窗还在桌面
				mzGameBarPlatform.onActivityDestroy();
	}
	
	@Override
    public void onTerminate() {             
    }
	
	@Override
	public boolean login(ILoginListener listener) {
		mLoginListener=listener;
		MzGameCenterPlatform.login(mActivity, new MzLoginListener() {			
			@Override
			public void onLoginResult(int code, MzAccountInfo accountInfo, String errorMsg) {
				// TODO 登录结果回调，该回调跑在应用主线程
				switch(code){
				case LoginResultCode.LOGIN_SUCCESS:
					// TODO 登录成功，拿到uid 和 session到自己的服务器去校验session合法性
					mUid = accountInfo.getUid();
					displayMsg("登录成功！\r\n 用户名：" + accountInfo.getName() + "\r\n Uid：" + accountInfo.getUid() + "\r\n session：" + accountInfo.getSession());
					ThirdPartyUserInfo info=new ThirdPartyUserInfo();
					info.userName=accountInfo.getName();
					info.id=accountInfo.getUid();
					info.key=accountInfo.getSession();
					mLoginListener.onLoginFinished(true, info);
					break;
				case LoginResultCode.LOGIN_ERROR_CANCEL:
					// TODO 用户取消登陆操作
					break;
				default:
					// TODO 登陆失败，包含错误码和错误消息。
					// TODO 注意，错误消息需要由游戏展示给用户，错误码可以打印，供调试使用
					displayMsg("登录失败 : " + errorMsg + " , code = " + code);
					Toast.makeText(mActivity, "登录失败 : " + errorMsg + " , code = " + code, Toast.LENGTH_SHORT).show();
					break;
				}				
			}
		});
		return true;
	}

	@Override
	public void logout(ILogoutListener listener) {			
		listener.onLogoutFinished(true,"");
	}

	@Override
	public void pay(BuyInfo info,final IPaymentListener arg1) {		
		displayMsg("pay:"+info.value);
		_info=info;
		mPaymentListener=arg1;
		final ProgressDialog progressDialog = new ProgressDialog(mActivity);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage("请稍等...");
		progressDialog.setCancelable(true);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
		AsyncTask<Void, Void, MzBuyInfo> task = new AsyncTask<Void, Void, MzBuyInfo>(){
			@Override
			protected MzBuyInfo doInBackground(Void... params) {
				displayMsg("doInBackground");
				MzBuyInfo info = createOrder(_info.value);
				progressDialog.dismiss();
				return info;
			}

			@Override
			protected void onPostExecute(MzBuyInfo result) {
				if(result != null){
					startPay(result);
				}else{
					displayMsg("生成订单失败.");
				}
			}
			
		};
		task.execute();
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
	

	
	
	private MzBuyInfo createOrder(String res){
		if(!TextUtils.isEmpty(mUid)){
			try{
				// TODO 以下信息的获取需要游戏厂商访问自己的服务器，生成对应的订单信息
				//　其中cp_order_id、sign、sign_type、appid、uid参数不能为空或空字符串
				//String url = "http://183.56.146.87:8080/game/demo/createorder?uid=" + mUid + "&product_id=1&buy_amount=1&pay_type=0";
				//String res = UrlRequest.request(url);
				JSONObject object = new JSONObject(res);
				object = object.getJSONObject("value");
				displayMsg("createOrder : " + object);
				String orderId = object.getString("cp_order_id");
				String sign = object.getString("sign");
				String signType = object.getString("sign_type");
				int buyCount = object.getInt("buy_amount");
				String cpUserInfo = object.has("user_info") ? object.getString("user_info") : "";
				String total_price = object.has("total_price") ? object.getString("total_price") : "0";
				String productId = object.getString("product_id");
				String productSubject = object.getString("product_subject");
				String productBody = object.getString("product_body");
				String productUnit = object.getString("product_unit");
				String appid = object.getString("app_id");
				String uid = object.getString("uid");
				String perPrice = object.getString("product_per_price");
				long createTime = object.getLong("create_time");
				int payType = object.getInt("pay_type");
				return new MzBuyInfo().setBuyCount(buyCount).setCpUserInfo(cpUserInfo)
						.setOrderAmount(total_price).setOrderId(orderId).setPerPrice(perPrice)
						.setProductBody(productBody).setProductId(productId).setProductSubject(productSubject)
						.setProductUnit(productUnit).setSign(sign).setSignType(signType).setCreateTime(createTime)
						.setAppid(appid).setUserUid(uid).setPayType(payType);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private void startPay(MzBuyInfo buyInfo){
		displayMsg("startPay " );
		MzGameCenterPlatform.payOnline(mActivity, buyInfo, new MzPayListener() {			
			@Override
			public void onPayResult(int code, MzBuyInfo info, String errorMsg) {
				// TODO 支付结果回调，该回调跑在应用主线程
				switch(code){
				case PayResultCode.PAY_SUCCESS:
					// TODO 如果成功，接下去需要到自己的服务器查询订单结果
					displayMsg("支付成功 : " + info.getOrderId());
					mPaymentListener.onPaymentFinished(PaymentManager.PayOrder_Result_OK);
					break;
				case PayResultCode.PAY_ERROR_CANCEL:
					// TODO 用户取消支付操作
					mPaymentListener.onPaymentFinished(PaymentManager.PayOrder_Result_Cancelled);
					displayMsg("支付取消 ");
					break;
				default:
					// TODO 支付失败，包含错误码和错误消息。
					// TODO 注意，错误消息需要由游戏展示给用户，错误码可以打印，供调试使用
					displayMsg("支付失败 : " + errorMsg + " , code = " + code);
					mPaymentListener.onPaymentFinished(PaymentManager.PayOrder_Result_Failed);
					break;
				}
			}
		});
	}
	private void displayMsg(String msg){
		Log.d(TAG,msg);
	}
}
