package com.mhd.elemantary.util.crypt;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class SimpleAES {
	
	private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
	//private static final String iv = "a2b71f6e7f89750a";

	public static String encrypt(String key, String text) throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, Exception {
		String hashKey = HashUtils.md5AsString(key);
		Key encryptKey = generateKey(hashKey.substring(0, 16));
		IvParameterSpec ivParameterSpec = generateIvParameterSpec(hashKey.substring(16));

		byte[] enc = aesEncode(text.getBytes(), encryptKey, ivParameterSpec);

		return byte2hex(enc);
	}

	public static String decrypt(String key, String text) throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, Exception {
		String hashKey = HashUtils.md5AsString(key);
		Key decryptKey = generateKey(hashKey.substring(0, 16));
		IvParameterSpec ivParameterSpec = generateIvParameterSpec(hashKey.substring(16));
		
		byte[] enc = hex2byte(text);
		byte[] dec = aesDecode(enc, decryptKey, ivParameterSpec);

		return new String(dec);
	}

	private static String byte2hex(byte[] b) {
		StringBuffer hs = new StringBuffer();
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1) {
				hs.append("0").append(stmp);
			} else {
				hs.append(stmp);
			}
		}
		return hs.toString();
	}

	private static byte[] hex2byte(String hex) {
		if (hex == null || hex.length() == 0) {
			return null;
		}

		byte[] ba = new byte[hex.length() / 2];
		for (int i = 0; i < ba.length; i++) {
			ba[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}
		return ba;
	}
	
	private static String padding16(String str) {
		if(str.length() < 16) {
			int nCount = 16 - (str.length() % 16);
			for(int i=0; i<nCount; i++){
				str += ' ';
			}
			return str;
		} else {
			return str.substring(0, 16);
		}
	}
	
	private static IvParameterSpec generateIvParameterSpec(String iv) {
		byte[] ivByte = iv.getBytes();
		return new IvParameterSpec(ivByte);
	}

	private static Key generateKey(String keyData) throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException {
		final byte[] keyByte = padding16(keyData).getBytes();
		SecretKeySpec keySpec = new SecretKeySpec(keyByte, "AES");
		return keySpec;
	}

	public static byte[] aesEncode(byte[] src, Key key, IvParameterSpec ivParameterSpec) {
		try {
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);
			return cipher.doFinal(src);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static byte[] aesDecode(byte[] src, Key key, IvParameterSpec ivParameterSpec) {
		try {
		
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
			return cipher.doFinal(src);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
