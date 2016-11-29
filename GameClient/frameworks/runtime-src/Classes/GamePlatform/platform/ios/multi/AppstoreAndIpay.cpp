//
//  AppstoreAndIpay.cpp
//  buyu
//
//  Created by ff on 2016/10/28.
//
//

#include "AppstoreAndIpay.h"
#include "appstore.h"
#ifdef mp_dwcby
#include "MPPlatform.h"
#else
#include "IPayPlatform.h"
#endif

#include "plazz/device/Device.h"

CAppStoreAndIpay::CAppStoreAndIpay():
m_pIpay(nullptr)
,m_pAppStore(nullptr)
{
    
}

CAppStoreAndIpay::~CAppStoreAndIpay()
{
    if(m_pAppStore)
    {
        delete m_pAppStore;
        m_pAppStore = nullptr;
    }
    
    if(m_pIpay)
    {
        delete m_pIpay;
        m_pIpay = nullptr;
    }
}

void CAppStoreAndIpay::initSDK(void* pView)
{
    if(m_pAppStore == nullptr)
    {
        m_pAppStore = new CAppStore();
        m_pAppStore->initSDK(pView);
    }
    
#ifdef mp_dwcby
    if(m_pIpay == nullptr)
    {
        m_pIpay = new CMPPlatform();
        m_pIpay->initSDK(pView);
    }
#else
    if(m_pIpay == nullptr)
    {
        m_pIpay = new CIPayPlatform();
        m_pIpay->initSDK(pView);
    }
#endif
    
}

void CAppStoreAndIpay::gamePay(const std::string& pstrPayInfo)
{
    if (JZ::Device::isThisVersionOnline())
    {
        if(m_pIpay)
        {
            m_pIpay->gamePay(pstrPayInfo);
        }
    }
    else{
        if(m_pAppStore)
        {
            m_pAppStore->gamePay(pstrPayInfo);
        }
    }
}

void CAppStoreAndIpay::platformOpenURL(void* pURL, void* sourceApplication, void* application, void* nid)
{
    if (JZ::Device::isThisVersionOnline())
    {
        if(m_pIpay)
        {
            m_pIpay->platformOpenURL(pURL, sourceApplication, application, nid);
        }
    }
    else{
        if(m_pAppStore)
        {
            m_pAppStore->platformOpenURL(pURL, sourceApplication, application, nid);
        }
    }
}

int CAppStoreAndIpay::getPayChannelID()
{
    int ret = 1;
    if (JZ::Device::isThisVersionOnline())
    {
        if(m_pIpay)
        {
            ret = m_pIpay->getPayChannelID();
        }
    }
    else{
        if(m_pAppStore)
        {
            ret = m_pAppStore->getPayChannelID();
        }
    }
    return  ret;
}

bool CAppStoreAndIpay::isHaveThirdPartyLogin()
{
    bool ret = false;
    if (JZ::Device::isThisVersionOnline())
    {
        if(m_pIpay)
        {
            ret = m_pIpay->isHaveThirdPartyLogin();
        }
    }
    else{
        if(m_pAppStore)
        {
            ret = m_pAppStore->isHaveThirdPartyLogin();
        }
    }
    return  ret;
}

void CAppStoreAndIpay::loginGame()
{
    if (JZ::Device::isThisVersionOnline())
    {
        if(m_pIpay)
        {
            m_pIpay->loginGame();
        }
    }
    else{
        if(m_pAppStore)
        {
            m_pAppStore->loginGame();
        }
    }
}
