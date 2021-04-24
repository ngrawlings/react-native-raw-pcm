package com.reactnativerawpcm;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;

import java.nio.ByteBuffer;

import com.reactnativerawpcm.sound.Capture;
import com.reactnativerawpcm.sound.Playback;
import com.reactnativerawpcm.utils.EventTrigger;

import android.util.Base64;
import android.util.Log;

@ReactModule(name = RawPcmModule.NAME)
public class RawPcmModule extends ReactContextBaseJavaModule {
    public static final String NAME = "RawPcm";

    protected Capture capture;
    protected Playback playback;

    public RawPcmModule(ReactApplicationContext reactContext) {
        super(reactContext);
    
        EventTrigger.getInstance(reactContext);
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @ReactMethod
    public void record(Promise promise) {
        capture = new Capture(new Capture.Events() {

            @Override
            public void onPCMData(Capture inst, short[] pcm) {
                ByteBuffer bb = ByteBuffer.allocate(pcm.length*2);
                for (int i=0; i<pcm.length; i++)
                  bb.putShort(pcm[i]).array();

                String b64pcm = Base64.encodeToString(bb.array(), Base64.NO_WRAP);
                  
                EventTrigger.getInstance().emit("data", b64pcm);
            }
    
          });
        capture.start();

        promise.resolve(true);
    }

    @ReactMethod
    public void stop(Promise promise) {
        capture.stop();
        promise.resolve(true);
    }

    @ReactMethod
    public void playback(String data, Promise promise) {
        if (playback == null)
            playback = new Playback();

        byte[] base64Data = Base64.decode(data, Base64.DEFAULT);

        ByteBuffer bb = ByteBuffer.allocate(base64Data.length);
        bb.put(base64Data, 0, base64Data.length);
        bb.position(0);
        
        short[] ret = new short[base64Data.length/2];
        for (int i=0; i<ret.length; i++) {
            ret[i] = bb.getShort();
        }

        playback.write(ret);

        promise.resolve(true);
    }

}
