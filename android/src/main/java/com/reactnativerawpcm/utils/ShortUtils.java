package com.reactnativerawpcm.utils;

import java.nio.ByteBuffer;

public class ShortUtils {

  public static byte[] shortToBytes(short val) {
    return ByteBuffer.allocate(2).putShort(val).array();
  }

  public static byte[] shortsToBytes(short[] val) {
    ByteBuffer bb = ByteBuffer.allocate(val.length*2);
    for (int i=0; i<val.length; i++)
      bb.putShort(val[i]).array();
    return bb.array();
  }

  public static short bytesToShort(byte[] val) {
		return ByteBuffer.allocate(2).put(val, 0, 2).getShort();
	}

  public static short[] bytesToShortArray(byte[] val) {
    ByteBuffer bb = ByteBuffer.allocate(val.length);
    bb.put(val, 0, val.length);
    bb.position(0);

    short[] ret = new short[val.length/2];
    for (int i=0; i<ret.length; i++) {
      ret[i] = bb.getShort();
    }

    return ret;
  }

  public static boolean copy(short[] dest, short[] src, int offset) {
    if (dest.length >= src.length+offset) {
      System.arraycopy(src, 0, dest, offset, src.length);
      return true;
    }
    return false;
  }

  public static short[] subShorts(short[] buffer, int offset, int length) {
    if (offset+length > buffer.length)
      length = buffer.length - offset;

    short[] ret = new short[length];

    if (length >= 0) System.arraycopy(buffer, offset, ret, 0, length);

    return ret;
  }

  public static int find(short[] buffer, short[] sequence) {
    int len = buffer.length-sequence.length;
    for (int i=0; i<len; i++) {
      boolean found = true;
      for (int x=0; x<sequence.length; x++) {
        if (buffer[i+x] != sequence[x]) {
          found = false;
          break;
        }
      }
      if (found)
        return i;
    }
    return -1;
  }

  public static short[] trim(short[] buffer) {
    int len = buffer.length;
    int s, e;
    for (s=0; s<len; s++) {
      if (buffer[s] != 0)
        break;
    }
    for (e=0; e<len; e++) {
      if (buffer[len-e-1] != 0)
        break;
    }
    e = buffer.length-e;

    return subShorts(buffer, s, e);
  }

}
