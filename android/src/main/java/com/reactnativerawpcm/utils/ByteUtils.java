package com.reactnativerawpcm.utils;

import java.nio.ByteBuffer;

public class ByteUtils {

	public static byte[] concat(byte[] b1, byte[] b2) {
		byte[] ret = new byte[b1.length+b2.length];

    System.arraycopy(b1, 0, ret, 0, b1.length);
    System.arraycopy(b2, 0, ret, b1.length, b2.length);

		return ret;
	}

	public static void setBytes(byte[] buffer, byte value, int offset, int length) {
		if (offset+length > buffer.length)
			length = buffer.length - offset;

		for (int i=0; i<length; i++) {
			buffer[offset+i] = value;
		}
	}

	public static byte[] subBytes(byte[] buffer, int offset, int length) {

		if (offset+length > buffer.length)
			length = buffer.length - offset;

		byte[] ret = new byte[length];

    if (length >= 0) System.arraycopy(buffer, offset, ret, 0, length);

		return ret;
	}

	public static boolean copyBytes(byte[] dest, byte[] src, int offset) {
		if (dest.length >= src.length+offset) {
      System.arraycopy(src, 0, dest, offset, src.length);
      return true;
		}
		return false;
	}

	public static int bytesToInt(byte[] b) {
		int l = 0;

	    l |= b[0] & 0xFF;
	    if (b.length>1) {
	    	l <<= 8;
		    l |= b[1] & 0xFF;
		    if (b.length>2) {
			    l <<= 8;
			    l |= b[2] & 0xFF;
			    if (b.length>3) {
				    l <<= 8;
				    l |= b[3] & 0xFF;
			    }
		    }
	    }

	    return l;
	}

	public static byte[] intToBytes(int val) {
		return ByteBuffer.allocate(4).putInt(val).array();
	}

	public static String bytesToHex(byte[] bytes) {
		char[] hexArray = "0123456789ABCDEF".toCharArray();
	    char[] hexChars = new char[bytes.length * 2];

	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}

	public static byte[] hexToBytes(String s) {
		int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}

	public static int findBytes(byte[] buffer, byte[] sequence) {
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

	public static byte[] trimBytes(byte[] buffer) {
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

		return subBytes(buffer, s, len-e-s);
	}

}
