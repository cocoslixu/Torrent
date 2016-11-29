package com.base;

import org.cocos2dx.lib.Cocos2dxActivity;
import com.libcore.utils.UIThread;
import com.thirdparty.ThirdPartyManager;
import android.util.Log;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

public class GameActivityBase extends Cocos2dxActivity
{
    private static final String TAG = GameActivityBase.class.getSimpleName();    

    private static GameActivityBase sInstance;
   
    public static Object getInstance() { return sInstance; }

    public static void setInstance(final GameActivityBase instance) { sInstance = instance; }    
    public static Application app=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setInstance(this);
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);      
        UIThread.init();
        //
        try
        {
        	ThirdPartyManager.getInstance().onCreate(this);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }   

    @Override
    protected void onPause()
    {    
        try
        {           
            ThirdPartyManager.getInstance().onPause();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
    }
    
    @Override
    protected void onResume()
    {
        super.onResume();        
        try
        {
        	ThirdPartyManager.getInstance().onResume(this);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    protected void onDestroy() {
        try
        {
        	ThirdPartyManager.getInstance().onDestroy();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }          
    @Override
    protected void onStop() {
        try
        {
        	ThirdPartyManager.getInstance().onStop();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        super.onStop();
    }    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);        
        try
        {
        	ThirdPartyManager.getInstance().onActivityResult(requestCode,resultCode,data);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "onActivityResult req:" + requestCode + " res:" +resultCode);              
    }       
    @Override
    public void onRestart()
    {       
    	 try
         {
         	ThirdPartyManager.getInstance().onRestart();
         }
         catch (Exception e) {
             e.printStackTrace();
         }
         super.onRestart();
    }
}
