//
//  MessageQueue.h
//  *****
//
//  Created by xuli on 16/11/09.
// 
//	游戏网络消息队列

#ifndef MESSAGEQUEUE_H
#define MESSAGEQUEUE_H

#include "cocos2d.h"
USING_NS_CC;

#include <iostream> 
#include <list>
#include <mutex>
using namespace std;

class CSocketMsg;

class CMessageQueque
{
	typedef std::list<CSocketMsg*>	LIST_MSG;
	typedef LIST_MSG::iterator		ITER_MSG;
public:
	static CMessageQueque* Instance();

	static void ReleaseInstance();

	void ReleaseAllMsg();

	void SendMessage(CSocketMsg* pMsg);

	void PostMessage(CSocketMsg* pMsg);

	CSocketMsg* popMessage();

protected:
	CMessageQueque();

	~CMessageQueque();

private:
	LIST_MSG	m_ListMsg;

	static CMessageQueque* s_Instance;
	std::mutex			m_Mutex;
};


#endif // !MESSAGEQUEUE_H
