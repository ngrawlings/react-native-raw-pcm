package com.reactnativerawpcm.sound;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder.AudioSource;

import com.reactnativerawpcm.utils.ByteUtils;
import com.reactnativerawpcm.utils.ShortUtils;

import android.util.Log;

public class Capture {

    public interface Events {
        void onPCMData(Capture inst, short[] pcm);
    }

    protected AudioRecord recorder;
    protected boolean is_recording = false;
    protected Events events;

    private final int bufferSize;

    public Capture(Events events) {
        this.events = events;

        bufferSize = AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        recorder = new AudioRecord(AudioSource.MIC, 44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize*4);
    }

    protected void finalize() {
        recorder.stop();
    }

    public void start() {
        is_recording = true;
        recorder.startRecording();

        Thread recordingThread = new Thread(new Runnable() {
            public void run() {
                try {
                    int read;
                    short[] buffer = new short[bufferSize];

                    while (is_recording) {
                        read = recorder.read(buffer, 0, bufferSize);
                        if (read > 0) {
                            events.onPCMData(Capture.this, buffer);
                        }
                    }
                    recorder.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        recordingThread.start();
    }

    public void stop() {
        is_recording = false;
    }

}
