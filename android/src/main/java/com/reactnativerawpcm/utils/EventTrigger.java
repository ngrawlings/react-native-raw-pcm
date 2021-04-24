package com.reactnativerawpcm.utils;

import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class EventTrigger {

  protected ReactApplicationContext reactContext;
  private static EventTrigger instance = null;

  private EventTrigger(ReactApplicationContext reactContext) {
    this.reactContext = reactContext;
  }

  public static EventTrigger getInstance(ReactApplicationContext reactContext) {
    if (instance == null)
      instance = new EventTrigger(reactContext);

    return instance;
  }

  public static EventTrigger getInstance() {
    return instance;
  }

  public void emit(String eventName, Object param) {
    try {
      reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, param);
    } catch (RuntimeException e) {
      Log.e(eventName, "java.lang.RuntimeException: Trying to invoke JS before CatalystInstance has been set!", e);
    }
  }

}
