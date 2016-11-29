//////////////////////////////////////////////////////////////////////////
// 协议加密 加密解密类
//////////////////////////////////////////////////////////////////////////

#ifndef QPCIPHER_H
#define QPCIPHER_H

#include "EngineBase.h"

/** 验证包大小 **/
static const int SIZE_VALIDATE = 136;

class QPCipher
{
public:
	static UINT8* encryptBuffer(UINT8* data, INT32 dataSize);
	static UINT8* decryptBuffer(UINT8* data, INT32 start, INT32 dataSize);

	static INT32   getCipherMode();

	static INT32   getMainCommand(UINT8* data, INT32 start);
	static INT32   getSubConmmand(UINT8* data, INT32 start);
	static INT32   getPackSize(UINT8* data, INT32 start);
	static void  setPackInfo(UINT8* data, INT32 dataSize , INT32 main, INT32 sub);

	static INT32   getCipher(UINT8* data, INT32 start);
	static INT32   getCipherCode(UINT8* data, INT32 start);

	static UINT8* tcpValidate(UINT8* data, INT32 start);
};
#endif //QPCIPHER_H
