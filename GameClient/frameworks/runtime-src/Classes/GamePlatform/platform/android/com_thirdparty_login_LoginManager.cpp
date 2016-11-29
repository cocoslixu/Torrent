#include "com_thirdparty_login_LoginManager.h"
#include "../../GamePlatformSystem.h"
#include <jni.h>
#include "platform/android/jni/JniHelper.h"
USING_NS_CC;

#ifdef __cplusplus

// static const char* BUNDLE_CLASS_PATH = "com/thirdparty/login/ThirdPartyUserInfo";

JNIEXPORT void JNICALL Java_com_thirdparty_login_LoginManager_nativeOnActionResult
  (JNIEnv * env, jclass objclass, jint action,  jboolean result, jstring juserName, jstring jstrKey, jstring jstrID)
  {
  	if(1 == action) // login
  	{
  		if(result)
  		{
  			if(CGamePlatformSystem::share())
  			{
  				std::string strName = JniHelper::jstring2string(juserName);
  				std::string strKey = JniHelper::jstring2string(jstrKey);
  				std::string strID = JniHelper::jstring2string(jstrID);

  				CGamePlatformSystem::share()->onLoginGame(strKey.c_str(), strID.c_str(), strName.c_str());
  			}
  		}
  	}
  	else if(2 == action) //logout
  	{
  		if(result)
  		{
  			if(CGamePlatformSystem::share())
  			{
  				CGamePlatformSystem::share()->onLogoutGame();
  			}
  		}
  	}
  }

#endif