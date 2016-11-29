//
//  GameSocketSystem.h
//  *****
//
//  Created by xuli on 16/11/09.
// 
//	socket 处理网络底层数据包


#ifndef GAMESOCKETSYSTEM_H
#define GAMESOCKETSYSTEM_H

#include "GameSocket.h"
#include "EngineBase.h"
#include "IGameSocketSystem.h"

#include "cocos2d.h"
USING_NS_CC;

enum SocketSystemType
{
	SOCKET_CONNECT_SUCCESS,

	SOCKET_CONNECT_FAIL,

	SOCKET_CLOSET,
};

#define CLIENT_MSG_SOCKET_MAIN		0xffffff01
#define CLIENT_MSG_SOCKET_SUB		0xffffff00

class CGameSocketSystem:public IGameSocketSystem
{
protected:
	CGameSocketSystem();

	~CGameSocketSystem();
public:
	
	static IModule* instance();

	static void releaseInstance();

    virtual bool init();
    
    virtual void release();
    
    virtual int  lua_GameModule_register(void* pL);

	virtual void closeSocket(int isocketType);

	virtual int sendMsg(int isocketType, const char* pdata, int isize);

	virtual int connect(const char* purl, int iprot);

	void sendNetMsg();

private:
	typedef std::map<int, CGameSocket*> MAP_SOCKET;
	typedef MAP_SOCKET::iterator ITER_SOCKET;

	MAP_SOCKET m_mapSocket;

	static CGameSocketSystem* s_pInstance;
};

#endif // !GAMESOCKETSYSTEM_H
