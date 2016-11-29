//
//  XYSDKDelegate.cpp
//  xxz
//
//  Created by  on 15/6/23.
//
//

#import "XYSDKDelegate.h"
//#import <XYPlatform/XYPlatform.h>
#include "XYPlatform.h"
#include "XY_Platform.h"

@interface XYDelegate ()<XYCheckPayOrderDelegate, XYPayDelegate>{
//    NSArray *_actionItems;
//    
//    UIButton *btnLogin;
//    
//    
//    CGFloat _amountNumber;
//    
//    NSString *_amountPathStr;
//    
//    UITableView *tableView;
}
@end


@implementation XYDelegate

+(id)SharedInstance
{
    static XYDelegate* pInstance = NULL;
    if(pInstance == NULL)
    {
        pInstance = [XYDelegate alloc];
        [pInstance init];
    }
    return pInstance;
}

-(void) initDelegate
{
    //添加XYPlatform 各类通知的观察者
    
    /*初始化结束通知, 登录等操作务必在收到该通知后调用*/
    
    /*登录通知*/
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(xyplatformLoginNoti:)
                                                 name:kXYPlatformLoginNotification
                                               object:nil];
    
    /* 注销登录通知 */
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(xyplatformLogoutFinished:)
                                                 name:kXYPlatformLogoutNotification
                                               object:nil];
    
    /*离开平台通知*/
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(xyplatformLeavedPlatform:)
                                                 name:kXYPlatformLeavedNotification
                                               object:nil];
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(xyplatformGuestTurnOffical:)
                                                 name:kXYPlatformGuestTurnOfficialNotification
                                               object:nil];
    
//    [[NSNotificationCenter defaultCenter] addObserver:self
//                                             selector:@selector(xyplatformSetLogin:)
//                                                 name:kXYPlatformSetLoginNotification
//                                               object:nil];
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(xyplatformInitDidFinished:)
                                                 name:kXYPlatformInitDidFinishedNotification
                                               object:nil];
    
}



-(void)xyplatformInitDidFinished:(NSNotification*)notification
{
    //初始化完成
    [[XYPlatform defaultPlatform] XYUserLogin:0];
}


-(void)xyplatformLoginNoti:(NSNotification*)notification
{
    // 登录完成, 提供token 以及 openuid 给游戏校验
    NSDictionary *userInfo = notification.userInfo;
    if ([userInfo[kXYPlatformErrorKey] intValue] == XY_PLATFORM_NO_ERROR) {
        //登录成功
        //        [self doSomeThingAfterLoginOrRegister:0];
        NSString* token = [[XYPlatform defaultPlatform] XYToken];       // token
        const char* pstrToken =[token UTF8String];
        
        NSString* uid = [[XYPlatform defaultPlatform] XYOpenUID];   // uid
        const char* pstrUid =[uid UTF8String];
        
        NSString* uName = [[XYPlatform defaultPlatform] XYLoginUserAccount];
        const char* pstrUname = [uName UTF8String];
        
        //登陆成功回调
        //        cocos2d::CGamePlatform::SharedInstance()->OnEventLoginPlatform(pstrToken,pstrUid);
        CXY_Platform* login= new CXY_Platform();
        login->onLoginGame(pstrToken, pstrUid, pstrUname);
        
        [self refreshLoginMark];
    }

}


- (void) refreshLoginMark
{
    NSString *acc = [[XYPlatform defaultPlatform] XYLoginUserAccount];
    
    if ([acc length] > 0) {
//        [btnLogin setTitle:[NSString stringWithFormat:@"登录：%@", acc] forState:UIControlStateNormal];
    }else{
//        [btnLogin setTitle:@"登录" forState:UIControlStateNormal];
    }
}

#pragma mark-- 注销

- (void)xyplatformLogoutFinished:(NSNotification*)notification
{
//    cocos2d::CGamePlatform::SharedInstance()->OnEventLoginOut();
    
    CXY_Platform* logout= new CXY_Platform();
    logout->onLogoutGame();
}

/*离开平台通知*/
- (void)xyplatformLeavedPlatform:(NSNotification*)notification
{
    NSNumber* leavedType = (NSNumber*)notification.object;
    
    switch ([leavedType integerValue]) {
        case XYPlatformLeavedDefault: {
//            TTDEBUGLOG(@"用户离开平台－－> XYPlatformLeavedDefault");
            break;
        }
        case XYPlatformLeavedFromLogin: {
//            TTDEBUGLOG(@"用户离开平台－－> XYPlatformLeavedFromLogin");
            
            [[XYPlatform defaultPlatform] XYIsLogined:^(BOOL isLogined) {
                if (!isLogined) {
                    [[XYPlatform defaultPlatform] XYUserLogin:0];
                }
            }];
            
            break;
        }
        case XYPlatformLeavedFromRegister: {
//            TTDEBUGLOG(@"用户离开平台－－> XYPlatformLeavedFromRegister");
            
            [[XYPlatform defaultPlatform] XYIsLogined:^(BOOL isLogined) {
                if (!isLogined) {
                    [[XYPlatform defaultPlatform] XYUserLogin:0];
                }
            }];
            
            break;
        }
        case XYPlatformLeavedFromPayment: {
//            TTDEBUGLOG(@"用户离开平台－－> XYPlatformLeavedFromPayment");
            break;
        }
        case XYPlatformLeavedFromSNSCenter:{
//            TTDEBUGLOG(@"用户离开平台－－> XYPlatformLeavedFromUserCenter");
            break;
        }
            
        default:
//            TTDEBUGLOG(@"用户离开平台－－> NULL");
            break;
    }
}

- (void) xyPlatformGuestTurnOffical:(id)sender
{
     [self refreshLoginMark];
}

/**
 * @brief 支付成功
 * 该方法在用户完成充值过程， 在充值成功界面点击“确定”时回调，该界面会显示"充值成功，返回游戏，自动发放道具...."
 */
- (void) XYPaySuccessWithOrder:(NSString *) orderId
{
    // 支付成功
    NSLog(@"支付成功  orderID＝ %@", orderId);
    [self doSomeThingAfterPayActionWithOrder:orderId];
    
}


/**
 * @brief 支付失败
 *  该方法在用户完成充值过程，充值失败界面点击“确定”时回调，该界面会显示“充值失败，请返回游戏重试或者联系客服”
 *  支付失败可能是 1、用户支付问题 2、支付成功，但回调游戏方服务器问题
 */
- (void) XYPayFailedWithOrder:(NSString *) orderId
{
    UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:orderId message:@"充值失败" delegate:nil cancelButtonTitle:@"不会吧" otherButtonTitles:nil, nil];
    [alertView show];
    
    //  支付失败
    NSLog(@"支付失败  orderID＝ %@", orderId);
    
}

/**
 * @brief 用户点击充值界面右上角叉叉取消充值
 * 注： 1、orderId若为空，则说明用户在选择“充值方式”界面上未点击“确认”进行下一步支付，用户取消点击右上角取消按钮取消支付
 *     2、orderId若不为空，则说明已生成订单号，用户在支付过程中点击界面右上角叉叉取消
 *     3、该回调不能说明充值成功或失败，若orderId不为空，用户可能会在支付宝支付完毕回调到商户的时间内点击界面右上角叉叉取消
 *        开发者应在该回调中调用sdk查询订单接口或者请求游戏服务端获取该笔订单是否成功
 */
- (void) XYPayDidCancelByUser:(NSString *)orderId
{
    // 用户取消操作，若orderId为空则是用户取消充值， 若orderId不为空则不确定支付成功或失败
    
    NSLog(@"用户取消支付 orderID＝ %@", orderId);
    if ([orderId length] == 0) {
        // here 用户取消充值
    }else{
        [self doSomeThingAfterPayActionWithOrder:orderId];
    }
}

- (void) doSomeThingAfterPayActionWithOrder:(NSString *) orderId
{
    NSLog(@"此处可查询订单情况");
    
    //  若需要查询订单情况，可访问游戏服务器查看订单支付情况 或者调用 sdk 接口 XYCheckPayOrderInfo:delegate: 查看
    
    [self performSelector:@selector(doCheckOrderAction:) withObject:orderId afterDelay:1.0];
}

#pragma mark-- 查询订单
- (void) doCheckOrderAction:(NSString *) orderId
{
    
//    NSString *orderId = [[XYPlatform defaultPlatform] XYGetLastPayOrder];
    NSLog(@"查询订单 orderID ＝ %@", orderId);
    int ret =  [[XYPlatform defaultPlatform] XYCheckPayOrderInfo:orderId delegate:self];
    if (ret == XY_PLATFORM_ERROR_PAY_ORDERID_NIL) {
        [[[UIAlertView alloc] initWithTitle:nil message:@"订单为空" delegate:nil cancelButtonTitle:@"好" otherButtonTitles:nil, nil] show];
    }
}

@end
