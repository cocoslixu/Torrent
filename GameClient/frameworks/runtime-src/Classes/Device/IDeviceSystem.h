//
//  IDeviceSystem.h
//  GameClient
//
//  Created by ff on 2016/11/17.
//
// interface

#ifndef IDeviceSystem_h
#define IDeviceSystem_h

#include "EngineBase.h"

class IDeviceSystem:public IModule
{
protected:
    IDeviceSystem(){};
    
    virtual ~IDeviceSystem(){};
    
public:
    static IDeviceSystem* share();
    
    static void releaseInstance();
    
    virtual const std::string& getDeveicUDID()=0;
    
    virtual void openURL(const string& strURL)=0;
    
    virtual void openOfficialWebSite()=0;
    
    virtual const string& getPackageName()=0;
};


#endif /* IDeviceSystem_h */
