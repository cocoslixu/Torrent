
package com.thirdparty;

import java.util.ArrayList;
import com.thirdparty.exit.IExitable;
import com.thirdparty.login.ILogin;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.util.Log;
import android.util.SparseArray;
import org.cocos2dx.lib.Cocos2dxHelper;

public final class ThirdPartyManager {
    private static final String TAG = ThirdPartyManager.class.getSimpleName();
    
    private static SparseArray<IThirdParty> mThirdPartys = new SparseArray<IThirdParty>();

    private static ThirdPartyManager mThirdPartyManager = null;
   
    public static ThirdPartyManager getInstance() {
        if (mThirdPartyManager == null) {
            mThirdPartyManager = new ThirdPartyManager();
        }
        return mThirdPartyManager;
    }
    public static int isHaveThirdParty(int provider){
    	IThirdParty i=ThirdPartyManager.getInstance().getThirdPartyByProvider(provider);
    	if(i==null){
    		Log.d(TAG, "isHaveThirdParty： "+provider+" 没找到");
    		return 0;
    	}
   		Log.d(TAG, "isHaveThirdParty： "+provider+"找到");
    	return 1;
    }
    
    public IThirdParty getThirdPartyByProvider(int provider) {
        return mThirdPartys.get(provider,null);
    }
    public static int getPayChannelID(){
    	 ArrayList<Integer> providers = ThirdPartConfig.getInstance().getAllProviders();
         for (int i : providers) {            
             String type = ThirdPartConfig.getInstance().getThirdPartyProperty(i, ThirdPartConfig.CONFIG_TYPE);            
             if (type.equals("pay")) {
                return i;
             }            
         }
    	return 0;
    }
    public static void submitExtendData(String data){
    	 Log.d(TAG, "submitExtendData： "+data);
    	 for (int i = 0; i < mThirdPartys.size(); ++i) {
             IThirdParty listener = mThirdPartys.valueAt(i);                       
             if(listener instanceof ISubmitExtendData){            	 
            	 ISubmitExtendData thirdParty = (ISubmitExtendData)listener;
            	 thirdParty.submitExtendData(data);
             }
         }    	 
    }
    public static boolean isHaveThirdPartyLogin(){
    	 for (int i = 0; i < mThirdPartys.size(); ++i) {
             IThirdParty listener = mThirdPartys.valueAt(i);                       
             if(listener instanceof ILogin){            	 
            	 return true;
             }
         }
    	 return false;
    } 

    public static boolean isHaveThirdPartyExit(){
      for (int i = 0; i < mThirdPartys.size(); ++i) {
             IThirdParty listener = mThirdPartys.valueAt(i);                       
             if(listener instanceof IExitable){               
               return true;
             }
         }
       return false;
    }
       
    public String getThirdPartyParamsByProvider(int provider) {
    	return ThirdPartConfig.getInstance().getThirdPartyProperty(provider, ThirdPartConfig.CONFIG_PARAMS);
    }

   
    public static String getThirdPartyNameByProvider(int provider) {
        return ThirdPartConfig.getInstance().getThirdPartyProperty(provider, ThirdPartConfig.CONFIG_CLASS_NAME);
    }
  
    public int getRequestCodeByProvider(int provider) {
    	String str=ThirdPartConfig.getInstance().getThirdPartyProperty(provider, ThirdPartConfig.CONFIG_REQUESTCODE);
    	if(!str.isEmpty()){
    		return Integer.valueOf(str);
    	}
        return -1;
    }

    private ThirdPartyManager() {
        configThirdParty();
    }

    private  void addThirdPart(int provider, IThirdParty thirdParty){
        mThirdPartys.put(provider, thirdParty);
        Log.i(TAG, "addThirdPart "+provider);
    }

    private void configThirdParty() {
        Log.i(TAG, "configThirdParts ...");
        ArrayList<Integer> providers = ThirdPartConfig.getInstance().getAllProviders();
        for (int i : providers) {
            Log.i(TAG, "configThirdParts provider:" + i);

            String thirdPartyName = ThirdPartConfig.getInstance().getThirdPartyProperty(i, ThirdPartConfig.CONFIG_CLASS_NAME);
            Log.i(TAG, "configThirdParts provider class:" + thirdPartyName);
            if (thirdPartyName.isEmpty()) {
                continue;
            }

            IThirdParty iThirdParty = createThirdPayty(thirdPartyName);        
            if(iThirdParty!=null){	           
	            String config = getThirdPartyParamsByProvider(i);	          
	            iThirdParty.configDeveloperInfo(config);
	            addThirdPart(i, iThirdParty);
            }
        }
    }
    public static IThirdParty createThirdPayty(String className) {
        try {
            return (IThirdParty) Class.forName(className).newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null; 
    }
    public void onCreate(Application application) {
        for (int i = 0; i < mThirdPartys.size(); ++i) {
            IThirdParty listener = mThirdPartys.valueAt(i);
            listener.onCreate(application);
        }
    }

    public void onCreate(Activity activity) {
        for (int i = 0; i < mThirdPartys.size(); ++i) {
            IThirdParty listener = mThirdPartys.valueAt(i);                       
            listener.onCreate(activity);
        }
    }
   
   public void onPause() {
       for (int i = 0; i < mThirdPartys.size(); i++) { 
           try {

               IThirdParty listener = mThirdPartys.valueAt(i);
               listener.onPause();

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
   }
  
   public void onResume(Activity activity) {
       for (int i = 0; i < mThirdPartys.size(); i++) {
           try {
               IThirdParty listener = mThirdPartys.valueAt(i);
               listener.onResume(activity);
           } catch (Exception e) {
               e.printStackTrace();
           }
       }
   }

   public void onDestroy() {
       for (int i = 0; i < mThirdPartys.size(); i++) {
           try {
               IThirdParty listener = mThirdPartys.valueAt(i);
               listener.onDestroy();
           } catch (Exception e) {
               e.printStackTrace();
           }
       }
   }
   public void onStop() {
       for (int i = 0; i < mThirdPartys.size(); i++) {
           try {
               IThirdParty listener = mThirdPartys.valueAt(i);
               listener.onStop();
           } catch (Exception e) {
               e.printStackTrace();
           }
       }
   }
   public void onTerminate() {
       for (int i = 0; i < mThirdPartys.size(); i++) {
           try {
               IThirdParty listener = mThirdPartys.valueAt(i);
               listener.onTerminate();
           } catch (Exception e) {
               e.printStackTrace();
           }
       }
   } 
   public void onRestart() {
       for (int i = 0; i < mThirdPartys.size(); i++) {
           try {
               IThirdParty listener = mThirdPartys.valueAt(i);
               listener.onRestart();
           } catch (Exception e) {
               e.printStackTrace();
           }
       }
   }      
   public static SparseArray<IThirdParty> getAllThirdPartys() {
       return mThirdPartys;
   }
   public void onActivityResult(int requestCode, int resultCode, Intent data) {
		 for (int i = 0; i < mThirdPartys.size(); i++) {
	         try {
	             IThirdParty listener = mThirdPartys.valueAt(i);
	             listener.onActivityResult(requestCode,resultCode,data);
	         } catch (Exception e) {
	             e.printStackTrace();
	         }
	     }		
	}
   public static boolean initThirdPartySDK() {
		 for (int i = 0; i < mThirdPartys.size(); i++) {
	         try {
	             IThirdParty listener = mThirdPartys.valueAt(i);
	             if(listener instanceof IInitThirdPartySDK){
	            	 IInitThirdPartySDK thirdParty = (IInitThirdPartySDK)listener;
	            	 if(thirdParty.initThirdPartySDK()){
	            		 return true;
	            	 };
	             }
	             
	         } catch (Exception e) {
	             e.printStackTrace();
	         }
	     }		
		 return false;
	}
   public static void initSDKOver(){
	   	Cocos2dxHelper.runOnGLThread(new Runnable() {
	       @Override
	       public void run() {                    
	           Log.d(TAG, "ThirdPartyManager initThirdPartySDKOver");
	           initThirdPartySDKOver();
	       }
	   });	   
   };
    // Call C layer through jni
   private static native void initThirdPartySDKOver();
}

