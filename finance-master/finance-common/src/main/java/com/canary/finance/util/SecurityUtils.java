package com.canary.finance.util;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class SecurityUtils {
	private static PrivateKey privateKey;
	private static PublicKey publicKey;
	
	static {
		  try {
	            java.security.Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	        }
	        catch (Exception e) {
	        	e.printStackTrace();
	        	System.out.println("密钥初始化失败");
	        }
	}
	/**
	 *  init:初始化私钥
	 */
	public static void initPrivateKey(){
		try {
				if(privateKey==null){
					privateKey = getPrivateKey();
				}
		} catch (Exception e) {
			System.out.println("SecurityUtils初始化失败" + e.getMessage());
			e.printStackTrace();
			System.out.println("密钥初始化失败");
		}
	}
	/**
	 * 初始化公钥
	 */
	public static void initPublicKey(){
		try {
			if(publicKey==null){
				publicKey = getPublicKey();
			}
		} catch (Exception e) {
			System.out.println("SecurityUtils初始化失败" + e.getMessage());
			e.printStackTrace();
			System.out.println("密钥初始化失败");
		}
	}
	/**
	 * 对传入字符串进行签名
	 * @param inputStr
	 * @return
	 * @ 
	 */
	public static String sign(String inputStr) {
		String result = null;
		  try {
			    if(privateKey==null){
			    	//初始化
			    	initPrivateKey();
			    }
	            byte[] tByte;
	            Signature signature = Signature.getInstance("SHA1withRSA","BC");
	            signature.initSign(privateKey);
	            signature.update(inputStr.getBytes("UTF-8"));
	            tByte = signature.sign();
	            result = Base64.encode(tByte);
	        }
	        catch (Exception e) {
	        	e.printStackTrace();
	        	System.out.println("密钥初始化失败");
	        }
		return result;
	}
	/**
	 * 对富友返回的数据进行验签
	 * @param src 返回数据明文
	 * @param signValue 返回数据签名
	 * @return
	 */
	public static boolean verifySign(String src,String signValue) {
		  boolean bool = false;
		  try {
			  	if(publicKey==null){
			  		initPublicKey();
				}
	            Signature signature = Signature.getInstance("SHA1withRSA","BC");
	            signature.initVerify(publicKey);
	            signature.update(src.getBytes("UTF-8"));
	            bool = signature.verify(Base64.decode(signValue));
	        }
	        catch (Exception e) {
	        	e.printStackTrace();
	        	System.out.println("密钥初始化失败");
	        }
		return bool;
	}
	private static PrivateKey getPrivateKey() {
		InputStream in = SecurityUtils.class.getResourceAsStream("/rsa/prkey.key");
		String base64edKey = readFile(in);
		KeyFactory kf;
		PrivateKey privateKey = null;
		try {
			kf = KeyFactory.getInstance("RSA", "BC");
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decode(base64edKey));
			privateKey = kf.generatePrivate(keySpec);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("密钥初始化失败");
		}
		return privateKey;
	}
	private static PublicKey getPublicKey(){
		InputStream in = SecurityUtils.class.getResourceAsStream("/rsa/pbkey.key");
		String base64edKey = readFile(in);
		KeyFactory kf;
		PublicKey   publickey = null;
		try {
			kf = KeyFactory.getInstance("RSA", "BC"); 
			X509EncodedKeySpec   keySpec   =   new   X509EncodedKeySpec(Base64.decode(base64edKey));
			publickey   =   kf.generatePublic(keySpec);   
		 } catch (Exception e) {
			e.printStackTrace();
			System.out.println("密钥初始化失败");
		}
		return publickey;
	}
	private static String readFile(InputStream in) {
      try {
      	//File f = new File(fileName);
          //FileInputStream in = new FileInputStream(f);
          //int len = (int)f.length();
          int len = in.available();
          byte[] data = new byte[len];
          int read = 0;
          while (read <len) {
              read += in.read(data, read, len-read);
          }
          in.close();
          return new String(data);
      } catch (IOException e) {
          return null;
      }
  }
}
