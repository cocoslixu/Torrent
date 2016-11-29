//
//  DeviceBase.cpp
//  GameClient
//
//  Created by ff on 2016/11/16.
//
//

#include "DeviceBase.h"


DeviceBase::DeviceBase()
{

}
    
DeviceBase::~CDeviceBase()
{

}

bool DeviceBase::init()
{
    return true;
}

void DeviceBase::release()
{

}

const std::string& DeviceBase::getUDDID()
{
    static std::string strUID("12345678");
    return strUID;
}