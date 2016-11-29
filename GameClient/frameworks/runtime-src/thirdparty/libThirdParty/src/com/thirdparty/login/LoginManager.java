
package com.thirdparty.login;

import org.cocos2dx.lib.Cocos2dxHelper;
import android.util.Log;
import android.util.SparseArray;

import com.libcore.utils.UIThread;
import com.thirdparty.login.ILogin;
import com.thirdparty.IThirdParty;
import com.thirdparty.ThirdPartyManager;


public final class LoginManager {
    private static final String TAG = LoginManager.class.getSimpleName();
    
    public static final int AccountAction_Login    = 1;
    public static final int AccountAction_Logout   = 2;

    public static final boolean NeedThirdExitMenu = true;
    public static final boolean NoThirdExitMenu = false;

    private static LoginManager m_loginManager = null;
    
    public static LoginManager getInstance() {
        if (m_loginManager == null) {
            m_loginManager = new LoginManager();
        }
        return m_loginManager;
    }

    private LoginManager() {

    }

    public static void login() {
        Log.d(TAG, "login with provider:" );
        final Runnable runnable = new Runnable() {
        @Override
        public void run() {

            try{
                login(new ILoginListener() {
                    @Override
                    public void onLoginFinished(final boolean result,final ThirdPartyUserInfo userinfo) {                        
                    	Cocos2dxHelper.runOnGLThread(new Runnable() {
                            @Override
                            public void run() {
                            	if(userinfo!=null){
                                    Log.d(TAG, "nativeOnActionResult:" +  " result:" + result+"  " +userinfo.userName+"  "+ userinfo.key+"  " +userinfo.id);       
                                    if(userinfo!=null){
                                    	 nativeOnActionResult(AccountAction_Login, result, userinfo.userName, userinfo.key, userinfo.id); 
                                    }
                            	}else{
                            		Log.d(TAG, "nativeOnActionResult:" +  " result:" + result);       
	                                if(userinfo!=null){
	                                	 nativeOnActionResult(AccountAction_Login, result, "", "", ""); 
	                                }
                            	}
                                
                            }
                        });
                    }

                    @Override
                    public void onLoginCancel(int provider) {
                        
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "NotLoginProviderException:" + e.getMessage());
                e.printStackTrace();
              }
           }};
        UIThread.runDelayed(runnable, 100);
    }

    private static boolean login(ILoginListener listener) throws Exception{

    	SparseArray<IThirdParty> mThirdPartys= ThirdPartyManager.getAllThirdPartys();
    	try {
	    	for (int i = 0; i < mThirdPartys.size(); i++) {
			    IThirdParty thirdPart = mThirdPartys.valueAt(i);       
			    if (thirdPart instanceof ILogin) {
				    ILogin login = (ILogin)thirdPart;
				    return login.login(listener);
				} 
	    	}
	 	} catch (Exception e) {
	        e.printStackTrace();
	    }
    	return false;
    }
    
    public static void logout() {
        Log.d(TAG, "logout with provider:" );
        final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                logout(new ILogoutListener() {
                    @Override
                    public void onLogoutFinished(final boolean result,final String msg) {
                    	Cocos2dxHelper.runOnGLThread(new Runnable() {
                            @Override
                            public void run() {
                                nativeOnActionResult(AccountAction_Logout,  result,  " ", " ", " ");
                            }                            
                        });
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
              }
        }};
        UIThread.runDelayed(runnable, 100);
    }
    

    private static void logout(ILogoutListener listener) throws Exception {
    	SparseArray<IThirdParty> mThirdPartys= ThirdPartyManager.getAllThirdPartys();
    	try {
	    	for (int i = 0; i < mThirdPartys.size(); i++) {
			    IThirdParty thirdPart = mThirdPartys.valueAt(i);       
			    if (thirdPart instanceof ILogin) {
				    ILogin login = (ILogin)thirdPart;
				    login.logout(listener);
				    return;
				} 
	    	}
	 	} catch (Exception e) {
	        e.printStackTrace();
	    }       
    }   
   
    
    private static void changeAccount() throws Exception {
    	SparseArray<IThirdParty> mThirdPartys= ThirdPartyManager.getAllThirdPartys();
    	try {
	    	for (int i = 0; i < mThirdPartys.size(); i++) {
			    IThirdParty thirdPart = mThirdPartys.valueAt(i);       
			    if (thirdPart instanceof ILogin) {
				    ILogin login = (ILogin)thirdPart;
				    login.changeAccount();
				    return;
				} 
	    	}
	 	} catch (Exception e) {
	        e.printStackTrace();
	    }       
    }  
    
    
    private static native void nativeOnActionResult(int action,  boolean result, String userName, String strKey, String strID);

}
