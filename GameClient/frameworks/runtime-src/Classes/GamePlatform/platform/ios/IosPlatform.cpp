#include "IosPlatform.h"

CIosPlatform::CIosPlatform()
{

}

CIosPlatform::~CIosPlatform()
{

}

INT32 CIosPlatform::getGameCP()
{
    return 2;
}

INT32 CIosPlatform::getGameVersion()
{
    return 2;
}

INT32 CIosPlatform::getPayChannelID()
{
    return 2;
}

void CIosPlatform::initSDK(void* pView)
{
    
}

void CIosPlatform::registerGame()
{
    
}

void CIosPlatform::onClickLogin(INT32 iLoginType)
{
    
}


void CIosPlatform::loginGame()
{
    
}

void CIosPlatform::logoutGame()
{
    
}

void CIosPlatform::submitUserData(const INT8* pUserData)
{
    
}

/************************************************************************/
/*
 std::string fromat = R"({
 "PurchaseType":"%d",
 "dwUserID":"%d",
 "orderIdStr":"%s",
 "price":"%d",
 "validateAddr":"%s",
 "reason":"%s",
 "productName":"%s"
 })";
 */
/************************************************************************/
void CIosPlatform::gamePay(const std::string& pstrPayInfo)
{
    Json* mjs = Json_create(pstrPayInfo.c_str());
    if(mjs)
    {
        
        int purchaseType = 0;
        std::string strUserId = "";
        std::string orderId = "";
        
        const char* pstr = Json_getString(mjs,"PurchaseType", "");
        if(pstr != NULL)
        {
            purchaseType = atoi(pstr);
        }
        
        pstr = Json_getString(mjs,"dwUserID", "");
        if(pstr != NULL)
        {
            strUserId = pstr;
        }
        
        pstr = Json_getString(mjs,"orderIdStr", "");
        if(pstr != NULL)
        {
            orderId = pstr;
        }
        
        //JZ::Device::pay(purchaseType, strUserId, orderId);
        
    }
}

void CIosPlatform::applicationDidEnterBackground()
{
    
}

void CIosPlatform::applicationWillEnterForeground()
{
    
}

void CIosPlatform::platformOpenURL(void* pURL, void* sourceApplication, void* application, void* nid)
{
    
}

//call back
void CIosPlatform::onLoginGame(const char* pToken, const INT8* pstrUID, const INT8* userName)
{
    
}

void CIosPlatform::onLogoutGame()
{
    
}

void CIosPlatform::onChangeAccount()
{
    
}

void CIosPlatform::onGamePay(bool bSuc)
{
    
}

