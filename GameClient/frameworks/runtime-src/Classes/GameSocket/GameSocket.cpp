//
//  GameSocket.cpp
//  *****
//
//  Created by xuli on 16/11/09.
// 
//	socket 处理网络底层数据包

#include "GameSocket.h"
#include "SocketMsg.h"
#include "MessageQueue.h"


#include "QPCipher.h"

#if (CC_TARGET_PLATFORM==CC_PLATFORM_WIN32)
#include <WinSock2.h>  
#include <WS2tcpip.h>
#pragma comment (lib,"WS2_32.Lib")  
#define LHS_EAGAIN          WSAEWOULDBLOCK    
#define LHS_EWOULDBLOCK     WSAEWOULDBLOCK  
#else
#include <signal.h>  
#include <sys/socket.h>  
#include <netinet/in.h>  
#include<netinet/tcp.h>  
#include <netdb.h>  
#include <errno.h>  
#include <fcntl.h>  
#include <unistd.h>  
#include <sys/stat.h>  
#include <sys/types.h>  
#include <arpa/inet.h> 
typedef int					SOCKET;
#define INVALID_SOCKET		(SOCKET)(~0)  
#define SOCKET_ERROR		(-1)  
#define LHS_EAGAIN          EAGAIN              //此操作会令线程阻塞,但socket为不可阻塞的  
#define LHS_EWOULDBLOCK     EWOULDBLOCK         //资源暂时不可用  
#endif

using namespace std;

static std::mutex s_Mutex;

#define MAX_BUFFER_SIZE  1024*1024*16

/** 头包大小 **/
static const int	SIZE_PACK_HEAD = 8;

/************************************************************************/
/*                                                                      */

static void socket_close(int s)
{
#if (CC_TARGET_PLATFORM==CC_PLATFORM_WIN32)  
	shutdown(s, SD_BOTH);
	closesocket(s);
#else  
	close(s);
#endif 
}

static int socket_send(int s, const char* data, int size)
{
	unsigned int flags = 0;
#if (CC_TARGET_PLATFORM==CC_PLATFORM_ANDROID)  
	flags = MSG_NOSIGNAL;
#endif  
	return send(s, data, size, flags);
}

static int socket_recv(int s, char* data, int size)
{
	unsigned int flags = 0;
#if (CC_TARGET_PLATFORM==CC_PLATFORM_ANDROID)  
	flags = MSG_NOSIGNAL;
#endif  
	return recv(s, data, size, flags);
}

static int socket_error()
{
#if (CC_TARGET_PLATFORM==CC_PLATFORM_WIN32)
	return WSAGetLastError();
#else
	return errno;
#endif
}

static void socket_sleep(unsigned int delay)
{
#if (CC_TARGET_PLATFORM==CC_PLATFORM_WIN32)
	Sleep(delay);
#else
	usleep(delay * 1000);
#endif
}
/************************************************************************/


CGameSocket::CGameSocket()
:m_pBuffer(nullptr)
,m_iSocket(INVALID_SOCKET)
, m_iPort(0)
, m_strUrl("")
{
	m_pBuffer = new ByteBuffer(MAX_BUFFER_SIZE);
}

CGameSocket::~CGameSocket()
{
	if (m_pBuffer)
	{
		delete m_pBuffer;
		m_pBuffer = nullptr;
	}

	disconnect();
}

bool CGameSocket::isAlive() const
{
	return m_iSocket != INVALID_SOCKET;
}

int  CGameSocket::connect(int iGameFlag, const char* url, int port)
{
	cocos2d::log("connecting %s , port : %d ***********************", url, port);
	disconnect();

	char strIP[100];
	sprintf(strIP, "%s", url);

	char strPort[100];
	sprintf(strPort, "%d", port);

	struct  addrinfo *ailist, *aip;
	struct addrinfo hint;
	struct sockaddr_in *sinp;
	int sockfd;
	int err;
	char seraddr[INET_ADDRSTRLEN];
	short serport;

	hint.ai_family = 0;
	hint.ai_socktype = SOCK_STREAM;
	hint.ai_flags = AI_CANONNAME;
	hint.ai_protocol = 0;
	hint.ai_addrlen = 0;
	hint.ai_addr = NULL;
	hint.ai_canonname = NULL;
	hint.ai_next = NULL;

	if ((err = getaddrinfo(url, strPort, &hint, &ailist)) != 0) {
		cocos2d::log("getaddrinfo error: %s\n", gai_strerror(err));
		return -1;
	}

	bool isConnectOK = false;
	for (aip = ailist; aip != NULL; aip = aip->ai_next) {
		sinp = (struct sockaddr_in *)aip->ai_addr;
		if (inet_ntop(sinp->sin_family, &sinp->sin_addr, seraddr, INET_ADDRSTRLEN) != NULL) {
			cocos2d::log("server address is %s\n", seraddr);
		}
		else
		{
			continue;
		}

		serport = ntohs(sinp->sin_port);
		cocos2d::log("server port is %d\n", serport);

		if (serport == 0)
		{
			sinp->sin_port = htons(port);
		}

		if ((sockfd = socket(aip->ai_family, SOCK_STREAM, 0)) < 0) {
			cocos2d::log("create socket failed: %s\n", strerror(errno));
			isConnectOK = false;
			//onSocketError(1025);//socket_error());
			continue;
		}

		cocos2d::log("create socket ok\n");

		if (::connect(sockfd, aip->ai_addr, aip->ai_addrlen) < 0) {
			//SocketDetector::getInstance()->pop(sockfd);
			/// failed
			int errCode = socket_error();
			cocos2d::log("connect error code = %d, ***********", errCode);
			socket_close(sockfd);
			//onSocketError(1025);//errCode);
			isConnectOK = false;
			continue;
		}
		isConnectOK = true;
		break;
	}
	freeaddrinfo(ailist);
	if (isConnectOK) {
		cocos2d::log("Connect %s OK", url);
	}
	else {
		return  -1;
	}

	m_iSocket = (int)sockfd;
	m_iGameSocketFlag = iGameFlag;
	m_iPort = port;
	m_strUrl = url;

	// Create Receiver Thread
	thread t(recv_thread,this);
	t.detach();
	return m_iSocket;
}

void CGameSocket::disconnect()
{
	socket_close(m_iSocket);
	m_iSocket = INVALID_SOCKET;
}

int CGameSocket::send(const char* data, int size)
{
	return 0;
}

void CGameSocket::recv_thread(void* p)
{
	CGameSocket* pThis = (CGameSocket*)p;
	ByteBuffer* pBuffer = pThis->m_pBuffer;
	while (1)
	{
		int dataSize = socket_recv(pThis->m_iSocket, pBuffer->getBuffer(), pBuffer->remaining());
		if (dataSize == SOCKET_ERROR)
		{
			int errCode = socket_error();
			if (errCode == LHS_EWOULDBLOCK)
			{
				socket_sleep(20);
				continue;
			}
			else if (errCode == LHS_EAGAIN)
			{
				socket_sleep(20);
				continue;
			}
			if (errCode == 10004)
			{
				break;
			}

			/*if (__SocketMap.size() > socketIndex && pTHIS && MTSocketQueue::shared())
			{
			MTSocketQueue::shared()->mDataMutex.lock();
			MTSocketQueue::shared()->push(socketIndex, 1, 0, errCode);
			MTSocketQueue::shared()->mDataMutex.unlock();
			socket_sleep(20);
			continue;
			}*/

			break;
		}
		else if (dataSize == 0)	// server close socket
		{
			/*MTSocketQueue::shared()->mDataMutex.lock();
			MTSocketQueue::shared()->push(socketIndex, 1, 0, 0);
			MTSocketQueue::shared()->mDataMutex.unlock();*/
			socket_sleep(20);
			break;
		}
		else
		{
				
			pBuffer->setPosition(dataSize);
			int nRecvDataMax = pBuffer->getPosition();
			pBuffer->flip();

			int nCurLenth = 0;
			int off = 0;
			while (nRecvDataMax > nCurLenth)//多个包的时候由于socket会把多个包拼凑成一个包 这个地方需要拆分为单包
			{
				if (nRecvDataMax - nCurLenth < SIZE_PACK_HEAD) // 剩余的长度小于一个 short
				{
					memcpy(pBuffer->buffer, pBuffer->buffer + nCurLenth, nRecvDataMax - nCurLenth);
					off = nRecvDataMax - nCurLenth;
					cocos2d::log("one byte offset = %d, curpostion = %d", off, pBuffer->getPosition());
					break;
				}

				int sDataSize = QPCipher::getPackSize((unsigned char*)pBuffer->getBuffer(), pBuffer->getPosition());
				int nDataLen = sDataSize;

				if (nRecvDataMax >= nCurLenth + sDataSize)
				{
					char* tmpRecData = new char[nDataLen];
					memset(tmpRecData, 0, nDataLen);
					pBuffer->get(tmpRecData, 0, nDataLen);
					pBuffer->setPosition(sDataSize);
					nCurLenth += sDataSize;

					pThis->unpack((unsigned char*)tmpRecData, 0, nDataLen);

					delete[] tmpRecData;

					cocos2d::log("Socket:%d process message length: Max:%d nCur:%d nCurLenth:%d", pThis->m_iSocket, nRecvDataMax, sDataSize, nCurLenth);
				}
				else{
					memcpy(pBuffer->buffer, pBuffer->buffer + nCurLenth, nRecvDataMax - nCurLenth);
					off = nRecvDataMax - nCurLenth;
					cocos2d::log("Data is truncated and need to be removed offset = %d, curpostion = %d", off, pBuffer->getPosition());

					break;
				}
			}

			pBuffer->flip();
			pBuffer->setPosition(off);

			if (nRecvDataMax != nCurLenth)
			{
				cocos2d::log("Socket: process message length Error %d, %d \n", nRecvDataMax, nCurLenth);
			}
			else
			{
				cocos2d::log("Socket: process message length Success %d \n", nRecvDataMax);
			}

		}

	}

	if (s_Mutex.try_lock())
	{
		pThis->m_iSocket = INVALID_SOCKET;
		s_Mutex.unlock();
	}
	
}

bool CGameSocket::unpack(UINT8* data, int start, int length)
{
	// 解密
	if ((data[start] & QPCipher::getCipherMode()) > 0)
	{
		QPCipher::decryptBuffer(data, start, length);
	}
	else
	{
		return false;
	}
	// 主命令码
	int main = QPCipher::getMainCommand(data, start);
	// 次命令码
	int sub = QPCipher::getSubConmmand(data, start);

	// 附加数据
	if (length > 8)
	{
		//memcpy(mBufUnPack, &data[start + 8], length - 8);
	}

	length -= 8;

	if (main == 400 && sub == 1)
	{
		/*SocketDetector::getInstance()->update(&mSocket);
		onHeartBeat(); /// 回复心跳*/
	}
	else
	{
		//if (mISocketEngineSink != 0)
		//{
		//	PLAZZ_PRINTF("====CSocketEngine::unpack main=%d sub=%d  buffsize=%d  ===beg==", main, sub, length);

		//	if (length < 0)
		//	{
		//		return  false;
		//	}

		//	bool bHandle = mISocketEngineSink->onEventTCPSocketRead(main, sub, mBufUnPack, length);
		//	//收到捕鱼消息，重置消息时间
		//	if (main == 200)
		//	{
		//		SocketDetector::getInstance()->update(&mSocket);
		//	}

		//	PLAZZ_PRINTF("====CSocketEngine::unpack main=%d sub=%d  ======end==", main, sub);

		//	return bHandle;
		//}
	}


	return true;
}

