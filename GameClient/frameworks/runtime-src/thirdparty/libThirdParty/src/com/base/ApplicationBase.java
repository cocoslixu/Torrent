package com.base;

import com.thirdparty.ThirdPartyManager;
import android.app.Application;

public class ApplicationBase extends Application {	
	public static ApplicationBase app=null;
    @Override
    public void onCreate() {
        super.onCreate();
        app=this;
        GameActivityBase.app=this;
        try
        {        	
        	ThirdPartyManager.getInstance().onCreate(this);
        }
        catch (Exception e) {
            e.printStackTrace();
        }           
    }

    @Override
    public void onTerminate() {
        try
        {
        	ThirdPartyManager.getInstance().onTerminate();
        }
        catch (Exception e) {
            e.printStackTrace();
        }        
        super.onTerminate();
    }   
}
