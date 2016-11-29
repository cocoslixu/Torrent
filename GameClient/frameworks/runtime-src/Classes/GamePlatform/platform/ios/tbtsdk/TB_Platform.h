//
//  TB_Platform.h
//  xxz
//
//  Created by daily-play on 16/2/26.
//
//

#ifndef TB_Platform_h
#define TB_Platform_h
//#include "GamePlatform.h"
#include "GamePlatform/PlatformBase.h"
NS_CC_BEGIN
class CTB_Platform : public  CPlatformBase
{
public:
    CTB_Platform();
    
    ~CTB_Platform();
    
    virtual void initSDK(void* pView);
    
    virtual void loginGame();    //登录游戏
    
    virtual void logoutGame();    //登出游戏
    
    virtual void gamePay(const std::string& pStrPayInfo);    //支付
    
    virtual void platformOpenURL(void* pURL,void* sourceApplication,void* application,void* nid);
    
    virtual bool isHaveThirdPartyLogin();
    
    virtual bool isHaveThirdParyExit();
    
    virtual bool isAllHouseOpen();
    
public:
    virtual void onChangeAccount();
    
    virtual void onGamePay(bool bSuc);
    
    virtual void onLoginGame(const char* pToken,const char* pstrUID,const char* userName);
    
    virtual void onHttpRequestCompleted(network::HttpClient *sender,network::HttpResponse *reponse);
    
private:
    std::string m_strKey;
    std::string m_strID;
    std::string m_uerName;
};
NS_CC_END

#endif /* TB_Platform_h */
