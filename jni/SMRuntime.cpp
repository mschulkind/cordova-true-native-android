#include "SMRuntime.h"

#include <stdlib.h>

#include <android/asset_manager.h>
#include <android/asset_manager_jni.h>
#include <android/log.h>
#include <assert.h>
#include <jni.h>
#include <string.h>

#include <jsapi.h>

#include "org_apache_cordova_plugins_truenative_SMRuntime.h"

#define  LOG_TAG    "JNI_org_apache_cordova_plugins_truenative_SMRuntime"
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)

static jfieldID getLongFieldID(JNIEnv* env, jobject obj, const char* name) {
  jfieldID fid;
  const char *str;

  jclass cls = env->GetObjectClass(obj);

  fid = env->GetFieldID(cls, name, "J");
  assert(fid);

  return fid;
}

static void setInstanceLong(
    JNIEnv* env, jobject obj, const char* name, long value) {
  return env->SetLongField(obj, getLongFieldID(env, obj, name), value);
}

static long getInstanceLong(JNIEnv* env, jobject obj, const char* name) {
  return env->GetLongField(obj, getLongFieldID(env, obj, name));
}

static JSContext* getJsContext(JNIEnv* env, jobject obj) {
  return (JSContext*)getInstanceLong(env, obj, "mJSContext");
}

static JSObject* getJsGlobalObject(JNIEnv* env, jobject obj) {
  return (JSObject*)getInstanceLong(env, obj, "mJSGlobalObject");
}

static JSRuntime* getJsRuntime(JNIEnv* env, jobject obj) {
  return (JSRuntime*)getInstanceLong(env, obj, "mJSRuntime");
}

// The caller is responsible for freeing the returned string.
static char* getStringChars(JSContext* jsContext, jsval stringJsval) {
  JSString* jsString = JS_ValueToString(jsContext, stringJsval);

  char* str = JS_EncodeString(jsContext, jsString);
  assert(str);

  return str;
}
static char* getStringChars(JNIEnv* env, jobject obj, jsval stringJsval) {
  return getStringChars(getJsContext(env, obj), stringJsval);
}

static jstring convertToJstring(JNIEnv* env, jobject obj, jsval stringJsval) {
  JSContext* jsContext = getJsContext(env, obj);

  char* str = getStringChars(env, obj, stringJsval);
  jstring jString = env->NewStringUTF(str);
  JS_free(jsContext, str);

  return jString;
}

static JSBool jsLog(JSContext *cx, unsigned argc, jsval *vp) {
  jsval* argv = JS_ARGV(cx, vp);
  assert(argc == 1);

  char* msg = getStringChars(cx, argv[0]);
  LOGD("%s", msg);
  JS_free(cx, msg);

  JS_SET_RVAL(cx, vp, JSVAL_VOID);
  return JS_TRUE;
}

static JSBool jsNativeExec(JSContext *cx, unsigned argc, jsval *vp) {
  LOGD("NativeExec");
  assert(false);

  assert(argc == 1);
  //NSString* commandJSON = stringWithJsval(cx, argv[0]);


  JS_SET_RVAL(cx, vp, JSVAL_VOID);
  return JS_TRUE;
}

static JSClass jsTimerIDClass = {
    "TimerID", JSCLASS_HAS_PRIVATE,
    JS_PropertyStub, JS_PropertyStub, JS_PropertyStub, JS_StrictPropertyStub,
    JS_EnumerateStub, JS_ResolveStub, JS_ConvertStub, JS_FinalizeStub,
    JSCLASS_NO_OPTIONAL_MEMBERS
};

static JSBool jsSetTimer(
    JSContext* cx, unsigned argc, jsval *vp, bool repeats) {
  jsval* argv = JS_ARGV(cx, vp);
  assert(argc == 2);
  jsval callbackJsval = argv[0];
  assert(JSVAL_IS_OBJECT(callbackJsval));

  jsval intervalJsval = argv[1];
  int intervalMsecs;
  assert(JS_ValueToInt32(cx, intervalJsval, &intervalMsecs) == JS_TRUE);
  assert(intervalMsecs >= 0);

  //SMTimer* timer = 
      //[SMTimer registeredTimerWithCallback:callbackJsval
                                 //jsContext:cx
                                  //interval:intervalMsecs / 1000.0
                                   //repeats:repeats];

  JSObject* timerID = JS_NewObject(cx, &jsTimerIDClass, NULL, NULL);
  //JS_SetPrivate(cx, timerID, timer);

  JS_SET_RVAL(cx, vp, OBJECT_TO_JSVAL(timerID));
  return JS_TRUE;
}

static JSBool jsSetTimeout(JSContext *cx, unsigned argc, jsval *vp)
{
  LOGD("SetTimeout");
  assert(false);

  return jsSetTimer(cx, argc, vp, false);
}

static JSBool jsSetInterval(JSContext *cx, unsigned argc, jsval *vp)
{
  LOGD("SetInterval");
  assert(false);

  return jsSetTimer(cx, argc, vp, true);
}

static JSBool jsClearTimer(JSContext *cx, unsigned argc, jsval *vp)
{
  LOGD("ClearTimer");
  assert(false);

  jsval* argv = JS_ARGV(cx, vp);
  assert(argc == 1);
  jsval timerIDJsval = argv[0];
  if (JSVAL_IS_OBJECT(timerIDJsval) && !JSVAL_IS_NULL(timerIDJsval)) {
    JSObject* timerID = JSVAL_TO_OBJECT(timerIDJsval);

    //SMTimer* timer = 
        //(SMTimer*)JS_GetInstancePrivate(cx, timerID, &jsTimerIDClass, NULL);
    //assert(timer);

    //[timer unregister];
  }

  JS_SET_RVAL(cx, vp, JSVAL_VOID);
  return JS_TRUE;
}

static JSBool jsSetItem(JSContext *cx, unsigned argc, jsval *vp)
{
  LOGD("SetItem");
  assert(false);

  jsval* argv = JS_ARGV(cx, vp);
  assert(argc == 2);
  //NSString* key = stringWithJsval(cx, argv[0]);
  //jsval valueJsval = argv[1];

  //[[NSUserDefaults standardUserDefaults] 
      //setObject:stringWithJsval(cx, valueJsval)
         //forKey:key];

  JS_SET_RVAL(cx, vp, JSVAL_VOID);
  return JS_TRUE;
}

static JSBool jsRemoveItem(JSContext *cx, unsigned argc, jsval *vp)
{
  LOGD("RemoveItem");
  assert(false);

  jsval* argv = JS_ARGV(cx, vp);
  assert(argc == 1);
  //NSString* key = stringWithJsval(cx, argv[0]);

  //[[NSUserDefaults standardUserDefaults] removeObjectForKey:key];

  JS_SET_RVAL(cx, vp, JSVAL_VOID);
  return JS_TRUE;
}

static JSBool jsGetItem(JSContext *cx, unsigned argc, jsval *vp)
{
  LOGD("GetItem");
  assert(false);

  jsval* argv = JS_ARGV(cx, vp);
  assert(argc == 1);
  //NSString* key = stringWithJsval(cx, argv[0]);

  //NSString* value = [[NSUserDefaults standardUserDefaults] objectForKey:key];
  //if (value) {
    //jsval valueJsval =
        //STRING_TO_JSVAL(
            //JS_NewUCStringCopyZ(
                //cx, 
                //(jschar*)[value cStringUsingEncoding:
                    //NSUnicodeStringEncoding]));
    //JS_SET_RVAL(cx, rval, valueJsval);
  //} else {
    //JS_SET_RVAL(cx, rval, JSVAL_VOID);
  //}
  return JS_TRUE;
}

static void reportError(
    JSContext* cx, const char* message, JSErrorReport* report)
{
  LOGD(
      "%s:%u:%s\n",
      report->filename ? report->filename : "<no filename>",
      (unsigned int) report->lineno, message);
}

void reportException(JSContext* cx) {
  if (JS_IsExceptionPending(cx) == JS_TRUE) {
    jsval exception;
    assert(JS_GetPendingException(cx, &exception) == JS_TRUE);
    JS_ReportPendingException(cx);

    JSObject* exceptionObject;
    assert(JS_ValueToObject(cx, exception, &exceptionObject) == JS_TRUE);
    jsval stack;
    assert(JS_GetProperty(cx, exceptionObject, "stack", &stack) == JS_TRUE);
    char *stackTrace = getStringChars(cx, stack);
    LOGD("Stack trace:\n%s", stackTrace);
    JS_free(cx, stackTrace);
  } else {
    assert(false);
  }

  assert(false);
}

static void loadFile(
    JNIEnv* env, jobject obj, jobject assetManager, const char* filename) {
  JSContext* jsContext = getJsContext(env, obj);
  JSObject* jsGlobalObject = getJsGlobalObject(env, obj);

  LOGD("Loading %s", filename);

  AAssetManager* aAssetManager = AAssetManager_fromJava(env, assetManager);
  assert(aAssetManager);

  char* fullPath = (char*)malloc(strlen(filename) + 5);
  assert(fullPath);
  sprintf(fullPath, "www/%s", filename);
  AAsset* asset = 
      AAssetManager_open(aAssetManager, fullPath, AASSET_MODE_UNKNOWN);
  assert(asset);
  free(fullPath);

  off_t length = AAsset_getLength(asset);
  char* code = (char*)malloc(length+1);
  code[length] = 0;

  int bytesRead = AAsset_read(asset, (void*)code, length);
  assert(bytesRead == length);

  AAsset_close(asset);
  JSBool success = JS_EvaluateScript(
      jsContext, jsGlobalObject, code, length, filename, 1, NULL);
  if (success == JS_FALSE) {
    reportException(jsContext);
  }
  
  free(code);
}

static JSClass jsGlobalClass = {
    "global", JSCLASS_GLOBAL_FLAGS,
    JS_PropertyStub, JS_PropertyStub, JS_PropertyStub, JS_StrictPropertyStub,
    JS_EnumerateStub, JS_ResolveStub, JS_ConvertStub, JS_FinalizeStub,
    JSCLASS_NO_OPTIONAL_MEMBERS
};

// See here for more info:
// https://developer.mozilla.org/En/SpiderMonkey/JSAPI_User_Guide
JNIEXPORT void JNICALL 
Java_org_apache_cordova_plugins_truenative_SMRuntime_setupSpiderMonkey(
    JNIEnv *env, jobject obj, 
    jobject assetManager, jobjectArray sourceFilenames)
{
  LOGD("Starting setting up SpiderMonkey");

  JSRuntime* jsRuntime = JS_NewRuntime(8L * 1024L * 1024L);
  assert(jsRuntime);
  setInstanceLong(env, obj, "mJSRuntime", (long)jsRuntime);

  // Create a context. 
  JSContext* jsContext = JS_NewContext(jsRuntime, 8192);
  assert(jsContext);
  setInstanceLong(env, obj, "mJSContext", (long)jsContext);

  JS_SetOptions(jsContext, JSOPTION_VAROBJFIX | JSOPTION_DONT_REPORT_UNCAUGHT);
  JS_SetVersion(jsContext, JSVERSION_LATEST);
  JS_SetErrorReporter(jsContext, reportError);

  // Create the global object.
  JSObject* jsGlobalObject =
      JS_NewCompartmentAndGlobalObject(jsContext, &jsGlobalClass, NULL);
  assert(jsGlobalObject);
  setInstanceLong(env, obj, "mJSGlobalObject", (long)jsGlobalObject);

  // Populate the global object with the standard globals, like object and
  // array. 
  JSBool success = JS_InitStandardClasses(jsContext, jsGlobalObject);
  assert(success);

  // Point 'window' right back at the global object.
  JS_DefineProperty(
      jsContext, jsGlobalObject, "window", OBJECT_TO_JSVAL(jsGlobalObject), 
      NULL, NULL, 0);

  // Add some global functions.
  JS_DefineFunction(
      jsContext, jsGlobalObject, "nativeExec", jsNativeExec, 1, 0);
  JS_DefineFunction(
      jsContext, jsGlobalObject, "setTimeout", jsSetTimeout, 2, 0);
  JS_DefineFunction(
      jsContext, jsGlobalObject, "clearTimeout", jsClearTimer, 1, 0);
  JS_DefineFunction(
      jsContext, jsGlobalObject, "setInterval", jsSetInterval, 2, 0);
  JS_DefineFunction(
      jsContext, jsGlobalObject, "clearInterval", jsClearTimer, 1, 0);

  // Create the console object with log and error functions.
  JSObject* consoleObject = JS_NewObject(jsContext, NULL, NULL, NULL);
  JS_DefineProperty(
      jsContext, jsGlobalObject, "console", OBJECT_TO_JSVAL(consoleObject),
      NULL, NULL, 0);
  JS_DefineFunction(jsContext, consoleObject, "log", jsLog, 1, 0);
  JS_DefineFunction(jsContext, consoleObject, "error", jsLog, 1, 0);

  // Create the localStorage object with setItem and getItem functions.
  JSObject* localStorageObject = JS_NewObject(jsContext, NULL, NULL, NULL);
  JS_DefineProperty(
      jsContext, jsGlobalObject, "localStorage", 
      OBJECT_TO_JSVAL(localStorageObject), NULL, NULL, 0);
  JS_DefineFunction(
      jsContext, localStorageObject, "getItem", jsGetItem, 1, 0);
  JS_DefineFunction(
      jsContext, localStorageObject, "setItem", jsSetItem, 2, 0);
  JS_DefineFunction(
      jsContext, localStorageObject, "removeItem", jsRemoveItem, 1, 0);

  // Create the document object.
  JSObject* documentObject = JS_NewObject(jsContext, NULL, NULL, NULL);
  JS_DefineProperty(
      jsContext, jsGlobalObject, "document", OBJECT_TO_JSVAL(documentObject),
      NULL, NULL, 0);

  // Load all of the source files.
  for (int i = 0; i < env->GetArrayLength(sourceFilenames); ++i) {
    jstring filenameJstring = 
        (jstring)env->GetObjectArrayElement(sourceFilenames, i);
    const char* filename = env->GetStringUTFChars(filenameJstring, NULL);
    assert(filename);

    loadFile(env, obj, assetManager, filename);

    env->ReleaseStringUTFChars(filenameJstring, filename);
  }

  // Set the document readyState to 'loaded'.
  JS_DefineProperty(
      jsContext, documentObject, "readyState",
      STRING_TO_JSVAL(JS_NewStringCopyZ(jsContext, "loaded")), NULL, NULL, 0);

  LOGD("Done setting up SpiderMonkey");
}


JNIEXPORT void JNICALL 
Java_org_apache_cordova_plugins_truenative_SMRuntime_destroy(
    JNIEnv* env, jobject obj) {
  JS_DestroyContext(getJsContext(env, obj));
  JS_DestroyRuntime(getJsRuntime(env, obj));
}

JNIEXPORT jstring JNICALL 
Java_org_apache_cordova_plugins_truenative_SMRuntime_writeJavascript(
    JNIEnv* env, jobject obj, jstring sourceCode) {
  JSContext* jsContext = getJsContext(env, obj);
  JSObject* jsGlobalObject = getJsGlobalObject(env, obj);

  // Convert the jstring into a UTF-8 C string.
  const char* sourceStr = env->GetStringUTFChars(sourceCode, NULL);
  assert(sourceStr);

  // Convert the C string into a jschar string.
  size_t sourceJsLen = 0;
  JSBool success;
  success = JS_DecodeBytes(
      jsContext, sourceStr, strlen(sourceStr), NULL, &sourceJsLen);
  assert(success);
  jschar* sourceJsChar = (jschar *)JS_malloc(jsContext, sourceJsLen);
  assert(sourceJsChar);
  success = JS_DecodeBytes(
      jsContext, sourceStr, strlen(sourceStr), sourceJsChar, &sourceJsLen);
  assert(success);
  env->ReleaseStringUTFChars(sourceCode, sourceStr);

  // Run the source code.
  JS_BeginRequest(jsContext);

  jsval retVal;
  success = JS_EvaluateUCScript(
      jsContext, jsGlobalObject, sourceJsChar, sourceJsLen, 
      "writeJavascript", 1, &retVal);
  if (success == JS_FALSE) {
    reportException(jsContext);
  }

  jstring resultString = convertToJstring(env, obj, retVal);

  JS_free(jsContext, sourceJsChar);
  JS_EndRequest(jsContext);

  return resultString;
}

//- (void)runGC
//{
  //JS_GC(jsContext_);
//}
