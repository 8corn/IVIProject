#include <jni.h>
#include <string>
#include <ctime>
#include <random>
#include <vector>
#include <numeric>

#define FILTER_WINDOW_SIZE 5
std::vector<float> distanceHistory;

// 연비
extern "C" JNIEXPORT jint JNICALL
Java_com_corn_hyundaiproject_data_car_CarPropertyDataSource_getEfficiencyGrade(
        JNIEnv* env, jobject /* this */, jfloat fuelEfficiency) {
    if (fuelEfficiency >= 15.0) return 1;
    if (fuelEfficiency >= 12.0) return 2;
    if (fuelEfficiency >= 9.0) return 3;
    return 4;
}

// 위험 속도 확인 속도
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
    unsigned char adviceFlag = 0x00;

    if (diff > 10.0f)       adviceFlag |= 0x01;
    else if (diff < -5.0f)  adviceFlag |= 0x02;

    if (adviceFlag & 0x01) {
        return env->NewStringUTF("외부 온도가 너무 높습니다. 내기 순환 모드를 권장합니다.");
    } else if (adviceFlag & 0x02) {
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

extern "C" JNIEXPORT jobject JNICALL
Java_com_corn_hyundaiproject_data_car_CarPropertyDataSource_getDetailedCarData(
        JNIEnv *env, jobject thiz, jfloat speed) {
    jclass mapClass = env->FindClass("java/util/HashMap");
    jmethodID init = env->GetMethodID(mapClass, "<init>", "()V");
    jobject hashMap = env->NewObject(mapClass, init);
    jmethodID put = env->GetMethodID(mapClass, "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");

    // 속도
    int gear = 1;
    float rpmFactor = 1.0f;

    if (speed > 140)      { gear = 8; rpmFactor = 18.0f; }
    else if (speed > 110) { gear = 7; rpmFactor = 22.0f; }
    else if (speed > 85)  { gear = 6; rpmFactor = 28.0f; }
    else if (speed > 60)  { gear = 5; rpmFactor = 36.0f; }
    else if (speed > 40)  { gear = 4; rpmFactor = 45.0f; }
    else if (speed > 25)  { gear = 3; rpmFactor = 60.0f; }
    else if (speed > 10)  { gear = 2; rpmFactor = 90.0f; }
    else                  { gear = 1; rpmFactor = 150.0f; }

    // 가상의 RPM 계산
    int calculatedRpm = static_cast<int>(speed * rpmFactor);

    if (gear > 1) {
        calculatedRpm += 600;
    } else {
        calculatedRpm += 800;
    }
    if (calculatedRpm > 6500) calculatedRpm = 6500;

    std::string mode = "NORMAL";
    if (speed > 130)      mode = "SPORT+";
    else if (speed > 90)  mode = "SPORT";
    else if (speed > 50)  mode = "ECO";

    // 엔진 온도 랜덤 형식(테스트)
    // 난수 생성 (c++ 11 스타일)
    static std::random_device rd;
    static std::default_random_engine generator(rd());
    std::uniform_real_distribution<float> distribution(-0.3f, 0.3f);
    // 온도 계산
    float baseTemp = 90.5f + (speed / 40.0f);
    float currentTemp = baseTemp + distribution(generator);
    // 소수정 한자리까지 자르기
    char tempBuffer[10];
    sprintf(tempBuffer, "%.1f", currentTemp);

    std::string speedStr = std::to_string(static_cast<int>(speed));
    std::string rpmStr = std::to_string(calculatedRpm);

    // rpm, 현재 주행 모드, 엔진 온도
    env->CallObjectMethod(hashMap, put, env->NewStringUTF("speed"), env->NewStringUTF(speedStr.c_str()));
    env->CallObjectMethod(hashMap, put, env->NewStringUTF("rpm"), env->NewStringUTF(rpmStr.c_str()));
    env->CallObjectMethod(hashMap, put, env->NewStringUTF("drive_mode"), env->NewStringUTF(mode.c_str()));
    env->CallObjectMethod(hashMap, put, env->NewStringUTF("engine_temp"), env->NewStringUTF(tempBuffer));

    return hashMap;
}

// 에뮬레이터에서 받은 거리 데이터를 가공하는 함수
extern "C" JNIEXPORT jfloat JNICALL
Java_com_corn_hyundaiproject_data_car_CarPropertyDataSource_getAdasDistanceNative(
        JNIEnv* env, jobject thiz, jfloat raw_sensor_data) {
    if (raw_sensor_data < 0) return 0.0f;

    distanceHistory.push_back(raw_sensor_data);
    if (distanceHistory.size() > FILTER_WINDOW_SIZE) {
        distanceHistory.erase(distanceHistory.begin());
    }

    float sum = std::accumulate(distanceHistory.begin(), distanceHistory.end(), 0.0f);
    float filteredDistance = sum / static_cast<float>(distanceHistory.size());

    return filteredDistance;
}