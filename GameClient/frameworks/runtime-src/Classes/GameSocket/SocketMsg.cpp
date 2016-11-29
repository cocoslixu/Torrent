//
// SocketMsg.h
//  *****
//
//  Created by xuli on 16/11/09.
// 
//	分包后的消息

#include <iostream> 
#include "SocketMsg.h"
using namespace std;

CSocketMsg::CSocketMsg():
m_iSize(0),
m_iMain(0),
m_iSub(0),
m_pBuffer(nullptr)
{

}

CSocketMsg::~CSocketMsg()
{
	Release();
}

void CSocketMsg::Release()
{
	if (m_pBuffer != nullptr)
	{
		delete[] m_pBuffer;
		m_pBuffer = nullptr;
	}

	m_iSub = 0;
	m_iMain = 0;
	m_iSub = 0;
}

void CSocketMsg::InitData(unsigned int iMain, unsigned int iSub, const unsigned char* pBuffer, unsigned int iSize)
{
	Release();

	m_iMain = iMain;
	m_iSub = iSub;
	m_iSize = iSize;

	if (pBuffer != nullptr)
	{
		m_pBuffer = new unsigned char[iSize + 1];
		memset(m_pBuffer, 0, iSize + 1);
		memcpy(m_pBuffer, pBuffer,iSize);
	}
}

unsigned int CSocketMsg::GetMainID()const
{
	return m_iMain;
}

unsigned int CSocketMsg::GetSubID()const
{
	return m_iSub;
}

const unsigned char* CSocketMsg::GetBuffer()const
{
	return m_pBuffer;
}

const unsigned int CSocketMsg::GetBufferSize()const
{
	return m_iSize;
}