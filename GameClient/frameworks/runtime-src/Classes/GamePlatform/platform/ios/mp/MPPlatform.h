//
//  MPPlatform.hpp
//  buyu
//
//  Created by ff on 2016/11/10.
//
//

#ifndef MPPlatform_hpp
#define MPPlatform_hpp

#include "../../../PlatformBase.h"

class CMPPlatform:public CPlatformBase
{
public:
    CMPPlatform();
    
    ~CMPPlatform();
    
    virtual void initSDK(void* pView);
    
    virtual void loginGame();
    
    virtual void logoutGame();
    
    virtual void gamePay(const std::string& pstrPayInfo);
    
    virtual void platformOpenURL(void* pURL, void* sourceApplication, void* application, void* nid);
    
    virtual int getPayChannelID();
    
    virtual bool isHaveThirdPartyLogin();
    
    virtual void onLoginGame(const char* pToken, const char* pstrUID, const char* userName);
    
    virtual void onLogoutGame();
protected:
     void onHttpRequestCompleted(network::HttpClient *sender, network::HttpResponse *response);
private:
    std::string m_strKey;
    std::string m_strID;
    std::string m_uerName;
};

#endif /* MPPlatform_hpp */
