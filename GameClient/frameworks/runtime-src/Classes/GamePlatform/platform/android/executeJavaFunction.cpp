#include "executejavafunction.h"
#include "plazz/device/Device.h"
#include <jni.h>
#include "platform/android/jni/JniHelper.h"
#include "event/MTNotification.h"
//#include "common/MobClickCpp.h"
#include "consts/GameConsts.h"
#include "plazz/data/GlobalState.h"
#include "utils/GameUtils.h"
#include "plazz/df/types.h"
#include "spine/Json.h"
#include "cocos2d.h"
#include "cocos-ext.h"
#include "network/HttpClient.h"
#include "../../../plazz/data/StringData.h"
#include "../../../shuihu/plazz/Plazz_SHZ.h"

USING_NS_CC;
using namespace JZ;

#define  CLASS_NAME "com/thirdparty/login/LoginManager"

void CExecuteJavaFunction::executeLogin()
{
	JniMethodInfo minfo;
	jobject jobj;

	bool isHave = JniHelper::getStaticMethodInfo(minfo, CLASS_NAME, "login", "()V");
	if (isHave)
	{
		minfo.env->CallStaticVoidMethod(minfo.classID,minfo.methodID);
	}
	minfo.env->DeleteLocalRef(minfo.classID);
}

void CExecuteJavaFunction::executeLogout()
{
	JniMethodInfo minfo;
	jobject jobj;

	bool isHave = JniHelper::getStaticMethodInfo(minfo, CLASS_NAME, "logout", "()V");
	if (isHave)
	{
		minfo.env->CallStaticVoidMethod(minfo.classID,minfo.methodID);
	}
	minfo.env->DeleteLocalRef(minfo.classID);
}

void CExecuteJavaFunction::executChangeAccount()
{
	JniMethodInfo minfo;
	jobject jobj;
	bool isHave = JniHelper::getStaticMethodInfo(minfo, CLASS_NAME, "changeAccount", "()V");
	if (isHave)
		{
			minfo.env->CallStaticVoidMethod(minfo.classID,minfo.methodID);
		}
		minfo.env->DeleteLocalRef(minfo.classID);
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
void CExecuteJavaFunction::executeGamePay(const std::string& pstrPayInfo)
{
	PLAZZ_PRINTF(" CExecuteJavaFunction::executeGamePay : %s", pstrPayInfo.c_str());
	if (!pstrPayInfo.empty())
	{
		// std::vector<std::string> strinfo;
		// GameUtils::split(pstrPayInfo, SPLIT_TAG, strinfo);
		// if (strinfo.size() != 7)
		// {
		// 	return;
		// }

		// std::string strUserId = strinfo[0];
		// int purchaseType = atoi(strinfo[1].c_str());
		// int price = atoi(strinfo[2].c_str());
		// std::string orderId = strinfo[3];
		// std::string addr = strinfo[4];
		// std::string paykey = strinfo[5];
		// std::string productName = strinfo[6];
		// JZ::Device::pay(strUserId, purchaseType, price, orderId, addr,paykey, productName);

		Json* mjs = Json_create(pstrPayInfo.c_str());
        if(mjs)
        {
        	
			int purchaseType = 0;
			int price = 0;
			std::string strUserId = "";
			std::string orderId = "";
			std::string addr = "";
			std::string paykey = "";
			std::string productName = "";
			std::string value = "";

            const char* pstr = Json_getString(mjs,"PurchaseType", "");
            if(pstr != NULL)
            {
            	purchaseType = atoi(pstr);
            }

            pstr = Json_getString(mjs,"dwUserID", "");
            if(pstr != NULL)
            {
            	strUserId = pstr;
            }

            pstr = Json_getString(mjs,"orderIdStr", "");
            if(pstr != NULL)
            {
            	orderId = pstr;
            }

            pstr = Json_getString(mjs,"price", "");
            if(pstr != NULL)
            {
            	price = atoi(pstr);
            }

            pstr = Json_getString(mjs,"validateAddr", "");
            if(pstr != NULL)
            {
            	addr = pstr;
            }

            pstr = Json_getString(mjs,"reason", "");
            if(pstr != NULL)
            {
            	paykey = pstr;
            }

            pstr = Json_getString(mjs,"productName", "");
            if(pstr != NULL)
            {
            	productName = pstr;
            }
			pstr = Json_getString(mjs, "value", "");
			if (pstr != NULL)
			{
				value = pstr;
				value = StringData::replace_all(value, "|", "\"");
			}

			JZ::Device::pay(strUserId, purchaseType, price, orderId, addr,paykey, productName,value);
        }
	}
}

void CExecuteJavaFunction::executSubmitInfo(const char* pInfo)
{

}
