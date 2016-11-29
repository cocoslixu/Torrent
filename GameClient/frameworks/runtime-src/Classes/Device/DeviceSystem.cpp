//
//  DeviceSystem.cpp
//  GameClient
//
//  Created by ff on 2016/11/16.
//
//
#include "cocos2d.h"
#include "DeviceSystem.h"
#include "DeviceBase.h"
#include "GameEngine.h"

#include "scripting/lua-bindings/auto/lua_IDeviceSystem_auto.hpp"

#include "scripting/lua-bindings/manual/tolua_fix.h"
#include "scripting/lua-bindings/manual/LuaBasicConversions.h"
#include "scripting/lua-bindings/manual/cocos2d/LuaScriptHandlerMgr.h"
#include "scripting/lua-bindings/manual/CCLuaValue.h"
#include "scripting/lua-bindings/manual/CCLuaEngine.h"

USING_NS_CC;

CDeviceSystem* CDeviceSystem::s_pInstance = nullptr;

IDeviceSystem* IDeviceSystem::share()
{
    return (IDeviceSystem*)CDeviceSystem::instance();
}

void IDeviceSystem::releaseInstance()
{
    CDeviceSystem::releaseInstance();
}

CDeviceSystem::CDeviceSystem()
:m_pDeviceBase(nullptr)
{
    
}

CDeviceSystem::~CDeviceSystem()
{
    
}


IModule* CDeviceSystem::instance()
{
    if(s_pInstance == nullptr)
    {
        s_pInstance = new CDeviceSystem;
        if(s_pInstance && s_pInstance->init() == false)
        {
            SAVE_RELEASE(s_pInstance);
        }
    }
    
    return s_pInstance;
}

void CDeviceSystem::releaseInstance()
{
    SAVE_RELEASE_GAMEENGINE(s_pInstance);
    SAVE_RELEASE(s_pInstance);
}

bool CDeviceSystem::init()
{
    m_pDeviceBase = new CDeviceBase();
    return  true;
}

void CDeviceSystem::release()
{
   SAVE_RELEASE(m_pDeviceBase);
}

int  CDeviceSystem::lua_GameModule_register(void* pL)
{
//    lua_State* L = static_cast<lua_State*>(pL);
//    if(L != nullptr)
//    {
//        lua_getglobal(L, "_G");
//        if (lua_istable(L,-1))//stack:...,_G,
//        {
//            register_all_IDeviceSystem(L);
//        }
//        lua_pop(L, 1);
//    }
    lua_State* L = static_cast<lua_State*>(pL);
    register_all_IDeviceSystem(L);
    return 1;
}

const std::string& CDeviceSystem::getDeveicUDID()
{
    static string tm(" ");
    return m_pDeviceBase == nullptr ?  tm: m_pDeviceBase->getUDDID();
}

void CDeviceSystem::openURL(const string& strURL)
{
    Application::getInstance()->openURL(strURL);
}

void CDeviceSystem::openOfficialWebSite()
{
    if(m_pDeviceBase)
    {
        m_pDeviceBase->openOfficialWebSite();
    }
}

const string& CDeviceSystem::getPackageName()
{
    if(m_pDeviceBase)
    {
        m_pDeviceBase->getPackageName();
    }
}

