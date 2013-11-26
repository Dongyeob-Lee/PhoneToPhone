 
JAVA_CLASSES_DIR = bin/classes
JAVA_NATIVES_DIR = $(JAVA_CLASSES_DIR)/com/example/remotedroid/natives
JAVA_NATIVES_U_DIR = $(JAVA_CLASSES_DIR)/com/example/remotedroid/universal/natives
JNI_HEADER_DIR = jni/include
SOURCES = InputHandler.cpp FrameHandler.cpp

all: header lib

header : ../$(JAVA_NATIVES_DIR)/InputHandler.class ../$(JAVA_NATIVES_DIR)/FrameHandler.class ../$(JAVA_NATIVES_U_DIR)/FrameHandlerU.class
	cd ../$(JAVA_CLASSES_DIR); \
	javah com.example.remotedroid.natives.InputHandler; \
	javah com.example.remotedroid.natives.FrameHandler; \
	javah com.example.remotedroid.universal.natives.FrameHandlerU; \
	mv com_example_remotedroid_natives_InputHandler.h InputHandler.h; \
	mv com_example_remotedroid_natives_FrameHandler.h FrameHandler.h; \
	mv com_example_remotedroid_universal_natives_FrameHandlerU.h FrameHandlerU.h; \
	mv InputHandler.h ../../jni/include/InputHandler.h; \
	mv FrameHandler.h ../../jni/include/FrameHandler.h; \
	mv FrameHandlerU.h ../../jni/include/FrameHandlerU.h
	
lib : $(SOURCES)
	ndk-build APP_ABI="armeabi x86"
	
clean :
	rm -rf ../libs/armeabi/libremoteroid.so; \
	rm -rf ../libs/armeabi/libfbuffer.so; \
	rm -rf ../libs/x86/libremoteroid.so; \
	rm -rf ../libs/x86/libfbuffer.so; \
	rm -rf ../obj