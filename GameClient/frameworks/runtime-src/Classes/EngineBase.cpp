//
//  EngineBase.cpp
//  GameClient
//
//  Created by ff on 2016/11/15.
//
//

#include "EngineBase.h"

UINT32 APHash(const INT8* str)
{
    unsigned int hash = 0;
    int i;
    
    for (i = 0; *str; i++)
    {
        if ((i & 1) == 0)
        {
            hash ^= ((hash << 7) ^ (*str++) ^ (hash >> 3));
        }
        else
        {
            hash ^= (~((hash << 11) ^ (*str++) ^ (hash >> 5)));
        }
    }
    
    return (hash & 0x7FFFFFFF);
}

IModule::IModule()
{
    
}

IModule::~IModule()
{
    
}
