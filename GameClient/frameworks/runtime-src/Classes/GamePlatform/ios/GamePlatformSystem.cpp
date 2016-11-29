#include "GamePlatformSystem.h"
#include "../platform/ios/IosPlatform.h"
#include "GameEngine.h"

#include "scripting/lua-bindings/auto/lua_IGamePlatformSystem_auto.hpp"
#include "scripting/lua-bindings/manual/tolua_fix.h"
#include "scripting/lua-bindings/manual/LuaBasicConversions.h"
#include "scripting/lua-bindings/manual/cocos2d/LuaScriptHandlerMgr.h"
#include "scripting/lua-bindings/manual/CCLuaValue.h"
#include "scripting/lua-bindings/manual/CCLuaEngine.h"


CGamePlatformSystem* CGamePlatformSystem::s_Instance = nullptr;


void IGamePlatformSystem::releaseInstace()
{
    CGamePlatformSystem::releaseInstance();
}

IGamePlatformSystem* IGamePlatformSystem::share()
{
    return (IGamePlatformSystem*)CGamePlatformSystem::instance();
}

CGamePlatformSystem::CGamePlatformSystem()
:m_pPlatform(nullptr)
, m_strLoginReqAddr("")
{

}

CGamePlatformSystem::~CGamePlatformSystem()
{
	if (m_pPlatform)
	{
		delete m_pPlatform;
		m_pPlatform = nullptr;
	}
}

bool CGamePlatformSystem::init()
{
    return true;
}

void CGamePlatformSystem::release()
{
    
}

int  CGamePlatformSystem::lua_GameModule_register(void* pL)
{
//    lua_State* L = static_cast<lua_State*>(pL);
//    if(L != nullptr)
//    {
//        lua_getglobal(L, "_G");
//        if (lua_istable(L,-1))//stack:...,_G,
//        {
//            register_all_IGamePlatformSystem(L);
//        }
//        lua_pop(L, 1);
//    }
    lua_State* L = static_cast<lua_State*>(pL);
    register_all_IGamePlatformSystem(L);
    return 1;
}

IModule* CGamePlatformSystem::instance()
{
	if (s_Instance == nullptr)
	{
		s_Instance = new CGamePlatformSystem;
		if(s_Instance->init() == false || s_Instance->createBase() == false)
        {
            SAVE_RELEASE(s_Instance);
        }
	}
	return s_Instance;
}

void CGamePlatformSystem::releaseInstance()
{
    SAVE_RELEASE_GAMEENGINE(s_Instance);
    SAVE_RELEASE(s_Instance);
}

void CGamePlatformSystem::initGameSDK(void* pView)
{
    if(m_pPlatform)
    {
        m_pPlatform->initSDK(pView);
    }
}

void CGamePlatformSystem::applicationDidEnterBackground()
{
    if(m_pPlatform)
    {
        m_pPlatform->applicationDidEnterBackground();
    }
}


void CGamePlatformSystem::applicationWillEnterForeground()
{
    if(m_pPlatform)
    {
        m_pPlatform->applicationWillEnterForeground();
    }
}

void CGamePlatformSystem::registerGame()
{
	if (m_pPlatform)
	{
		m_pPlatform->registerGame();
	}
}

void CGamePlatformSystem::loginGame()
{
	if (m_pPlatform)
	{
		m_pPlatform->loginGame();
	}
}
void CGamePlatformSystem::changeAccount()
{
	if (m_pPlatform)
	{
		m_pPlatform->onChangeAccount();
	}
}

void CGamePlatformSystem::submitUserData(const char* pUserData)
{
	if (m_pPlatform)
	{
		m_pPlatform->submitUserData(pUserData);
	}
}

void CGamePlatformSystem::gamePay(const std::string& pstrPayInfo)
{
	if (m_pPlatform)
	{
		m_pPlatform->gamePay(pstrPayInfo);
	}
}

void CGamePlatformSystem::onClickLogin(int iLoginType)
{
	if (m_pPlatform)
	{
		m_pPlatform->onClickLogin(iLoginType);
	}
}

void CGamePlatformSystem::Logout()
{
	if (m_pPlatform)
	{
		m_pPlatform->logoutGame();
	}
}

void CGamePlatformSystem::sdkOpenURL(void* pURL, void* sourceApplication, void* application, void* nid)
{
    if(m_pPlatform)
    {
        m_pPlatform->platformOpenURL(pURL, sourceApplication, application, nid);
    }
}

void CGamePlatformSystem::onLoginGame(const char* pToken, const char* pstrUID, const char* userName)
{
	if (m_pPlatform)
	{
		m_pPlatform->onLoginGame(pToken, pstrUID, userName);
	}
}

void CGamePlatformSystem::onLogoutGame()
{
	if (m_pPlatform)
	{
		m_pPlatform->onLogoutGame();
	}
}

bool CGamePlatformSystem::createBase()
{
	if (s_Instance && s_Instance->m_pPlatform == nullptr)
	{
        s_Instance->m_pPlatform = new CIosPlatform;
		return true;
	}
	return false;
}

const std::string& CGamePlatformSystem::getLoginResAddr()
{
	return m_strLoginReqAddr;
}

void CGamePlatformSystem::setLoginResAddr(const std::string& strAddr)
{
	m_strLoginReqAddr = strAddr;
}

int CGamePlatformSystem::getPayChannelID()
{
    return  m_pPlatform == nullptr ? 1 : m_pPlatform->getPayChannelID();
}

INT32 CGamePlatformSystem::getGameCP()
{
    return  m_pPlatform == nullptr ? 1 : m_pPlatform->getGameCP();
}

INT32 CGamePlatformSystem::getGameVersion()
{
    return  m_pPlatform == nullptr ? 1 : m_pPlatform->getGameVersion();
}
