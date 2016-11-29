#ifndef ANDROIDBASE_H
#define ANDROIDBASE_H
#include "../../PlatformBase.h"
#include "cocos2d.h"
#include "cocos-ext.h"
#include "network/HttpClient.h"

class CAndroidPlatform : public CPlatformBase, public cocos2d::Ref
{
public:
	CAndroidPlatform();

	virtual ~CAndroidPlatform();

	virtual void registerGame();

	virtual void onClickLogin(int iLoginType);

	virtual void loginGame();

	virtual void logoutGame();

	virtual void submitUserData(const char* pUserData);

	virtual void gamePay(const std::string& pstrPayInfo);

public:
	virtual void onLoginGame(const char* pToken, const char* pstrUID, const char* userName);

	virtual void onLogoutGame();

	virtual void onGamePay(bool bSuc);

	virtual void onChangeAccount();

	void onHttpRequestCompleted(network::HttpClient *sender, network::HttpResponse *response);

	//
private:
	std::string m_strKey;
	std::string m_strID;
	std::string m_uerName;
};

#endif
