package com.nubia;


import java.util.HashMap;
import cn.nubia.componentsdk.constant.ConstantProgram;
import cn.nubia.nbgame.demo.util.ErrorMsgUtil;
import cn.nubia.nbgame.demo.util.MD5Signature;
import cn.nubia.nbgame.sdk.GameSdk;
import cn.nubia.nbgame.sdk.entities.AppInfo;
import cn.nubia.nbgame.sdk.entities.ErrorCode;
import cn.nubia.nbgame.sdk.interfaces.CallbackListener;
import com.nubia.AppConfig;
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
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


public class NubiaWrapper implements ILogin,IPayable{
	private static final String TAG = NubiaWrapper.class.getSimpleName();
	private static Activity mActivity = (Activity) GameActivityBase.getInstance();
	private static ILoginListener mLoginListener=null;	
	private static IPaymentListener mPaymentListener=null;
	private static final int REQUEST_STORAGE_PERMISSION_LOGIN = 1000;
    private static final int REQUEST_STORAGE_PERMISSION_PAY = 1001;
    
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
	public void onCreate(Activity activity) {
		mActivity = activity;
	}   	
	
	@Override
	public void onCreate(Application app) {	
		initSdk(app);
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
		this.doLogin();
		return true;
	}

	@Override
	public void logout(ILogoutListener listener) {			
		listener.onLogoutFinished(true,"");
	}

	@Override
	public void pay(BuyInfo info,final IPaymentListener arg1) {
		mPaymentListener=arg1;
		AppConfig.APP_SECRET=info.paykey;
		doPay(info);
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
	
	private void initSdk(final Application app){
        //如果当前进程不是主进程，则不执行初始化操作
        if(!isMainProcess(app)){
            return;
        }

        int appId = AppConfig.APP_ID; // 配置您自己的appid
        String appKey = AppConfig.APP_KEY;// 配置您自己 的appkey
        AppInfo appInfo = new AppInfo();
        appInfo.setAppId(appId);
        appInfo.setAppKey(appKey);
        appInfo.setChannelId(1); //选填，配置渠道,默认为1
        appInfo.setOrientation(0); //0：横屏；1：竖屏
        appInfo.setCanUseAdjunct(true);
        GameSdk.initSdk(app, appInfo,new CallbackListener<Bundle>() {
            @Override
            public void callback(int responseCode, Bundle bundle){
                String msg = "";
                if (responseCode == ErrorCode.SUCCESS) {
                    msg = "sdk初始化成功";
                } else {
                    msg = "sdk初始化失败（" + ErrorMsgUtil.translate(responseCode) + "）";
                }
                Log.d(TAG, msg);            
            }
        });
    }

    /**
     * 假设当前APP的包名为cn.nubia.nbgame.sdk，则获取到的进程名称也为：cn.nubia.nbgame.sdk；
     * 如果在AndroidManifest.xml的某个组件(activity,service,receiver,provider)中申明了android:process=":remote"，则获取到的进程名为：cn.nubia.nbgame.sdk:remote
     *
     * 当申明了独立进程名的组件被打开时，Android系统认为一个新的进程被触发了，会首先执行SdkApplication的onCreate()方法，从而导致再次执行初始化，
     * 因此需要通过进程名来控制，确保初始化只被调用一次
     *
     * @param context
     * @return
     */
    String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    /**
     * 建议将初始化、登录、支付等与联运SDK相关的操作放到主进程(即进程名为APP包名cn.nubia.nbgame.sdk的进程)
     * 原因是：不同的进程之间数据是隔离的，而登录或者支付失败等业务需要用到初始化完成后的数据
     * @param context
     * @return
     */
    private boolean isMainProcess(Context context){
        String pkgName = context.getPackageName();
        String curProcessName = getCurProcessName(context);
        if((null!=curProcessName)&&(curProcessName.equals(pkgName))){
            Log.i(TAG, "Application isMainProcess:"+curProcessName);
            return true;
        }else{
            Log.i(TAG, "Application isSubProcess:"+curProcessName);
            return false;
        }
    }
    private void doLogin() {
        if (GameSdk.isLogined()) {
            String msg = String.format("账号:%s 昵称：%s已登录，不需要重复登录。",
                    GameSdk.getLoginGameId(),
                    GameSdk.getNickName());
            //Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
            ThirdPartyUserInfo info=new ThirdPartyUserInfo();
        	info.id=GameSdk.getLoginGameId();
        	info.userName=GameSdk.getNickName();
        	info.key=GameSdk.getSessionId();
            mLoginListener.onLoginFinished(true, info);
            return;
        }
        GameSdk.openLoginActivity(mActivity, new CallbackListener<Bundle>() {
            @Override
            public void callback(int responseCode, Bundle bundle){
                switch (responseCode) {
                    case ErrorCode.SUCCESS:
                        //登陆成功，拿uid和sessionId去CP服务器完成角色创建或查询等操作
                        String msg = String.format("账号:%s 昵称:%s 登录", GameSdk.getLoginGameId(), GameSdk.getNickName());
                        showText(responseCode, msg);
                        ThirdPartyUserInfo info=new ThirdPartyUserInfo();
                    	info.id=GameSdk.getLoginGameId();
                    	info.userName=GameSdk.getNickName();
                    	info.key=GameSdk.getSessionId();
                        mLoginListener.onLoginFinished(true, info);
                        break;
                    case ErrorCode.NO_PERMISSION:
                        // Android6.0没允许安装和更新所需权限，需要运行时请求，主要是存储权限
                        Toast.makeText(mActivity, "登录需要安装努比亚联运中心服务，未获得安装和更新所需权限", Toast.LENGTH_SHORT).show();
//                        requestStoragePermission(REQUEST_STORAGE_PERMISSION_LOGIN);
                        mLoginListener.onLoginFinished(false, null);
                        break;
                    default:
                        // 登录失败，包含错误码和错误消息
                        Toast.makeText(mActivity, "登录失败：" + ErrorMsgUtil.translate(responseCode), Toast.LENGTH_SHORT).show();
                        mLoginListener.onLoginFinished(false, null);
                        break;
                }
            }
        });
    }
    private void showText(int responseCode, String str) {
        String msg = str;
        if (responseCode == ErrorCode.SUCCESS) {
            msg += "成功";
        } else {
            msg += "失败（错误码：" + responseCode + "）";
        }
        Log.d(TAG, msg);
        //Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
    }
//    private void requestStoragePermission(int requestCode) {
//        ActivityCompat.requestPermissions(mActivity,
//                new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
//    }
    /**
     * 支付
     */
    private void doPay(BuyInfo info) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(ConstantProgram.TOKEN_ID, GameSdk.getSessionId());
        map.put(ConstantProgram.UID, GameSdk.getLoginUid());
        map.put(ConstantProgram.APP_ID, String.valueOf(AppConfig.APP_ID));
        map.put(ConstantProgram.APP_KEY, AppConfig.APP_KEY);
        map.put(ConstantProgram.AMOUNT, info.productPrice+"");
        map.put(ConstantProgram.PRICE, info.productPrice+"");
        map.put(ConstantProgram.NUMBER, "1");
        map.put(ConstantProgram.PRODUCT_NAME, info.productName);
        map.put(ConstantProgram.PRODUCT_DES, info.productName);
        //map.put(ConstantProgram.PRODUCT_ID, "A01");
        //map.put(ConstantProgram.PRODUCT_UNIT, "个");
        String timeStamp = String.valueOf(System.currentTimeMillis());
        //由CP服务器返回的订单编号，订单号不能重复
        //String cpOrderId = "nb" + timeStamp;
        map.put(ConstantProgram.CP_ORDER_ID, info.orderId);
        // 为了安全性考虑，doSign最好在服务端执行, 时间戳在DATA_TIMESTAMP和签名两个地方传递的必须是一致的
        map.put(ConstantProgram.SIGN, MD5Signature.doSign(info.orderId, info.productName, "1", info.productPrice+"", GameSdk.getLoginUid(), timeStamp,info.productName));
        map.put(ConstantProgram.DATA_TIMESTAMP, timeStamp);
        map.put(ConstantProgram.CHANNEL_DIS, "1");
        map.put(ConstantProgram.GAME_ID, GameSdk.getLoginGameId());

        Log.i(TAG, "支付请求参数：" + map.toString());
        GameSdk.doPay(mActivity, map, new CallbackListener() {
            @Override
            public void callback(int responseCode, Object o) {
                switch (responseCode) {
                    case 0:
                        // 支付完成
                        showPayResult(responseCode, "支付成功!");
                        mPaymentListener.onPaymentFinished(PaymentManager.PayOrder_Result_OK);
                        break;
                    case -1:
                        // 本次支付失败
                        showPayResult(responseCode, "支付失败!"+ "{" + o + "}");
                        mPaymentListener.onPaymentFinished(PaymentManager.PayOrder_Result_Failed);
                        break;
                    case 10001:
                        // 用户取消了本次支付
                        showPayResult(responseCode, "您已经取消本次支付"+ "{" + o + "}");
                        mPaymentListener.onPaymentFinished(PaymentManager.PayOrder_Result_Cancelled);
                        break;
                    case 10002:
                        // 网络异常
                        showPayResult(responseCode, "网络异常，请检查网络设置"+ "{" + o + "}");
                        mPaymentListener.onPaymentFinished(PaymentManager.PayOrder_Result_Failed);
                        break;
                    case 10003:
                        // 订单结果确认中，建议客户端向自己业务服务器校验支付结果
                        showPayResult(responseCode, "支付结果确认中，请稍后查看"+ "{" + o + "}");
                        mPaymentListener.onPaymentFinished(PaymentManager.PayOrder_Result_Failed);
                        break;
                    case 10004:
                        // 支付服务正在升级
                        showPayResult(responseCode, "支付服务正在升级"+ "{" + o + "}");
                        mPaymentListener.onPaymentFinished(PaymentManager.PayOrder_Result_Failed);
                        break;
                    case 10005:
                        // 支付组件安装失败或是未安装
                        showPayResult(responseCode, "支付服务安装失败或未安装"+ "{" + o + "}");
                        mPaymentListener.onPaymentFinished(PaymentManager.PayOrder_Result_Failed);
                        break;
                    case 10006:
                        // 订单信息有误
                        showPayResult(responseCode, "订单信息有误"+ "{" + o + "}");
                        mPaymentListener.onPaymentFinished(PaymentManager.PayOrder_Result_Failed);
                        break;
                    case 10007:
                        // 获取支付渠道失败
                        showPayResult(responseCode, "获取支付渠道失败，请稍后重试"+ "{" + o + "}");
                        mPaymentListener.onPaymentFinished(PaymentManager.PayOrder_Result_Failed);
                        break;
                    case 10008:
                        // Android6.0没允许相关权限，需要运行时请求，主要是存储权限
                        Toast.makeText(mActivity, "未获得安装和更新所需权限", Toast.LENGTH_SHORT).show();
//                        requestStoragePermission(REQUEST_STORAGE_PERMISSION_PAY);
                        mPaymentListener.onPaymentFinished(PaymentManager.PayOrder_Result_Failed);
                        break;
                    default:
                        // 其他所有场景统一处理为支付失败
                        showPayResult(responseCode, "支付失败! " + "{" + o + "}");
                        mPaymentListener.onPaymentFinished(PaymentManager.PayOrder_Result_Failed);
                        break;
                }
            }
        });
    }
    public void showPayResult(int code, String payResult) {
        String msg =  payResult + " (错误码:" + code + ")";
        //Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
        Log.d(TAG,msg);
    }
}
