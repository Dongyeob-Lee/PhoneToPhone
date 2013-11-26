/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_example_remotedroid_natives_InputHandler */

#ifndef _Included_com_example_remotedroid_natives_InputHandler
#define _Included_com_example_remotedroid_natives_InputHandler
#ifdef __cplusplus
extern "C" {
#endif
#undef com_example_remotedroid_natives_InputHandler_DIMENSION
#define com_example_remotedroid_natives_InputHandler_DIMENSION 4096L
#undef com_example_remotedroid_natives_InputHandler_HALF_DIMENSION
#define com_example_remotedroid_natives_InputHandler_HALF_DIMENSION 2048L
/*
 * Class:     com_example_remotedroid_natives_InputHandler
 * Method:    openInputDevice
 * Signature: (II)Z
 */
JNIEXPORT jboolean JNICALL Java_com_example_remotedroid_natives_InputHandler_openInputDevice
  (JNIEnv *, jobject, jint, jint);

/*
 * Class:     com_example_remotedroid_natives_InputHandler
 * Method:    openInputDeviceWithoutPermission
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_com_example_remotedroid_natives_InputHandler_openInputDeviceWithoutPermission
  (JNIEnv *, jobject);

/*
 * Class:     com_example_remotedroid_natives_InputHandler
 * Method:    closeInputDevice
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_example_remotedroid_natives_InputHandler_closeInputDevice
  (JNIEnv *, jobject);

/*
 * Class:     com_example_remotedroid_natives_InputHandler
 * Method:    closeInputDeviceWithoutRevertPermission
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_example_remotedroid_natives_InputHandler_closeInputDeviceWithoutRevertPermission
  (JNIEnv *, jobject);

/*
 * Class:     com_example_remotedroid_natives_InputHandler
 * Method:    keyDown
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_example_remotedroid_natives_InputHandler_keyDown
  (JNIEnv *, jobject, jint);

/*
 * Class:     com_example_remotedroid_natives_InputHandler
 * Method:    keyUp
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_example_remotedroid_natives_InputHandler_keyUp
  (JNIEnv *, jobject, jint);

/*
 * Class:     com_example_remotedroid_natives_InputHandler
 * Method:    keyStroke
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_example_remotedroid_natives_InputHandler_keyStroke
  (JNIEnv *, jobject, jint);

/*
 * Class:     com_example_remotedroid_natives_InputHandler
 * Method:    touchDown
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_example_remotedroid_natives_InputHandler_touchDown
  (JNIEnv *, jobject);

/*
 * Class:     com_example_remotedroid_natives_InputHandler
 * Method:    touchUp
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_example_remotedroid_natives_InputHandler_touchUp
  (JNIEnv *, jobject);

/*
 * Class:     com_example_remotedroid_natives_InputHandler
 * Method:    touchSetPtr
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_com_example_remotedroid_natives_InputHandler_touchSetPtr
  (JNIEnv *, jobject, jint, jint);

#ifdef __cplusplus
}
#endif
#endif
