package com.thirdparty.payment;


import com.thirdparty.IThirdParty;


public interface IPayable extends IThirdParty {

    //public boolean handleUnfinishedPayment();

    public void pay(BuyInfo buyInfo,final IPaymentListener listener);

    //public String getExtendStr();
}