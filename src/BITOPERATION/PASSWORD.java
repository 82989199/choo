package BITOPERATION;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class PASSWORD {

	//TODO 암호화 진행
	public static String _PASSWORD = "1004";
	
	public static void main(String[] args) {
		SHA256(_PASSWORD);
		System.out.println("암호화 전 : " + _PASSWORD + "");
	}
	
	public static String SHA256(String str) {

		String SHA = "";

		try {

			MessageDigest sh = MessageDigest.getInstance("SHA-256");

			sh.update(str.getBytes());

			byte byteData[] = sh.digest();

			StringBuffer sb = new StringBuffer();

			for (int i = 0; i < byteData.length; i++) {

				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));

			}

			SHA = sb.toString();

			System.out.println("加密后 : " + SHA + "");

		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();

			SHA = null;

		}
		return SHA;

	}
}
