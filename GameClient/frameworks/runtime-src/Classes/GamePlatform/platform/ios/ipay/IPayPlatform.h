//
//  IPayPaltform.hpp
//  buyu
//
//  Created by ff on 2016/10/21.
//
//

#ifndef IPayPaltform_hpp
#define IPayPaltform_hpp

#include "../../../PlatformBase.h"

class CIPayPlatform : public CPlatformBase
{
public:
    CIPayPlatform();
    
    ~CIPayPlatform();
    
    virtual void initSDK(void* pView);
    
    virtual void gamePay(const std::string& pstrPayInfo);
    
    virtual void platformOpenURL(void* pURL, void* sourceApplication, void* application, void* nid);
    
    virtual int getPayChannelID();
};

#endif /* IPayPaltform_hpp */
