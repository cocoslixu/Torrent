package cn.uc.gamesdk.jni;

import org.json.JSONObject;

import android.app.Activity;
import android.util.Log;
import cn.uc.gamesdk.UCGameSdk;
import cn.uc.gamesdk.open.GameParamInfo;
import cn.uc.gamesdk.open.PaymentInfo;
import cn.uc.gamesdk.open.UCLogLevel;
import cn.uc.gamesdk.open.UCOrientation;


/**
 * UC游戏SDK面向 C/C++ 的 JNI 接口
 * 应从Java代码中调用 setCurrentActivity 方法设置游戏的当前 Activity，其它方法一般从 C++ 中调用。
 * 
 */
public class GameSdk {
    private final static String TAG = "JNI_UCGameSdk.defaultSdk()";

    private static Activity _gameActivity = null;

    /**
     * 向SDK设置游戏的当前Activity，如果游戏有多个Activity，对于需要调用本类中的方法的Activity，在调用方法前都应该调用此方法设置当前的Activity。
     * 注意：此方法须从Java中调用的，不应从C++中调用。
     * 
     * @param gameActivity
     *            当前的Activity
     */
    public static void setCurrentActivity(Activity gameActivity) {
        Log.d(TAG, "setCurrentActivity calling...");
        _gameActivity = gameActivity;
    }

    /**
     * 初始化
     * 初始化SDK,调用 UCGameSdk.defaultSdk().initSDK 的部分需要放置在主线程中
     * 
     * @param debugMode
     *            是否联调模式， false=连接SDK的正式生产环境，true=连接SDK的测试联调环境
     * @param logLevel
     *            日志级别，
     *            0=错误信息级别，记录错误日志，
     *            1=警告信息级别，记录错误和警告日志，
     *            2=调试信息级别，记录错误、警告和调试信息，为最详尽的日志级别
     * @param gameId
     *            游戏ID，该ID由UC游戏中心分配，唯一标识一款游戏
     * @param cpId
     *            游戏合作商ID，该ID由UC游戏中心分配，唯一标识一个游戏合作商
     * @param serverId
     *            游戏服务器（游戏分区）标识，由UC游戏中心分配
     * @param serverName
     *            游戏服务器（游戏分区）名称
     * @param enablePayHistory
     *            是否启用支付查询功能
     * @param enableLogout
     *            是否启用用户切换功能
     */
    public static void initSDK(final boolean debugMode, int logLevel, int gameId, int serverId, boolean enablePayHistory, boolean enableLogout, int orient) {
        Log.d(TAG, "initSDK calling...");        
        try {
            Log.d(TAG, "initSDK parameters: debugMode=" + debugMode + ", loglevel=" + logLevel + ", gameId=" + gameId + ", serverId=" + serverId + ", enablePayHistory=" + enablePayHistory
                    + ", enableLogout=" + enableLogout);

            UCOrientation orientation = UCOrientation.valueOf(orient);
            final GameParamInfo gp = new GameParamInfo();
            gp.setEnablePayHistory(enablePayHistory);
            gp.setEnableUserChange(enableLogout);
            gp.setGameId(gameId);
            gp.setServerId(serverId);
            gp.setOrientation(UCOrientation.LANDSCAPE);
            //gp.setOrientation(orientation);
            Log.d(TAG, "after gp values set");

            UCLogLevel level = UCLogLevel.fromValue(logLevel);

            Log.d(TAG, "setLogoutNotifyListener...");
            UCGameSdk.defaultSdk().setLogoutNotifyListener(LogoutListener.getInstance());
            Log.d(TAG, "will init SDK...");

            UCGameSdk.defaultSdk().initSdk(_gameActivity, level, debugMode, gp, InitResultListener.getInstance());

            Log.d(TAG, "after init SDK invoked");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage(), e);
        }

    }


    /**
     * 调用SDK的用户登录
     */
    public static void login() {
        Log.d(TAG, "login calling...");

        _gameActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                try {
                    UCGameSdk.defaultSdk().login(LoginResultListener.getInstance());
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        });



    }

    /**
     * 返回用户登录后的会话标识，此标识会在失效时刷新，游戏在每次需要使用该标识时应从SDK获取
     * 
     * @return 用户登录会话标识
     */
    public static String getSid() {
        Log.d(TAG, "getSid calling...");
        return UCGameSdk.defaultSdk().getSid();
    }

    /**
     * 退出当前登录的账号
     */
    public static void logout() {
        Log.d(TAG, "logout calling...");
        try {
            UCGameSdk.defaultSdk().logout();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage(), e);
        }
    }


    /**
     * 在当前 Activity 上创建九游的悬浮按钮
     */
    public static void createFloatButton() {
        Log.d(TAG, "createFloatButton calling...");
        _gameActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                UCGameSdk.defaultSdk().createFloatButton(_gameActivity);
            }
        });

    }

    /**
     * 显示/隐藏九游的悬浮按钮
     * 
     * @param x
     *            悬浮按钮显示位置的横坐标，单位：%，支持小数。该参数只支持 0 和 100，分别表示在屏幕最左边或最右边显示悬浮按钮。
     * @param y
     *            悬浮按钮显示位置的纵坐标，单位：%，支持小数。例如：80，表示悬浮按钮显示的位置距屏幕顶部的距离为屏幕高度的 80% 。
     * @param visible
     *            true=显示 false=隐藏，隐藏时x,y的值忽略
     */
    public static void showFloatButton(final float x, final float y, final boolean visible) {
        Log.d(TAG, "showFloatButton calling...");

        _gameActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (visible) {
                    UCGameSdk.defaultSdk().showFloatButton(_gameActivity, x, y);
                } else {
                    UCGameSdk.defaultSdk().hideFloatButton(_gameActivity);
                }
            }
        });
    }

    /**
     * 销毁当前 Activity 的九游悬浮按钮
     */
    public static void destroyFloatButton() {

        Log.d(TAG, "destroyFloatButton calling...");
        _gameActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UCGameSdk.defaultSdk().destoryFloatButton(_gameActivity);
            }
        });
    }

    /**
     * 执行充值下单操作，此操作会调出充值界面。
     * 
     * @param allowContinuousPay
     *            设置是否允许连接充值，true表示在一次充值完成后在充值界面中可以继续下一笔充值，false表示只能进行一笔充值即返回游戏。
     * @param amount
     *            充值金额。默认为0，如果不设或设为0，充值时用户从充值界面中选择或输入金额；如果设为大于0的值，表示固定充值金额，不允许用户选择或输入其它金额。
     * @param serverId
     *            当前充值的游戏服务器（分区）标识，此标识即UC分配的游戏服务器ID
     * @param roleId
     *            当前充值用户在游戏中的角色标识
     * @param roleName
     *            当前充值用户在游戏中的角色名称
     * @param grade
     *            当前充值用户在游戏中的角色等级
     * @param customInfo
     *            充值自定义信息，此信息作为充值订单的附加信息，充值过程中不作任何处理，仅用于游戏设置自助信息，比如游戏自身产生的订单号、玩家角色、游戏模式等。
     *            如果设置了自定义信息，UC在完成充值后，调用充值结果回调接口向游戏服务器发送充值结果时将会附带此信息，游戏服务器需自行解析自定义信息。
     *            如果不需设置自定义信息，将此参数置为空字符串即可。
     * @param notifyUrl
     *            支付回调地址
     * @param transactionNum
     *            自有交易号
     */
    public static void pay(boolean allowContinuousPay, float amount, int serverId, String roleId, String roleName, String grade, String customInfo, String notifyUrl, String transactionNum) {
        Log.d(TAG, "pay calling...");
        try {
            PaymentInfo payInfo = new PaymentInfo();
            payInfo.setAmount(amount);
            payInfo.setRoleId(roleId);
            payInfo.setRoleName(roleName);
            payInfo.setGrade(grade);
            payInfo.setCustomInfo(customInfo);
            payInfo.setNotifyUrl(notifyUrl);// 加入支付回调地址
            payInfo.setTransactionNumCP(transactionNum);//加入自有交易号

            UCGameSdk.defaultSdk().pay(payInfo, PayListener.getInstance());

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage(), e);
        }

    }


    /**
     * 进入九游社区（用户中心）
     */
    public static void enterUserCenter() {
        Log.d(TAG, "enterUserCenter calling...");
        try {
            UCGameSdk.defaultSdk().enterUserCenter(UserCenterListener.getInstance());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage(), e);
        }
    }


    /**
     * 进入SDK的某一指定界面
     * 
     * @param business
     *            业务标识
     */
    public static void enterUI(String business) {
        Log.d(TAG, "enterUserCenter calling...");

        try {
            UCGameSdk.defaultSdk().enterUserCenter(EnterUIListener.getInstance());
            UCGameSdk.defaultSdk().enterUI(business, EnterUIListener.getInstance());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage(), e);
        }
    }


    /**
     * 提交游戏扩展数据，在登录成功以后可以调用。具体的数据种类和数据内容定义，请参考“开发参考说明书”。
     * 
     * @param dataType
     *            数据种类
     * @param dataStr
     *            数据内容，是一个 JSON 字符串。
     * 
     */
    public static void submitExtendData(final String dataType, final String dataStr) {
        Log.d(TAG, "submitExtendData calling...");
        try {
            final JSONObject data = new JSONObject(dataStr);
            UCGameSdk.defaultSdk().submitExtendData(dataType, data);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage(), e);
        }

    }
    public static void submitExtendData(final String dataType, final JSONObject data) {
        Log.d(TAG, "submitExtendData calling..."+data);
        try {          
            UCGameSdk.defaultSdk().submitExtendData(dataType, data);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage(), e);
        }

    }
    /**
     * 退出SDK，游戏退出前必须调用此方法，以清理SDK占用的系统资源。如果游戏退出时不调用该方法，可能会引起程序错误。
     */
    public static void exitSDK() {
        Log.d(TAG, "exitSDK calling...");
        _gameActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                try {
                    UCGameSdk.defaultSdk().exitSDK(_gameActivity, ExitListener.getInstance());
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        });
    }
}
