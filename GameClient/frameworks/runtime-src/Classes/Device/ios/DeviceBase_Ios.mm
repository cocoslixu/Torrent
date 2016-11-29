//
//  DeviceBase.cpp
//  GameClient
//
//  Created by ff on 2016/11/16.
//
//

#include "DeviceBase.h"
#import "GameUUID.h"


CDeviceBase::CDeviceBase()
{
    
}

CDeviceBase::~CDeviceBase()
{
    
}

bool CDeviceBase::init()
{
    return true;
}

void CDeviceBase::release()
{
    
}

const std::string& CDeviceBase::getUDDID()
{
    static std::string strUDID("");
    
    //获取uuid
    NSString* keyUUID = [GameUUID uniqueAppId];
    if(keyUUID != nil)
    {
        strUDID = keyUUID.UTF8String;
    }
    
    return strUDID;
}

void CDeviceBase::openOfficialWebSite()
{
    [GameUUID openOfficialWebSite];
}

const string& CDeviceBase::getPackageName()
{
    static std::string strName("");
    
    //获取uuid
    NSString* packagename = [GameUUID packageNameString];
    if(packagename != nil)
    {
        strName = packagename.UTF8String;
    }
    
    return strName;
}
