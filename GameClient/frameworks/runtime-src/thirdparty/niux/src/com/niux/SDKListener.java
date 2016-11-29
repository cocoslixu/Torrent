package com.niux;

import android.app.Activity;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import com.thirdparty.login.ILoginListener;
import com.thirdparty.login.ThirdPartyUserInfo;
import com.thirdparty.payment.IPaymentListener;
import com.thirdparty.payment.PaymentManager;
import com.xunlei.niux.mobilegame.sdk.constants.InitResult;
import com.xunlei.niux.mobilegame.sdk.constants.LoginResult;
import com.xunlei.niux.mobilegame.sdk.listener.NiuxMobileGameListener;
import com.xunlei.niux.mobilegame.sdk.platform.NiuxMobileGame;
import com.xunlei.niux.mobilegame.sdk.util.DialogUtils;
import com.xunlei.niux.mobilegame.sdk.vo.User;


public class SDKListener extends NiuxMobileGameListener {
    private static final String TAG = SDKListener.class.getSimpleName();
    private Activity activity;

    public SDKListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onSDKInitialized(int result) {
        super.onSDKInitialized(result);
        Log.d(TAG, "result = " + result);

        NiuxMobileGame.getInstance().hideWaiting();
        switch (result) {
            case InitResult.Success: //初始化成功
                Log.d(TAG, InitResult.getResultMessage(result));
                //LoginManager.login();
                break;
            case InitResult.Missing_Parameter:
            case InitResult.Oauth_Failed:
            case InitResult.Network_Failed:
                DialogUtils.alert(activity, null, InitResult.getResultMessage(result), null, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

                Log.d(TAG, InitResult.getResultMessage(result));
        }
    }

    @Override
    public void onAcitvityFinish(boolean needShowAd) {
        super.onAcitvityFinish(needShowAd);

        if(needShowAd){
            NiuxMobileGame.getInstance().showLoginAdDialog();
        }

        Log.d(TAG, "退出SDK界面");
    }

    // 此方法在用户登录完成后调用
    @Override
    public void onLoginFinish(int result, User user) {
        super.onLoginFinish(result, user);
        Log.d(TAG,"登录用户Id:"+user.getUid());
        ILoginListener listener= NiuxWrapper.getLoginListener();
        if(NiuxMobileGame.getInstance().isLogin()){
            Log.d(TAG, "用户登入成功");
            Log.d(TAG, user.toString());
            if(listener!=null){            	
            	ThirdPartyUserInfo info=new ThirdPartyUserInfo();
            	info.id=user.getCustomerId();
            	info.userName=user.getNickname();
            	info.key=user.getCustomerKey();
            	listener.onLoginFinished( true,info);            	
            }            
        }else {
            Log.d(TAG, "用户登入失败: " + LoginResult.getResultMessage(result));
            if(listener!=null){
            	listener.onLoginFinished( false,null);
            }
        }
    }

    // 此方法只是提示游戏方用户充值操作已经完成
    // 具体游戏币发放的通知将由联运的充值服务器通知
    @Override
    public void onChargeFinish(int money, String serverid, String orderid) {
        super.onChargeFinish(money, serverid, orderid);
        Log.d(TAG, "充值结束");
        Log.d(TAG, "money = " + money);
        Log.d(TAG, "serverid = " + serverid);
        Log.d(TAG, "orderid = " + orderid);
        IPaymentListener listener= NiuxWrapper.getPaymentListener();
        listener.onPaymentFinished(PaymentManager.PayOrder_Result_OK);
        //Toast.makeText(activity, "充值结束", Toast.LENGTH_LONG).show();
    }

    // 此方法只是提示游戏方用户开始充值操作
    @Override
    public void onChargeStart(int money, String serverid, String orderid) {
        super.onChargeStart(money, serverid, orderid);
        Log.d(TAG, "充值开始");
        Log.d(TAG, "money = " + money);
        Log.d(TAG, "serverid = " + serverid);
        Log.d(TAG, "orderid = " + orderid);        
    }

    //退出登录时调用
    @Override
    public void onLogout() {
        super.onLogout();
        if (!NiuxMobileGame.getInstance().isLogin()) {
            Log.d(TAG, "用户成功注销");
        }
    }

    //退出游戏时调用
    @Override
    public void onSDKFinish(){
        super.onSDKFinish();

        Log.d(TAG, "用户成功退出应用");
        activity.finish();
    }

    //开机广告弹窗关闭时调用
    @Override
    public void onLoginAdDialogClosed(){
        super.onLoginAdDialogClosed();

        Log.d(TAG, "开机广告弹窗已关闭");
    }
}
