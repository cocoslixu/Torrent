//
//  MPPlatform.cpp
//  buyu
//
//  Created by ff on 2016/11/10.
//
//
#import <UIKit/UIKit.h>
#include "MPPlatform.h"
#include "plazz/device/Device.h"
#include "utils/GameUtils.h"
#include "consts/GameConsts.h"
//#include "MPSDKDelegate.h"
#include "plazz/data/GlobalState.h"
#include "GamePlatform/GamePlatformSystem.h"
#include "consts/GameConsts.h"
#include "plazz/data/GlobalUserInfo.h"
#include "event/NotifyKey.h"
#include "event/MTNotification.h"
#include "plazz/device/Device.h"

static UIViewController* m_pView = nullptr;

#define Release_OC(obc) {if(obc)[obc release]; obc = nullptr;}

#import "MPPayManager.h"


CMPPlatform::CMPPlatform()
{
    
}

CMPPlatform::~CMPPlatform()
{
    
}

void CMPPlatform::initSDK(void* pView)
{
    m_pView = (UIViewController*)pView;
    
    if(m_pView == nullptr)return;
    
    //环境设置
    //    [MPPayManager defaultManager].isDebug = YES;
    //SDK初始化
    [[MPPayManager defaultManager] mpInitializeWithCompletion:^(NSNumber *error) {
//        if (error.integerValue == 2000 || error.integerValue == 2001) {
//            //如果对接正版,则当此处返回2001时,调起魔品登录和支付;否则调起苹果登录和支付
//            //如果对接企业签版,则无论此处返回2000或2001时,都调起魔品登录和支付
//        }
//        NSString *message = [NSString stringWithFormat:@"ErrCode: %ld", (long)error.integerValue];
//        UIAlertView *alertView=[[UIAlertView alloc] initWithTitle:@"初始化结果" message:message delegate:m_pView cancelButtonTitle:@"好的" otherButtonTitles:nil];
//        [alertView show];
    }];
    
    //处理支付结果
    [[MPPayManager defaultManager] mpManageWithTreatedOrder:^(TradeInfo *tradeInfo) {
        NSString *payResult = [NSString stringWithFormat:@"订单号: %@, ErrCode: %ld", tradeInfo.trade_no_out, (long)tradeInfo.errorCode];
        UIAlertView *alertView=[[UIAlertView alloc] initWithTitle:@"支付结果" message:payResult delegate:m_pView cancelButtonTitle:@"好的" otherButtonTitles:nil];
        [alertView show];
    } untreatedOrder:^(TradeInfo *tradeInfo) {
        NSString *payResult = [NSString stringWithFormat:@"订单号: %@, ErrCode: %ld", tradeInfo.trade_no_out, (long)tradeInfo.errorCode];
        UIAlertView *alertView=[[UIAlertView alloc] initWithTitle:@"轮询结果" message:payResult delegate:m_pView cancelButtonTitle:@"好的" otherButtonTitles:nil];
        [alertView show];
    }];
}

void CMPPlatform::loginGame()
{
    if(m_pView == nullptr)return;
    
    [[MPPayManager defaultManager] mpPresentLoginMenu:m_pView cancelButton:YES game_type:MP_CasualGame completion:^(NSDictionary *responseObject) {
//        NSString *loginResult = [NSString stringWithFormat:@"userid: %@, token: %@", responseObject[@"userid"], responseObject[@"token"]];
//        UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"登录成功" message:loginResult delegate:m_pView cancelButtonTitle:@"好的" otherButtonTitles:nil];
//        [alertView show];
        
        const char* pstrToken =[responseObject[@"token"] UTF8String];
        const char* pstrUid =[responseObject[@"userid"] UTF8String];
        const char* pstrUname = " ";
        
        onLoginGame(pstrToken, pstrUid, pstrUname);
    }];
}

void CMPPlatform::logoutGame()
{
    
}

void CMPPlatform::gamePay(const std::string& pStrPayInfo)
{
    if(m_pView == nullptr)return;
    
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
        
        
        
         [[MPPayManager defaultManager] mpRequestPayMode:m_pView product_name:waresName product_price:srcPrice cp_trade_no:cpOrderId cp_notify_url:notifyUrl cp_attach:@" " player_server:@" " player_role:appUserId];
        
//        [[MPPayManager defaultManager] mpRequestPayMode:m_pView product_name:waresName product_price:@"1" cp_trade_no:cpOrderId cp_notify_url:notifyUrl cp_attach:@" " player_server:@" " player_role:appUserId];
        
           
        Release_OC(appUserId);
        Release_OC(cpOrderId);
        Release_OC(notifyUrl);
        Release_OC(waresId);
        Release_OC(price);
        Release_OC(waresName);
        Release_OC(srcPrice);
        
    }
}

void CMPPlatform::platformOpenURL(void* pURL, void* sourceApplication, void* application, void* nid)
{
    NSURL* url = static_cast<NSURL*>(pURL);
    if(url)
    {
        [[MPPayManager defaultManager] handlePayResult:url];
    }
}

int CMPPlatform::getPayChannelID()
{
    return CURRENT_CP;
}

bool CMPPlatform::isHaveThirdPartyLogin()
{
    return  true;
}

void CMPPlatform::onLoginGame(const char* pToken, const char* pstrUID, const char* userName)
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
    request->setResponseCallback(CC_CALLBACK_2(CMPPlatform::onHttpRequestCompleted, this));
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

void CMPPlatform::onLogoutGame()
{
    
}

void CMPPlatform::onHttpRequestCompleted(network::HttpClient *sender, network::HttpResponse *response)
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
