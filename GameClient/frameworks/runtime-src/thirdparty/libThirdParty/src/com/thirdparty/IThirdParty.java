
package com.thirdparty;


import android.app.Activity;
import android.app.Application;
import android.content.Intent;


public interface IThirdParty {    
    
    //public int getID();   
   
    public void onPause();
   
    public void onResume(Activity activity);
    
    public void onStop();        
    
    public void onDestroy();
    
    public void onCreate(Activity activity);
    
    public void onCreate(Application application);
    
    public void onTerminate();
  
    public void configDeveloperInfo(String cpInfo);
  
    public void onRestart();

	public void onActivityResult(int requestCode, int resultCode, Intent data);
  
}
