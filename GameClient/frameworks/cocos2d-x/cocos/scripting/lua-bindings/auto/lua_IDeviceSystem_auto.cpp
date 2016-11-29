#include "scripting/lua-bindings/auto/lua_IDeviceSystem_auto.hpp"
#include "IDeviceSystem.h"
#include "scripting/lua-bindings/manual/tolua_fix.h"
#include "scripting/lua-bindings/manual/LuaBasicConversions.h"

int lua_IDeviceSystem_IDeviceSystem_openOfficialWebSite(lua_State* tolua_S)
{
    int argc = 0;
    IDeviceSystem* cobj = nullptr;
    bool ok  = true;

#if COCOS2D_DEBUG >= 1
    tolua_Error tolua_err;
#endif


#if COCOS2D_DEBUG >= 1
    if (!tolua_isusertype(tolua_S,1,"IDeviceSystem",0,&tolua_err)) goto tolua_lerror;
#endif

    cobj = (IDeviceSystem*)tolua_tousertype(tolua_S,1,0);

#if COCOS2D_DEBUG >= 1
    if (!cobj) 
    {
        tolua_error(tolua_S,"invalid 'cobj' in function 'lua_IDeviceSystem_IDeviceSystem_openOfficialWebSite'", nullptr);
        return 0;
    }
#endif

    argc = lua_gettop(tolua_S)-1;
    if (argc == 0) 
    {
        if(!ok)
        {
            tolua_error(tolua_S,"invalid arguments in function 'lua_IDeviceSystem_IDeviceSystem_openOfficialWebSite'", nullptr);
            return 0;
        }
        cobj->openOfficialWebSite();
        lua_settop(tolua_S, 1);
        return 1;
    }
    luaL_error(tolua_S, "%s has wrong number of arguments: %d, was expecting %d \n", "IDeviceSystem:openOfficialWebSite",argc, 0);
    return 0;

#if COCOS2D_DEBUG >= 1
    tolua_lerror:
    tolua_error(tolua_S,"#ferror in function 'lua_IDeviceSystem_IDeviceSystem_openOfficialWebSite'.",&tolua_err);
#endif

    return 0;
}
int lua_IDeviceSystem_IDeviceSystem_getDeveicUDID(lua_State* tolua_S)
{
    int argc = 0;
    IDeviceSystem* cobj = nullptr;
    bool ok  = true;

#if COCOS2D_DEBUG >= 1
    tolua_Error tolua_err;
#endif


#if COCOS2D_DEBUG >= 1
    if (!tolua_isusertype(tolua_S,1,"IDeviceSystem",0,&tolua_err)) goto tolua_lerror;
#endif

    cobj = (IDeviceSystem*)tolua_tousertype(tolua_S,1,0);

#if COCOS2D_DEBUG >= 1
    if (!cobj) 
    {
        tolua_error(tolua_S,"invalid 'cobj' in function 'lua_IDeviceSystem_IDeviceSystem_getDeveicUDID'", nullptr);
        return 0;
    }
#endif

    argc = lua_gettop(tolua_S)-1;
    if (argc == 0) 
    {
        if(!ok)
        {
            tolua_error(tolua_S,"invalid arguments in function 'lua_IDeviceSystem_IDeviceSystem_getDeveicUDID'", nullptr);
            return 0;
        }
        const std::string& ret = cobj->getDeveicUDID();
        tolua_pushcppstring(tolua_S,ret);
        return 1;
    }
    luaL_error(tolua_S, "%s has wrong number of arguments: %d, was expecting %d \n", "IDeviceSystem:getDeveicUDID",argc, 0);
    return 0;

#if COCOS2D_DEBUG >= 1
    tolua_lerror:
    tolua_error(tolua_S,"#ferror in function 'lua_IDeviceSystem_IDeviceSystem_getDeveicUDID'.",&tolua_err);
#endif

    return 0;
}
int lua_IDeviceSystem_IDeviceSystem_openURL(lua_State* tolua_S)
{
    int argc = 0;
    IDeviceSystem* cobj = nullptr;
    bool ok  = true;

#if COCOS2D_DEBUG >= 1
    tolua_Error tolua_err;
#endif


#if COCOS2D_DEBUG >= 1
    if (!tolua_isusertype(tolua_S,1,"IDeviceSystem",0,&tolua_err)) goto tolua_lerror;
#endif

    cobj = (IDeviceSystem*)tolua_tousertype(tolua_S,1,0);

#if COCOS2D_DEBUG >= 1
    if (!cobj) 
    {
        tolua_error(tolua_S,"invalid 'cobj' in function 'lua_IDeviceSystem_IDeviceSystem_openURL'", nullptr);
        return 0;
    }
#endif

    argc = lua_gettop(tolua_S)-1;
    if (argc == 1) 
    {
        std::string arg0;

        ok &= luaval_to_std_string(tolua_S, 2,&arg0, "IDeviceSystem:openURL");
        if(!ok)
        {
            tolua_error(tolua_S,"invalid arguments in function 'lua_IDeviceSystem_IDeviceSystem_openURL'", nullptr);
            return 0;
        }
        cobj->openURL(arg0);
        lua_settop(tolua_S, 1);
        return 1;
    }
    luaL_error(tolua_S, "%s has wrong number of arguments: %d, was expecting %d \n", "IDeviceSystem:openURL",argc, 1);
    return 0;

#if COCOS2D_DEBUG >= 1
    tolua_lerror:
    tolua_error(tolua_S,"#ferror in function 'lua_IDeviceSystem_IDeviceSystem_openURL'.",&tolua_err);
#endif

    return 0;
}
int lua_IDeviceSystem_IDeviceSystem_getPackageName(lua_State* tolua_S)
{
    int argc = 0;
    IDeviceSystem* cobj = nullptr;
    bool ok  = true;

#if COCOS2D_DEBUG >= 1
    tolua_Error tolua_err;
#endif


#if COCOS2D_DEBUG >= 1
    if (!tolua_isusertype(tolua_S,1,"IDeviceSystem",0,&tolua_err)) goto tolua_lerror;
#endif

    cobj = (IDeviceSystem*)tolua_tousertype(tolua_S,1,0);

#if COCOS2D_DEBUG >= 1
    if (!cobj) 
    {
        tolua_error(tolua_S,"invalid 'cobj' in function 'lua_IDeviceSystem_IDeviceSystem_getPackageName'", nullptr);
        return 0;
    }
#endif

    argc = lua_gettop(tolua_S)-1;
    if (argc == 0) 
    {
        if(!ok)
        {
            tolua_error(tolua_S,"invalid arguments in function 'lua_IDeviceSystem_IDeviceSystem_getPackageName'", nullptr);
            return 0;
        }
        const std::string& ret = cobj->getPackageName();
        tolua_pushcppstring(tolua_S,ret);
        return 1;
    }
    luaL_error(tolua_S, "%s has wrong number of arguments: %d, was expecting %d \n", "IDeviceSystem:getPackageName",argc, 0);
    return 0;

#if COCOS2D_DEBUG >= 1
    tolua_lerror:
    tolua_error(tolua_S,"#ferror in function 'lua_IDeviceSystem_IDeviceSystem_getPackageName'.",&tolua_err);
#endif

    return 0;
}
int lua_IDeviceSystem_IDeviceSystem_releaseInstance(lua_State* tolua_S)
{
    int argc = 0;
    bool ok  = true;

#if COCOS2D_DEBUG >= 1
    tolua_Error tolua_err;
#endif

#if COCOS2D_DEBUG >= 1
    if (!tolua_isusertable(tolua_S,1,"IDeviceSystem",0,&tolua_err)) goto tolua_lerror;
#endif

    argc = lua_gettop(tolua_S) - 1;

    if (argc == 0)
    {
        if(!ok)
        {
            tolua_error(tolua_S,"invalid arguments in function 'lua_IDeviceSystem_IDeviceSystem_releaseInstance'", nullptr);
            return 0;
        }
        IDeviceSystem::releaseInstance();
        lua_settop(tolua_S, 1);
        return 1;
    }
    luaL_error(tolua_S, "%s has wrong number of arguments: %d, was expecting %d\n ", "IDeviceSystem:releaseInstance",argc, 0);
    return 0;
#if COCOS2D_DEBUG >= 1
    tolua_lerror:
    tolua_error(tolua_S,"#ferror in function 'lua_IDeviceSystem_IDeviceSystem_releaseInstance'.",&tolua_err);
#endif
    return 0;
}
int lua_IDeviceSystem_IDeviceSystem_share(lua_State* tolua_S)
{
    int argc = 0;
    bool ok  = true;

#if COCOS2D_DEBUG >= 1
    tolua_Error tolua_err;
#endif

#if COCOS2D_DEBUG >= 1
    if (!tolua_isusertable(tolua_S,1,"IDeviceSystem",0,&tolua_err)) goto tolua_lerror;
#endif

    argc = lua_gettop(tolua_S) - 1;

    if (argc == 0)
    {
        if(!ok)
        {
            tolua_error(tolua_S,"invalid arguments in function 'lua_IDeviceSystem_IDeviceSystem_share'", nullptr);
            return 0;
        }
        IDeviceSystem* ret = IDeviceSystem::share();
        object_to_luaval<IDeviceSystem>(tolua_S, "IDeviceSystem",(IDeviceSystem*)ret);
        return 1;
    }
    luaL_error(tolua_S, "%s has wrong number of arguments: %d, was expecting %d\n ", "IDeviceSystem:share",argc, 0);
    return 0;
#if COCOS2D_DEBUG >= 1
    tolua_lerror:
    tolua_error(tolua_S,"#ferror in function 'lua_IDeviceSystem_IDeviceSystem_share'.",&tolua_err);
#endif
    return 0;
}
static int lua_IDeviceSystem_IDeviceSystem_finalize(lua_State* tolua_S)
{
    printf("luabindings: finalizing LUA object (IDeviceSystem)");
    return 0;
}

int lua_register_IDeviceSystem_IDeviceSystem(lua_State* tolua_S)
{
    tolua_usertype(tolua_S,"IDeviceSystem");
    tolua_cclass(tolua_S,"IDeviceSystem","IDeviceSystem","IModule",nullptr);

    tolua_beginmodule(tolua_S,"IDeviceSystem");
        tolua_function(tolua_S,"openOfficialWebSite",lua_IDeviceSystem_IDeviceSystem_openOfficialWebSite);
        tolua_function(tolua_S,"getDeveicUDID",lua_IDeviceSystem_IDeviceSystem_getDeveicUDID);
        tolua_function(tolua_S,"openURL",lua_IDeviceSystem_IDeviceSystem_openURL);
        tolua_function(tolua_S,"getPackageName",lua_IDeviceSystem_IDeviceSystem_getPackageName);
        tolua_function(tolua_S,"releaseInstance", lua_IDeviceSystem_IDeviceSystem_releaseInstance);
        tolua_function(tolua_S,"share", lua_IDeviceSystem_IDeviceSystem_share);
    tolua_endmodule(tolua_S);
    std::string typeName = typeid(IDeviceSystem).name();
    g_luaType[typeName] = "IDeviceSystem";
    g_typeCast["IDeviceSystem"] = "IDeviceSystem";
    return 1;
}
TOLUA_API int register_all_IDeviceSystem(lua_State* tolua_S)
{
	tolua_open(tolua_S);
	
	tolua_module(tolua_S,nullptr,0);
	tolua_beginmodule(tolua_S,nullptr);

	lua_register_IDeviceSystem_IDeviceSystem(tolua_S);

	tolua_endmodule(tolua_S);
	return 1;
}

