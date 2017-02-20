package permission.common;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * Md5加密
 * com.yhj.common.encrypt.Md5Encrypt.java
 * @author 余焕军 创建于 2010-8-29 下午03:43:34
 */
public class Md5Encrypt {
	/**
	 * Used building output as Hex
	 */
	private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * 对字符串进行MD5加密
	 * @param text 明文
	 * @param charSet 字符编码
	 * @return 密文
	 */
	public  static final String encrypt(String text, String charSet) {
		MessageDigest msgDigest = null;
		try {
			msgDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("System doesn't support MD5 algorithm.");
		}
		//charSet为null时，不重新编码
		if(charSet != null){
			try {
				msgDigest.update(text.getBytes(charSet)); //注意该接口是按照utf-8编码形式加密
			} catch (UnsupportedEncodingException e) {
				throw new IllegalStateException("System doesn't support your  EncodingException.");
			}
		}
		
		byte[] bytes = msgDigest.digest();
		return new String(encodeHex(bytes));
	}
	
	/**
	 * 对字符串进行MD5加密  默认为UTF-8编码
	 * @param text 明文
	 * @return 密文
	 */
	public  static final String encrypt(String text) {
		return encrypt(text, "UTF-8");
	}

	private static final char[] encodeHex(byte[] data) {
		int l = data.length;
		char[] out = new char[l << 1];
		// two characters form the hex value.
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
			out[j++] = DIGITS[0x0F & data[i]];
		}
		return out;
	}
	
	public static void main(String[] args) {
		System.out.println(Md5Encrypt.encrypt("admin"));
	}
}