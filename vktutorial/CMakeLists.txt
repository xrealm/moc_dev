cmake_minimum_required(VERSION 3.4.1)

# build native_app_glue as a static lib
set(APP_GLUE_DIR ${ANDROID_NDK}/sources/android/native_app_glue)
include_directories(${APP_GLUE_DIR})
add_library(app-glue STATIC ${APP_GLUE_DIR}/android_native_app_glue.c)

#build vulkan app
set(SRC_DIR src/main/cpp)
set(WRAPPER_DIR src/main/cpp/wrapper)

add_library(vktuts SHARED
        )

