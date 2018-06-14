package com.canary.finance.util;

import static com.canary.finance.util.ConstantUtil.AES;
import static com.canary.finance.util.ConstantUtil.AND;
import static com.canary.finance.util.ConstantUtil.CBC_PADDING;
import static com.canary.finance.util.ConstantUtil.COLON;
import static com.canary.finance.util.ConstantUtil.EMPTY;
import static com.canary.finance.util.ConstantUtil.EQUAL;
import static com.canary.finance.util.ConstantUtil.KEY;
import static com.canary.finance.util.ConstantUtil.MD5_WITH_RSA;
import static com.canary.finance.util.ConstantUtil.RSA;
import static com.canary.finance.util.ConstantUtil.SIGN;
import static com.canary.finance.util.ConstantUtil.SIGN_TYPE;
import static com.canary.finance.util.ConstantUtil.UTF_8;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import com.canary.finance.enumeration.SignTypeEnum;

public class DigestSignUtil {
    private static final DigestSignUtil INSTANCE = new DigestSignUtil();

    private DigestSignUtil() {

    }

    public static DigestSignUtil getInstance() {
        return INSTANCE;
    }
    
    public static String encodeBytes(byte[] bytes) {
		StringBuilder buffer = new StringBuilder();
		for (int i=0; i<bytes.length; i++) {
			buffer.append((char) (((bytes[i] >> 4) & 0xF) + ((int) 'a')));
			buffer.append((char) (((bytes[i]) & 0xF) + ((int) 'a')));
		}

		return buffer.toString();
	}
	
	public static byte[] decodeBytes(String str) {
		byte[] bytes = new byte[str.length() / 2];
		for (int i = 0; i < str.length(); i += 2) {
			char c = str.charAt(i);
			bytes[i / 2] = (byte) ((c - 'a') << 4);
			c = str.charAt(i + 1);
			bytes[i / 2] += (c - 'a');
		}
		return bytes;
	}
    
    public static String signAES(String encodeData ,String secretKey, String vector) throws Exception {
    	if(secretKey == null || secretKey.length() != 16) {
			return null;
		}
		
		byte[] raw = secretKey.getBytes();
		SecretKeySpec skeySpec = new SecretKeySpec(raw, AES);
		Cipher cipher = Cipher.getInstance(CBC_PADDING);
		IvParameterSpec iv = new IvParameterSpec(vector.getBytes());
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
		byte[] encrypted = cipher.doFinal(encodeData.getBytes());
		return encodeBytes(encrypted);
    }
    
    public static String signMD5(String signSource) {
    	return DigestUtils.md5Hex(signSource);
    }

    public static String signRSA(String signSource, String privateKey) throws Exception {
            PKCS8EncodedKeySpec privatePKCS8 = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            PrivateKey myPrivateKey = keyFactory.generatePrivate(privatePKCS8);
            Signature signature = Signature.getInstance(MD5_WITH_RSA);
            signature.initSign(myPrivateKey);
            signature.update(signSource.getBytes(UTF_8));
            byte[] signed = signature.sign(); 
            return new String(Base64.encodeBase64(signed));
    }
    
    public static String sign(Map<String, Object> signSourceMap, String privateKey, String md5Key) throws Exception {
    	if(signSourceMap != null && signSourceMap.size() > 0) {
    		String[] signTypeArray = StringUtils.split(SIGN_TYPE, COLON);
    		if(signTypeArray != null && signTypeArray.length == 2) {
    			Object signObject = signSourceMap.get(signTypeArray[0]);
    			if(signObject == null) {
    				signObject = signSourceMap.get(signTypeArray[1]);
    			}
    			String signType = signObject == null ? "" : signObject.toString();
    			if(StringUtils.equals(signType, SignTypeEnum.MD5.getCode())) {
    				return addSignMD5(signSourceMap, md5Key);
    			} else if(StringUtils.equals(signType, SignTypeEnum.RSA.getCode())) {
    				return addSignRSA(signSourceMap, privateKey);
    			} else {
    				//do nothing.
    			}
    		}
    	}
    	
    	return null;
    }
    
    public static boolean checkSign(Map<String, Object> signSourceMap, String publicKey, String md5Key) throws Exception {
    	if(signSourceMap != null && signSourceMap.size() > 0) {
    		String[] signTypeArray = StringUtils.split(SIGN_TYPE, COLON);
    		if(signTypeArray != null && signTypeArray.length == 2) {
    			Object signObject = signSourceMap.get(signTypeArray[0]);
    			if(signObject == null) {
    				signObject = signSourceMap.get(signTypeArray[1]);
    			}
    			String signType = signObject == null ? "" : signObject.toString();
    			if(StringUtils.equals(signType, SignTypeEnum.MD5.getCode())) {
    				return checkSignMD5(signSourceMap, md5Key);
    			} else {
    				return checkSignRSA(signSourceMap, publicKey);
    			}
    		}
    	}
    	return false;
    }
    
    public static String generateSignData(Map<String, Object> map) {
		StringBuilder content = new StringBuilder();
		List<String> keys = new ArrayList<String>(map.keySet());
		Collections.sort(keys, String.CASE_INSENSITIVE_ORDER);
		for(String key : keys) {
			Object object = map.get(key);
			String value = object == null ? "" : object.toString();
			if (StringUtils.equals(SIGN, key) || StringUtils.isBlank(value)) {
				continue;
			}
			content.append(key+EQUAL+value+AND);
		}
		String signSource = content.toString();
		if(StringUtils.startsWith(signSource, AND)) {
			signSource = StringUtils.replaceOnce(signSource, AND, EMPTY);
		}
		if(StringUtils.endsWith(signSource, AND)) {
			signSource = StringUtils.substringBeforeLast(signSource, AND);
		}
		
		return signSource;
	}
    
    private static boolean checkSignMD5(Map<String, Object> signSourceMap, String md5Key) {
    	Object object = signSourceMap.get(SIGN);
    	String sign = object == null ? "" : object.toString();
    	String signSource = generateSignData(signSourceMap);
    	signSource += AND+KEY+EQUAL+md5Key;
    	return StringUtils.equals(DigestUtils.md5Hex(signSource), sign);
    }

    private static boolean checkSignRSA(Map<String, Object> signSourceMap, String publicKey) throws Exception {
    	Object object = signSourceMap.get(SIGN);
    	String sign = object == null ? "" : object.toString();
    	String signSource = generateSignData(signSourceMap);
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKey));
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        PublicKey pubKey = keyFactory.generatePublic(publicKeySpec);
        byte[] signed = Base64.decodeBase64(sign);
        Signature signature = Signature.getInstance(MD5_WITH_RSA);
        signature.initVerify(pubKey);
        signature.update(signSource.getBytes(UTF_8));
        return signature.verify(signed);
    }

	private static String addSignRSA(Map<String, Object> signSourceMap, String privateKey) throws Exception {
		String signSource = generateSignData(signSourceMap);
		return signRSA(signSource, privateKey);
	}
	
	private static String addSignMD5(Map<String, Object> signSourceMap, String md5Key) {
		String signSource = generateSignData(signSourceMap);
		signSource += AND+KEY+EQUAL+md5Key;
		return signMD5(signSource);
	}
}