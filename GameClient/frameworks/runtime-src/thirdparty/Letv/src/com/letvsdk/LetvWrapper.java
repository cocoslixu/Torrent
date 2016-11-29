package com.letvsdk;


import java.util.LinkedList;
import java.util.List;

import com.base.GameActivityBase;
import com.le.accountoauth.utils.LeUserInfo;
import com.le.legamesdk.LeGameSDK;
import com.le.legamesdk.LeGameSDK.ExitCallback;
import com.le.legamesdk.LeGameSDK.LoginCallback;
import com.le.legamesdk.LeGameSDK.PayCallback;
import com.letv.lepaysdk.smart.LePayInfo;
import com.thirdparty.exit.IExitListener;
import com.thirdparty.exit.IExitable;
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
import android.util.Log;
import android.widget.Toast;

public class LetvWrapper implements ILogin,IExitable,IPayable{
	
	static public List<Activity> mList = new LinkedList<Activity>();
	
	private String userId = null;
	private String accessToken = null;
	
	private static final String TAG = "LetvSDK";

	private LeGameSDK leGameSDK;
	private static Activity mActivity = (Activity) GameActivityBase.getInstance();

	private static ILoginListener mLoginListener=null;
	private static IPaymentListener mPaymentListener=null;
	
	@Override
	public void configDeveloperInfo(String paramStr) {		
	}	
	
	@Override
	public void onPause() {		
		Log.i(TAG, "===onPause is called===");
		leGameSDK.onPause(mActivity);
	}
	@Override
	public void onRestart() {
	
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	
	}
	
	
	@Override
	public void onResume(Activity activity) {
		leGameSDK.onResume(activity);
	}	
	
	@Override
	public void onStop() {		
		Log.i(TAG, "===onStop is called===");
	}
	
	@Override
    public void onExit(IExitListener listener){
		Log.i(TAG, "===onBackPressed is called");
		leGameSDK.onExit(mActivity, exitCallback);
    }
	
	@Override
	public void onCreate(Activity activity) {
		mActivity = activity;
		
		mList.add(activity);
		
		leGameSDK = LeGameSDK.getInstance();
		
		System.out.println(leGameSDK);
		
        leGameSDK.onCreate(mActivity, new LeGameSDK.ActionCallBack() {
            @Override
            public void onExitApplication() {
            	mActivity.finish();
            }
        });
	}   
	@Override
	public void onCreate(Application app) {	
		LeGameSDK.init(app.getApplicationContext());
	}
	
	@Override
    public void onTerminate() { 
    }
	
	@Override
	public void onDestroy() {	
		Log.i(TAG, "===onDestroy is called===");
		leGameSDK.onDestory(mActivity);
	}
	
	@Override
	public boolean login(ILoginListener listener) {
		Log.i(TAG, "===doLogin is called");
		mLoginListener = listener;
		leGameSDK.doLogin(mActivity, loginCallback, false);	
		return true;
	}
	
	@Override
	public void logout(ILogoutListener listener) {	
		listener.onLogoutFinished(true, null);	
	}
	
	@Override
	public boolean isCanExit() {		
		return true;
	}
	
		
	@Override
	public void pay(BuyInfo info,final IPaymentListener arg1) {
		mPaymentListener=arg1;
		Log.i(TAG, "==doPay is began==");
		doPay(info);
	}

	private void doPay(BuyInfo info) {
		Log.i(TAG, "==doPay is called==");
		displayToast(mActivity, "发起支付");
		
		LePayInfo payInfo = new LePayInfo();
		payInfo.setLetv_user_access_token(accessToken);
		payInfo.setLetv_user_id(userId);
		payInfo.setNotify_url(info.notifyurl);
		payInfo.setCooperator_order_no(info.orderId);
		payInfo.setPrice((info.productPrice+"").toString());
		payInfo.setProduct_name(info.productName);
		payInfo.setProduct_desc("1".toString());
		payInfo.setPay_expire("".toString());
		payInfo.setProduct_id((info.waresid+"").toString());
		payInfo.setCurrency("RMB".toString());
//		payInfo.setExtro_info(extra_info_edt.getText().toString());// cp閼奉亜鐣炬稊澶婂棘閺侊拷
		leGameSDK.doPay(mActivity, payInfo, payCallback);		
	}
	
	@Override
	public void changeAccount()
	{
		Log.i(TAG, "===doSwitchUser is called");

		// true表示切换账号
		leGameSDK.doLogin(mActivity, loginCallback, true);
	}
		
	private LoginCallback loginCallback = new LoginCallback() {
	        @Override
	        public void onLoginSuccess(LeUserInfo userInfo) {
	            Log.i(TAG,"LoginCallback:onLoginSuccess");
	            if (userInfo != null) {
	                // 获取access_token
	                accessToken = userInfo.getAccessToken();
	                // 获取letv_uid
	                userId = userInfo.getUserId();
	                String nickname = userInfo.getUserName();
//	                displayToast(mActivity, "登录成功\n" + "AccessToken："
//	                        + accessToken + "\nuserId:" + userId
//	                        + "\nNickName:" + nickname);
	                ThirdPartyUserInfo info=new ThirdPartyUserInfo();
	            	info.id = userId;
	            	info.userName = nickname;
	            	info.key = accessToken;
	            	mLoginListener.onLoginFinished(true, info);
	            	
	            } else {
	                Log.i(TAG,"LoginCallback:Login Failed");
	                mLoginListener.onLoginFinished(false, null);
//	                displayToast(mActivity, "登录失败");
	            }

	        }
	        @Override
	        public void onLoginFailure(int errorCode, String errorMsg) {
	            Log.i(TAG,"LoginCallback:onLoginFailure");
	            mLoginListener.onLoginFinished(false, null);
//	            displayToast(mActivity, "登录失败,错误代码:" + errorCode + "错误描述:" + errorMsg);
	        }
	        
	        @Override
	        public void onLoginCancel() {
	            Log.i(TAG,"LoginCallback:onLoginCancel");
//	            displayToast(mActivity, "取消登录");
	        }

	};
	
	private PayCallback payCallback = new PayCallback() {

		@Override
		public void onPayResult(String status, String errorMessage) {
			Log.i(TAG, "==onPayResult is called==");

//			Toast.makeText(mActivity, "Status:" + status + ", message" + errorMessage, Toast.LENGTH_LONG)
//					.show();

			if (status.equals("SUCCESS")) {
//				displayToast(mActivity, "支付成功，订单信息如下：\n" + errorMessage);
				mPaymentListener.onPaymentFinished(PaymentManager.PayOrder_Result_OK);
			} else if (status.equals("FAILT")) {
//				displayToast(mActivity, "支付失败，失败原因>>>" + errorMessage);
			} else if (status.equals("PAYED")) {
//				displayToast(mActivity,"已经支付，订单信息如下：\n" + errorMessage);
			} else if (status.equals("WAITTING")) {
//				displayToast(mActivity,"支付等待中，具体信息如下>>>" + errorMessage);
			} else if (status.equals("NONETWORK")) {
//				displayToast(mActivity, "无法支付，请检查网络>>>" + errorMessage);
			} else if (status.equals("NONE")) {
//				displayToast(mActivity, "支付失败，原因未知：\n" + errorMessage);
			} else if (status.equals("CANCEL")) {
//				displayToast(mActivity, "取消支付，具体信息>>>" + errorMessage);
			} else if (status.equals("COINLOCKUSER")) {
//				displayToast(mActivity, "用户游戏币支付受限>>>" + errorMessage);
			} else {
//				displayToast(mActivity, "出现未知情况");
			}
		}
	};

    private static Toast mToast;
    public static void displayToast(Context context, String msg) {
    	System.out.println("========="+msg.length());
    	if(msg == null || msg.length() < 1)
    		return;
        if (mToast == null) {
        	mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        } else {
        	mToast.setText(msg);
        }
        mToast.show();
    }

	
	private ExitCallback exitCallback = new ExitCallback() {

		@Override
		public void onSdkExitConfirmed() {
			 try {
		           for (Activity activity : mList) {
		              if (activity != null)
		                 activity.finish();
		                 }
		          } catch (Exception e) {
		                 e.printStackTrace();
		         } finally {
		               System.exit(0);
		         }
		}

		@Override
		public void onSdkExitCanceled() {
			Log.i(TAG, "===onSdkExitCanceled is called===");
//			displayToast(mActivity,"取消退出游戏");
		}
	};
	
}
