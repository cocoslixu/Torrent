//
//  GameEngine.hpp
//  GameClient
//
//  Created by ff on 2016/11/16.
//
//

#ifndef GameEngine_hpp
#define GameEngine_hpp

#include "EngineBase.h"
#include "lua.h"
#include "scripting/lua-bindings/manual/Lua-BindingsExport.h"

class CGameEngine
{
private:
    CGameEngine();
    
    ~CGameEngine();
public:
    
    static CGameEngine* instance();
    
    static void releaseInstance();

    bool init();
    
    void release();
    
    void releaseModule(IModule* pModule);
    
    int  lua_GameModule_register(lua_State* L);
    
    void initPlatformSDK(void* pView);
    
    void runLuaMainLoop(float dt);
    
private:
    typedef std::vector<IModule*> VECMODULE;
    typedef VECMODULE::iterator   ITERMODULE;
    
    VECMODULE   m_vecMoudle;
    
    static CGameEngine* s_pInstance;
};

//define

#define SAVE_RELEASE_GAMEENGINE(X) {if(CGameEngine::instance()){CGameEngine::instance()->releaseModule(X);}}

#endif /* GameEngine_hpp */
