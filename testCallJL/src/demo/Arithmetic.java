package demo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Arithmetic {

	/**
	 * base64编码
	 * 
	 * @param str
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String encode(String str) throws UnsupportedEncodingException {
		String target = null;
		if (str != null) {
			target = new String(Base64_T.encode(str.getBytes("GBK")));

		}
		return target;
	}

	/**
	 * base64编码
	 * 
	 * @param str
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String encode(byte[] str) throws UnsupportedEncodingException {
		String target = null;
		if (str != null) {
			target = new String(Base64_T.encode(str));
		}
		return target;
	}

	/**
	 * base64解码
	 * 
	 * @param str
	 * @return
	 * @throws IOException
	 */
	public static String decode(String str) throws IOException {
		byte[] dec = Base64_T.decode(str.getBytes());
		return new String(dec, "GBK");
		
	}

	/**
	 * base64解码
	 * 
	 * @param str
	 * @return
	 * @throws IOException
	 */
	public static byte[] decodeForByte(String str) {
		try {
			return Base64_T.decode(str.getBytes());
		} catch (Exception e) {
//			throw new EVoucherException("BASE64解码失败", e);
			return null;
		}
	}

	/**
	 * 把数组转化为字符串格式：如：{"1","2","3"} ---> "1,2,3"
	 * 
	 * @param array
	 * @return
	 */
	public static String array2String(String[] array) {
		StringBuffer voucherNos = new StringBuffer();
		if (array.length > 0) {
			for (int i = 0; i < array.length; i++) {
				voucherNos.append("'");
				voucherNos.append(array[i]).append("',");
			}
		} else {
			System.out.println("字符串数组为空，不能转化为字符串！");
		}
		return voucherNos.substring(0, voucherNos.length() - 1);
	}

}
