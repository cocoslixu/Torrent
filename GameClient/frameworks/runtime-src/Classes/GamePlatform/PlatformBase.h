#ifndef PLATFORMBASE_H
#define PLATFORMBASE_H

#include "cocos2d.h"
#include "editor-support/spine/Json.h"
#include "../../../cocos2d-x/extensions/cocos-ext.h"
#include "network/HttpClient.h"
#include "EngineBase.h"
USING_NS_CC;

class CPlatformBase
{
public:
	CPlatformBase();

	virtual ~CPlatformBase();
    
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
    
public:
	virtual void onLoginGame(const char* pToken, const INT8* pstrUID, const INT8* userName);

	virtual void onLogoutGame();

	virtual void onChangeAccount();

	virtual void onGamePay(bool bSuc);
protected:
	std::string m_strToken;

	std::string m_strUID;
};

#endif	 //PLATFORMBASE_H
