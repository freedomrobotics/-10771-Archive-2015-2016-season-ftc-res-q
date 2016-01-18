/*
 * Creates hooks for build in color processing functions.
 * Probably not a good thing to do normally and might decrease performance
 */

#include <GLES2/gl2.h>
#include <GLES2/gl2ext.h>
#include <stdlib.h>
#include <android/log.h>
#include <time.h>
#include <fastcv/fastcv.h>

#include "ColorConversion.h"


// Preprocessor defines for android logging.
#define LOG_TAG         "ColorConversion"
#define DPRINTF(...)    __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)  // Debug printf
#define IPRINTF(...)    __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)   // Info printf
#define EPRINTF(...)    __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)  // Error printf

