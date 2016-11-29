//
//  XY_Platform.h
//  xxz
//
//  Created by daily-play on 16/2/27.
//
//

#ifndef XY_Platform_h
#define XY_Platform_h
#include "PlatformBase.h"
NS_CC_BEGIN

class CXY_Platform:public CPlatformBase
{
public:
          
    CXY_Platform();
    
    ~CXY_Platform();
    
    virtual void initSDK(void* pView);
    
    virtual void loginGame();   //登录游戏
    
    virtual void logoutGame();   //登出游戏
    
    virtual void  gamePay(const std::string& pStrPayInfo);     //支付
    
     virtual void platformOpenURL(void* pURL, void* sourceApplication, void* application, void* nid);
    
    virtual bool isHaveThirdPartyLogin();
    
    virtual bool isHaveThirdParyExit();
    
    virtual bool isAllHouseOpen();
    
public:
    
    virtual void onChangeAccount();
    
    virtual void onGamePay(bool bSuc);
    
    virtual void onLoginGame(const char* pToken,const char* pstrUID,const char* userName);
    
    void onHttpRequestCompleted(network::HttpClient *sender, network::HttpResponse *response);
private:
    std::string m_strKey;
    std::string m_strID;
    std::string m_uerName;
};
NS_CC_END

#endif /* XY_Platform_h */
