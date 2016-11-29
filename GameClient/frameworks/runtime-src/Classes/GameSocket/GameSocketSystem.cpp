//
//  GameSocketSystem.cpp
//  *****
//
//  Created by xuli on 16/11/09.
// 
//	socket 处理网络底层数据包

#include "GameSocketSystem.h"
#include "GameEngine.h"

#include "scripting/lua-bindings/auto/lua_IGameSocketSystem_auto.hpp"

#include "scripting/lua-bindings/manual/tolua_fix.h"
#include "scripting/lua-bindings/manual/LuaBasicConversions.h"
#include "scripting/lua-bindings/manual/cocos2d/LuaScriptHandlerMgr.h"
#include "scripting/lua-bindings/manual/CCLuaValue.h"
#include "scripting/lua-bindings/manual/CCLuaEngine.h"

CGameSocketSystem* CGameSocketSystem::s_pInstance = nullptr;

IGameSocketSystem* IGameSocketSystem::share()
{
    return (IGameSocketSystem*)CGameSocketSystem::instance();
}

void IGameSocketSystem::releaseInstance()
{
    CGameSocketSystem::releaseInstance();
}

CGameSocketSystem::CGameSocketSystem()
{

}

CGameSocketSystem::~CGameSocketSystem()
{

}

IModule* CGameSocketSystem::instance()
{
	if (s_pInstance == nullptr)
	{
		s_pInstance = new CGameSocketSystem;
        if(s_pInstance && s_pInstance->init() == false)
        {
            SAVE_RELEASE(s_pInstance);
        }
	}
	return s_pInstance;
}

void CGameSocketSystem::releaseInstance()
{
    SAVE_RELEASE_GAMEENGINE(s_pInstance);
    SAVE_RELEASE(s_pInstance);
}

bool CGameSocketSystem::init()
{
    return true;
}

void CGameSocketSystem::release()
{
	ITER_SOCKET iter = m_mapSocket.begin();
	for (; iter != m_mapSocket.end(); ++iter)
	{
		CGameSocket* psocket = iter->second;
		if (psocket != nullptr)
		{
			delete psocket;
		}
		psocket = nullptr;
	}

	m_mapSocket.clear();
}

int  CGameSocketSystem::lua_GameModule_register(void* pL)
{
//    lua_State* L = static_cast<lua_State*>(pL);
//    if(L != nullptr)
//    {
//        lua_getglobal(L, "_G");
//        if (lua_istable(L,-1))//stack:...,_G,
//        {
//            register_all_IGameSocketSystem(L);
//        }
//        lua_pop(L, 1);
//    }
    lua_State* L = static_cast<lua_State*>(pL);
    register_all_IGameSocketSystem(L);
    return 1;
}

void CGameSocketSystem::closeSocket(int isocketType)
{
	ITER_SOCKET iter = m_mapSocket.find(isocketType);
	if (iter != m_mapSocket.end())
	{
		CGameSocket* pSocket = iter->second;
		if (pSocket != nullptr)
		{
			pSocket->disconnect();
			delete pSocket;
			pSocket = nullptr;
			m_mapSocket.erase(iter);
		}
	}
}

int CGameSocketSystem::sendMsg(int isocketType, const char* pdata, int isize)
{
	int buffsize = 0;
	ITER_SOCKET iter = m_mapSocket.find(isocketType);
	if (iter != m_mapSocket.end())
	{
		CGameSocket* pSocket = iter->second;
		if (pSocket != nullptr)
		{
			buffsize = pSocket->send(pdata, isize);
		}
	}

	return buffsize;
}

int CGameSocketSystem::connect(const char* purl, int iprot)
{
	unsigned int isocketType = APHash(purl);
	ITER_SOCKET iter = m_mapSocket.find(isocketType);
	if (iter != m_mapSocket.end())
	{
		CGameSocket* pSocket = iter->second;
		if (pSocket)
		{
			if (pSocket->isAlive() == false)
			{
				pSocket->connect(isocketType, purl, iprot);
			}
		}
	}
	else
	{
		CGameSocket* pSocket = new CGameSocket();
		pSocket->connect(isocketType, purl, iprot);

		m_mapSocket[isocketType] = pSocket;
	}
	return 0;
}

void CGameSocketSystem::sendNetMsg()
{

}
