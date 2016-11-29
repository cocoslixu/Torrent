
package com.thirdparty.exit;

import org.cocos2dx.lib.Cocos2dxHelper;

import com.libcore.utils.UIThread;
import com.thirdparty.IThirdParty;
import com.thirdparty.ThirdPartyManager;

import android.util.Log;
import android.util.SparseArray;

public final class ExitManager {

    public static int ExitAction_Quit = 1;
    public static int ExitAction_PlayAgain = 2;
    public static int ExitAction_Cancel = 3;

    private static final String TAG = ExitManager.class.getSimpleName();
    private static ExitManager mExitManager = null;

    public static ExitManager getInstance() {
        if (mExitManager == null) {
            mExitManager = new ExitManager();
        }
        return mExitManager;
    }
    
    public static boolean onExit() {
        Log.d(TAG, "onExit");
        SparseArray<IThirdParty> mThirdPartys= ThirdPartyManager.getAllThirdPartys();    	
	    for (int i = 0; i < mThirdPartys.size(); i++) {
	    	IThirdParty thirdPart =(IThirdParty) mThirdPartys.valueAt(i);  	        
	        if(thirdPart instanceof IExitable) {
	            final IExitable aExit= (IExitable)thirdPart;	
	            final Runnable runnable = new Runnable() {
	                @Override
	                public void run() {	                    
	                       aExit.onExit( new IExitListener() {
	                            @Override
	                            public void onExitFinished(final int provider, final int actionType){
//	                            	Cocos2dxHelper.runOnGLThread(new Runnable() {
//	                                    @Override
//	                                    public void run() {                    
//	                                        Log.d(TAG, "onExitFinished:" + provider + " actionType:" + actionType);
//	                                        nativeOnExitFinished(provider, actionType);
//	                                    }
//	                                });
	                            }
	                        });
	                }	
	            };	
	            UIThread.runDelayed(runnable, 100);	
	            return aExit.isCanExit();
	        }
	    }
	    return false;
    }

    public static void onBeforeExit() {
        Log.d(TAG, "onBeforeExit" );
      
//        ThirdPartyManager.getInstance();
//		SparseArray<IThirdParty>  mThirdPartys=ThirdPartyManager.getAllThirdPartys();
//        for (int i = 0; i < mThirdPartys.size(); i++) {
//            try {
//                IThirdParty thirdPart = mThirdPartys.valueAt(i);
//                if(thirdPart instanceof IBeforeExitable) {
//                    final IBeforeExitable aExit= (IBeforeExitable)thirdPart;
//                    Log.d(TAG, "onBeforeExit:" +thirdPart.getClass().getSimpleName());
//                    aExit.onBeforeExit();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }   
    }
    //call c layer through jni
    /**@param actionType:
     * enum ExitAction
{
    //退出
    kExitActionExit = 1,
    //快速开始
    kExitActionPlayAgain,
    //取消退出
    kExitActionClose,
};
     * */
    private static native void nativeOnExitFinished(int provider, int actionType);

}