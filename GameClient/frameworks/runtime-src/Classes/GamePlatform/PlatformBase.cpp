#include "PlatformBase.h"

CPlatformBase::CPlatformBase()
:m_strUID("")
, m_strToken("")
{

}

CPlatformBase::~CPlatformBase()
{

}

INT32 CPlatformBase::getGameCP()
{
    return 1;
}

INT32 CPlatformBase::getGameVersion()
{
    return 1;
}

INT32 CPlatformBase::getPayChannelID()
{
    return 1;
}

void CPlatformBase::initSDK(void* pView)
{
    
}

void CPlatformBase::onClickLogin(int iLoginType)
{
}

void CPlatformBase::registerGame()
{

}

void CPlatformBase::loginGame()
{

}
    
void CPlatformBase::onChangeAccount()
{

}

void CPlatformBase::logoutGame()
{
	CPlatformBase::onLogoutGame();
}

void CPlatformBase::submitUserData(const INT8* pUserData)
{

}

void CPlatformBase::gamePay(const std::string& pstrPayInfo)
{

}

void CPlatformBase::applicationDidEnterBackground()
{
    
}

void CPlatformBase::applicationWillEnterForeground()
{
    
}

void CPlatformBase::platformOpenURL(void* pURL, void* sourceApplication, void* application, void* nid)
{
    
}


void CPlatformBase::onLoginGame(const INT8* pToken, const INT8* pstrUID, const INT8* userName)
{
}

void CPlatformBase::onGamePay(bool bSuc)
{

}

void CPlatformBase::onLogoutGame()
{
}
