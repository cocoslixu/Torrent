package com.yyhsdk;


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
import com.yyh.sdk.AccountCallback;
import com.yyh.sdk.CPInfo;
import com.yyh.sdk.LoginCallback;
import com.yyh.sdk.PayParams;
import com.yyh.sdk.PayResultCallback;
import com.yyh.sdk.YYHSDKAPI;

import com.appchina.usersdk.ErrorMsg;
import com.appchina.usersdk.Account;
import com.appchina.usersdk.GlobalUtils;
import com.base.GameActivityBase;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.util.Log;

public class YYHWrapper implements ILogin,IExitable,IPayable,ISubmitExtendData{
	private static final String TAG = YYHWrapper.class.getSimpleName();
	private static Activity mActivity = (Activity) GameActivityBase.getInstance();
	private static ILoginListener mLoginListener=null;	
	private static IPaymentListener mPaymentListener=null;
	private boolean logoutFlag = true;
	private CPInfo mCpInfo;
	
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
		

		// 初始化CP信息
		initCpInfo();
		
		// 设置Debug模式，可在控制台查看SDK内部日志
		YYHSDKAPI.setDebugModel(false);
		
		//开启启动页，闪屏页需要安装要求在最外层调用，最少3秒中
	    YYHSDKAPI.startSplash(mActivity, mCpInfo.orientation, 3000);
	    
		//初始化SDk
	    initGameSDk();
	}   
	
	@Override
	public void onCreate(Application app) {	

	}

	@Override
	public void onDestroy() {		
		System.out.println("============onDestroy=============");
//        GameSdk.destroyFloatButton();//销毁悬浮按钮                
//        android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	@Override
    public void onTerminate() {             
    }
	
	@Override
	public boolean login(ILoginListener listener) {
		mLoginListener=listener;
		
		//点击登陆
		loginGame();
		return true;
	}

	@Override
	public void logout(ILogoutListener listener) {			
		//登出调用		
		if(logoutFlag)
		{
			YYHSDKAPI.logout();
		}
		else
		{
			logoutFlag = true;
		}
		listener.onLogoutFinished(true,"");
	}
	@Override
	public boolean isCanExit() {
		return YYHSDKAPI.isLogined();
	}
	
	@Override
	public void pay(BuyInfo info,final IPaymentListener arg1) {
		mPaymentListener=arg1;
		
		//初始化支付参数
		PayParams params = new PayParams();
		params.buildWaresid(info.waresid);          //必填
		params.buildCporderid(info.orderId);        //必填
		params.buildNotifyurl(info.notifyurl);      //可选
		
		//发起支付
		startPay(mActivity, params);
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
		
	/*---------------------此分界线以上实现接口逻辑，以下实现具体方法以及sdk回调----------------------*/

	
	/**
	 * 初始化CP信息
	 */
	private void initCpInfo() {
		mCpInfo = new CPInfo();
		
		// 接入应用汇帐号体系，必须设置needAccount为ture，并且loginId和loginKey与给定的值必须一一对应
		mCpInfo.needAccount = true;
		mCpInfo.loginId = 12539;
		mCpInfo.loginKey = "P5WcFbMLgdp8lb42";
		
		
		// 支付参数设置
		mCpInfo.appid = "5001609982";
		mCpInfo.privateKey = "MIICXQIBAAKBgQCuidLXDIUnIHsUn5wKHUZfyTwoSFtahSFmNiOmC7HCvv/q9NPki71EiU5LsZWuhrQAGuKpTcA/BmMvEGooQP8Y3PetLktCig8WVCyyg2nMlrFE+5vl2aJ9JA8hq6gMRUVbxH29lfKazth09o3BiyoFjsMAoh84HkbKROWRWrHUjwIDAQABAoGBAKNHQdt11fa1dw7jBbmci2I+Qi25CuUSmB0jVpQWapQx0/18TeGHLGc6P+ml1s4vK6LRg+s4Vjlfg1fa9kR0EW8nebEem9o72HDIr0MwHqH1pWQrip3oEujehbcFClyGLKOBcxP5FCYgt1INdSZwc4DpC2CwUzcijnfeIgXDAE/pAkEA4zO61BhgaPYFul+5vC+vG7R5/E9vNzHBzWS7czGyZRGN7fC2rYL2xci5Tbxzm2X7Pj2kYPO0x2SDw0fgHqdvBQJBAMSpQhVl5Ioz2TcrWkwMZ6VOLxYAjLhs7Zb3impYo7kBb3yOe4S4A9pORHCDbBG0fj4lsNpUjHJ/o2oWTMdRAYMCQEiw/E1vnyj1zc9bYSl8eCH9FCpNn9+g48i7Od4ZQGJlw2slYM0S4bFWDzN33+UQM9ouDrGWR3ikCCQqkuI736ECQQCH/2iktY7jB9H59e/+0UFHrpafR4gI7cMr+ElvG5c1FEoNv9apRDvd1uw7RpcQc1ouDhCMG5X4hE30yjEUUO+TAkAiZwcC/sqdlKrASAW+SrfUBLN/BylRYzR0tzMNQ00ohoRxpyBldMJMTLaUPCjFILLIxcisf5P9OnEsENjuF2EQ";
		mCpInfo.publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCGS7JwGwODy7MiNdQxr65dy5t5yqYTZka1aWl7PlRZDG5IoeFKyxARVa88X1GK2zdhRvqq0cWkZYH5sb+jyDQ1dkBWpTPXaRZ5l57BW2jAs582z6enaQw9GpBu7sr0UwIcyIsc1WpzQErgENc5DaEhk93j5VDVQ2ntfUu+EoJCawIDAQAB";
		
		// 横竖屏设置,只针对启动页和登陆页
		mCpInfo.orientation = CPInfo.LANDSCAPE;
	}
	
		/*----------初始化游戏sdk-----------*/
		private void initGameSDk(){
			YYHSDKAPI.singleInit(mActivity, mCpInfo, new AccountCallback() {
				@Override
				public void onSwitchAccount(Account old, Account curret) {
					// 在个人中心点击切换小号之后的回调
					Log.d(TAG, "old:"+old.userName + "\tn current:"+curret.userName);
				}

				@Override 
				public void onLogout() {
					// 在个人中心点击退出
					Log.d(TAG, "logout success");
					if(logoutFlag == false)
					{
//						logoutFlag = false;
						LoginManager.logout();
					}
					else
					{
						logoutFlag = false;
						LoginManager.logout();
					}
				}
			});
		}
		
		/*-----------点击登陆----------------*/
		private void loginGame(){			// 登录
		   YYHSDKAPI.login(mActivity, new LoginCallback() {
		   @Override 
		   public void onLoginSuccess(Activity activity, Account account) {
		      // 显示悬浮框
			    YYHSDKAPI.showToolbar(true);
			    // 登录成功
				GlobalUtils.showToast(activity, "登录成功");
			    
	            Log.i(TAG,"LoginCallback:onLoginSuccess");
	            if (account != null) {
	                ThirdPartyUserInfo info=new ThirdPartyUserInfo();
	            	info.id = account.userId+"";
	            	info.userName = account.nickName;
	            	info.key = account.ticket;
	            	mLoginListener.onLoginFinished(true, info);
	            	
	            } else {
	                Log.i(TAG,"LoginCallback:Login Failed");
	                mLoginListener.onLoginFinished(false, null);
//	                displayToast(mActivity, "登录失败");
	            }
			}

				@Override
				public void onLoginError(Activity activity, ErrorMsg error) {
				// 登录失败
				GlobalUtils.showToast(activity, error.message);
				     mLoginListener.onLoginFinished(false, null);
				}

				@Override
				public void onLoginCancel() {
				   // 取消登录
				   GlobalUtils.showToast(mActivity, "登录取消");
				}
				});
		}	
		
		/**发起支付*/
		public void startPay( final Activity activity , PayParams param) {
			YYHSDKAPI.startPay(activity, param, new PayResultCallback() {
				@Override
				public void onPaySuccess(int resultCode, String resultInfo) {
					Log.d(TAG, "支付成功");
					mPaymentListener.onPaymentFinished(PaymentManager.PayOrder_Result_OK);
				}
				
				@Override
				public void onPayFaild(int resultCode, String resultInfo) {
					Log.d(TAG, "支付失败");
				}
			});
		}

}


