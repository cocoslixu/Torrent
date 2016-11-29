//
// SocketMsg.h
//  *****
//
//  Created by xuli on 16/11/09.
// 
//	分包后的消息

#ifndef SOCKETMSG_H
#define SOCKETMSG_H

#include "cocos2d.h"
USING_NS_CC;

class CSocketMsg
{
public:
	CSocketMsg();

	~CSocketMsg();

	void InitData(unsigned int iMain, unsigned int iSub, const unsigned char* pBuffer, unsigned int iSize);

	unsigned int GetMainID()const;

	unsigned int GetSubID()const;

	const unsigned char* GetBuffer()const;

	const unsigned int GetBufferSize()const;

protected:
	void Release();

private:
	unsigned int m_iMain;
	unsigned int m_iSub;
	unsigned char* m_pBuffer;
	unsigned int m_iSize;
};

#endif//SOCKETMSG_H