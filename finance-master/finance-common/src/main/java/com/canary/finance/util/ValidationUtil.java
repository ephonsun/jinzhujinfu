package com.canary.finance.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtil {
	
	private ValidationUtil() {
		
	} 
	
	public static boolean isIdCard(String idCard) {
		if(isEmpty(idCard)) {
			return false;
		}
		String regex = "[1-9]\\d{13,16}[a-zA-Z0-9]{1}"; 
        return Pattern.matches(regex, idCard); 
	}
	
	public static boolean isEmail(String email){
		if(isEmpty(email)) {
			return false;
		}
		String regex = "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?"; 
        return Pattern.matches(regex, email); 
	}
	
	public static boolean isMobile(String mobile){
		if(isEmpty(mobile)) {
			return false;
		}
		String regex = "(\\+\\d+)?1[34578]\\d{9}$"; 
        return Pattern.matches(regex, mobile);
	}
	
	public static int couponCodeLength(String code){
		if(isEmpty(code)) {
			return 0;
		}
		
		int length = 0;
		Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]");  
		char[] codec = code.toCharArray();  
		for(int i=0; i < codec.length; i++){  
		    Matcher matcher = pattern.matcher(String.valueOf(codec[i]));  
		    if(matcher.matches()){  
		    	length += 2;  
		    } else {
		    	length++;
		    }
		} 
		return length;
	}
	
	public static boolean isPhone(String phone) {
		if(isEmpty(phone)) {
			return false;
		}
		String regex = "(\\+\\d+)?(\\d{3,4}\\-?)?\\d{7,8}$"; 
        return Pattern.matches(regex, phone);
	}
	
	public static boolean isLoginPassword(String password){
		if(isEmpty(password)){
			return false;
		}
		String regex = "^[0-9a-zA-Z]{6,20}";
		return Pattern.matches(regex, password);
	}
	
	public static boolean isChinese(String chinese) {
		if(isEmpty(chinese)) {
			return false;
		}
		String regex = "^[\u4E00-\u9FA5]+$"; 
        return Pattern.matches(regex, chinese); 
	}
	
	public static boolean isBirthday(String birthday) { 
		if(isEmpty(birthday)) {
			return false;
		}
        String regex = "[1-9]{4}([-./])\\d{1,2}\\1\\d{1,2}"; 
        return Pattern.matches(regex, birthday); 
    } 
	
	public static boolean isURL(String url) {
		if(isEmpty(url)) {
			return false;
		}
		String regex = "(https?://(w{3}\\.)?)?\\w+\\.\\w+(\\.[a-zA-Z]+)*(:\\d{1,5})?(/\\w*)*(\\??(.+=.*)?(&.+=.*)?)?"; 
        return Pattern.matches(regex, url); 
	}
	
	public static boolean isPostcode(String postcode) { 
		if(isEmpty(postcode)) {
			return false;
		}
        String regex = "[1-9]\\d{5}"; 
        return Pattern.matches(regex, postcode); 
    } 
	
	public static boolean isImageUrl(String url) { 
		if(isEmpty(url)) {
			return false;
		}
		String regex = "[^\u4e00-\u9fa5]*(.jpg|.png)$"; 
		return Pattern.matches(regex, url); 
	} 
	
	public static boolean isDate(String date) { 
		if(isEmpty(date)) {
			return false;
		}
		String regex = "([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8])))"; 
		return Pattern.matches(regex, date); 
	} 
	
	public static boolean isDateTime(String dateTime) { 
		if(isEmpty(dateTime)) {
			return false;
		}
		String regex = "^((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29))\\s+([0-1]?[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$";
		return Pattern.matches(regex, dateTime); 
	}
	
	public static boolean isLoginValidateCode(String code) { 
		if(isEmpty(code)) {
			return false;
		}
        String regex = "^[0-9]{4}$"; 
        return Pattern.matches(regex, code); 
    }
	
	public static boolean isLoginSmsValidateCode(String code) { 
		if(isEmpty(code)) {
			return false;
		}
        String regex = "^[0-9]{6}$"; 
        return Pattern.matches(regex, code); 
    }
	
	public static boolean isLoginName(String name) { 
		if(isEmpty(name)) {
			return false;
		}
		String regex = "^([\u4e00-\u9fa5]+|[a-zA-Z0-9]+){4,20}$";
		return Pattern.matches(regex, name);
	}
	
	public static boolean isIpAddress(String ipAddress) { 
		if(isEmpty(ipAddress)) {
			return false;
		}
        String regex = "[1-9](\\d{1,2})?\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))"; 
        return Pattern.matches(regex, ipAddress); 
    } 
	
	public static boolean isDecimal(String decimal, int count) {
		if (null == decimal || "".equals(decimal)) return false;
		String regex = "^(-)?(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){" + count+ "})?$";
//		String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
//				+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
//				+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
//				+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
		
		return decimal.matches(regex);
	}
	
	public static boolean hasSpecialChar(String text){
		if(isEmpty(text)) {
			return false;
		}
		if(text.replaceAll("[a-z]*[A-Z]*\\d*-*_*\\s*", "").length()>0){
		    return true;
		 }
		 return false;
	}
	
	/**
	 * http://www.silverfox-cn.com/share/detail.htm ->> silverfox-cn.com
	 * @param url
	 * @return
	 */
	public static String getDomain(String url) {
	    Pattern p = Pattern.compile("(?<=http://|\\.)[^.]*?\\.(com|cn|net|org|biz|info|cc|tv)", Pattern.CASE_INSENSITIVE);
	    Matcher matcher = p.matcher(url);
	    matcher.find();
	    return matcher.group();
	}
	
	public static boolean isEmpty(String str){
		if(str == null || "".equals(str))
			return true;
		return false;
	}
	 
    public static boolean StrisNull(String str) {  
        return null == str || str.trim().length() <= 0 ? true : false ;  
    }  
    
    public static boolean StrNotNull(String str) {  
        return !StrisNull(str) ;  
    } 
	
	public static boolean StrNotNullAndMinAndMax(String str, int min, int max) {  
    	return StrNotNull(str) &&  (min <= str.trim().length() &&  str.trim().length() <= max); 
    }
	
	public  static boolean isOrganizationCode(String str) {  
        return Regular(str,"^\\d+(-\\d+)*$") ;  
    }
	
    public  static boolean isTaxRegistrationCode(String str) {  
        return Regular(str, "^\\d+(-\\d+)*$") ;  
    }
    
    public  static boolean isRoleName(String str) {  
        return Regular(str,"^([\u4e00-\u9fa5]+|[a-zA-Z0-9]+)$") ;  
    }
	
    private  static boolean Regular(String str,String pattern){  
        if(null == str || str.trim().length()<=0) {
            return false;           
        }
        Pattern p = Pattern.compile(pattern);  
        Matcher m = p.matcher(str);  
        return m.matches();  
    }
}