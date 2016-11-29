#include "GameJniHelper.h"
// #include "OS.h"
    
#define JAVAVM    android::GameJniHelper::getJavaVM()

using namespace std;

extern "C"
{
    //////////////////////////////////////////////////////////////////////////
    // java vm helper function
    //////////////////////////////////////////////////////////////////////////

    static bool getEnv_(JNIEnv **env)
    {
        bool bRet = false;

        do 
        {
			int err = JAVAVM->GetEnv((void**)env, JNI_VERSION_1_4); 
            if (err != JNI_OK && err != JNI_EDETACHED)
            {
                //DLOG("Failed to get the environment using getEnv_()");
                break;
            }

			if( err == JNI_EDETACHED)
			{
                //DLOG("JAVAVM->AttachCurrentThread");
				if (JAVAVM->AttachCurrentThread(env, 0) < 0)
				{
					//DLOG("Failed to get the environment using AttachCurrentThread()");
					break;
				}
			}

            bRet = true;
        } while (0);        

        return bRet;
    }

    static jclass getClassID_(const char *className, JNIEnv *env)
    {
        JNIEnv *pEnv = env;
        jclass ret = 0;

        do 
        {
            if (! pEnv)
            {
                if (! getEnv_(&pEnv))
                {
                    break;
                }
            }
            
            ret = pEnv->FindClass(className);
            if (! ret)
            {
                //DLOG("Failed to find class of %s", className);
                break;
            }
        } while (0);

        return ret;
    }

    static bool getStaticMethodInfo_(android::JniMethodInfo &methodinfo, const char *className, const char *methodName, const char *paramCode)
    {
        jmethodID methodID = 0;
        JNIEnv *pEnv = 0;
        bool bRet = false;

        do 
        {
            if (! getEnv_(&pEnv))
            {
                break;
            }

            jclass classID = getClassID_(className, pEnv);

            methodID = pEnv->GetStaticMethodID(classID, methodName, paramCode);
            if (! methodID)
            {
                //DLOG("Failed to find static method id of %s", methodName);
                break;
            }

            methodinfo.classID = classID;
            methodinfo.env = pEnv;
            methodinfo.methodID = methodID;

            bRet = true;
        } while (0);

        return bRet;
    }

    static bool getMethodInfo_(android::JniMethodInfo &methodinfo, const char *className, const char *methodName, const char *paramCode)
    {
        jmethodID methodID = 0;
        JNIEnv *pEnv = 0;
        bool bRet = false;

        do 
        {
            if (! getEnv_(&pEnv))
            {
                break;
            }

            jclass classID = getClassID_(className, pEnv);

            methodID = pEnv->GetMethodID(classID, methodName, paramCode);
            if (! methodID)
            {
               // DLOG("Failed to find method id of %s", methodName);
                break;
            }

            methodinfo.classID = classID;
            methodinfo.env = pEnv;
            methodinfo.methodID = methodID;

            bRet = true;
        } while (0);

        return bRet;
    }

    static string jstring2string_(JNIEnv* env, jstring jstr)
    {
        if (jstr == NULL)
        {
            return "";
        }

        if(env == NULL)
        {
            if (! getEnv_(&env))
            {
                return 0;
            }    
        }  
        
        //DLOG("Before GetStringUTFChars:%x", (unsigned int)jstr);
        const char* chars = env->GetStringUTFChars(jstr, NULL);
        string ret(chars); 

        //DLOG("GetStringUTFChars: %s", ret.c_str());
        env->ReleaseStringUTFChars(jstr, chars);

        return ret;
    }
}

namespace android
{

JavaVM* GameJniHelper::m_psJavaVM = NULL;

JavaVM* GameJniHelper::getJavaVM()
{
    return m_psJavaVM;
}

void GameJniHelper::setJavaVM(JavaVM *javaVM)
{
    m_psJavaVM = javaVM;
}

JNIEnv* GameJniHelper::getJEnv()
{
    JNIEnv* ret = NULL;
    getEnv_(&ret);
    return ret;
}

jclass GameJniHelper::getClassID(const char *className, JNIEnv *env)
{
    //DLOG("GameJniHelper::getClassID %s", className);
    return getClassID_(className, env);
}

bool GameJniHelper::getStaticMethodInfo(JniMethodInfo &methodinfo, const char *className, const char *methodName, const char *paramCode)
{
    //DLOG("GameJniHelper::getStaticMethodInfo %s %s", className, methodName);
    return getStaticMethodInfo_(methodinfo, className, methodName, paramCode);
}

bool GameJniHelper::getMethodInfo(JniMethodInfo &methodinfo, const char *className, const char *methodName, const char *paramCode)
{
   // DLOG("GameJniHelper::getMethodInfo %s %s", className, methodName);
    return getMethodInfo_(methodinfo, className, methodName, paramCode);
}

string GameJniHelper::jstring2string(JNIEnv* env, jstring str)
{
    return jstring2string_(env, str);
}

jstring GameJniHelper::cstr2jstring(JNIEnv* env, const char* pat)
{
    if(!env)
    {
        getEnv_(&env);
    }

    return env->NewStringUTF(pat);
}

jstring GameJniHelper::string2jstring(JNIEnv* env, const std::string& s)
{
    return cstr2jstring(env, s.c_str());
}

}
