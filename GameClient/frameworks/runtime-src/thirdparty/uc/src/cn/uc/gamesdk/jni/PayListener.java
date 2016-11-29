package cn.uc.gamesdk.jni;

import com.thirdparty.payment.IPaymentListener;
import com.thirdparty.payment.PaymentManager;
import com.uc.UCWrapper;

import android.util.Log;
import cn.uc.gamesdk.open.OrderInfo;
import cn.uc.gamesdk.open.UCCallbackListener;
import cn.uc.gamesdk.open.UCGameSdkStatusCode;
/**
 * 下单结果侦听器，此侦听器得到的结果仅为下单结果，不代表最终支付结果。
 *
 */
public class PayListener implements UCCallbackListener<OrderInfo> {
    private final static String TAG = "JNI_PayListener";
    
    private static PayListener _instance = null;
    
    public static PayListener getInstance() {
        if (_instance == null) {
            _instance = new PayListener();
        }
        return _instance;
    }


    @Override
    public void callback(int code, OrderInfo orderInfo) {
        String text = null;
        IPaymentListener listener=UCWrapper.getPaymentListener();
        int result=PaymentManager.PayOrder_Result_Failed;
        switch (code) {
        case UCGameSdkStatusCode.SUCCESS:
            if (orderInfo != null) {
                String orderId = orderInfo.getOrderId();
                float amount = orderInfo.getOrderAmount();
                int payway = orderInfo.getPayWay();
                String paywayName = orderInfo.getPayWayName();

                text = "Order Result: OrderId=" + orderId + ", Amount=" + amount + ", PayWayId=" + payway + ", PayWayName=" + paywayName;
                Log.d(TAG, text);
                text = null;

                //
            } else {
                Log.e(TAG, "Received empty order result");
            }
            result=PaymentManager.PayOrder_Result_OK;
            break;
        case UCGameSdkStatusCode.NO_INIT:
            text = "Paying failed: no init";
            Log.e(TAG, text);
            break;
        case UCGameSdkStatusCode.PAY_USER_EXIT:
            text = "User exit the paying page, return to game page.";
            Log.d(TAG, text);
            result=PaymentManager.PayOrder_Result_Cancelled;
            break;
        default:
            text = "Unknown paying result code: code=" + code;
            Log.e(TAG, text);
            break;

        }

        listener.onPaymentFinished(result);
//        try {
//            String orderId = "";
//            float orderAmount = 0;
//            int payWayId = 0;
//            String payWayName = "";
//            if (orderInfo != null) {
//                orderId = orderInfo.getOrderId();
//                orderAmount = orderInfo.getOrderAmount();
//                payWayId = orderInfo.getPayWay();
//                payWayName = orderInfo.getPayWayName();
//            }
//            
//            String msg;
//            if (code == UCGameSdkStatusCode.SUCCESS) {
//                msg = "callback paying result (ordering result, not the final actual pay result) to C++, code=" + code + 
//                        ", orderId=" + orderId + ", orderAmount=" + orderAmount + ", payWay=" + payWayId + ", payWayName=" + payWayName ;
//            } else {
//                msg = "callback paying event to C++, code=" +code;
//            }
//            
//            Log.d(TAG, msg);
            //JniCallback.nativePayCallback(code, orderId, orderAmount, payWayId, payWayName);
//            
//        } catch (Throwable e) {
//            Log.e(TAG, e.getMessage(), e);
//        }

    }

}