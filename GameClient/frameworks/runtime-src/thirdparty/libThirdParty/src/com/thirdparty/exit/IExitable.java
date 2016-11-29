
package com.thirdparty.exit;

import com.thirdparty.IThirdParty;

public interface IExitable extends IThirdParty {
	//调用sdk的退出对话框
    public void onExit(IExitListener listener);   
    //默认为false
    public boolean isCanExit();
   
}    

