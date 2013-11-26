LOCAL_PATH := $(call my-dir)
# APP_ABI := armeabi x86
# include $(call all-subdir-makefiles)
include $(CLEAR_VARS)

LOCAL_MODULE := fbuffer
LOCAL_SRC_FILES := FrameHandler.cpp	\
	DisplayInfo.cpp
				   
LOCAL_LDLIBS := -llog

include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)