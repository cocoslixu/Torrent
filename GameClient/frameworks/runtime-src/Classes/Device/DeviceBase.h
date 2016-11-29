//
//  DeviceBase.hpp
//  GameClient
//
//  Created by ff on 2016/11/16.
//
//

#ifndef DeviceBase_hpp
#define DeviceBase_hpp

#include "EngineBase.h"

class CDeviceBase
{
public:
    CDeviceBase();
    
    ~CDeviceBase();
    
    virtual bool init();
    
    virtual void release();
    
    const std::string& getUDDID();
    
    void openOfficialWebSite();
    
    const string& getPackageName();
};

#endif /* DeviceBase_hpp */
