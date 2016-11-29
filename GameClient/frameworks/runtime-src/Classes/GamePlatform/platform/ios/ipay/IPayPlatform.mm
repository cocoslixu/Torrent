//
//  IPayPaltform.cpp
//  buyu
//
//  Created by ff on 2016/10/21.
//
//

#include "IPayPlatform.h"
#include "plazz/device/Device.h"
#include "utils/GameUtils.h"

#import <IapppayKit/IapppayKit.h>
#import <IapppayKit/IapppayOrderUtils.h>
#import "IapppayConstantKey.h"
#import "IPayListener.h"
#include "consts/GameConsts.h"

#ifdef ios_ipaydwcby
#define NSScheme @"iapppay.alipay.com.ipay.buyu.dwcby"
#elif ios_buyuji
#define NSScheme @"iapppay.alipay.com.ipay.buyuji"
#elif ipay_dwby
#define NSScheme @"iapppay.alipay.com.ipay.buyu.dwby"
#elif ios_ipaybydmx
#define NSScheme @"iapppay.alipay.com.dwc.by"
#elif defined(ipay_bydfw)
#define NSScheme @"iapppay.alipay.com.dwc.by"
#endif

#define Release_OC(obc) {if(obc)[obc release]; obc = nullptr;}

@interface IPayListener() <IapppayKitPayRetDelegate>

@end

@implementation IPayListener

/**
 * 此处方法是支付结果处理
 **/
#pragma mark - IapppayKitPayRetDelegate
- (void)iapppayKitRetPayStatusCode:(IapppayKitPayRetCode)statusCode
resultInfo:(NSDictionary *)resultInfo
{
    NSLog(@"statusCode : %d, resultInfo : %@", (int)statusCode, resultInfo);
    
    if (statusCode == IapppayPayRetSuccessCode)
    {
        BOOL isSuccess = [IapppayOrderUtils checkPayResult:resultInfo[@"Signature"]
                                                withAppKey:mOrderUtilsCheckResultKey];
        if (isSuccess) {
            //支付成功，验签成功
//            [MBProgressHUD showTextHUDAddedTo:self.view Msg:@"支付成功，验签成功" animated:YES];
        } else {
            //支付成功，验签失败
//            [MBProgressHUD showTextHUDAddedTo:self.view Msg:@"支付成功，验签失败" animated:YES];
        }
    }
    else if (statusCode == IapppayPayRetFailedCode)
    {
        //支付失败
        NSString *message = @"支付失败";
        if (resultInfo != nil) {
            message = [NSString stringWithFormat:@"%@:code:%@\n（%@）",message,resultInfo[@"RetCode"],resultInfo[@"ErrorMsg"]];
        }
        
//        [MBProgressHUD showTextHUDAddedTo:self.view Msg:message animated:YES];
    }
    else
    {
        //支付取消
        NSString *message = @"支付取消";
        if (resultInfo != nil) {
            message = [NSString stringWithFormat:@"%@:code:%@\n（%@）",message,resultInfo[@"RetCode"],resultInfo[@"ErrorMsg"]];
        }
//        [MBProgressHUD showTextHUDAddedTo:self.view Msg:message animated:YES];
    }
}
@end

CIPayPlatform::CIPayPlatform()
{
    
}

CIPayPlatform::~CIPayPlatform()
{
    
}


void CIPayPlatform::initSDK(void* pView)
{
    //设置支付宝回调地址
    [[IapppayKit sharedInstance] setAppAlipayScheme:NSScheme];
    
    //初始化SDK信息
    [[IapppayKit sharedInstance] setAppId:mOrderUtilsAppId mACID:mOrderUtilsChannel];
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
void CIPayPlatform::gamePay(const std::string& pstrPayInfo)
{
    static IPayListener* s_instance = [IPayListener alloc];
    
    if(s_instance)
    {
        Json* mjs = Json_create(pstrPayInfo.c_str());
        if(mjs)
        {
            
            NSString* openIdToken = @"";
            
            NSString* appUserId = nullptr;
            NSString* cpOrderId = nullptr;
            NSString* notifyUrl = nullptr;
            NSString* waresId = nullptr;
            NSString* price = nullptr;
            NSString* waresName = nullptr;
            std::string strNotifyurl;
            
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
                strNotifyurl = pstr;
            }
            
            pstr = Json_getString(mjs,"productName", "");
            if(pstr != NULL)
            {
                waresName = [[NSString alloc] initWithUTF8String:pstr];
            }
            
            IapppayOrderUtils *orderInfo = [[IapppayOrderUtils alloc] init];
            orderInfo.appId         = mOrderUtilsAppId;
            orderInfo.cpPrivateKey  = mOrderUtilsCpPrivateKey;
//            if(strNotifyurl.empty() == false)
//            {
//                orderInfo.notifyUrl = notifyUrl;
//            }

            orderInfo.cpOrderId     = cpOrderId;
            orderInfo.waresId       = waresId;
            orderInfo.price         = price;
            orderInfo.appUserId     = appUserId;
            orderInfo.waresName     = waresName;
            orderInfo.cpPrivateInfo = @"商户按次商品的私有信息";
            
            NSString *trandInfo = [orderInfo getTrandData];
            
            [[IapppayKit sharedInstance] makePayForTrandInfo:trandInfo
                                            openIDTokenValue:openIdToken
                                           payResultDelegate:s_instance];
            
            Release_OC(orderInfo);
            Release_OC(appUserId);
            Release_OC(cpOrderId);
            Release_OC(notifyUrl);
            Release_OC(waresId);
            Release_OC(price);
            Release_OC(waresName);
        }
    }
}

void CIPayPlatform::platformOpenURL(void* pURL, void* sourceApplication, void* application, void* nid)
{
    if(pURL)
    {
        NSURL* url = static_cast<NSURL*>(pURL);
        if(url)
        {
          [[IapppayKit sharedInstance] handleOpenUrl:url];
        }
    }
    
}

int CIPayPlatform::getPayChannelID()
{
    return CURRENT_CP;
}
