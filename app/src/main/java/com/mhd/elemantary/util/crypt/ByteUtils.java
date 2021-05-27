package com.mhd.elemantary.util.crypt;

public class ByteUtils {

	public static String toHexString(byte[] bytes) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			//sb.append(Integer.toHexString(0xFF & bytes[i]));
			sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
		}

		return sb.toString();
	}
}
