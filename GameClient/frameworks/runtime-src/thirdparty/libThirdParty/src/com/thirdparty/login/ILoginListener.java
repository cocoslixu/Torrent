
package com.thirdparty.login;

public interface ILoginListener {
    public void onLoginFinished(boolean result,ThirdPartyUserInfo userinfo);
    public void onLoginCancel(int provider);
}
