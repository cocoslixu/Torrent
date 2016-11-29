package com.lemon;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.cocos2dx.lib.Cocos2dxHelper;
import org.json.JSONObject;
import org.json.JSONTokener;
import com.base.GameActivityBase;
import com.snowfish.cn.ganga.base.SFActionCallback;
import com.snowfish.cn.ganga.base.SFNativeAdapter;
import com.snowfish.cn.ganga.helper.SFOnlineExitListener;
import com.snowfish.cn.ganga.helper.SFOnlineHelper;
import com.snowfish.cn.ganga.helper.SFOnlineInitListener;
import com.snowfish.cn.ganga.helper.SFOnlineLoginListener;
import com.snowfish.cn.ganga.helper.SFOnlinePayResultListener;
import com.snowfish.cn.ganga.helper.SFOnlineUser;
import com.thirdparty.ISubmitExtendData;
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



public class LemonWrapper implements ILogin,IExitable,IPayable,ISubmitExtendData{
	private static final String TAG = LemonWrapper.class.getSimpleName();
	private static Activity mActivity = (Activity) GameActivityBase.getInstance();
	private static ILoginListener mLoginListener=null;	
	private static IPaymentListener mPaymentListener=null;
    private boolean isLoginSuccess=false;
    static LoginHelper helper = null;
   
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
		SFOnlineHelper.onPause(mActivity);
	}
	
	@Override
	public void onResume(Activity activity) {
		SFOnlineHelper.onResume(activity);	
	}	
	
	@Override
    public void onRestart()
    {       
		SFOnlineHelper.onRestart(mActivity);
    }
	
	@Override
	public void onStop() {       
        Log.d(TAG, "onStop()");
        SFOnlineHelper.onStop(mActivity);
    }
	
	@Override
    public void onExit(IExitListener listener){    
		Log.d(TAG, "onExit()");
		SFOnlineHelper.exit(mActivity, new SFOnlineExitListener() {
			/* onSDKExit
			* @description 当SDK有退出方法及界面，回调该函数
			* @param bool SDK是否退出标志位
			*/
			@Override
			public void onSDKExit(boolean bool) {
				Log.d(TAG, "onSDKExit()"+ bool);
				if (bool){
				//SDK已经退出，此处可以调用游戏的退出函数
					//System.exit(0);
				}
			}
			/* onNoExiterProvide
			* @description SDK没有退出方法及界面，回调该函数，可在此使用游戏退
			易接网游 SDK 中间件接入标准流程
			出界面
			 */
			@Override
			public void onNoExiterProvide() {
				Log.d(TAG, "onNoExiterProvide()");
				//System.exit(0);
			}
			});

    }
	
	@Override
	public void onCreate(Activity activity) {
		mActivity = activity;	
		//
		SFOnlineHelper.onCreate(mActivity,new SFOnlineInitListener() {
			@Override
			public void onResponse(String tag, String value) {
			if(tag.equalsIgnoreCase("success")){
			 //初始化成功的回调
				Log.d(TAG, "初始化成功");
			}else if(tag.equalsIgnoreCase("fail")){
			 //初始化失败的回调，value：如果SDK返回了失败的原因，会给value赋值
				Log.d(TAG, "初始化失败："+value);
			}
			}});
		//
		SFOnlineHelper.setLoginListener(activity, new SFOnlineLoginListener()
		{
			@Override
			public void onLoginSuccess(SFOnlineUser user, Object customParams)
			{
				if(helper != null){
					helper.setOnlineUser(user);
				}
				//LoginCheck(user);
				Log.d(TAG, "demo onLoginSuccess "+user.getChannelUserId()+"  "+ user.getUserName()+"  "+user.getToken());	
				ThirdPartyUserInfo info=new ThirdPartyUserInfo();
            	info.id=user.getChannelUserId();
            	info.userName=user.getUserName();
            	info.key=user.getToken();
				mLoginListener.onLoginFinished(true, info);
			}
			@Override
			public void onLoginFailed(String reason, Object customParams) {
				Log.d(TAG, "demo onLoginFailed:"+reason+", "+customParams );		
				Toast.makeText(mActivity, "账户登陆失败", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onLogout(Object customParams) {
				Log.d(TAG, "demo onLogout:" + customParams);
				//Toast.makeText(mActivity, "账户登出", Toast.LENGTH_SHORT).show();
				if(helper != null){
					helper.setOnlineUser(null);
					helper.setLogin(false);
//					helper.getHandler(mActivity).postDelayed(new Runnable() {				
//						@Override
//						public void run() {
//							Log.d(TAG, "logout in postAtTime");
//							SFOnlineHelper.login(mActivity, "Login");	
//						}
//					}, 200);
				}		
			}		
		});

		helper = LoginHelper.instance();		
	}   
	
	@Override
	public void onCreate(Application app) {	
	}

	@Override
	public void onDestroy() {		
		System.out.println("============onDestroy=============");
		SFOnlineHelper.onDestroy(mActivity);
	}
	
	@Override
    public void onTerminate() {             
    }
	
	@Override
	public boolean login(ILoginListener listener) {
		mLoginListener=listener;
		SFOnlineHelper.login(mActivity, "Login");
		return true;
	}

	@Override
	public void logout(ILogoutListener listener) {			
		listener.onLogoutFinished(true,"");
		SFOnlineHelper.logout(mActivity, "LoginOut");
	}
	@Override
	public boolean isCanExit() {
		return false;
	}
	
	@Override
	public void pay(BuyInfo info,final IPaymentListener arg1) {
		mPaymentListener=arg1;
		SFOnlineHelper.pay(mActivity, (int)(info.productPrice*100), info.productName, 1, info.orderId,info.notifyurl,//
				new SFOnlinePayResultListener() {
				@Override
				public void onSuccess(String remain) {
				   Toast.makeText(mActivity, "支付成功" , Toast.LENGTH_LONG).show();
                   mPaymentListener.onPaymentFinished(PaymentManager.PayOrder_Result_OK);	              
				}
				@Override
				public void onFailed(String remain) {   
					Toast.makeText(mActivity, "支付失败" , Toast.LENGTH_LONG).show();
	                mPaymentListener.onPaymentFinished(PaymentManager.PayOrder_Result_Failed);
				}
				@Override
				public void onOderNo(String arg0) {					
					Log.d(TAG, "onOderNo:" + arg0);
				}
				});

	}	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {       
    }
	
	@Override
	public void changeAccount() {
		// TODO Auto-generated method stub
		
	}


	/**
     *  LoginCheck
     *  从服务器端验证用户是否登陆
     * @param user 登陆账户
     */
	public void LoginCheck(final SFOnlineUser user) {
        if(helper == null){
        	Toast.makeText(mActivity, "Error, helper为null", Toast.LENGTH_SHORT).show();
        	return;
        }
		if (helper.isDebug()) {
			helper.setLogin(true);		
			return;
		}
		Log.d(TAG, "LoginCheck user:"+user.toString());
		new Thread(new Runnable() {
			@Override
			public void run() { 
				try {
					String url = LoginHelper.CP_LOGIN_CHECK_URL + createLoginURL();
					if (url == null)
						return;

 					String result = LoginHelper.executeHttpGet(url);
 					Log.d(TAG, "LoginCheck result:"+result);
					if (result == null || !result.equalsIgnoreCase("SUCCESS")) {
						if(helper != null){
							helper.setLogin(false);
						}
						LoginHelper.showMessage("未登录", mActivity);
					} else {
						if(helper != null){
							helper.setLogin(true);
							ThirdPartyUserInfo info=new ThirdPartyUserInfo();
			            	info.id=user.getChannelUserId();
			            	info.userName=user.getUserName();
			            	info.key=user.getToken();
							mLoginListener.onLoginFinished(true, info);
						}
					  /* 部分渠道如UC渠道，要对游戏人物数据进行统计，而且为接入规范，调用时间：在游戏角色登录成功后调用
					   *  public static void setRoleData(Context context, String roleId，
					   *  	String roleName, String roleLevel, String zoneId, String zoneName)
					   *  
					   *  @param context   上下文Activity
					   *  @param roleId    角色唯一标识
					   *  @param roleName  角色名
					   *  @param roleLevel 角色等级
					   *  @param zoneId    区域唯一标识
					   *  @param zoneName  区域名称
					   *  */
//						SFOnlineHelper.setRoleData(mActivity, "1",
//								"猎人", "100", "1", "阿狸一区");
						
//						JSONObject roleInfo = new JSONObject();
//						roleInfo.put("roleId", "1");         //当前登录的玩家角色ID，必须为数字
//						roleInfo.put("roleName", "猎人");  //当前登录的玩家角色名，不能为空，不能为null
//						roleInfo.put("roleLevel", "100");   //当前登录的玩家角色等级，必须为数字，且不能为0，若无，传入1
//						roleInfo.put("zoneId", "1");       //当前登录的游戏区服ID，必须为数字，且不能为0，若无，传入1
//						roleInfo.put("zoneName", "阿狸一区");//当前登录的游戏区服名称，不能为空，不能为null
//						roleInfo.put("balance", "0");   //用户游戏币余额，必须为数字，若无，传入0
//						roleInfo.put("vip", "1");            //当前用户VIP等级，必须为数字，若无，传入1
//						roleInfo.put("partyName", "无帮派");//当前角色所属帮派，不能为空，不能为null，若无，传入“无帮派”
//						roleInfo.put("roleCTime", "21322222");	 //单位为秒，创建角色的时间
//						roleInfo.put("roleLevelMTime", "54456556");	//单位为秒，角色等级变化时间
//						
//						SFOnlineHelper.setData(mActivity,"createrole",roleInfo.toString()); //  创建新角色时调用       必接
//						SFOnlineHelper.setData(mActivity,"levelup",roleInfo.toString());    //  玩家升级角色时调用     必接
//						SFOnlineHelper.setData(mActivity,"enterServer",roleInfo.toString());//  选择服务器进入时调用   必接
						
						LoginHelper.showMessage("已验证成功登录", mActivity);
//						UserLoginView.this.post(new Runnable() {							
//							@Override
//							public void run() {
//								goChargeView();								
//							}
//						});
					}
				} catch (Exception e) {
					Log.d(TAG, "LoginCheck ERROR:"+e.toString());
				}
			}
		}).start();
	}
	private String createLoginURL() throws UnsupportedEncodingException {
		if (helper == null || helper.getOnlineUser()  == null) {
			Toast.makeText(mActivity, "未登录", Toast.LENGTH_SHORT).show();
			return null;
		}
		SFOnlineUser user = helper.getOnlineUser();
		StringBuilder builder = new StringBuilder();
		builder.append("?app=");
		builder.append(user.getProductCode());
		builder.append("&sdk=");
		builder.append(user.getChannelId());
		builder.append("&uin=");
		builder.append(URLEncoder.encode(user.getChannelUserId(), "utf-8"));
		builder.append("&sess=");
		builder.append(URLEncoder.encode(user.getToken(), "utf-8"));
		return builder.toString();
	}

	@Override
	public void submitExtendData(String data) {
		try {
			JSONTokener jsonParser = new JSONTokener(data);
			JSONObject jsonExData = (JSONObject) jsonParser.nextValue();
			//
			String roleId=jsonExData.getString("roleId"); 
			String roleName=jsonExData.getString("roleName"); 
			String zoneId=jsonExData.getString("zoneId");
			String roleLevel=jsonExData.getString("roleLevel");
			//
			String submitType=jsonExData.getString("submitType");			
			if(submitType.equals("createrole")){					
				SFOnlineHelper.setData(mActivity,"createrole",jsonExData); //  创建新角色时调用       必接
			}
			if(submitType.equals("levelup")){						
				SFOnlineHelper.setData(mActivity,"levelup",jsonExData);    //  玩家升级角色时调用     必接			
			}
			if(submitType.equals("login")){					
				SFOnlineHelper.setData(mActivity,"enterServer",jsonExData);//  选择服务器进入时调用   必接
				SFOnlineHelper.setRoleData(mActivity, roleId,roleName, roleLevel, zoneId, "1");
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
}
