#ifndef APPSTORE_H
#define APPSTORE_H
#include "../../PlatformBase.h"

class CIosPlatform : public CPlatformBase
{
public:
	CIosPlatform();

	~CIosPlatform();
    
    virtual INT32 getGameCP();
    
    virtual INT32 getGameVersion();
    
    virtual INT32 getPayChannelID();
    
    virtual void initSDK(void* pView);
    
    virtual void registerGame();
    
    virtual void onClickLogin(INT32 iLoginType);
    
    virtual void loginGame();
    
    virtual void logoutGame();
    
    virtual void submitUserData(const INT8* pUserData);
    
    virtual void gamePay(const std::string& pstrPayInfo);
    
    virtual void applicationDidEnterBackground();
    
    virtual void applicationWillEnterForeground();
    
    virtual void platformOpenURL(void* pURL, void* sourceApplication, void* application, void* nid);
    
    //call back
    virtual void onLoginGame(const char* pToken, const INT8* pstrUID, const INT8* userName);
    
    virtual void onLogoutGame();
    
    virtual void onChangeAccount();
    
    virtual void onGamePay(bool bSuc);
};

#endif //APPSTORE_H
