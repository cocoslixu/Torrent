#include "scripting/lua-bindings/auto/lua_IGameSocketSystem_auto.hpp"
#include "IGameSocketSystem.h"
#include "scripting/lua-bindings/manual/tolua_fix.h"
#include "scripting/lua-bindings/manual/LuaBasicConversions.h"

int lua_IGameSocketSystem_IGameSocketSystem_closeSocket(lua_State* tolua_S)
{
    int argc = 0;
    IGameSocketSystem* cobj = nullptr;
    bool ok  = true;

#if COCOS2D_DEBUG >= 1
    tolua_Error tolua_err;
#endif


#if COCOS2D_DEBUG >= 1
    if (!tolua_isusertype(tolua_S,1,"IGameSocketSystem",0,&tolua_err)) goto tolua_lerror;
#endif

    cobj = (IGameSocketSystem*)tolua_tousertype(tolua_S,1,0);

#if COCOS2D_DEBUG >= 1
    if (!cobj) 
    {
        tolua_error(tolua_S,"invalid 'cobj' in function 'lua_IGameSocketSystem_IGameSocketSystem_closeSocket'", nullptr);
        return 0;
    }
#endif

    argc = lua_gettop(tolua_S)-1;
    if (argc == 1) 
    {
        int arg0;

        ok &= luaval_to_int32(tolua_S, 2,(int *)&arg0, "IGameSocketSystem:closeSocket");
        if(!ok)
        {
            tolua_error(tolua_S,"invalid arguments in function 'lua_IGameSocketSystem_IGameSocketSystem_closeSocket'", nullptr);
            return 0;
        }
        cobj->closeSocket(arg0);
        lua_settop(tolua_S, 1);
        return 1;
    }
    luaL_error(tolua_S, "%s has wrong number of arguments: %d, was expecting %d \n", "IGameSocketSystem:closeSocket",argc, 1);
    return 0;

#if COCOS2D_DEBUG >= 1
    tolua_lerror:
    tolua_error(tolua_S,"#ferror in function 'lua_IGameSocketSystem_IGameSocketSystem_closeSocket'.",&tolua_err);
#endif

    return 0;
}
int lua_IGameSocketSystem_IGameSocketSystem_connect(lua_State* tolua_S)
{
    int argc = 0;
    IGameSocketSystem* cobj = nullptr;
    bool ok  = true;

#if COCOS2D_DEBUG >= 1
    tolua_Error tolua_err;
#endif


#if COCOS2D_DEBUG >= 1
    if (!tolua_isusertype(tolua_S,1,"IGameSocketSystem",0,&tolua_err)) goto tolua_lerror;
#endif

    cobj = (IGameSocketSystem*)tolua_tousertype(tolua_S,1,0);

#if COCOS2D_DEBUG >= 1
    if (!cobj) 
    {
        tolua_error(tolua_S,"invalid 'cobj' in function 'lua_IGameSocketSystem_IGameSocketSystem_connect'", nullptr);
        return 0;
    }
#endif

    argc = lua_gettop(tolua_S)-1;
    if (argc == 2) 
    {
        const char* arg0;
        int arg1;

        std::string arg0_tmp; ok &= luaval_to_std_string(tolua_S, 2, &arg0_tmp, "IGameSocketSystem:connect"); arg0 = arg0_tmp.c_str();

        ok &= luaval_to_int32(tolua_S, 3,(int *)&arg1, "IGameSocketSystem:connect");
        if(!ok)
        {
            tolua_error(tolua_S,"invalid arguments in function 'lua_IGameSocketSystem_IGameSocketSystem_connect'", nullptr);
            return 0;
        }
        int ret = cobj->connect(arg0, arg1);
        tolua_pushnumber(tolua_S,(lua_Number)ret);
        return 1;
    }
    luaL_error(tolua_S, "%s has wrong number of arguments: %d, was expecting %d \n", "IGameSocketSystem:connect",argc, 2);
    return 0;

#if COCOS2D_DEBUG >= 1
    tolua_lerror:
    tolua_error(tolua_S,"#ferror in function 'lua_IGameSocketSystem_IGameSocketSystem_connect'.",&tolua_err);
#endif

    return 0;
}
int lua_IGameSocketSystem_IGameSocketSystem_sendMsg(lua_State* tolua_S)
{
    int argc = 0;
    IGameSocketSystem* cobj = nullptr;
    bool ok  = true;

#if COCOS2D_DEBUG >= 1
    tolua_Error tolua_err;
#endif


#if COCOS2D_DEBUG >= 1
    if (!tolua_isusertype(tolua_S,1,"IGameSocketSystem",0,&tolua_err)) goto tolua_lerror;
#endif

    cobj = (IGameSocketSystem*)tolua_tousertype(tolua_S,1,0);

#if COCOS2D_DEBUG >= 1
    if (!cobj) 
    {
        tolua_error(tolua_S,"invalid 'cobj' in function 'lua_IGameSocketSystem_IGameSocketSystem_sendMsg'", nullptr);
        return 0;
    }
#endif

    argc = lua_gettop(tolua_S)-1;
    if (argc == 3) 
    {
        int arg0;
        const char* arg1;
        int arg2;

        ok &= luaval_to_int32(tolua_S, 2,(int *)&arg0, "IGameSocketSystem:sendMsg");

        std::string arg1_tmp; ok &= luaval_to_std_string(tolua_S, 3, &arg1_tmp, "IGameSocketSystem:sendMsg"); arg1 = arg1_tmp.c_str();

        ok &= luaval_to_int32(tolua_S, 4,(int *)&arg2, "IGameSocketSystem:sendMsg");
        if(!ok)
        {
            tolua_error(tolua_S,"invalid arguments in function 'lua_IGameSocketSystem_IGameSocketSystem_sendMsg'", nullptr);
            return 0;
        }
        int ret = cobj->sendMsg(arg0, arg1, arg2);
        tolua_pushnumber(tolua_S,(lua_Number)ret);
        return 1;
    }
    luaL_error(tolua_S, "%s has wrong number of arguments: %d, was expecting %d \n", "IGameSocketSystem:sendMsg",argc, 3);
    return 0;

#if COCOS2D_DEBUG >= 1
    tolua_lerror:
    tolua_error(tolua_S,"#ferror in function 'lua_IGameSocketSystem_IGameSocketSystem_sendMsg'.",&tolua_err);
#endif

    return 0;
}
int lua_IGameSocketSystem_IGameSocketSystem_releaseInstance(lua_State* tolua_S)
{
    int argc = 0;
    bool ok  = true;

#if COCOS2D_DEBUG >= 1
    tolua_Error tolua_err;
#endif

#if COCOS2D_DEBUG >= 1
    if (!tolua_isusertable(tolua_S,1,"IGameSocketSystem",0,&tolua_err)) goto tolua_lerror;
#endif

    argc = lua_gettop(tolua_S) - 1;

    if (argc == 0)
    {
        if(!ok)
        {
            tolua_error(tolua_S,"invalid arguments in function 'lua_IGameSocketSystem_IGameSocketSystem_releaseInstance'", nullptr);
            return 0;
        }
        IGameSocketSystem::releaseInstance();
        lua_settop(tolua_S, 1);
        return 1;
    }
    luaL_error(tolua_S, "%s has wrong number of arguments: %d, was expecting %d\n ", "IGameSocketSystem:releaseInstance",argc, 0);
    return 0;
#if COCOS2D_DEBUG >= 1
    tolua_lerror:
    tolua_error(tolua_S,"#ferror in function 'lua_IGameSocketSystem_IGameSocketSystem_releaseInstance'.",&tolua_err);
#endif
    return 0;
}
int lua_IGameSocketSystem_IGameSocketSystem_share(lua_State* tolua_S)
{
    int argc = 0;
    bool ok  = true;

#if COCOS2D_DEBUG >= 1
    tolua_Error tolua_err;
#endif

#if COCOS2D_DEBUG >= 1
    if (!tolua_isusertable(tolua_S,1,"IGameSocketSystem",0,&tolua_err)) goto tolua_lerror;
#endif

    argc = lua_gettop(tolua_S) - 1;

    if (argc == 0)
    {
        if(!ok)
        {
            tolua_error(tolua_S,"invalid arguments in function 'lua_IGameSocketSystem_IGameSocketSystem_share'", nullptr);
            return 0;
        }
        IGameSocketSystem* ret = IGameSocketSystem::share();
        object_to_luaval<IGameSocketSystem>(tolua_S, "IGameSocketSystem",(IGameSocketSystem*)ret);
        return 1;
    }
    luaL_error(tolua_S, "%s has wrong number of arguments: %d, was expecting %d\n ", "IGameSocketSystem:share",argc, 0);
    return 0;
#if COCOS2D_DEBUG >= 1
    tolua_lerror:
    tolua_error(tolua_S,"#ferror in function 'lua_IGameSocketSystem_IGameSocketSystem_share'.",&tolua_err);
#endif
    return 0;
}
static int lua_IGameSocketSystem_IGameSocketSystem_finalize(lua_State* tolua_S)
{
    printf("luabindings: finalizing LUA object (IGameSocketSystem)");
    return 0;
}

int lua_register_IGameSocketSystem_IGameSocketSystem(lua_State* tolua_S)
{
    tolua_usertype(tolua_S,"IGameSocketSystem");
    tolua_cclass(tolua_S,"IGameSocketSystem","IGameSocketSystem","IModule",nullptr);

    tolua_beginmodule(tolua_S,"IGameSocketSystem");
        tolua_function(tolua_S,"closeSocket",lua_IGameSocketSystem_IGameSocketSystem_closeSocket);
        tolua_function(tolua_S,"connect",lua_IGameSocketSystem_IGameSocketSystem_connect);
        tolua_function(tolua_S,"sendMsg",lua_IGameSocketSystem_IGameSocketSystem_sendMsg);
        tolua_function(tolua_S,"releaseInstance", lua_IGameSocketSystem_IGameSocketSystem_releaseInstance);
        tolua_function(tolua_S,"share", lua_IGameSocketSystem_IGameSocketSystem_share);
    tolua_endmodule(tolua_S);
    std::string typeName = typeid(IGameSocketSystem).name();
    g_luaType[typeName] = "IGameSocketSystem";
    g_typeCast["IGameSocketSystem"] = "IGameSocketSystem";
    return 1;
}
TOLUA_API int register_all_IGameSocketSystem(lua_State* tolua_S)
{
	tolua_open(tolua_S);
	
	tolua_module(tolua_S,nullptr,0);
	tolua_beginmodule(tolua_S,nullptr);

	lua_register_IGameSocketSystem_IGameSocketSystem(tolua_S);

	tolua_endmodule(tolua_S);
	return 1;
}

