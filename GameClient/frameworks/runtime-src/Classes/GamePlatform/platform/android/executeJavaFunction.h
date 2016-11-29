#ifndef EXECUTEJAVAFUNCTOIN_H
#define EXECUTEJAVAFUNCTOIN_H

#include "../../PlatformBase.h"
#include "cocos2d.h"
#include "cocos-ext.h"
#include "network/HttpClient.h"

class CExecuteJavaFunction
{
public:
	static void executeLogin();

	static void executeLogout();

	static void executeGamePay(const std::string& pstrPayInfo);

	static void executSubmitInfo(const char* pInfo);

	static void executChangeAccount();
};

#endif // !EXECUTEJAVAFUNCTOIN_H
