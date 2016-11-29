#include "GamePlatformSystem.h"
#if CC_TARGET_PLATFORM == CC_PLATFORM_IOS
	#include "platform/ios/AppStore.h"
#elif CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
	#include "platform/android/AndroidPlatform.h"	
#else
    #include "PlatformBase.h"
#endif




CGamePlatformSystem* CGamePlatformSystem::s_Instance = nullptr;

CGamePlatformSystem::CGamePlatformSystem()
:m_pPlatform(nullptr)
, m_strLoginReqAddr("")
{

}

CGamePlatformSystem::~CGamePlatformSystem()
{
	if (m_pPlatform)
	{
		delete m_pPlatform;
		m_pPlatform = nullptr;
	}
}

bool CGamePlatformSystem::init()
{
    return true;
}

void CGamePlatformSystem::release()
{
    
}

CGamePlatformSystem* CGamePlatformSystem::instance()
{
	if (s_Instance == nullptr)
	{
		s_Instance = new CGamePlatformSystem;
		createBase();
	}
	return s_Instance;
}

void CGamePlatformSystem::releaseInstance()
{
	if (s_Instance)
	{
		delete s_Instance;
		s_Instance = nullptr;
	}
}

void CGamePlatformSystem::initGameSDK(void* pView)
{
    if(m_pPlatform)
    {
        m_pPlatform->initSDK(pView);
    }
}

void CGamePlatformSystem::applicationDidEnterBackground()
{
    if(m_pPlatform)
    {
        m_pPlatform->applicationDidEnterBackground();
    }
}


void CGamePlatformSystem::applicationWillEnterForeground()
{
    if(m_pPlatform)
    {
        m_pPlatform->applicationWillEnterForeground();
    }
}

void CGamePlatformSystem::registerGame()
{
	if (m_pPlatform)
	{
		m_pPlatform->registerGame();
	}
}

void CGamePlatformSystem::loginGame()
{
	if (m_pPlatform)
	{
		m_pPlatform->loginGame();
	}
}
void CGamePlatformSystem::changeAccount()
{
	if (m_pPlatform)
	{
		m_pPlatform->onChangeAccount();
	}
}

void CGamePlatformSystem::submitUserData(const char* pUserData)
{
	if (m_pPlatform)
	{
		m_pPlatform->submitUserData(pUserData);
	}
}

void CGamePlatformSystem::gamePay(const std::string& pstrPayInfo)
{
	if (m_pPlatform)
	{
		m_pPlatform->gamePay(pstrPayInfo);
	}
}

void CGamePlatformSystem::onClickLogin(int iLoginType)
{
	if (m_pPlatform)
	{
		m_pPlatform->onClickLogin(iLoginType);
	}
}

void CGamePlatformSystem::Logout()
{
	if (m_pPlatform)
	{
		m_pPlatform->logoutGame();
	}
}

void CGamePlatformSystem::sdkOpenURL(void* pURL, void* sourceApplication, void* application, void* nid)
{
    if(m_pPlatform)
    {
        m_pPlatform->platformOpenURL(pURL, sourceApplication, application, nid);
    }
}

void CGamePlatformSystem::onLoginGame(const char* pToken, const char* pstrUID, const char* userName)
{
	if (m_pPlatform)
	{
		m_pPlatform->onLoginGame(pToken, pstrUID, userName);
	}
}

void CGamePlatformSystem::onLogoutGame()
{
	if (m_pPlatform)
	{
		m_pPlatform->onLogoutGame();
	}
}

bool CGamePlatformSystem::createBase()
{
	if (s_Instance && s_Instance->m_pPlatform == nullptr)
	{
#if CC_TARGET_PLATFORM == CC_PLATFORM_IOS
    s_Instance->m_pPlatform = new CAppStore;
#elif CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
	s_Instance->m_pPlatform = new CAndroidPlatform;
#else
	s_Instance->m_pPlatform = new CPlatformBase;
#endif	
		return true;
	}
	return false;
}

const std::string& CGamePlatformSystem::getLoginResAddr()
{
	return m_strLoginReqAddr;
}

void CGamePlatformSystem::setLoginResAddr(const std::string& strAddr)
{
	m_strLoginReqAddr = strAddr;
}

int CGamePlatformSystem::getPayChannelID()
{
    return  m_pPlatform == nullptr ? 1 : m_pPlatform->getPayChannelID();
}

INT32 CGamePlatformSystem::getGameCP()
{
    return  m_pPlatform == nullptr ? 1 : m_pPlatform->getGameCP();
}

INT32 CGamePlatformSystem::getGameVersion()
{
    return  m_pPlatform == nullptr ? 1 : m_pPlatform->getGameVersion();
}
