#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_sari_downloader_MainActivity_nativeVersion(
        JNIEnv* env,
        jobject /* thiz */) {

    const std::string version =
            "SARI Downloader Engine 1.0";

    return env->NewStringUTF(
            version.c_str()
    );
}
