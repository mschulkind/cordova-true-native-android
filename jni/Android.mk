LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := SMRuntime
LOCAL_SRC_FILES := SMRuntime.cpp

#LOCAL_C_INCLUDES := $(LOCAL_PATH)

#LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)

LOCAL_WHOLE_STATIC_LIBRARIES := spidermonkey_static

LOCAL_LDLIBS := -llog 
LOCAL_LDLIBS += -landroid

include $(BUILD_SHARED_LIBRARY)

$(call import-module,spidermonkey/android)
