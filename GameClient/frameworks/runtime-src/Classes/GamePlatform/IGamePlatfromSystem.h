//
//  IGamePlatfromSystem.h
//  GameClient
//
//  Created by ff on 2016/11/18.
//
// interface

#ifndef IGamePlatfromSystem_h
#define IGamePlatfromSystem_h

#include "EngineBase.h"

class IGamePlatformSystem:public IModule
{
protected:
    IGamePlatformSystem(){};
    
    virtual ~IGamePlatformSystem(){};
    
public:
    static void releaseInstace();
    
    static IGamePlatformSystem* share();
    
    virtual void initGameSDK(void* pView) = 0;

    virtual int getPayChannelID()=0;
    
    virtual INT32 getGameCP()=0;
    
    virtual INT32 getGameVersion()=0;
    
    virtual void registerGame()=0;
    
    virtual void loginGame()=0;
    
    virtual void submitUserData(const char* pUserData)=0;
    
    virtual void gamePay(const std::string& pstrPayInfo)=0;
    
    virtual void onClickLogin(int iLoginType)=0;
    
    virtual void Logout() =0;
};

#endif /* IGamePlatfromSystem_h */
