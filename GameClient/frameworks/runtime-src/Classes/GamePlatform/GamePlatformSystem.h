#ifndef GAMEPLATFORMSYSTEM_H
#define  GAMEPLATFORMSYSTEM_H

#include "cocos2d.h"
#include "EngineBase.h"
#include "PlatformBase.h"
#include "IGamePlatfromSystem.h"

class CGamePlatformSystem:public IGamePlatformSystem
{
public:
	static IModule* instance();

	static void releaseInstance();
    
    virtual void initGameSDK(void* pView);
    
    virtual bool init();
    
    virtual void release();
    
    virtual int  lua_GameModule_register(void* pL);
    
    int getPayChannelID();
    
    INT32 getGameCP();
    
    INT32 getGameVersion();
    
	void registerGame();

	void loginGame();

	void submitUserData(const char* pUserData);

	void gamePay(const std::string& pstrPayInfo);

	void onClickLogin(int iLoginType);

	void Logout();

	const std::string& getLoginResAddr();

	void setLoginResAddr(const std::string& strAddr);

	void changeAccount();
    
    void sdkOpenURL(void* pURL, void* sourceApplication, void* application, void* nid);
    
    void applicationDidEnterBackground();
    
    void applicationWillEnterForeground();
    
public:
	void onLoginGame(const char* pToken, const char* pstrUID, const char* userName);

	void onLogoutGame();

protected:
	CGamePlatformSystem();

	~CGamePlatformSystem();

    bool createBase();

private:
	static CGamePlatformSystem* s_Instance;

	CPlatformBase* m_pPlatform;

	std::string m_strLoginReqAddr;
};

#endif //GAMEPLATFORMSYSTEM_H
