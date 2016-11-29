//
//  TBSDKDelegate.cpp
//  xxz
//
//  Created by daily-play on 15/8/26.
//
//

/***
 @property (retain, nonatomic) NSArray *questionTitleTrip;
 在头文件中这样写，默认会生成一个_questionTitleTrip变量。
 所以，在实现文件中最好这样写。
 @synthesize questionTitleTrip ＝ _questionTitleTrip
***/

#import "TBSDKDelegate.h"
#import <TBPlatform/TBPlatform.h>
//#include "GamePlatform.h"
#include "GamePlatform/PlatformBase.h"
#include "PlatformBase.h"
#include "TB_Platform.h"

@interface TBDelegate ()<TBBuyGoodsProtocol,TBCheckOrderDelegate>

@end



@implementation TBDelegate
@synthesize m_pView = _m_pView;

+(id)SharedInstance
{
    static TBDelegate* pInstance = nil;
    if(pInstance == nil)
    {
            pInstance = [TBDelegate alloc];
            [pInstance init];
    }
    return pInstance;
}


//初始化
-(void)InitDeleage:(UIViewController*)pView
{
    m_pView = pView;
    
    //添加SDK各类通知的观察者
    /*初始化结束通知，登录等操作务必在收到该通知后调用！！*/
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(sdkInitFinished)
                                                 name:kTBInitDidFinishNotification
                                               object:Nil];
    /*登录成功通知*/
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(loginFinished)
                                                 name:kTBLoginNotification
                                               object:nil];
    /*注销通知（个人中心页面的注销也会触发该通知，注意处理*/
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(didLogout)
                                                 name:kTBUserLogoutNotification
                                               object:nil];
    /*离开平台通知（包括登录页面、个人中心页面、web充值页等*/
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(leavedSDKPlatform:)
                                                 name:kTBLeavePlatformNotification
                                               object:nil];
    
}


#pragma mark - 消息回调处理
/**
 *  SDK初始化结束通知（登录等操作务必放到初始化完成的通知里！！！！）
 */
- (void)sdkInitFinished{
    [[TBPlatform defaultPlatform] TBLogin:0];
}

/**
 *  离开平台
 *
 *  @param notification
 */
- (void)leavedSDKPlatform:(NSNotification *)notification{
    NSDictionary *notifyUserInfo = notification.userInfo;
    TBPlatformLeavedType leavedFromType = (TBPlatformLeavedType)[[notifyUserInfo objectForKey:
                                                                  TBLeavedPlatformTypeKey] intValue];
    switch (leavedFromType) {
            //从登录页离开
        case TBPlatformLeavedFromLogin:{
        }
            break;
            //从个人中心离开
        case TBPlatformLeavedFromUserCenter:{
        }
            break;
            //从充值页面离开
        case TBPlatformLeavedFromUserPay:{
            NSString *orderString = [notifyUserInfo objectForKey:TBLeavedPlatformOrderKey];
            [[TBPlatform defaultPlatform] TBCheckPaySuccess:orderString
                                                   delegate:self];
        }
            break;
        default:
            break;
    }
}

/**
 *  登录结束通知
 */
- (void)loginFinished{
    if ([[TBPlatform defaultPlatform] TBIsLogined]) {
        TBPlatformUserInfo *userInfo = [[TBPlatform defaultPlatform] TBGetMyInfo];
        
        //显示浮动工具条
        [[TBPlatform defaultPlatform] TBShowToolBar:TBToolBarAtMiddleLeft
                                      isUseOldPlace:YES];
        
        NSString* session = userInfo.sessionID;
        
        NSString* userid = [userInfo userID];
        
        NSString* userName = [userInfo nickName];
        
        //使用参数按照服务器验证文档，去服务器验证账号是否为iiapple账号
        const char* pstrToken =[session UTF8String];
        const char* pstrUid =[userid UTF8String];
        const char* pstrUname = [userName UTF8String];
//        cocos2d::CGamePlatform::SharedInstance()->OnEventLoginPlatform(pstrToken,pstrUid);
        CTB_Platform *login = new CTB_Platform();
        login->onLoginGame(pstrToken, pstrUid, pstrUname);
        
        //验证Session（建议放在服务器做）
//        if ([[UIDevice currentDevice] systemVersion].floatValue > 5.0) {
//            NSString *urlString = [NSString stringWithFormat:@"http://tgi.tongbu.com/checkv2.aspx?k=%@",
//                                   [TBPlatform defaultPlatform].sessionID];
//            __unsafe_unretained TBViewController *weakSelf = self;
            /* 这个方法只有iOS5以上才有=。= */
//            [NSURLConnection sendAsynchronousRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:urlString]]
//                                               queue:[[NSOperationQueue alloc]init] completionHandler:^(NSURLResponse *response, NSData *data, NSError *connectionError) {
//                                                   NSString *resultString = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
//                                                   [weakSelf performSelectorOnMainThread:@selector(quickShowMessage:)
//                                                                              withObject:[NSString stringWithFormat:@"Session验证结果：%@",resultString]
//                                                                           waitUntilDone:YES];
//                                               }];
//        }
    }
}

/**
 *  注销通知
 */
- (void)didLogout{
    //    [self quickShowMessage:@"已注销"];
    [[TBPlatform defaultPlatform] TBHideToolBar];
//    cocos2d::CGamePlatform::SharedInstance()->OnEventLoginOut();
    CTB_Platform *logout = new CTB_Platform();
    logout->onLogoutGame();
}

#pragma mark - 购买物品回调

/**
 *	@brief	使用推币直接购买商品成功
 *
 *	@param 	order 	订单号
 */
- (void)TBBuyGoodsDidSuccessWithOrder:(NSString*)order{
    
}

/**
 *	@brief	使用推币直接购买商品失败
 *
 *	@param 	order 	订单号
 *	@param 	errorType  错误类型，见TB_BUYGOODS_ERROR
 */
- (void)TBBuyGoodsDidFailedWithOrder:(NSString *)order
                          resultCode:(TB_BUYGOODS_ERROR)errorType{

}

/**
 *	@brief	推币余额不足，进入充值页面（开发者需要手动查询订单以获取充值购买结果）
 *
 *	@param 	order 	订单号
 */
- (void)TBBuyGoodsDidStartRechargeWithOrder:(NSString*)order{
    
}

/**
 *	@brief  跳提示框时，用户取消
 *
 *	@param	order	订单号
 */
- (void)TBBuyGoodsDidCancelByUser:(NSString *)order{
    
}

#pragma mark - 查询订单回调

/**
 *  查询订单结束
 *
 *  @param orderString 订单号
 *  @param amount      订单金额（单位：分）
 *  @param statusType  订单状态（详见TBPlatformDefines.h）
 */
- (void)TBCheckOrderFinishedWithOrder:(NSString *)orderString
                               amount:(int)amount
                               status:(TBCheckOrderStatusType)statusType{
}
/**
 *  @brief 查询订单失败（网络不通畅，或服务器返回错误）
 */
- (void)TBCheckOrderDidFailed:(NSString*)order{
}


@end
