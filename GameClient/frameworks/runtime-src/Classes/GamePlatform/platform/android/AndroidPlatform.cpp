#include "AndroidPlatform.h"
#include "executejavafunction.h"
#include "consts/GameConsts.h"
#include "event/MTNotification.h"
#include "plazz/data/GlobalState.h"
#include "plazz/device/Device.h"
#include "../../GamePlatformSystem.h"
#include "../../../plazz/data/GlobalUserInfo.h"

USING_NS_CC;
USING_NS_CC_EXT;

CAndroidPlatform::CAndroidPlatform()
{

}

CAndroidPlatform::~CAndroidPlatform()
{

}

void CAndroidPlatform::registerGame()
{

}

void CAndroidPlatform::onClickLogin(int iLoginType)
{
	CPlatformBase::onClickLogin(iLoginType);
}

void CAndroidPlatform::loginGame()
{
	CExecuteJavaFunction::executeLogin();
}

void CAndroidPlatform::logoutGame()
{
	if (JZ::Device::isHaveThirdPartyLogin())
	{
		CExecuteJavaFunction::executeLogout();
	}
	else
	{
		onLogoutGame();
	}
	
}

void CAndroidPlatform::onChangeAccount()
{
	CExecuteJavaFunction::executChangeAccount();
}

void CAndroidPlatform::submitUserData(const char* pUserData)
{
	CExecuteJavaFunction::executSubmitInfo(pUserData);
}

void CAndroidPlatform::gamePay(const std::string& pstrPayInfo)
{
	CExecuteJavaFunction::executeGamePay(pstrPayInfo.c_str());
}

void CAndroidPlatform::onLoginGame(const char* pToken, const char* pstrUID, const char* userName)
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
	cocos2d::log("CAndroidPlatform::onLoginGame：url：%s", url.c_str());

	request->setUrl(url.c_str());
	request->setRequestType(network::HttpRequest::Type::POST);
	request->setResponseCallback(CC_CALLBACK_2(CAndroidPlatform::onHttpRequestCompleted, this));
	std::string requestContent=StringUtils::format("{ \"customerid\" : \"%s\", \"customerKey\" : \"%s\", \"cp\" : \"%d\" }", m_strID.c_str(), m_strKey.c_str(), CURRENT_CP);
	cocos2d::log("CAndroidPlatform::onLoginGame：%s", requestContent.c_str());
	std::vector<std::string> headers;
	headers.push_back("Content-Type: application/json; charset=utf-8");
	request->setHeaders(headers);
	request->setRequestData(requestContent.c_str(), requestContent.size());
	network::HttpClient::getInstance()->send(request);
	request->release();

}

void CAndroidPlatform::onLogoutGame()
{
	CPlatformBase::onLogoutGame();
}

void CAndroidPlatform::onGamePay(bool bSuc)
{

}

/******************************/
/*
public class ret_niux
    {

        public int ret ;//0成功 1失败
    }
customerid,customerKey,cp
*/
/******************************/
void CAndroidPlatform::onHttpRequestCompleted(network::HttpClient *sender, network::HttpResponse *response)
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

