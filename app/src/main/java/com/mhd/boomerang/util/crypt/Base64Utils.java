package com.mhd.boomerang.util.crypt;

import android.util.Base64;
import java.io.IOException;


public class Base64Utils {
	
	/**
	 * From a base 64 representation, returns the corresponding byte[]
	 * @param data String The base64 representation
	 * @return byte[]
	 * @throws IOException
	 */
	public static byte[] base64ToByte(String data) throws IOException {
		return Base64.decode(data, Base64.DEFAULT);
	}
	
	/**
	 * From a byte[] returns a base 64 representation
	 * @param data byte[]
	 * @return String
	 * @throws IOException
	 */
	public static String byteToBase64(byte[] data) {
		return Base64.encodeToString(data, Base64.DEFAULT);
	}
	
}
