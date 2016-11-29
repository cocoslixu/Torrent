
package com.thirdparty.login;


import com.thirdparty.IThirdParty;

public interface ILogin extends IThirdParty {
   
    public boolean login(ILoginListener listener);
    public void logout(ILogoutListener listener);
	public void changeAccount();
   
}
