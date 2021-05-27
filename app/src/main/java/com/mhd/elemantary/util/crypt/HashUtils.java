/**
 * 
 */
package com.mhd.elemantary.util.crypt;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class HashUtils {

	public static final String ALGORITHM_SHA256	= "SHA-256";
	public static final String ALGORITHM_MD5		= "MD5";

	private static final String DEFAULT_CHARSET		= "UTF-8";

	/**
	 * Hash
	 * 
	 * @param algorithm hash 알고리즘
	 * @param input 입력값
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[] hash(String algorithm, byte[] input) throws NoSuchAlgorithmException {
		byte[] digest = null;

		MessageDigest md;
		md = MessageDigest.getInstance(algorithm);
		md.reset();
		md.update(input);
		digest = md.digest();

		return digest;
	}

	/**
	 * MD5 Hash
	 * 
	 * @param input 입력값
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[] md5(byte[] input) throws NoSuchAlgorithmException {
		return hash(ALGORITHM_MD5, input);
	}

	/**
	 * MD5 Hash
	 * 
	 * @param input 입력값
	 * @param charset 캐릭터셋
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[] md5(String input, String charset) throws UnsupportedEncodingException,
            NoSuchAlgorithmException {
		byte[] inputBytes = input.getBytes(charset);
		return md5(inputBytes);
	}

	/**
	 * MD5 Hash
	 * (Default Charset : UTF-8)
	 * 
	 * @param input 입력값
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[] md5(String input) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		return md5(input, DEFAULT_CHARSET);
	}

	/**
	 * MD5 Hash
	 * 
	 * @param input 입력값
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 */
	public static String md5AsString(byte[] input) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		byte[] output = md5(input);
		return ByteUtils.toHexString(output);
	}

	/**
	 * MD5 Hash
	 * 
	 * @param input 입력값
	 * @param charset 캐릭터셋
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 */
	public static String md5AsString(String input, String charset) throws UnsupportedEncodingException,
            NoSuchAlgorithmException {
		byte[] output = md5(input, charset);
		return ByteUtils.toHexString(output);
	}

	/**
	 * MD5 Hash
	 * (Default Charset : UTF-8) 
	 * 
	 * @param input 입력값
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 */
	public static String md5AsString(String input) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		return md5AsString(input, DEFAULT_CHARSET);
	}

	/**
	 * SHA256 Hash
	 * 
	 * @param input 입력값
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[] sha256(byte[] input) throws NoSuchAlgorithmException {
		return hash(ALGORITHM_SHA256, input);
	}

	/**
	 * SHA256 Hash
	 * 
	 * @param input 입력값
	 * @param charset 캐릭터셋
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[] sha256(String input, String charset) throws UnsupportedEncodingException,
            NoSuchAlgorithmException {
		byte[] inputBytes = input.getBytes(charset);
		return sha256(inputBytes);
	}

	/**
	 * SHA256 Hash
	 * 
	 * @param input 입력값
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[] sha256(String input) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		return sha256(input, DEFAULT_CHARSET);
	}

	/**
	 * SHA256 Hash
	 * 
	 * @param input 입력값
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 */
	public static String sha256AsString(byte[] input) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		byte[] output = sha256(input);
		return ByteUtils.toHexString(output);
	}

	/**
	 * SHA256 Hash
	 * 
	 * @param input 입력값
	 * @param charset 캐릭터셋
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 */
	public static String sha256AsString(String input, String charset) throws UnsupportedEncodingException,
            NoSuchAlgorithmException {
		byte[] output = sha256(input, charset);
		return ByteUtils.toHexString(output);
	}

	/**
	 * SHA256 Hash
	 * 
	 * @param input 입력값
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 */
	public static String sha256AsString(String input) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		return sha256AsString(input, DEFAULT_CHARSET);
	}

}
