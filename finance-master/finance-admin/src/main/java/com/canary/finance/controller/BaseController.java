package com.canary.finance.controller;

import static com.canary.finance.util.AliyunOSSUtil.ALIYUN_OSS;
import static com.canary.finance.util.AliyunOSSUtil.BUCKETNAME;
import static com.canary.finance.util.ConstantUtil.AND;
import static com.canary.finance.util.ConstantUtil.ASK;
import static com.canary.finance.util.ConstantUtil.DOT;
import static com.canary.finance.util.ConstantUtil.EMPTY;
import static com.canary.finance.util.ConstantUtil.EQUAL;
import static com.canary.finance.util.ConstantUtil.ISO_8859_1;
import static com.canary.finance.util.ConstantUtil.MINUS;
import static com.canary.finance.util.ConstantUtil.NORMAL_DATETIME_FORMAT;
import static com.canary.finance.util.ConstantUtil.NUMBER_MONTH_FORMAT;
import static com.canary.finance.util.ConstantUtil.SLASH;
import static com.canary.finance.util.ConstantUtil.UTF_8;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;
import com.canary.finance.config.CanaryFinanceProperties;
import com.canary.finance.util.AliyunOSSUtil;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;

@Controller
public class BaseController {
	protected static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);
	protected static final JwtBuilder BUILDER = Jwts.builder();
	protected static final JwtParser PARSER = Jwts.parser();
	protected final Base64 base64 = new Base64(true);
	protected final Random random = new Random(100000L);
	protected static final ScheduledExecutorService EXECUTOR = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors()*2, new ThreadFactory() {
		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(r);
			t.setDaemon(true);
			return t;
		}
	});
	@Autowired
	protected CanaryFinanceProperties properties;
	@Autowired
	protected MessageSource messageSource;
	
	@InitBinder
	public void initBinder(ServletRequestDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat(NORMAL_DATETIME_FORMAT), true));
	}
	
	protected String getIpAddr(HttpServletRequest request) { 
	    String ip = request.getHeader("x-forwarded-for"); 
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	        ip = request.getHeader("Proxy-Client-IP"); 
	    } 
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	        ip = request.getHeader("WL-Proxy-Client-IP"); 
	    } 
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	        ip = request.getRemoteAddr(); 
	    } 
	    return ip; 

	}
	
	protected int getOffset(Integer page, Integer size) {
		if(page == null || page == 0 ) {
			page = 1;
		}
		if(size == null || size == 0) {
			size = 15;
		}
		return (page-1)*size;
	}
	
	protected int getPageSize(Integer size) {
		if(size == null || size == 0) {
			size = 15;
		}
		return size;
	}
	
	protected int getPage(Integer page) {
		if(page == null || page == 0) {
			page = 1;
		}
		return page;
	}
	
	protected int getTotalPage(int total, Integer size) {
		if(size == null || size == 0) {
			size = 15;
		}
		return (int)Math.ceil((double)total / (double)size);
	}
	
	protected LinkedHashMap<String, String> upload(HttpServletRequest request) {
		SimpleDateFormat sdf = new SimpleDateFormat(NUMBER_MONTH_FORMAT);
		LinkedHashMap<String, String> result = new LinkedHashMap<String, String>();
		CommonsMultipartResolver multipartResolver  = new CommonsMultipartResolver(request.getSession().getServletContext());
    	if(multipartResolver.isMultipart(request)){
    		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
    	    Iterator<String> iterator = multiRequest.getFileNames();
    		 while(iterator.hasNext()){
 		    	MultipartFile file = multiRequest.getFile(iterator.next());
 		    	String originalFilename = file.getOriginalFilename();
 		    	if(file != null && StringUtils.isNotBlank(originalFilename)){
					try(InputStream stream = file.getInputStream()) {
						String uuid = UUID.randomUUID().toString();
						uuid = StringUtils.replace(uuid, MINUS, EMPTY);
						String suffix = StringUtils.substring(originalFilename, StringUtils.lastIndexOf(originalFilename, DOT));
						String name = file.getName();
						String fileName = sdf.format(new Date())+SLASH+uuid+suffix;
						putOSSObject(fileName, BUCKETNAME, stream);
						result.put(name, ALIYUN_OSS+SLASH+fileName);
					} catch (IOException e) {
						LOGGER.error("upload file to oss error: "+e.getMessage());
					}
 		    	}
 		    }
    	}
		return result;
	}
	
	protected void putOSSObject(String key, String bucketName, InputStream stream) {
		try {
			AliyunOSSUtil.ensureBucket( bucketName);
			AliyunOSSUtil.setBucketPublicReadable(bucketName);
			AliyunOSSUtil.uploadFile(bucketName, key, stream);
		} catch (OSSException | ClientException | IOException e) {
			LOGGER.error("put oss object to aliyun error: "+e.getMessage());
		}
	}
	
	protected JSONObject getJSONFromBase64(String base64) throws Exception {
		JSONObject json = new JSONObject();
		if(base64 != null && Base64.isBase64(base64)) {
			String decodeBase64 = new String(Base64.decodeBase64(base64), UTF_8);
			if(decodeBase64.startsWith(ASK)) {
				decodeBase64 = decodeBase64.substring(1);
			}
			String[] parameters = StringUtils.split(decodeBase64, AND);
			for(String parameter : parameters) {
				String[] entity = StringUtils.split(parameter, EQUAL);
				if(entity != null && entity.length == 2) {
					json.put(entity[0], entity[1]);
				} else {
					json.put(entity[0], null);
				}
			}
		}
		return json;
	}
	
	protected String getMessage(String code) {
		return this.messageSource.getMessage(code, null, Locale.getDefault());
	}
	
	protected String getMessage(String code, Object[] args) {
		return this.messageSource.getMessage(code, args, Locale.getDefault());
	}
	
	protected String getMessage(String code, Object[] args, Locale locale) {
		return this.messageSource.getMessage(code, args, locale);
	}
	
	protected String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
		return this.messageSource.getMessage(code, args, defaultMessage, locale);
	}
	
	protected String execute(String uri, List<NameValuePair> nvps, String encoding) {
		CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
		
		HttpPost httpPost = new HttpPost(uri);
		try {
			if(nvps != null && nvps.size() > 0) {
				httpPost.setEntity(new UrlEncodedFormEntity(nvps, encoding));
			}
			CloseableHttpResponse httpResponse = closeableHttpClient.execute(httpPost);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if(statusCode == 200) {
				return EntityUtils.toString(httpResponse.getEntity(), encoding);
			} else {
				return EntityUtils.toString(httpResponse.getEntity(), encoding);
			}
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			httpPost.releaseConnection();
		}
		return null;
	}
	
	protected ResponseEntity<byte[]> export(String realPath, String fileName, HttpServletRequest request) {
		String agent = request.getHeader("User-Agent");
		boolean isMSIE = (agent != null && agent.toUpperCase().indexOf("MSIE") > 0);
   		HttpHeaders headers = new HttpHeaders();
   		File file = new File(realPath+fileName);
   		try {
   			if(isMSIE){ 
   				fileName = URLEncoder.encode(fileName, UTF_8);
   				if(fileName.length() > 150){  
   					fileName = new String(fileName.getBytes(UTF_8), ISO_8859_1);  
   				}  
   			} else {
   				fileName = new String(fileName.getBytes(UTF_8), ISO_8859_1);
   			}
   			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
   			headers.setContentDispositionFormData("attachment", fileName);
   			return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, org.springframework.http.HttpStatus.OK);
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
   		return null;
	}
}
