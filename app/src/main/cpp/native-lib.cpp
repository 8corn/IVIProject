#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_corn_hyundaiproject_data_car_CarPropertyDataSource_getNativeMessage(
        JNIEnv* env,
        jobject/* this */) {
    std::string hello = "G70 Native Engine Active (C++)";
    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT jint JNICALL
Java_com_corn_hyundaiproject_data_car_CarPropertyDataSource_getEfficiencyGrade(
        JNIEnv* env, jobject /* this */, jfloat fuelEfficiency) {
    if (fuelEfficiency >= 15.0) return 1;
    if (fuelEfficiency >= 12.0) return 2;
    if (fuelEfficiency >= 9.0) return 3;
    return 4;
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_corn_hyundaiproject_data_car_CarPropertyDataSource_checkDrivingStatus(
        JNIEnv* env, jobject /* this */, jfloat speed) {
    if (speed > 120.0f) {
        return env->NewStringUTF("과속 위험! 속도를 줄이세요.");
    } else if (speed > 100.0f) {
        return env->NewStringUTF("고속 주행 중");
    } else {
        return env->NewStringUTF("정상 주행 중");
    }
}