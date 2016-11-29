//
//  XY_Pratform.cpp
//  xxz
//
//  Created by on 15/6/23.
//
//

#include <stdio.h>

#include "XY_Platform.h"
//#import <XYPlatform/XYPlatform.h>
#include "XYPlatform.h"
#import "XYSDKDelegate.h"
#import <UIKit/UIKit.h>
#include "plazz/data/GlobalState.h"
#include "GamePlatform/GamePlatformSystem.h"
#include "consts/GameConsts.h"
#include "plazz/data/GlobalUserInfo.h"
#include "event/NotifyKey.h"
#include "event/MTNotification.h"
#include "plazz/device/Device.h"

USING_NS_CC;

#define  APP_ID  @"100031080"
#define  APP_KEY  @"G9GcRhxI4u1EaxGCmPlYeyk0UteYEamq"

#define Release_OC(obc) {if(obc)[obc release]; obc = nullptr;}
CXY_Platform::CXY_Platform()
{
          
}

CXY_Platform::~CXY_Platform()
{
}

void CXY_Platform::initSDK(void* pView)
{
    [XYPlatform defaultPlatform];

    [[XYDelegate SharedInstance] initDelegate];
    
    [[XYPlatform defaultPlatform] initializeWithAppId:APP_ID appKey:APP_KEY isContinueWhenCheckUpdateFailed:YES];
    
    [XYPlatform defaultPlatform].appScheme = @"com.leqi.buyu.alipay";
    
    // 默认为正式环境, 即为NO, 测试环境为 YES
    [[XYPlatform defaultPlatform] XYSetDebugModel:NO];
    
    //打印log到控制台， 设置方便查看日志，可不设置
//    [[XYPlatform defaultPlatform] XYSetShowSDKLog:YES];
    
}


/**
 *  @brief 充值, 该接口首先获取支付渠道，然后支付并进入web支付页面
 *
 *  @param amount 充值金额
 *
 *  @param serverId 伺服器id
 *
 *  @param extra  开发商透传数据
 *
 *  @param delegate XYPayDelegate 代理回调
 *
 *  @result 错误码, 默认为0
 */
void CXY_Platform::platformOpenURL(void* pURL, void* sourceApplication, void* application, void* nid)
{
    [[XYPlatform defaultPlatform] XYHandleOpenURL:(NSURL *)pURL];
}

void CXY_Platform::logoutGame()
{
    if(JZ::Device::isHaveThirdPartyLogin())
    {
         [[XYPlatform defaultPlatform] XYLogout:1];
    }
    else
    {
        onLogoutGame();
    }
    
}

void CXY_Platform::gamePay(const std::string &pStrPayInfo)
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
        
        
        
        [[XYPlatform defaultPlatform] XYPayWithAmount:srcPrice appServerId:@"0" appExtra:cpOrderId delegate:[XYDelegate SharedInstance]];
        
        Release_OC(appUserId);
        Release_OC(cpOrderId);
        Release_OC(notifyUrl);
        Release_OC(waresId);
        Release_OC(price);
        Release_OC(waresName);
        Release_OC(srcPrice);
        
    }

}

void CXY_Platform::loginGame()
{
    //此处进行登录操作
     [[XYPlatform defaultPlatform] XYAutoLogin:0];
}

void CXY_Platform::onChangeAccount()
{
    
}

bool CXY_Platform::isAllHouseOpen()
{
    return true;
}

void CXY_Platform::onLoginGame(const char* pToken,const char* pstrUID,const char* userName)
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
    request->setResponseCallback(CC_CALLBACK_2(CXY_Platform::onHttpRequestCompleted, this));
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

void CXY_Platform::onHttpRequestCompleted(network::HttpClient *sender, network::HttpResponse *response)
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


void CXY_Platform::onGamePay(bool bSuc)
{

}

bool CXY_Platform::isHaveThirdPartyLogin()
{
    return true;
}

bool CXY_Platform::isHaveThirdParyExit()
{
    return true;
}


