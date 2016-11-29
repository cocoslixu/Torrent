//
//  AppstoreAndIpay.hpp
//  buyu
//
//  Created by ff on 2016/10/28.
//
//

#ifndef AppstoreAndIpay_hpp
#define AppstoreAndIpay_hpp

#include "PlatformBase.h"

class CAppStoreAndIpay:public CPlatformBase
{
public:
    CAppStoreAndIpay();
    
    ~CAppStoreAndIpay();
    
    virtual void initSDK(void* pView);
    
    virtual void gamePay(const std::string& pstrPayInfo);
    
    virtual void platformOpenURL(void* pURL, void* sourceApplication, void* application, void* nid);
    
    virtual int getPayChannelID();
    
    virtual bool isHaveThirdPartyLogin();
    
    virtual void loginGame();
    
private:
    CPlatformBase* m_pAppStore;
    CPlatformBase* m_pIpay;
};

#endif /* AppstoreAndIpay_hpp */
