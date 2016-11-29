//
//  GamePlatform_Debug.cpp
//  xxz
//
//  Created by daily-play on 15/6/10.
//
//
//
//  PP_Plarform.cpp
//  xxz
//
//  Created by daily-play on 15/6/8.
//
//
/*****

 同步推：
 appid：150847
 appkey：*WMjwG9qg4saPnzK*Wj7w9TqfsCaPczJ
 包名：com.mtgame.xxjqt.tongbu
 支付回调：http://123.59.36.108:5008/tongbu/pay.php
******/

#include <stdio.h>
#include "TB_Platform.h"
#import <TBPlatform/TBPlatform.h>
#import "TBSDKDelegate.h"
#include "plazz/data/GlobalState.h"
#include "consts/GameConsts.h"
#include "plazz/data/GlobalUserInfo.h"
#include "GamePlatform/GamePlatformSystem.h"
#include "event/NotifyKey.h"
#include "event/MTNotification.h"
#include "plazz/device/Device.h"

USING_NS_CC;


#define Release_OC(obc) {if(obc)[obc release]; obc = nullptr;}
CTB_Platform::CTB_Platform()
{
    
}

CTB_Platform::~CTB_Platform()
{
    
}


void CTB_Platform::initSDK(void *pView)
{
    
//    [[TBDelegate SharedInstance] InitDeleage:(UIViewController *)pView];
    [[TBDelegate SharedInstance] InitDeleage:(UIViewController *)nullptr];
    [[TBPlatform defaultPlatform] TBInitPlatformWithAppID:161104
                                        screenOrientation:UIInterfaceOrientationLandscapeLeft
                          isContinueWhenCheckUpdateFailed:NO];
    
    //设置横屏
    [[TBPlatform defaultPlatform] TBSetAutoRotation:YES];
    
    //悬浮工具条显示
//    [[TBPlatform defaultPlatform] TBShowToolBar:TBToolBarAtTopLeft isUseOldPlace:YES];
    
    
}
void CTB_Platform::platformOpenURL(void *pURL, void *sourceApplication, void *application, void *nid)
{
//    [[TBPlatform defaultPlatform] tbhan]
}


void CTB_Platform::loginGame()
{
    //此处进行登录操作
     [[TBPlatform defaultPlatform] TBLogin:0];
    
}

void CTB_Platform::logoutGame()
{
    if(JZ::Device::isHaveThirdParyExit())
    {
         [[TBPlatform defaultPlatform] TBLogout:0];
    }
    else
    {
        onLogoutGame();
    }
    
}

void CTB_Platform::onChangeAccount()
{
    
}

void CTB_Platform::onLoginGame(const char* pToken,const char* pstrUID,const char* userName)
{
    if(pToken)
    {
        GlobalState::getInstance()->setCPKey(pToken);
        m_strKey = std::string(pToken);
    }
    
    if(pstrUID)
    {
        GlobalState::getInstance()->setCPID(pstrUID);
        m_strID = std::string(pstrUID);
    }
    
    if(userName)
    {
        GlobalState::getInstance()->setCPName(userName);
        m_uerName = std::string(userName);
    }
    
    //1请求web 2 发送登入消息
    cocos2d::network::HttpRequest *request = new (std::nothrow) network::HttpRequest();
    std::string url = CGamePlatformSystem::share()->getLoginResAddr(); //std::string("http://") +
    request->setUrl(url.c_str());
    request->setRequestType(network::HttpRequest::Type::POST);
    request->setResponseCallback(CC_CALLBACK_2(CTB_Platform::onHttpRequestCompleted, this));
    char requestContent[256] = { 0 };
    sprintf(requestContent, "{ \"customerid\" : \"%s\", \"customerKey\" : \"%s\", \"cp\" : \"%d\" }", m_strID.c_str(), m_strKey.c_str(), CURRENT_CP);
    cocos2d::log("%s", requestContent);
    std::vector<std::string> headers;
    headers.push_back("Content-Type: application/json; charset=utf-8");
    request->setHeaders(headers);
    request->setRequestData(requestContent, strlen(requestContent));
    network::HttpClient::getInstance()->send(request);
    request->release();
    
}

void CTB_Platform::onHttpRequestCompleted(network::HttpClient *sender, network::HttpResponse *response)
{
    if (!response)
    {
        return;
    }
    
    if (!response->isSucceed())
    {
        return;
    }
    
    std::vector<char> *buffer = response->getResponseData();
    if(buffer && !(*buffer).empty())
    {
        std::string responseString(buffer->begin(), buffer->end());
        cocos2d::log("=====CAndroidPlatform::onHttpRequestCompleted=====%s======", responseString.c_str());
        
        rapidjson::Document d;
        d.Parse<0>(responseString.c_str());
        if (d.HasParseError())
        {
            return;
        }
        
        if (d.HasMember("token"))
        {
            const rapidjson::Value &data = d["token"];
            if (data.IsString())
            {
                std::string str = data.GetString();
                if (!str.empty() && "null" != str)
                {
                    GlobalState::getInstance()->setCPKey(data.GetString());
                    m_strKey = data.GetString();
                }
                
            }
        }
        
        if (d.HasMember("err"))
        {
            const rapidjson::Value &data = d["err"];
            if (data.IsString())
            {
                std::string str = data.GetString();
                if (str.find("roleCTime:")!=-1)
                {
                    tagGlobalUserData   * pUser = CGlobalUserInfo::GetInstance()->GetGlobalUserData();
                    pUser->lRoleCTime = atol(str.substr(10).c_str());
                }
            }
        }
        
        if (d.HasMember("accountid"))
        {
            const rapidjson::Value &data = d["accountid"];
            if (data.IsString())
            {
                std::string str = data.GetString();
                if (!str.empty() && "null" != str)
                {
                    GlobalState::getInstance()->setCPID(str);
                }
            }
        }
        if (d.HasMember("ret"))
        {
            const rapidjson::Value &ret = d["ret"];
            if (ret.IsInt())
            {
                
                if (0 == ret.GetInt())
                {
                    G_NOTIFY(ON_MSG_CP_LOGINGAME, MTData::create(0));
                    return;
                }
            }
        }
    }
    else
    {
        onLoginGame(NULL, NULL, NULL);
    }
    
    //G_NOTIFY(ON_MSG_CP_LOGINGAME, MTData::create(1));
    return;
}

void CTB_Platform::gamePay(const std::string &pStrPayInfo)
{
    Json* mjs = Json_create(pStrPayInfo.c_str());
    if(mjs)
    {
        NSString* appUserId = nullptr;
        NSString* cpOrderId = nullptr;
        NSString* notifyUrl = nullptr;
        NSString* waresId = nullptr;
        NSString* price = nullptr;
        NSString* waresName = nullptr;
        NSString* srcPrice = nullptr;
        
        const char* pstr = Json_getString(mjs,"PurchaseType", "");
        if(pstr != NULL)
        {
            waresId = [[NSString alloc] initWithUTF8String:pstr];
        }
        
        pstr = Json_getString(mjs,"dwUserID", "");
        if(pstr != NULL)
        {
            appUserId = [[NSString alloc] initWithUTF8String:pstr];
        }
        
        pstr = Json_getString(mjs,"orderIdStr", "");
        if(pstr != NULL)
        {
            cpOrderId = [[NSString alloc] initWithUTF8String:pstr];
        }
        
        pstr = Json_getString(mjs,"price", "");
        if(pstr != NULL)
        {
            price = [[NSString alloc] initWithUTF8String:pstr];
        }
        
        pstr = Json_getString(mjs,"validateAddr", "");
        if(pstr != NULL)
        {
            notifyUrl = [[NSString alloc] initWithUTF8String:pstr];
        }
        
        pstr = Json_getString(mjs,"productName", "");
        if(pstr != NULL)
        {
            waresName = [[NSString alloc] initWithUTF8String:pstr];
        }
        
        pstr = Json_getString(mjs,"srcPrice", "");
        if(pstr != NULL)
        {
            srcPrice = [[NSString alloc] initWithUTF8String:pstr];
        }
        
        
        
        [[TBPlatform defaultPlatform] TBUniPayForCoin:cpOrderId needPayRMB:srcPrice.intValue payDescription:@"" delegate:[TBDelegate SharedInstance]];


        
        Release_OC(appUserId);
        Release_OC(cpOrderId);
        Release_OC(notifyUrl);
        Release_OC(waresId);
        Release_OC(price);
        Release_OC(waresName);
        Release_OC(srcPrice);
        
    }

}

void CTB_Platform::onGamePay(bool bSuc)
{
    
}

bool CTB_Platform::isAllHouseOpen()
{
    return true;
}
bool CTB_Platform::isHaveThirdParyExit()
{
    return true;
}

bool CTB_Platform::isHaveThirdPartyLogin()
{
    return true;
}
