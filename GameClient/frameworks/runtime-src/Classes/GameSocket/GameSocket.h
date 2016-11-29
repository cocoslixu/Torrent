//
//  GameSocket.h
//  *****
//
//  Created by xuli on 16/11/09.
// 
//	socket ��������ײ����ݰ� 

#ifndef GAMESOCKET_H
#define GAMESOCKET_H

#include "EngineBase.h"
#include <list>
#include <thread>
#include <mutex>
#include <iostream>   
#include <map>
#include "cocos2d.h"
#include "ByteBuffer.h"
USING_NS_CC;

enum socket_type
{

};

class CGameSocket
{
public:
	CGameSocket();

	~CGameSocket();

	bool isAlive() const;

	int  connect(int iGameFlag ,const char* url, int port);

	void disconnect();

	int send(const char* data, int size);

protected:
	// ���������߳�
	static void recv_thread(void* p);

	bool unpack(UINT8* data, int start, int length);

private:
	int				m_iSocket;

	ByteBuffer* m_pBuffer;

	int				m_iGameSocketFlag;

	std::string  m_strUrl;

	int m_iPort;
};

#endif // !GAMESOCKET_H
