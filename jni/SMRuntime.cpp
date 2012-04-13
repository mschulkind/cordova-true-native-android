#include "SMRuntime.h"

#include "org_apache_cordova_plugins_truenative_SMRuntime.h"

#include <android/log.h>
#include <assert.h>
#include <jni.h>
#include <string.h>

#include <jsapi.h>

#define  LOG_TAG    "JNI_org_apache_cordova_plugins_truenative_SMRuntime"
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)

static JSBool jsLog(JSContext *cx, unsigned argc, jsval *vp) {
  assert(argc == 1);

  //NSLog(@"%@", stringWithJsval(cx, argv[0]));

  JS_SET_RVAL(cx, vp, JSVAL_VOID);
  return JS_TRUE;
}

static JSBool jsNativeExec(JSContext *cx, unsigned argc, jsval *vp) {
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

static JSBool jsSetTimer(JSContext* cx, unsigned argc, jsval *vp, bool repeats) {
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
  return jsSetTimer(cx, argc, vp, false);
}

static JSBool jsSetInterval(JSContext *cx, unsigned argc, jsval *vp)
{
  return jsSetTimer(cx, argc, vp, true);
}

static JSBool jsClearTimer(JSContext *cx, unsigned argc, jsval *vp)
{
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
  jsval* argv = JS_ARGV(cx, vp);
  assert(argc == 1);
  //NSString* key = stringWithJsval(cx, argv[0]);

  //[[NSUserDefaults standardUserDefaults] removeObjectForKey:key];

  JS_SET_RVAL(cx, vp, JSVAL_VOID);
  return JS_TRUE;
}

static JSBool jsGetItem(JSContext *cx, unsigned argc, jsval *vp)
{
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
  //NSLog(
      //@"%s:%u:%s\n",
      //report->filename ? report->filename : "<no filename>",
      //(unsigned int) report->lineno, message);
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
    //NSLog(@"Stack trace:\n%@", stringWithJsval(cx, stack));
  } else {
    assert(false);
  }

  assert(false);
}

static JSClass jsGlobalClass = {
    "global", JSCLASS_GLOBAL_FLAGS,
    JS_PropertyStub, JS_PropertyStub, JS_PropertyStub, JS_StrictPropertyStub,
    JS_EnumerateStub, JS_ResolveStub, JS_ConvertStub, JS_FinalizeStub,
    JSCLASS_NO_OPTIONAL_MEMBERS
};

static void loadFile(char* filename) {
  LOGD("Loading %@", filename);

  //NSString* filePath =
      //[[NSBundle mainBundle] pathForResource:
          //[NSString stringWithFormat:@"www/%@", filename] ofType:nil];  
  //NSData *jsData = [NSData dataWithContentsOfFile:filePath];  
  //assert(jsData);

  //JSBool success = JS_EvaluateScript(
      //jsContext_, jsGlobalObject_, (const char*)[jsData bytes],
      //[jsData length], 
      //[filename cStringUsingEncoding:NSASCIIStringEncoding], 1, NULL);
  //if (success == JS_FALSE) {
    //reportException(jsContext_);
  //}
}

// See here for more info:
// https://developer.mozilla.org/En/SpiderMonkey/JSAPI_User_Guide
JNIEXPORT void JNICALL 
Java_org_apache_cordova_plugins_truenative_SMRuntime_setupSpiderMonkey(
    JNIEnv *, jobject, jobjectArray)
{
  LOGD("Starting setting up SpiderMonkey");

  JSRuntime* jsRuntime = JS_NewRuntime(64L * 1024L * 1024L);
  assert(jsRuntime);

  // Create a context. 
  JSContext* jsContext = JS_NewContext(jsRuntime, 8192);
  assert(jsContext);

  JS_SetOptions(
      jsContext, 
      //JS_GetOptions(jsContext)
      JSOPTION_VAROBJFIX | JSOPTION_COMPILE_N_GO 
      | JSOPTION_DONT_REPORT_UNCAUGHT);
      //| JSVERSION_LATEST);
  JS_SetVersion(jsContext, JSVERSION_LATEST);
  //JS_ToggleOptions(jsContext, JSOPTION_XML);
  JS_SetErrorReporter(jsContext, reportError);

  JS_BeginRequest(jsContext);

  // Create the global object.
  JSObject* jsGlobalObject =
      JS_NewObject(jsContext, &jsGlobalClass, NULL, NULL);
  assert(jsGlobalObject);
  JS_SetGlobalObject(jsContext, jsGlobalObject);

  // Populate the global object with the standard globals, like object and
  // array. 
  assert(JS_InitStandardClasses(jsContext, jsGlobalObject));

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

  //[self loadBuiltinJavascript];

  //if (sourceFiles) {
    //for (NSString* sourceFile in sourceFiles) {
      //[self loadJavascriptFile:sourceFile];
    //}
  //}

  // Set the document readyState to 'loaded'.
  JS_DefineProperty(
      jsContext, documentObject, "readyState",
      STRING_TO_JSVAL(JS_NewStringCopyZ(jsContext, "loaded")), NULL, NULL, 0);

  JS_EndRequest(jsContext);

  LOGD("Done setting up SpiderMonkey");
}

static long getInstanceLong(JNIEnv* env, jobject obj, const char* name) {
  jfieldID fid;
  jobject jobj;
  const char *str;

  jclass cls = env->GetObjectClass(obj);

  fid = env->GetFieldID(cls, name, "J");
  assert(fid);

  return env->GetLongField(obj, fid);
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

JNIEXPORT void JNICALL 
Java_org_apache_cordova_plugins_truenative_SMRuntime_destroy(
    JNIEnv* env, jobject obj) {
  JS_DestroyContext(getJsContext(env, obj));
  JS_DestroyRuntime(getJsRuntime(env, obj));
}

jstring convertToJstring(JNIEnv* env, jobject obj, jsval stringJsval) {
  JSContext* jsContext = getJsContext(env, obj);

  JSString* jsString = JS_ValueToString(jsContext, stringJsval);

  char* str = JS_EncodeString(jsContext, jsString);
  assert(str);

  jstring jString = env->NewStringUTF(str);

  JS_free(jsContext, str);

  return jString;
}

JNIEXPORT jstring JNICALL 
Java_org_apache_cordova_plugins_truenative_SMRuntime_writeJavascript(
    JNIEnv* env, jobject obj, jstring sourceCode) {
  JSContext* jsContext = getJsContext(env, obj);
  JSObject* jsGlobalObject = getJsGlobalObject(env, obj);

  // Convert the jstring into a UTF-8 C string.
  const char* sourceStr = env->GetStringUTFChars(sourceCode, NULL);
  assert(str);

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
