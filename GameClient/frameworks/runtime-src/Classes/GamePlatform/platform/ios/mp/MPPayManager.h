//
//  MPPay.h
//  MoPoMarketSDK
//
//  Created by HouYadi on 15/6/3.
//  Copyright (c) 2015年 魔品杭州魔品科技有限公司. All rights reserved.
//

#import <Foundation/Foundation.h>

@class TradeInfo;

typedef enum {
    kINITSuccess                 = 2000,    //初始化成功
    kINITDefaultChannel          = 2001,    //初始化成功,魔品渠道
    kINITCertificateError        = 5001,    //证书验证不通过,请检查包名与证书是否匹配
    kINITNoNetError              = 7,       //无网络连接
    kINITTimeoutError            = 8,       //网络连接超时

    kINITOtherError              = 0,       //其他错误
    
} MP_INIT_ERROR;


typedef enum {
    kTreatedOrderSuccess         = 1,        //支付成功
    kTreatedOrderFail            = 2,        //支付失败
    kTreatedOrderPaying          = 3,        //正在支付中
    kTreatedOrderCancel          = 4,        //取消支付
    kTreatedOrderCloseMenu       = 5,        //关闭支付界面
    kTreatedOrderNoModeList      = 6,        //服务端支付信息列表为空
    kTreatedOrderNoNet           = 7,        //无网络连接
    kTreatedOrderTimeout         = 8,        //网络连接超时
    
    kTreatedOrderWxUnsupport     = 9,        //微信当前版本不支持支付
    kTreatedOrderNoWx            = 10,       //微信未安装
    
    kTreatedOrderOtherError      = 0,        //其他错误
    
} MP_TreatedOrder_ERROR;


typedef enum {
    kUntreatedOrderSuccess       = 1,        //订单成功
    kUntreatedOrderFail          = 2,        //订单失败
    kUntreatedOrderPaying        = 3,        //正在支付中
    kUntreatedOrderCancel        = 4,        //取消订单
    kUntreatedOrderNoNet         = 7,        //无网络连接
    
    kUntreatedOrderOtherError    = 0,        //其他错误
    
} MP_UntreatedOrder_ERROR;


typedef enum {
    MP_CasualGame = 0,  //休闲游戏
    MP_OnlineGame = 1   //网游
    
} MP_GameType;


typedef void (^MPInitResultBlock)(NSNumber *error);

typedef void (^MPPayResultBlock)(TradeInfo *tradeInfo);

typedef void (^MPLoginSuccessBlock)(NSDictionary *responseObject);


@protocol MPPayManagerDelegate <NSObject>

/*
 * 支付宝支付结果回调
 */
- (void)AliPayCallback:(NSDictionary *)resultDic;

/*
 * 银联支付结果回调
 */
- (void)UPPayPluginResult:(NSString *)result;

@end


@interface MPPayManager : NSObject

@property (nonatomic, assign) BOOL isDebug;    //是否为测试环境

@property (nonatomic, copy) MPPayResultBlock treatedBlock; //处理正常订单的block
@property (nonatomic, copy) MPPayResultBlock untreatedBlock; //处理轮询订单的block

@property (nonatomic, strong) NSManagedObjectContext *managedObjectContext;
@property (nonatomic, weak) id<MPPayManagerDelegate> delegate; //处理支付返回结果的代理


/**
 *	@brief  单例对象
 *
 **/
+ (MPPayManager *)defaultManager;


#pragma - mark 初始化

/**
 *	@brief  SDK平台初始化方法
 *
 *  如果对接正版,则当此处返回2001时,调起魔品登录和支付,否则调起苹果支付
 *
 *  如果对接企业签版,则无论此处返回2000或2001时,都视为初始化成功,调起魔品登录和支付
 *
 *  @param  completion    初始化结果回调
 *
 **/
- (void)mpInitializeWithCompletion:(MPInitResultBlock)completion;


#pragma - mark 登录

/**
 * @brief 调起魔品登录界面
 *
 * @param viewController  将要调起魔品登录的视图控制器
 *
 * @param show            是否显示右上角的关闭按钮
 *
 * @param game_type       游戏类型（MP_CasualGame:休闲游戏; MP_OnlineGame:网游）
 *
 * @param completion      登录结果回调
 *
 */
- (void)mpPresentLoginMenu:(UIViewController *)viewController cancelButton:(BOOL)show
                 game_type:(MP_GameType)type completion:(MPLoginSuccessBlock)completion;


#pragma - mark 支付

/**
 *	@brief  调起魔品支付方式列表
 *
 *  @param  viewController 将要调起魔品支付的视图控制器
 *
 *	@param	product_name 商品名称
 *
 *  @param  product_price 商品价格（以元为单位）
 *
 *	@param	cp_trade_no cp自定义的订单号
 *
 *	@param	cp_notify_url cp服务端的回调地址
 *
 *  @param  cp_attach  cp附加信息
 *
 *  @param  player_server 玩家所在服务器
 *
 *  @param  player_role 玩家角色信息
 *
 **/
- (void)mpRequestPayMode:(UIViewController *)viewController product_name:(NSString *)product_name
           product_price:(NSString *)product_price cp_trade_no:(NSString *)cp_trade_no
           cp_notify_url:(NSString *)cp_notify_url cp_attach:(NSString *)cp_attach
           player_server:(NSString *)player_server player_role:(NSString *)player_role;


/**
 *  @brief  支付结果处理
 *
 *  @param  treatedBlock  正常订单的处理回调
 *
 *  @param  untreatedBlock  轮询订单的处理回调（轮询订单的支付结果返回可能会有延迟）
 *
 */
- (void)mpManageWithTreatedOrder:(MPPayResultBlock)treatedBlock untreatedOrder:(MPPayResultBlock)untreatedBlock;



/**
 * @brief  处理独立app支付完成后跳回cp应用时携带的支付结果Url
 *
 * @param  url  支付结果url,传入后由SDK解析
 *
 */
- (BOOL)handlePayResult:(NSURL *)url;


/**
 * @brief 处理本地存储的所有需要轮询的订单
 *
 */
- (void)dealWithUntreatedOrderList;


@end



@interface TradeInfo : NSObject

@property (nonatomic, copy) NSString *trade_no;  //sdk订单号
@property (nonatomic, copy) NSString *trade_no_out;   //cp订单号
@property (nonatomic, assign) NSInteger errorCode;  //订单支付结果返回码

@end




