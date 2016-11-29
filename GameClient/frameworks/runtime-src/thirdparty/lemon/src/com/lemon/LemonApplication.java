package com.lemon;

import com.base.GameActivityBase;
import com.snowfish.cn.ganga.helper.SFOnlineApplication;
import com.thirdparty.ThirdPartyManager;

public class LemonApplication extends SFOnlineApplication {	
	public static LemonApplication app=null;
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
