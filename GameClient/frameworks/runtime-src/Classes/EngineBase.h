//
//  EngineBase.h
//  GameClient
//
//  Created by ff on 2016/11/15.
//
//

#ifndef EngineBase_h
#define EngineBase_h

#include <map>
#include <vector>
#include <list>
#include <string>
#include <string.h>
#include <iostream>
using namespace std;

typedef unsigned char       UINT8;
typedef char                INT8;
typedef unsigned short      UINT16;
typedef short               INT16;
typedef unsigned int        UINT32;
typedef int                 INT32;
typedef unsigned long long  UINT64;
typedef long long           INT64;

#define SAVE_RELEASE(x) {if(x != NULL){x->release(); delete x; x = NULL;}}

#define SAVE_DELETE(x)  {if(x != NULL){delete x; x = NULL;}}

// c++ 统一接口类
class IModule
{
public:
    IModule();
    
    virtual ~IModule();
    
    virtual bool init()=0;
    
    virtual void release()=0;
    
    virtual int  lua_GameModule_register(void* pL)=0;
};


extern UINT32 APHash(const INT8* str);


#endif /* EngineBase_h */
