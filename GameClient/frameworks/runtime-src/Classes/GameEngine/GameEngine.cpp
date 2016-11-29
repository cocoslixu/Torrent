//
//  GameEngine.cpp
//  GameClient
//
//  Created by ff on 2016/11/16.
//
//

#include "GameEngine.h"
#include "GamePlatformSystem.h"
#include "GameSocketSystem.h"
#include "DeviceSystem.h"

#include "cocos2d.h"
#include <iostream>
extern "C"{
#include "lua.h"
#include "lualib.h"
#include "lauxlib.h"
};
#include "scripting/lua-bindings/manual/CCLuaEngine.h"

using namespace cocos2d;
using namespace std;


CGameEngine* CGameEngine::s_pInstance = nullptr;

CGameEngine::CGameEngine()
{
    
}

CGameEngine::~CGameEngine()
{
    
}


CGameEngine* CGameEngine::instance()
{
    if(s_pInstance == nullptr)
    {
        s_pInstance = new CGameEngine;
    }
    
    return s_pInstance;
}


void CGameEngine::releaseInstance()
{
    if(s_pInstance != nullptr)
    {
       s_pInstance->release();
    }
    
    SAVE_RELEASE(s_pInstance);
}

bool CGameEngine::init()
{
    IModule* pMoudle = nullptr;
    pMoudle = CGamePlatformSystem::instance();
    if(pMoudle)
    {
        m_vecMoudle.push_back(pMoudle);
    }
    
    pMoudle = CGameSocketSystem::instance();
    if(pMoudle)
    {
        m_vecMoudle.push_back(pMoudle);
    }
    
    pMoudle = CDeviceSystem::instance();
    if(pMoudle)
    {
        m_vecMoudle.push_back(pMoudle);
    }
    
    return true;
}

void CGameEngine::release()
{
    IGameSocketSystem::releaseInstance();
    
    IGamePlatformSystem::releaseInstace();
    
    IDeviceSystem::releaseInstance();
}

void CGameEngine::releaseModule(IModule* pModule)
{
    if(pModule == nullptr)return;
    
    for(ITERMODULE iter = m_vecMoudle.begin(); iter != m_vecMoudle.end(); ++iter)
    {
        if(*iter == pModule)
        {
            m_vecMoudle.erase(iter);
            break;
        }
    }
}

int  CGameEngine::lua_GameModule_register(lua_State* L)
{
    ITERMODULE iter = m_vecMoudle.begin();
    for(; iter != m_vecMoudle.end(); ++iter)
    {
        (*iter)->lua_GameModule_register(L);
    }
    return 0;
}

void CGameEngine::initPlatformSDK(void* pView)
{
    IGamePlatformSystem* pMoudle = static_cast<IGamePlatformSystem*>(CGamePlatformSystem::instance());
    if(pMoudle)
    {
        pMoudle->initGameSDK(pView);
    }
}

void CGameEngine::runLuaMainLoop(float dt)
{
    lua_State* pL = LuaEngine::getInstance()->getLuaStack()->getLuaState();
    if(pL)
    {
        lua_getglobal(pL, "");
        lua_pushnumber(pL, dt*1000);
        
        lua_call(pL, 1, 1);
        
        // 0 fail 1 success
        int ret = lua_tonumber(pL, -1);
        if(ret == 0)
        {
            //TODO:
        }
        
    }
}
