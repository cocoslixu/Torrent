package com.xlj;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.base.GameActivityBase;
import com.pepper.sdk.base.XljExitCallback;
import com.pepper.sdk.base.XljGameProxy;
import com.pepper.sdk.base.XljInitCallBack;
import com.pepper.sdk.base.XljLoginUser;
import com.pepper.sdk.base.XljLoginUserCallback;
import com.pepper.sdk.base.XljPayCallBack;
import com.pepper.sdk.base.XljPayParams;
import com.pepper.sdk.base.utils.MLog;
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
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class XljWrapper implements ILogin,IExitable,IPayable{
	private static final String TAG = XljWrapper.class.getSimpleName();
	private static Activity mActivity = (Activity) GameActivityBase.getInstance();
	private static ILoginListener mLoginListener=null;	
	private static IPaymentListener mPaymentListener=null;
	private XljLoginUser mUser;
    private boolean isLoginSuccess=false;
    
//    private int SDK_APPID = 100522;

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
		XljGameProxy.getInstance().onPause(mActivity);
	}
	
	@Override
	public void onResume(Activity activity) {
		XljGameProxy.getInstance().onResume(activity);
	}	
	@Override
    public void onRestart()
    {       
        XljGameProxy.getInstance().onRestart(mActivity);
    }
	@Override
	public void onStop() {       
        Log.d(TAG, "onStop()");
        XljGameProxy.getInstance().onStop(mActivity);
    }
	
	@Override
    public void onExit(IExitListener listener){    
		Log.d(TAG, "onExit()");
		doExit();
    }
	
	@Override
	public void onCreate(Activity activity) {
		mActivity = activity;
		MLog.isDebug = true;

        XljGameProxy.getInstance().initPepperSdk(activity, new XljInitCallBack()
        {
            @Override
            public void onResult(int code, String msg)
            {
            	Log.d(TAG, "initPepperSdk code:" + code + ",msg:" + msg);
            }

        });
        XljGameProxy.getInstance().onCreate(activity);				
	}   
	
	@Override
	public void onCreate(Application app) {	
	}

	@Override
	public void onDestroy() {		
		System.out.println("============onDestroy=============");
		XljGameProxy.getInstance().onDestroy(mActivity);
        android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	@Override
    public void onTerminate() {             
    }
	
	@Override
	public boolean login(ILoginListener listener) {
		mLoginListener=listener;
		//
		doLogin();
		return true;
	}

	@Override
	public void logout(ILogoutListener listener) {			
		doLogout();
		listener.onLogoutFinished(true,"");
	}
	@Override
	public boolean isCanExit() {
		return false;
	}
	
	@Override
	public void pay(BuyInfo info,final IPaymentListener arg1) {
		mPaymentListener=arg1;
		doPay((int)(info.productPrice*100),info.paykey,info.productName,info.orderId);
	}	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {       
        XljGameProxy.getInstance().onActivityResult(mActivity, requestCode, resultCode, data);
    }

	private void doLogin()
    {
        XljGameProxy.getInstance().login(mActivity, new XljLoginUserCallback()
        {
            @Override
            public void onLogout(Object customObject)
            {
                mUser = null;
                isLoginSuccess=false;

                MLog.d(TAG, "已成功登出");
                Toast.makeText(mActivity, "已成功登出", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLoginSuccess(XljLoginUser user, Object customObject)
            {
                isLoginSuccess=true;
                Toast.makeText(mActivity, "登陆成功", Toast.LENGTH_LONG).show();
                mUser = user;
                MLog.d(TAG, "渠道username: " + mUser.getChannelUserName() + "\nxlj_uid:" + mUser.getUserId() + "\ntoken: " + mUser.getToken() + "\n");
                // 登录成功后，进行登录信息校验，此步为必须完成操作，若不完成用户信息验证，平台拒绝登录
                // 需要CP用户服务端跟小辣椒的服务端进行检查登录
                ThirdPartyUserInfo info=new ThirdPartyUserInfo();
                info.id=mUser.getUserId();   
                info.key=mUser.getToken(); 
                info.userName=mUser.getChannelUserName();
                mLoginListener.onLoginFinished(true, info);
            }

            @Override
            public void onLoginFailed(String msg)
            {
                MLog.d(TAG, "登陆失败:" + msg);
                isLoginSuccess=false;

                Toast.makeText(mActivity, "登陆失败:" + msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    @SuppressLint("SimpleDateFormat")
    private void doPay(int amount,String paykey,String goodName,String orderID)
    {
    	try{
        // if (null==mUser)
        // {
        // mUser=new XljLoginUser("100028500", "13480745873",
        // "2e7adf250d014decb6e1118b0a2a84cf");
        // }
        if (!isLoginSuccess)
        {
            Toast.makeText(mActivity, "请先登录", Toast.LENGTH_LONG).show();
            return ;
        }
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        XljPayParams payParams = new XljPayParams();
        payParams.setAmount(amount);// 单位分
        payParams.setCpOrderId(orderID);
        payParams.setGoodsName(goodName);
        // 道具名称，会显示在支付界面，格式为：金额*兑换比率+ 商品名称如充值10元，兑换比率为1:10，则显示100xx钻石或者金币
        payParams.setGoodsDesc(goodName);
        payParams.setExtInfo("");        
        payParams.setSign(getSign(payParams,paykey));

        XljGameProxy.getInstance().startPay(mActivity, payParams, new XljPayCallBack()
        {
            @Override
            public void onPayCallback(int retCode, String paramString)
            {
                // retCode 状态码：0 支付成功， 1 支付失败， 2 支付取消，
                if (retCode == 0)
                {
                    Toast.makeText(mActivity, "支付成功:" + paramString, Toast.LENGTH_LONG).show();
                    mPaymentListener.onPaymentFinished(PaymentManager.PayOrder_Result_OK);
                }
                else if (retCode == 1)
                {
                    Toast.makeText(mActivity, "支付失败:" + paramString, Toast.LENGTH_LONG).show();
                    mPaymentListener.onPaymentFinished(PaymentManager.PayOrder_Result_Failed);
                }
                else if (retCode == 2)
                {
                    Toast.makeText(mActivity, "支付取消:" + paramString, Toast.LENGTH_LONG).show();
                    mPaymentListener.onPaymentFinished(PaymentManager.PayOrder_Result_Cancelled);
                }
                else
                {
                	mPaymentListener.onPaymentFinished(PaymentManager.PayOrder_Result_Failed);
                }
                MLog.d(TAG, "onPayCallback-->retCode:" + retCode + "," + paramString);

            }
        });
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
	public String getSign(XljPayParams payParams,String paykey)
    {
        Map<String, String> datas = new HashMap<String, String>();
        datas.put("uid", mUser.getUserId());
        datas.put("cpOrderId", payParams.getCpOrderId());
        datas.put("goodsName", payParams.getGoodsName());
        datas.put("goodsDesc", payParams.getGoodsDesc());
        datas.put("amount", String.valueOf(payParams.getAmount()));
        datas.put("extInfo", payParams.getExtInfo());        
        return Tools.toSortParamStr(datas,paykey);
    }

    /**
     * 登出接口说明：
     * 
     * @param activity
     *            当前activity
     * @param customObject
     *            用户自定义参数，在登陆回调中原样返回
     */
    private void doLogout()
    {
        XljGameProxy.getInstance().logout(mActivity);
    }

	 /**
     * 退出接口说明：
     * 
     * @param context
     *            当前activity
     * @param callback
     *            退出回调
     */
    private void doExit()
    {
        XljGameProxy.getInstance().exit(mActivity, new XljExitCallback()
        {

            @Override
            public void onExitSdk()
            {
                //CP自己的退出游戏处理
                //Toast.makeText(mActivity, "由cp退出", Toast.LENGTH_LONG).show();
                //mActivity.finish();

            }
        });
    }

	@Override
	public void changeAccount() {
		// TODO Auto-generated method stub
		
	}  
	
}
