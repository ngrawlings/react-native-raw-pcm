package com.reactnativerawpcm.sound;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Base64;
import android.util.Log;

import com.reactnativerawpcm.utils.EventTrigger;

import java.nio.ByteBuffer;

public class Playback {

    protected AudioTrack audio;
    protected int buffer_size = 0;
    protected int written_to_stream = 0;

    public Playback() {
        buffer_size = AudioTrack.getMinBufferSize(44100,
           AudioFormat.CHANNEL_OUT_MONO,
           AudioFormat.ENCODING_PCM_16BIT);

        audio = new AudioTrack(AudioManager.STREAM_MUSIC,
                            44100,
                            AudioFormat.CHANNEL_OUT_MONO,
                            AudioFormat.ENCODING_PCM_16BIT,
                            buffer_size,
                            AudioTrack.MODE_STREAM );

        audio.play();
    }

    protected void finalize() {
        audio.stop();
    }

    public int write(short[] pcm) {
      ByteBuffer b = ByteBuffer.allocate(pcm.length*2);
      for (int i=0; i<pcm.length; i++) {
        b.putShort(pcm[i]);
      }
      String base64Data = Base64.encodeToString(b.array(), Base64.NO_WRAP);
      EventTrigger.getInstance().emit("pcm", base64Data);

      int written = audio.write(pcm, 0, pcm.length, AudioTrack.WRITE_BLOCKING);
      written_to_stream += written;
      return written;
    }

    public int getBufferSize() {
      return 0;
    }

    public int getAvailableBufferSize() {
      int playback_pos = audio.getPlaybackHeadPosition();
      return playback_pos - written_to_stream;
    }

}
