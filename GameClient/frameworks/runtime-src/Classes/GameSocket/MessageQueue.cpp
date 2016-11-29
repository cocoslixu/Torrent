//
//  MessageQueue.cpp
//  *****
//
//  Created by xuli on 16/11/09.
// 
//	游戏网络消息队列

#include "MessageQueue.h"
#include "SocketMsg.h"

CMessageQueque* CMessageQueque::s_Instance = nullptr;

CMessageQueque::CMessageQueque()
{

}

CMessageQueque::~CMessageQueque()
{

}

CMessageQueque* CMessageQueque::Instance()
{
	if (s_Instance == nullptr)
	{
		s_Instance = new CMessageQueque;
	}
	return s_Instance;
}

void CMessageQueque::ReleaseInstance()
{
	if (s_Instance)
	{
		s_Instance->ReleaseAllMsg();
		delete s_Instance;
	}
	s_Instance = nullptr;
}

void CMessageQueque::ReleaseAllMsg()
{
	if (m_Mutex.try_lock())
	{
		CSocketMsg* pMsg = nullptr;
		ITER_MSG iter = m_ListMsg.begin();
		for (; iter != m_ListMsg.end();)
		{
			pMsg = *(iter);
			if (pMsg != nullptr)
			{
				delete pMsg;
				pMsg = nullptr;
			}

			iter = m_ListMsg.erase(iter);
		}
		m_Mutex.unlock();
	}
}

void CMessageQueque::SendMessage(CSocketMsg* pMsg)
{
	if (pMsg != nullptr)
	{
		if (m_Mutex.try_lock())
		{
			m_ListMsg.push_back(pMsg);
			m_Mutex.unlock();
		}
	}
}

void CMessageQueque::PostMessage(CSocketMsg* pMsg)
{
	if (pMsg != nullptr)
	{
	   //todo lua
	}
}

CSocketMsg* CMessageQueque::popMessage()
{
	CSocketMsg* pMsg = nullptr;
	if (m_Mutex.try_lock())
	{
		if (!m_ListMsg.empty())
		{
			pMsg = m_ListMsg.front();
			m_ListMsg.pop_front();

		}
		m_Mutex.unlock();
	}

	return pMsg;
}