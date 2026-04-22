#include <jni.h>
#include <string>
#include <ctime>

// 연비
extern "C" JNIEXPORT jint JNICALL
Java_com_corn_hyundaiproject_data_car_CarPropertyDataSource_getEfficiencyGrade(
        JNIEnv* env, jobject /* this */, jfloat fuelEfficiency) {
    if (fuelEfficiency >= 15.0) return 1;
    if (fuelEfficiency >= 12.0) return 2;
    if (fuelEfficiency >= 9.0) return 3;
    return 4;
}

// 속도
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

// 헤드라이트
extern "C" JNIEXPORT jint JNICALL
Java_com_corn_hyundaiproject_data_car_CarPropertyDataSource_changeHeadlight(
        JNIEnv* env, jobject /* this */, jboolean nightMode) {
    // 현재 시간 가져오기
    time_t now = time(nullptr);
    tm *ltm = localtime(&now);
    int hour = ltm->tm_hour;

    if ((hour >= 18 || hour < 6 || nightMode)) {
        return 1;
    } else {
        return 0;
    }
}

// 에어컨 온도
extern "C" JNIEXPORT jstring JNICALL
Java_com_corn_hyundaiproject_data_car_CarPropertyDataSource_getClimateAdvice(
        JNIEnv* env, jobject /* this */, jfloat exteriorTemp, jfloat interiorTemp) {
    float diff = exteriorTemp - interiorTemp;

    if (diff > 10.0f) {
        return env->NewStringUTF("외부 온도가 너무 높습니다. 내기 순환 모드를 권장합니다.");
    } else if (diff < -5.0f) {
        return env->NewStringUTF("외부 온도가 너무 낮습니다. 히터를 켜는 것을 권장합니다.");
    } else {
        return env->NewStringUTF("쾌적한 온도입니다.");
    }
}

// 연료 잔량
extern "C" JNIEXPORT jstring JNICALL
Java_com_corn_hyundaiproject_data_car_CarPropertyDataSource_checkFuelStatus(
        JNIEnv* env, jobject /* this */, jfloat fuelLevel) {
    if (fuelLevel < 10.0f) {
        return env->NewStringUTF("연료가 부족합니다! 근처 주유소를 찾으세요.");
    } else if (fuelLevel < 25.0f) {
        return env->NewStringUTF("연료 잔량이 적습니다.");
    } else {
        return env->NewStringUTF("연료는 충분합니다.");
    }
}

// 문 확인
extern "C" JNIEXPORT jboolean JNICALL
Java_com_corn_hyundaiproject_data_car_CarPropertyDataSource_isHazardous(
        JNIEnv* env, jobject /* this */, jint gear, jboolean isDoorOpen) {
    if (gear == 4 && isDoorOpen) {
        return JNI_TRUE;
    }
    return JNI_FALSE;
}