package com.mhd.stard.util.crypt;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;


public class AuthUtil {
	
//	private static final Logger logger = Logger.getLogger("AuthUtil");
	
	private static final String SALT = "scard!2016@";
	
	/**
	 * Hash
	 * @param param1
	 * @param param2
	 * @param param3
	 * @param param4
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 */
	public static String getSha256(String param1, long param2, String param3, String param4) throws Exception {
		String result = null;
		
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(param1);
			sb.append(param2);
			sb.append(SALT);
			sb.append(param3);
			sb.append(param4);
			result = HashUtils.sha256AsString(sb.toString());
		} catch (Exception e) {
			throw e;
		}
		return result;
	}
	
	
}
