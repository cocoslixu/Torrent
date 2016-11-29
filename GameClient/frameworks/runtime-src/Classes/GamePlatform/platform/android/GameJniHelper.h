#pragma once

#include <jni.h>
#include <string>

#define JCLASS_SIG(classPath)       "L" classPath";"

namespace android
{

typedef struct JniMethodInfo_
{
    JNIEnv *    env;
    jclass      classID;
    jmethodID   methodID;
} JniMethodInfo;

class GameJniHelper
{
public:
    static JavaVM* getJavaVM();
    static void setJavaVM(JavaVM *javaVM);
    static JNIEnv* getJEnv();
    static jclass getClassID(const char *className, JNIEnv *env=0);
    static bool getStaticMethodInfo(JniMethodInfo &methodinfo, const char *className, const char *methodName, const char *paramCode);
    static bool getMethodInfo(JniMethodInfo &methodinfo, const char *className, const char *methodName, const char *paramCode);
    
    
    static std::string jstring2string(JNIEnv* env, jstring str);
    static jstring string2jstring(JNIEnv* env, const std::string& s);

    static jstring cstr2jstring(JNIEnv* env, const char* pat);

private:
    static JavaVM *m_psJavaVM;
};

}
