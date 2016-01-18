#ifndef LEPAMPLEMOUSSE_HOOKS_FASTCV_COLORCONVERSION_H
#define LEPAMPLEMOUSSE_HOOKS_FASTCV_COLORCONVERSION_H


#include <jni.h>


#define JAVA_CLASS "org/fhs/robotics/ftcteam10771/lepamplemousse/hooks/fastcv/ColorConversion"

static

static JNINativeMethod methods[] = {
        {"", "", (void *)},
        {"", "", (void *)}
};

jint JNI_OnLoad(JavaDM *vm, void *reserved)
{
    JNIEnv *env;
    if ((*vm)->GetEnv(vm, (void **)&env, JNI_VERSION_1_4) != JNI_OK)
        return -1;

    jclass cls = (*env)->FindClass(env, JAVA_CLASS);
    (*env)->RegisterNatives(env, cls, methods, sizeof(methods) /
                                               sizeof(methods[0]));

    return JNI_VERSION_1_4;
}


#endif //RES_Q_COLORCONVERSION_H
