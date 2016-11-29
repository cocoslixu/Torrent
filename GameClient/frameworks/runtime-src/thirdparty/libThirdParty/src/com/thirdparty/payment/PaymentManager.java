package com.thirdparty.payment;

import com.libcore.utils.UIThread;
import com.thirdparty.IThirdParty;
import com.thirdparty.ThirdPartyManager;
import android.util.Log;
import android.util.SparseArray;
import org.cocos2dx.lib.Cocos2dxHelper;

public final class PaymentManager {
    
    private static final String TAG = PaymentManager.class.getSimpleName();
    
    public static final int PayOrder_Result_OK          = 0;
    public static final int PayOrder_Result_Failed      = 1;
    public static final int PayOrder_Result_Cancelled   = 2;
     
    private static PaymentManager mPaymentManager = null;

    public static PaymentManager getInstance() {
        if (mPaymentManager == null) {
            mPaymentManager = new PaymentManager();
        }
        return mPaymentManager;
    }   

    // Called by C layer through jni
    public static void payByProvider(final int provider, final BuyInfo buyInfo) {
    	try {
	        Log.d(TAG, "payByProvider:" +provider+ " price:"+buyInfo.productPrice+ " notifyurl:"+buyInfo.notifyurl+ buyInfo.productName);
	        if(provider!=0){
	        	final IPayable thirdPart =(IPayable) (ThirdPartyManager.getInstance().getThirdPartyByProvider(provider));
		        
		        if (thirdPart == null)
		        {
		            Log.e(TAG, "Failed to find sdk:" + provider);
		            return;
		        }       
		         
		        if(thirdPart instanceof IPayable){
			        final Runnable runnable = new Runnable() {
			            @Override
			            public void run() {	                                     
			            	thirdPart.pay(buyInfo, new IPaymentListener() {
		                        @Override
		                        public void onPaymentFinished(final int result){                            
		                        	Cocos2dxHelper.runOnGLThread(new Runnable() {
		                                @Override
		                                public void run() {                    
		                                    Log.d(TAG, "nativeOnPayResult:" +  " result:" + result);
		                                    nativeOnPayResult(result);
		                                }
		                            });
		                        }
		                    });		                
			            }       
			        };
			        UIThread.runDelayed(runnable, 100);		       
		        }	    	        	
	        }else{
	        	SparseArray<IThirdParty> mThirdPartys= ThirdPartyManager.getAllThirdPartys();    	
	     	    for (int i = 0; i < mThirdPartys.size(); i++) {
	     	    	IThirdParty thirdPart =(IThirdParty) mThirdPartys.valueAt(i);  	        
	     	        if(thirdPart instanceof IPayable) {
	     	           final IPayable pay= (IPayable)thirdPart;		     	          
	     	           final Runnable runnable = new Runnable() {
				            @Override
				            public void run() {	                                     
				            	pay.pay(buyInfo, new IPaymentListener() {
			                        @Override
			                        public void onPaymentFinished(final int result){                            
			                        	Cocos2dxHelper.runOnGLThread(new Runnable() {
			                                @Override
			                                public void run() {                    
			                                    Log.d(TAG, "nativeOnPayResult:" +  " result:" + result);
			                                    nativeOnPayResult(result);
			                                }
			                            });
			                        }
			                    });		                
				            }       
				        };
				        UIThread.runDelayed(runnable, 100);	
				        return;
	     	        }
	     	    }
	        	
	        }	        
    	 } catch (Exception e) {
             Log.e(TAG, e.getMessage());
         }
    }


        // Call C layer through jni
    private static native void nativeOnPayResult(int result);
    
    
    
}