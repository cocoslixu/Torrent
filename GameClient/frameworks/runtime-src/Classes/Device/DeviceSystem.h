//
//  DeviceSystem.hpp
//  GameClient
//
//  Created by ff on 2016/11/16.
//
//   获取手机的唯一标示

#ifndef DeviceSystem_hpp
#define DeviceSystem_hpp

#include "cocos2d.h"
#include "IDeviceSystem.h"

class CDeviceBase;

class CDeviceSystem:public IDeviceSystem
{
private:
    CDeviceSystem();
    
    ~CDeviceSystem();
public:
    
    static IModule* instance();
    
    static void releaseInstance();
    
    virtual bool init();
    
    virtual void release();
    
    virtual int  lua_GameModule_register(void* pL);
   
    virtual const std::string& getDeveicUDID();
    
    virtual void openURL(const string& strURL);
    
    virtual void openOfficialWebSite();
    
    virtual const string& getPackageName();
    
private:
    static CDeviceSystem* s_pInstance;
    CDeviceBase* m_pDeviceBase;
};

#endif /* DeviceSystem_hpp */
