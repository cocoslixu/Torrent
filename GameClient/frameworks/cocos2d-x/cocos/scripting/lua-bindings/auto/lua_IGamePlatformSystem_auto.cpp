#include "scripting/lua-bindings/auto/lua_IGamePlatformSystem_auto.hpp"
#include "IGamePlatfromSystem.h"
#include "scripting/lua-bindings/manual/tolua_fix.h"
#include "scripting/lua-bindings/manual/LuaBasicConversions.h"

int lua_IGamePlatformSystem_IGamePlatformSystem_gamePay(lua_State* tolua_S)
{
    int argc = 0;
    IGamePlatformSystem* cobj = nullptr;
    bool ok  = true;

#if COCOS2D_DEBUG >= 1
    tolua_Error tolua_err;
#endif


#if COCOS2D_DEBUG >= 1
    if (!tolua_isusertype(tolua_S,1,"IGamePlatformSystem",0,&tolua_err)) goto tolua_lerror;
#endif

    cobj = (IGamePlatformSystem*)tolua_tousertype(tolua_S,1,0);

#if COCOS2D_DEBUG >= 1
    if (!cobj) 
    {
        tolua_error(tolua_S,"invalid 'cobj' in function 'lua_IGamePlatformSystem_IGamePlatformSystem_gamePay'", nullptr);
        return 0;
    }
#endif

    argc = lua_gettop(tolua_S)-1;
    if (argc == 1) 
    {
        std::string arg0;

        ok &= luaval_to_std_string(tolua_S, 2,&arg0, "IGamePlatformSystem:gamePay");
        if(!ok)
        {
            tolua_error(tolua_S,"invalid arguments in function 'lua_IGamePlatformSystem_IGamePlatformSystem_gamePay'", nullptr);
            return 0;
        }
        cobj->gamePay(arg0);
        lua_settop(tolua_S, 1);
        return 1;
    }
    luaL_error(tolua_S, "%s has wrong number of arguments: %d, was expecting %d \n", "IGamePlatformSystem:gamePay",argc, 1);
    return 0;

#if COCOS2D_DEBUG >= 1
    tolua_lerror:
    tolua_error(tolua_S,"#ferror in function 'lua_IGamePlatformSystem_IGamePlatformSystem_gamePay'.",&tolua_err);
#endif

    return 0;
}
int lua_IGamePlatformSystem_IGamePlatformSystem_onClickLogin(lua_State* tolua_S)
{
    int argc = 0;
    IGamePlatformSystem* cobj = nullptr;
    bool ok  = true;

#if COCOS2D_DEBUG >= 1
    tolua_Error tolua_err;
#endif


#if COCOS2D_DEBUG >= 1
    if (!tolua_isusertype(tolua_S,1,"IGamePlatformSystem",0,&tolua_err)) goto tolua_lerror;
#endif

    cobj = (IGamePlatformSystem*)tolua_tousertype(tolua_S,1,0);

#if COCOS2D_DEBUG >= 1
    if (!cobj) 
    {
        tolua_error(tolua_S,"invalid 'cobj' in function 'lua_IGamePlatformSystem_IGamePlatformSystem_onClickLogin'", nullptr);
        return 0;
    }
#endif

    argc = lua_gettop(tolua_S)-1;
    if (argc == 1) 
    {
        int arg0;

        ok &= luaval_to_int32(tolua_S, 2,(int *)&arg0, "IGamePlatformSystem:onClickLogin");
        if(!ok)
        {
            tolua_error(tolua_S,"invalid arguments in function 'lua_IGamePlatformSystem_IGamePlatformSystem_onClickLogin'", nullptr);
            return 0;
        }
        cobj->onClickLogin(arg0);
        lua_settop(tolua_S, 1);
        return 1;
    }
    luaL_error(tolua_S, "%s has wrong number of arguments: %d, was expecting %d \n", "IGamePlatformSystem:onClickLogin",argc, 1);
    return 0;

#if COCOS2D_DEBUG >= 1
    tolua_lerror:
    tolua_error(tolua_S,"#ferror in function 'lua_IGamePlatformSystem_IGamePlatformSystem_onClickLogin'.",&tolua_err);
#endif

    return 0;
}
int lua_IGamePlatformSystem_IGamePlatformSystem_loginGame(lua_State* tolua_S)
{
    int argc = 0;
    IGamePlatformSystem* cobj = nullptr;
    bool ok  = true;

#if COCOS2D_DEBUG >= 1
    tolua_Error tolua_err;
#endif


#if COCOS2D_DEBUG >= 1
    if (!tolua_isusertype(tolua_S,1,"IGamePlatformSystem",0,&tolua_err)) goto tolua_lerror;
#endif

    cobj = (IGamePlatformSystem*)tolua_tousertype(tolua_S,1,0);

#if COCOS2D_DEBUG >= 1
    if (!cobj) 
    {
        tolua_error(tolua_S,"invalid 'cobj' in function 'lua_IGamePlatformSystem_IGamePlatformSystem_loginGame'", nullptr);
        return 0;
    }
#endif

    argc = lua_gettop(tolua_S)-1;
    if (argc == 0) 
    {
        if(!ok)
        {
            tolua_error(tolua_S,"invalid arguments in function 'lua_IGamePlatformSystem_IGamePlatformSystem_loginGame'", nullptr);
            return 0;
        }
        cobj->loginGame();
        lua_settop(tolua_S, 1);
        return 1;
    }
    luaL_error(tolua_S, "%s has wrong number of arguments: %d, was expecting %d \n", "IGamePlatformSystem:loginGame",argc, 0);
    return 0;

#if COCOS2D_DEBUG >= 1
    tolua_lerror:
    tolua_error(tolua_S,"#ferror in function 'lua_IGamePlatformSystem_IGamePlatformSystem_loginGame'.",&tolua_err);
#endif

    return 0;
}
int lua_IGamePlatformSystem_IGamePlatformSystem_registerGame(lua_State* tolua_S)
{
    int argc = 0;
    IGamePlatformSystem* cobj = nullptr;
    bool ok  = true;

#if COCOS2D_DEBUG >= 1
    tolua_Error tolua_err;
#endif


#if COCOS2D_DEBUG >= 1
    if (!tolua_isusertype(tolua_S,1,"IGamePlatformSystem",0,&tolua_err)) goto tolua_lerror;
#endif

    cobj = (IGamePlatformSystem*)tolua_tousertype(tolua_S,1,0);

#if COCOS2D_DEBUG >= 1
    if (!cobj) 
    {
        tolua_error(tolua_S,"invalid 'cobj' in function 'lua_IGamePlatformSystem_IGamePlatformSystem_registerGame'", nullptr);
        return 0;
    }
#endif

    argc = lua_gettop(tolua_S)-1;
    if (argc == 0) 
    {
        if(!ok)
        {
            tolua_error(tolua_S,"invalid arguments in function 'lua_IGamePlatformSystem_IGamePlatformSystem_registerGame'", nullptr);
            return 0;
        }
        cobj->registerGame();
        lua_settop(tolua_S, 1);
        return 1;
    }
    luaL_error(tolua_S, "%s has wrong number of arguments: %d, was expecting %d \n", "IGamePlatformSystem:registerGame",argc, 0);
    return 0;

#if COCOS2D_DEBUG >= 1
    tolua_lerror:
    tolua_error(tolua_S,"#ferror in function 'lua_IGamePlatformSystem_IGamePlatformSystem_registerGame'.",&tolua_err);
#endif

    return 0;
}
int lua_IGamePlatformSystem_IGamePlatformSystem_getGameVersion(lua_State* tolua_S)
{
    int argc = 0;
    IGamePlatformSystem* cobj = nullptr;
    bool ok  = true;

#if COCOS2D_DEBUG >= 1
    tolua_Error tolua_err;
#endif


#if COCOS2D_DEBUG >= 1
    if (!tolua_isusertype(tolua_S,1,"IGamePlatformSystem",0,&tolua_err)) goto tolua_lerror;
#endif

    cobj = (IGamePlatformSystem*)tolua_tousertype(tolua_S,1,0);

#if COCOS2D_DEBUG >= 1
    if (!cobj) 
    {
        tolua_error(tolua_S,"invalid 'cobj' in function 'lua_IGamePlatformSystem_IGamePlatformSystem_getGameVersion'", nullptr);
        return 0;
    }
#endif

    argc = lua_gettop(tolua_S)-1;
    if (argc == 0) 
    {
        if(!ok)
        {
            tolua_error(tolua_S,"invalid arguments in function 'lua_IGamePlatformSystem_IGamePlatformSystem_getGameVersion'", nullptr);
            return 0;
        }
        int ret = cobj->getGameVersion();
        tolua_pushnumber(tolua_S,(lua_Number)ret);
        return 1;
    }
    luaL_error(tolua_S, "%s has wrong number of arguments: %d, was expecting %d \n", "IGamePlatformSystem:getGameVersion",argc, 0);
    return 0;

#if COCOS2D_DEBUG >= 1
    tolua_lerror:
    tolua_error(tolua_S,"#ferror in function 'lua_IGamePlatformSystem_IGamePlatformSystem_getGameVersion'.",&tolua_err);
#endif

    return 0;
}
int lua_IGamePlatformSystem_IGamePlatformSystem_Logout(lua_State* tolua_S)
{
    int argc = 0;
    IGamePlatformSystem* cobj = nullptr;
    bool ok  = true;

#if COCOS2D_DEBUG >= 1
    tolua_Error tolua_err;
#endif


#if COCOS2D_DEBUG >= 1
    if (!tolua_isusertype(tolua_S,1,"IGamePlatformSystem",0,&tolua_err)) goto tolua_lerror;
#endif

    cobj = (IGamePlatformSystem*)tolua_tousertype(tolua_S,1,0);

#if COCOS2D_DEBUG >= 1
    if (!cobj) 
    {
        tolua_error(tolua_S,"invalid 'cobj' in function 'lua_IGamePlatformSystem_IGamePlatformSystem_Logout'", nullptr);
        return 0;
    }
#endif

    argc = lua_gettop(tolua_S)-1;
    if (argc == 0) 
    {
        if(!ok)
        {
            tolua_error(tolua_S,"invalid arguments in function 'lua_IGamePlatformSystem_IGamePlatformSystem_Logout'", nullptr);
            return 0;
        }
        cobj->Logout();
        lua_settop(tolua_S, 1);
        return 1;
    }
    luaL_error(tolua_S, "%s has wrong number of arguments: %d, was expecting %d \n", "IGamePlatformSystem:Logout",argc, 0);
    return 0;

#if COCOS2D_DEBUG >= 1
    tolua_lerror:
    tolua_error(tolua_S,"#ferror in function 'lua_IGamePlatformSystem_IGamePlatformSystem_Logout'.",&tolua_err);
#endif

    return 0;
}
int lua_IGamePlatformSystem_IGamePlatformSystem_getGameCP(lua_State* tolua_S)
{
    int argc = 0;
    IGamePlatformSystem* cobj = nullptr;
    bool ok  = true;

#if COCOS2D_DEBUG >= 1
    tolua_Error tolua_err;
#endif


#if COCOS2D_DEBUG >= 1
    if (!tolua_isusertype(tolua_S,1,"IGamePlatformSystem",0,&tolua_err)) goto tolua_lerror;
#endif

    cobj = (IGamePlatformSystem*)tolua_tousertype(tolua_S,1,0);

#if COCOS2D_DEBUG >= 1
    if (!cobj) 
    {
        tolua_error(tolua_S,"invalid 'cobj' in function 'lua_IGamePlatformSystem_IGamePlatformSystem_getGameCP'", nullptr);
        return 0;
    }
#endif

    argc = lua_gettop(tolua_S)-1;
    if (argc == 0) 
    {
        if(!ok)
        {
            tolua_error(tolua_S,"invalid arguments in function 'lua_IGamePlatformSystem_IGamePlatformSystem_getGameCP'", nullptr);
            return 0;
        }
        int ret = cobj->getGameCP();
        tolua_pushnumber(tolua_S,(lua_Number)ret);
        return 1;
    }
    luaL_error(tolua_S, "%s has wrong number of arguments: %d, was expecting %d \n", "IGamePlatformSystem:getGameCP",argc, 0);
    return 0;

#if COCOS2D_DEBUG >= 1
    tolua_lerror:
    tolua_error(tolua_S,"#ferror in function 'lua_IGamePlatformSystem_IGamePlatformSystem_getGameCP'.",&tolua_err);
#endif

    return 0;
}
int lua_IGamePlatformSystem_IGamePlatformSystem_getPayChannelID(lua_State* tolua_S)
{
    int argc = 0;
    IGamePlatformSystem* cobj = nullptr;
    bool ok  = true;

#if COCOS2D_DEBUG >= 1
    tolua_Error tolua_err;
#endif


#if COCOS2D_DEBUG >= 1
    if (!tolua_isusertype(tolua_S,1,"IGamePlatformSystem",0,&tolua_err)) goto tolua_lerror;
#endif

    cobj = (IGamePlatformSystem*)tolua_tousertype(tolua_S,1,0);

#if COCOS2D_DEBUG >= 1
    if (!cobj) 
    {
        tolua_error(tolua_S,"invalid 'cobj' in function 'lua_IGamePlatformSystem_IGamePlatformSystem_getPayChannelID'", nullptr);
        return 0;
    }
#endif

    argc = lua_gettop(tolua_S)-1;
    if (argc == 0) 
    {
        if(!ok)
        {
            tolua_error(tolua_S,"invalid arguments in function 'lua_IGamePlatformSystem_IGamePlatformSystem_getPayChannelID'", nullptr);
            return 0;
        }
        int ret = cobj->getPayChannelID();
        tolua_pushnumber(tolua_S,(lua_Number)ret);
        return 1;
    }
    luaL_error(tolua_S, "%s has wrong number of arguments: %d, was expecting %d \n", "IGamePlatformSystem:getPayChannelID",argc, 0);
    return 0;

#if COCOS2D_DEBUG >= 1
    tolua_lerror:
    tolua_error(tolua_S,"#ferror in function 'lua_IGamePlatformSystem_IGamePlatformSystem_getPayChannelID'.",&tolua_err);
#endif

    return 0;
}
int lua_IGamePlatformSystem_IGamePlatformSystem_submitUserData(lua_State* tolua_S)
{
    int argc = 0;
    IGamePlatformSystem* cobj = nullptr;
    bool ok  = true;

#if COCOS2D_DEBUG >= 1
    tolua_Error tolua_err;
#endif


#if COCOS2D_DEBUG >= 1
    if (!tolua_isusertype(tolua_S,1,"IGamePlatformSystem",0,&tolua_err)) goto tolua_lerror;
#endif

    cobj = (IGamePlatformSystem*)tolua_tousertype(tolua_S,1,0);

#if COCOS2D_DEBUG >= 1
    if (!cobj) 
    {
        tolua_error(tolua_S,"invalid 'cobj' in function 'lua_IGamePlatformSystem_IGamePlatformSystem_submitUserData'", nullptr);
        return 0;
    }
#endif

    argc = lua_gettop(tolua_S)-1;
    if (argc == 1) 
    {
        const char* arg0;

        std::string arg0_tmp; ok &= luaval_to_std_string(tolua_S, 2, &arg0_tmp, "IGamePlatformSystem:submitUserData"); arg0 = arg0_tmp.c_str();
        if(!ok)
        {
            tolua_error(tolua_S,"invalid arguments in function 'lua_IGamePlatformSystem_IGamePlatformSystem_submitUserData'", nullptr);
            return 0;
        }
        cobj->submitUserData(arg0);
        lua_settop(tolua_S, 1);
        return 1;
    }
    luaL_error(tolua_S, "%s has wrong number of arguments: %d, was expecting %d \n", "IGamePlatformSystem:submitUserData",argc, 1);
    return 0;

#if COCOS2D_DEBUG >= 1
    tolua_lerror:
    tolua_error(tolua_S,"#ferror in function 'lua_IGamePlatformSystem_IGamePlatformSystem_submitUserData'.",&tolua_err);
#endif

    return 0;
}
int lua_IGamePlatformSystem_IGamePlatformSystem_releaseInstace(lua_State* tolua_S)
{
    int argc = 0;
    bool ok  = true;

#if COCOS2D_DEBUG >= 1
    tolua_Error tolua_err;
#endif

#if COCOS2D_DEBUG >= 1
    if (!tolua_isusertable(tolua_S,1,"IGamePlatformSystem",0,&tolua_err)) goto tolua_lerror;
#endif

    argc = lua_gettop(tolua_S) - 1;

    if (argc == 0)
    {
        if(!ok)
        {
            tolua_error(tolua_S,"invalid arguments in function 'lua_IGamePlatformSystem_IGamePlatformSystem_releaseInstace'", nullptr);
            return 0;
        }
        IGamePlatformSystem::releaseInstace();
        lua_settop(tolua_S, 1);
        return 1;
    }
    luaL_error(tolua_S, "%s has wrong number of arguments: %d, was expecting %d\n ", "IGamePlatformSystem:releaseInstace",argc, 0);
    return 0;
#if COCOS2D_DEBUG >= 1
    tolua_lerror:
    tolua_error(tolua_S,"#ferror in function 'lua_IGamePlatformSystem_IGamePlatformSystem_releaseInstace'.",&tolua_err);
#endif
    return 0;
}
int lua_IGamePlatformSystem_IGamePlatformSystem_share(lua_State* tolua_S)
{
    int argc = 0;
    bool ok  = true;

#if COCOS2D_DEBUG >= 1
    tolua_Error tolua_err;
#endif

#if COCOS2D_DEBUG >= 1
    if (!tolua_isusertable(tolua_S,1,"IGamePlatformSystem",0,&tolua_err)) goto tolua_lerror;
#endif

    argc = lua_gettop(tolua_S) - 1;

    if (argc == 0)
    {
        if(!ok)
        {
            tolua_error(tolua_S,"invalid arguments in function 'lua_IGamePlatformSystem_IGamePlatformSystem_share'", nullptr);
            return 0;
        }
        IGamePlatformSystem* ret = IGamePlatformSystem::share();
        object_to_luaval<IGamePlatformSystem>(tolua_S, "IGamePlatformSystem",(IGamePlatformSystem*)ret);
        return 1;
    }
    luaL_error(tolua_S, "%s has wrong number of arguments: %d, was expecting %d\n ", "IGamePlatformSystem:share",argc, 0);
    return 0;
#if COCOS2D_DEBUG >= 1
    tolua_lerror:
    tolua_error(tolua_S,"#ferror in function 'lua_IGamePlatformSystem_IGamePlatformSystem_share'.",&tolua_err);
#endif
    return 0;
}
static int lua_IGamePlatformSystem_IGamePlatformSystem_finalize(lua_State* tolua_S)
{
    printf("luabindings: finalizing LUA object (IGamePlatformSystem)");
    return 0;
}

int lua_register_IGamePlatformSystem_IGamePlatformSystem(lua_State* tolua_S)
{
    tolua_usertype(tolua_S,"IGamePlatformSystem");
    tolua_cclass(tolua_S,"IGamePlatformSystem","IGamePlatformSystem","IModule",nullptr);

    tolua_beginmodule(tolua_S,"IGamePlatformSystem");
        tolua_function(tolua_S,"gamePay",lua_IGamePlatformSystem_IGamePlatformSystem_gamePay);
        tolua_function(tolua_S,"onClickLogin",lua_IGamePlatformSystem_IGamePlatformSystem_onClickLogin);
        tolua_function(tolua_S,"loginGame",lua_IGamePlatformSystem_IGamePlatformSystem_loginGame);
        tolua_function(tolua_S,"registerGame",lua_IGamePlatformSystem_IGamePlatformSystem_registerGame);
        tolua_function(tolua_S,"getGameVersion",lua_IGamePlatformSystem_IGamePlatformSystem_getGameVersion);
        tolua_function(tolua_S,"Logout",lua_IGamePlatformSystem_IGamePlatformSystem_Logout);
        tolua_function(tolua_S,"getGameCP",lua_IGamePlatformSystem_IGamePlatformSystem_getGameCP);
        tolua_function(tolua_S,"getPayChannelID",lua_IGamePlatformSystem_IGamePlatformSystem_getPayChannelID);
        tolua_function(tolua_S,"submitUserData",lua_IGamePlatformSystem_IGamePlatformSystem_submitUserData);
        tolua_function(tolua_S,"releaseInstace", lua_IGamePlatformSystem_IGamePlatformSystem_releaseInstace);
        tolua_function(tolua_S,"share", lua_IGamePlatformSystem_IGamePlatformSystem_share);
    tolua_endmodule(tolua_S);
    std::string typeName = typeid(IGamePlatformSystem).name();
    g_luaType[typeName] = "IGamePlatformSystem";
    g_typeCast["IGamePlatformSystem"] = "IGamePlatformSystem";
    return 1;
}
TOLUA_API int register_all_IGamePlatformSystem(lua_State* tolua_S)
{
	tolua_open(tolua_S);
	
	tolua_module(tolua_S,nullptr,0);
	tolua_beginmodule(tolua_S,nullptr);

	lua_register_IGamePlatformSystem_IGamePlatformSystem(tolua_S);

	tolua_endmodule(tolua_S);
	return 1;
}

