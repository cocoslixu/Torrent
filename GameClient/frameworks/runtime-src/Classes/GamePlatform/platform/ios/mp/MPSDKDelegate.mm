//
//  XYSDKDelegate.cpp
//  xxz
//
//  Created by  on 15/6/23.
//
//

#import "MPSDKDelegate.h"
#import "MPPayManager.h"

@interface MPDelegate()

@end

@implementation MPDelegate
@synthesize m_pView = _m_pview;

//全局委托对象
+(id)SharedInstance
{
    static MPDelegate* pInstance = nil;
    if(pInstance == nil)
    {
        pInstance = [MPDelegate alloc];
        [pInstance init];
    }
    return pInstance;
}

//初始化
-(void) initDelegate:(UIViewController*)pView
{
    m_pView = pView;
    
    //环境设置
    //    [MPPayManager defaultManager].isDebug = YES;
    //SDK初始化
    [[MPPayManager defaultManager] mpInitializeWithCompletion:^(NSNumber *error) {
        if (error.integerValue == 2000 || error.integerValue == 2001) {
            //如果对接正版,则当此处返回2001时,调起魔品登录和支付;否则调起苹果登录和支付
            //如果对接企业签版,则无论此处返回2000或2001时,都调起魔品登录和支付
        }
        NSString *message = [NSString stringWithFormat:@"ErrCode: %ld", (long)error.integerValue];
        UIAlertView *alertView=[[UIAlertView alloc] initWithTitle:@"初始化结果" message:message delegate:self cancelButtonTitle:@"好的" otherButtonTitles:nil];
        [alertView show];
    }];
    
    //处理支付结果
    [[MPPayManager defaultManager] mpManageWithTreatedOrder:^(TradeInfo *tradeInfo) {
        NSString *payResult = [NSString stringWithFormat:@"订单号: %@, ErrCode: %ld", tradeInfo.trade_no_out, (long)tradeInfo.errorCode];
        UIAlertView *alertView=[[UIAlertView alloc] initWithTitle:@"支付结果" message:payResult delegate:self cancelButtonTitle:@"好的" otherButtonTitles:nil];
        [alertView show];
    } untreatedOrder:^(TradeInfo *tradeInfo) {
        NSString *payResult = [NSString stringWithFormat:@"订单号: %@, ErrCode: %ld", tradeInfo.trade_no_out, (long)tradeInfo.errorCode];
        UIAlertView *alertView=[[UIAlertView alloc] initWithTitle:@"轮询结果" message:payResult delegate:self cancelButtonTitle:@"好的" otherButtonTitles:nil];
        [alertView show];
    }];

}

//监听sdk初始化
-(void)SNSInitResult:(NSNotification*)notification
{

//     //设置调试模式
// //    [[NdComPlatform defaultPlatform] NdSetDebugMode:0];
//     //显示工具栏
//     [[NdComPlatform defaultPlatform] NdShowToolBar:NdToolBarAtTopLeft];

    
}

//监听登陆结果
-(void)SNSLoginResult:(NSNotification*)notification
{
    // NSDictionary *dict = [notification userInfo];
    // BOOL success = [[dict objectForKey:@"result"] boolValue];
    // NdGuestAccountStatus* guestStatus = (NdGuestAccountStatus*)[dict objectForKey:@"NdGuestAccountStatus"];
    
    // //登录成功后处理
    // if([[NdComPlatform defaultPlatform] isLogined] && success) {
        
    //     //也可以通过[[NdComPlatform defaultPlatform] getCurrentLoginState]判断是否游客登录状态
    //     if (guestStatus) {
    //         NSString* strUin = [[NdComPlatform defaultPlatform] loginUin];
    //         NSString* strTip = nil;
    //         if ([guestStatus isGuestLogined]) {
    //             strTip = [NSString stringWithFormat:@"游客账号登录成功,\n uin = %@", strUin];
    //         }
    //         else if ([guestStatus isGuestRegistered]) {
    //             strTip = [NSString stringWithFormat:@"游客成功注册为普通账号,\n uin = %@", strUin];
    //         }
            
    //         if ([strTip length] > 0) {
    //             //[DemoComFunc messageBox: strTip];
    //         }
    //     }
    //     else {//普通登陆成功
            

    //     }
    //     //登录成功
    //     NSString* token = [[NdComPlatform defaultPlatform] sessionId];       // token
    //     const char* pstrToken =[token UTF8String];
        
    //     NSString* uid = [[NdComPlatform defaultPlatform] loginUin];   // uid
    //     const char* pstrUid =[uid UTF8String];
        
    //     dispatch_async(dispatch_get_main_queue(), ^{
    //         cocos2d::CGamePlatform::SharedInstance()->OnEventLoginPlatform(pstrToken,pstrUid);
    //     });
    // }
    // //登录失败处理和相应提示
    // else
    // {
    //     int error = [[dict objectForKey:@"error"] intValue];
    //     NSString* strTip = [NSString stringWithFormat:@"登录失败, error=%d", error];
    //     switch (error) {
    //         case ND_COM_PLATFORM_ERROR_USER_CANCEL://用户取消登录
    //             if (([[NdComPlatform defaultPlatform] getCurrentLoginState] == ND_LOGIN_STATE_GUEST_LOGIN)) {
    //                 strTip =  @"当前仍处于游客登录状态";
    //             }
    //             else {
    //                 strTip = @"用户未登录";
    //             }
    //             break;
                
    //             // {{ for demo tip
    //         case ND_COM_PLATFORM_ERROR_APP_KEY_INVALID://appId未授权接入, 或appKey 无效
    //             strTip = @"登录失败, 请检查appId/appKey";
    //             break;
    //         case ND_COM_PLATFORM_ERROR_CLIENT_APP_ID_INVALID://无效的应用ID
    //             strTip = @"登录失败, 无效的应用ID";
    //             break;
    //         case ND_COM_PLATFORM_ERROR_HAS_ASSOCIATE_91:
    //             strTip = @"有关联的91账号，不能以游客方式登录";	
    //             break;
                
    //             // }}
    //         default:
    //             break;
    //     }
    //     //[DemoComFunc messageBox:strTip];
    //      cocos2d::CGamePlatform::SharedInstance()->OnEventLoginOut();
    // }
}

//购买监听
-(void)SNSBuyResult:(NSNotification*)notification
{
    // NSDictionary* dic = [notification userInfo];
    // BOOL bSuccess = [[dic objectForKey:@"result"] boolValue];
    // NSString* str = bSuccess ? @"购买成功" : @"购买失败";
    // if (!bSuccess) {
    //     //TODO: 购买失败处理
    //     NSString* strError = nil;
    //     int nErrorCode = [[dic objectForKey:@"error"] intValue]; switch (nErrorCode) {
    //         case ND_COM_PLATFORM_ERROR_USER_CANCEL: strError = @"用户取消操作";
    //             break;
    //         case ND_COM_PLATFORM_ERROR_NETWORK_FAIL: strError = @"网络连接错误";
    //             break;
    //         case ND_COM_PLATFORM_ERROR_SERVER_RETURN_ERROR: strError = @"服务端处理失败";
    //             break;
    //         case ND_COM_PLATFORM_ERROR_ORDER_SERIAL_SUBMITTED: //!!!: 异步支付,用户进入充值界面了
    //             strError = @"支付订单已提交";
    //             break;
    //         default:
    //             strError = @"购买过程发生错误"; break;
    //     }
    //     str = [str stringByAppendingFormat:@"\n%@", strError];
    //     NSLog(str);
    // }
    // else {    
    //     //TODO: 购买成功处理
    //     NSLog(@"NdUiPayAsynResult: %@", @"购买成功处理");
        
    // }
    // //本次购买的请求参数
    // NdBuyInfo* buyInfo = (NdBuyInfo*)[dic objectForKey:@"buyInfo"];
    // str = [str stringByAppendingFormat:@"\n<productId = %@, productCount = %d, cooOrderSerial = %@>",
    //        buyInfo.productId, buyInfo.productCount, buyInfo.cooOrderSerial];
    // NSLog(@"NdUiPayAsynResult: %@", str);
}

@end

