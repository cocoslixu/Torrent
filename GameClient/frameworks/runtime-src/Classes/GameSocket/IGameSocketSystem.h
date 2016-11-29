//
//  IGameSocketSystem.hpp
//  GameClient
//
//  Created by ff on 2016/11/28.
//
//

#ifndef IGameSocketSystem_h
#define IGameSocketSystem_h

#include "EngineBase.h"

class IGameSocketSystem : public IModule
{
protected:
    IGameSocketSystem(){};
    
    virtual ~IGameSocketSystem(){};
    
public:
    static IGameSocketSystem* share();
    
    static void releaseInstance();
    
    virtual void closeSocket(int isocketType)=0;
    
    virtual int sendMsg(int isocketType, const char* pdata, int isize)=0;
    
    virtual int connect(const char* purl, int iprot)=0;
};

#endif /* IGameSocketSystem_h */
