package com.canary.finance.util;

import static com.canary.finance.util.AliyunOSSUtil.BUCKETNAME;
import static com.canary.finance.util.AliyunOSSUtil.ALIYUN_OSS;
import static com.canary.finance.util.ConstantUtil.BLANK;
import static com.canary.finance.util.ConstantUtil.DOT;
import static com.canary.finance.util.ConstantUtil.MINUS;
import static com.canary.finance.util.ConstantUtil.NUMBER_MONTH_FORMAT;
import static com.canary.finance.util.ConstantUtil.SLASH;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;

public class UeFileUploadUtil {
	private static final Log LOGGER = LogFactory.getLog(UeFileUploadUtil.class);
	private String[] allowFiles = { ".gif", ".png", ".jpg", ".jpeg", ".bmp" };
	private int maxSize = 2048000;
	private HttpServletRequest request = null;
	private String rootPath = null;
	private String contextPath = null;
	private String actionType = null;
	private ConfigManager configManager = null;
	
	public UeFileUploadUtil(HttpServletRequest request, String rootPath) {
		this.request = request;
		this.rootPath = rootPath;
		this.actionType = request.getParameter( "action" );
		this.contextPath = request.getContextPath();
		this.configManager = ConfigManager.getInstance(this.rootPath, this.contextPath, request.getRequestURI());
	}
	
	public String run() {
		String callbackName = this.request.getParameter("callback");
		if ( callbackName != null ) {
			if ( !validCallbackName( callbackName ) ) {
				return new Result( false, MessageInfo.ILLEGAL ).toString();
			}
			return callbackName+"("+this.start()+");";
		} else {
			return this.start();
		}
	}
	
	public String start() {
		if(actionType == null || !UploadType.types().containsKey(actionType)) {
			return new Result( false, MessageInfo.INVALID_ACTION).toString();
		}
		if (this.configManager == null) {
			this.configManager = ConfigManager.getInstance(this.rootPath, this.contextPath, request.getRequestURI());
		}
		if(this.configManager == null || !this.configManager.valid()) {
			return new Result( false, MessageInfo.CONFIG_ERROR).toString();
		}
		Result state = null;
		int actionCode = UploadType.getType(this.actionType);
		switch(actionCode) {
			case UploadType.CONFIG:
				return this.configManager.getAllConfig().toString();
			case UploadType.UPLOAD_IMAGE:
			try {
				state = uploadFile(request);
			} catch (Exception e) {
				e.printStackTrace();
			} 
			break;	
		}
		return state.toString();
	}

	private Result uploadFile(HttpServletRequest request) {
		SimpleDateFormat sdf = new SimpleDateFormat(NUMBER_MONTH_FORMAT);
		CommonsMultipartResolver multipartResolver  = new CommonsMultipartResolver(request.getSession().getServletContext());
    	if(multipartResolver.isMultipart(request)){
    		MultipartHttpServletRequest  multiRequest = multipartResolver.resolveMultipart(request); 
    	    Iterator<String>  iterator = multiRequest.getFileNames();
    		 while(iterator.hasNext()){
 		    	MultipartFile file = multiRequest.getFile(iterator.next());
 		    	String originalFilename = file.getOriginalFilename();
 		    	if(file != null && StringUtils.isNotBlank(originalFilename)){
					try (InputStream stream = file.getInputStream()) {
						String uuid = UUID.randomUUID().toString();
						uuid = StringUtils.replace(uuid, MINUS, BLANK);
						String suffix = StringUtils.substring(originalFilename, StringUtils.lastIndexOf(originalFilename, DOT));
						if (!validType(suffix, allowFiles)) {
							return new Result(false, MessageInfo.NOT_ALLOW_FILE_TYPE);
						}
						if(file.getSize() > maxSize){
							return new Result(false, MessageInfo.MAX_SIZE);
						}
						String fileName = sdf.format(new Date())+SLASH+uuid+suffix;
						putOSSObject(fileName, BUCKETNAME, stream);
						Result result = new Result(true,MessageInfo.SUCCESS);
						result.putInfo("url", ALIYUN_OSS+SLASH+fileName);
						result.putInfo("type", suffix);
						result.putInfo("original", originalFilename);
						return result;
					} catch (IOException e) {
						return new Result(false, MessageInfo.IO_ERROR);
					}
 		    	}
 		    }
    	}
		return new Result(false, MessageInfo.IO_ERROR);
	}
	
	private void putOSSObject(String key, String bucketName, InputStream stream) {
		try {
			AliyunOSSUtil.ensureBucket( bucketName);
			AliyunOSSUtil.setBucketPublicReadable(bucketName);
			AliyunOSSUtil.uploadFile(bucketName, key, stream);
		} catch (OSSException | ClientException | IOException e) {
			LOGGER.error("put oss object to aliyun error: "+e.getMessage());
		}
	}
	
	private static String toUnicode(String input) {
		StringBuilder builder = new StringBuilder();
		char[] chars = input.toCharArray();
		for(char ch : chars) {
			if (ch < 256) {
				builder.append( ch );
			} else {
				builder.append( "\\u" + Integer.toHexString(ch&0xffff));
			}	
		}
		return builder.toString();
	}
	
	private boolean validCallbackName(String name) {
		if(name.matches( "^[a-zA-Z_]+[\\w0-9_]*$")) {
			return true;
		}
		return false;
	}
	
	private static boolean validType(String type, String[] allowTypes) {
		List<String> list = Arrays.asList(allowTypes);
		return list.contains(type);
	}
	
	class Result {
		private boolean state = false;
		private String info = null;
		
		private Map<String, String> infoMap = new HashMap<String, String>();
		
		public Result() {
			this.state = true;
		}
		
		public Result(boolean state) {
			this.setState(state);
		}
		
		public Result(boolean state, String info) {
			this.setState(state);
			this.info = info;
		}
		
		public Result(boolean state, int infoCode) {
			this.setState(state);
			this.info = MessageInfo.getMessage(infoCode);
		}
		
		public boolean isSuccess() {
			return this.state;
		}
		
		public void setState(boolean state) {
			this.state = state;
		}
		
		public void setInfo(String info) {
			this.info = info;
		}
		
		public void setInfo(int infoCode) {
			this.info = MessageInfo.getMessage(infoCode);
		}
				
		public String toString() {
			String key = null;
			String stateVal = this.isSuccess() ? MessageInfo.getMessage(MessageInfo.SUCCESS) : this.info;
			StringBuilder builder = new StringBuilder();
			builder.append("{\"state\": \"" + stateVal + "\"");
			Iterator<String> iterator = this.infoMap.keySet().iterator();
			while ( iterator.hasNext() ) {
				key = iterator.next();
				builder.append(",\"" + key + "\": \"" + this.infoMap.get(key) + "\"");	
			}
			builder.append("}");
			return toUnicode(builder.toString());
		}

		public void putInfo(String name, String val) {
			this.infoMap.put(name, val);
		}

		public void putInfo(String name, long val) {
			this.putInfo(name, val+"");
		}
	}
	
	static final class MessageInfo {
		public static final int SUCCESS = 0;
		public static final int MAX_SIZE = 1;
		public static final int PERMISSION_DENIED = 2;
		public static final int FAILED_CREATE_FILE = 3;
		public static final int IO_ERROR = 4;
		public static final int NOT_MULTIPART_CONTENT = 5;
		public static final int PARSE_REQUEST_ERROR = 6;
		public static final int NOTFOUND_UPLOAD_DATA = 7;
		public static final int NOT_ALLOW_FILE_TYPE = 8;
		public static final int INVALID_ACTION = 101;
		public static final int CONFIG_ERROR = 102;
		public static final int PREVENT_HOST = 201;
		public static final int CONNECTION_ERROR = 202;
		public static final int REMOTE_FAIL = 203;
		public static final int NOT_DIRECTORY = 301;
		public static final int NOT_EXIST = 302;
		public static final int ILLEGAL = 401;
		
		public static Map<Integer, String> messages(){
			Map<Integer, String> messages = new HashMap<Integer, String>();
			messages.put( MessageInfo.SUCCESS, "SUCCESS" ); //保存成功啦
			messages.put( MessageInfo.INVALID_ACTION, "\u65E0\u6548\u7684Action" );// 无效的Action
			messages.put( MessageInfo.CONFIG_ERROR, "\u914D\u7F6E\u6587\u4EF6\u521D\u59CB\u5316\u5931\u8D25" );// 配置文件初始化失败
			messages.put( MessageInfo.REMOTE_FAIL, "\u6293\u53D6\u8FDC\u7A0B\u56FE\u7247\u5931\u8D25" );// 抓取远程图片失败
			messages.put( MessageInfo.PREVENT_HOST, "\u88AB\u963B\u6B62\u7684\u8FDC\u7A0B\u4E3B\u673A" );// 被阻止的远程主机
			messages.put( MessageInfo.CONNECTION_ERROR, "\u8FDC\u7A0B\u8FDE\u63A5\u51FA\u9519" );// 远程连接出错
			messages.put( MessageInfo.MAX_SIZE, "\u6587\u4ef6\u5927\u5c0f\u8d85\u51fa\u9650\u5236" );// "文件大小超出限制"
			messages.put( MessageInfo.PERMISSION_DENIED, "\u6743\u9650\u4E0D\u8DB3" );// 权限不足， 多指写权限
			messages.put( MessageInfo.FAILED_CREATE_FILE, "\u521B\u5EFA\u6587\u4EF6\u5931\u8D25" );// 创建文件失败
			messages.put( MessageInfo.IO_ERROR, "IO\u9519\u8BEF" );// IO错误
			messages.put( MessageInfo.NOT_MULTIPART_CONTENT, "\u4E0A\u4F20\u8868\u5355\u4E0D\u662Fmultipart/form-data\u7C7B\u578B" );// 上传表单不是multipart/form-data类型
			messages.put( MessageInfo.PARSE_REQUEST_ERROR, "\u89E3\u6790\u4E0A\u4F20\u8868\u5355\u9519\u8BEF" ); // 解析上传表单错误
			messages.put( MessageInfo.NOTFOUND_UPLOAD_DATA, "\u672A\u627E\u5230\u4E0A\u4F20\u6570\u636E" );// 未找到上传数据
			messages.put( MessageInfo.NOT_ALLOW_FILE_TYPE, "\u4E0D\u5141\u8BB8\u7684\u6587\u4EF6\u7C7B\u578B" );// 不允许的文件类型
			messages.put( MessageInfo.NOT_DIRECTORY, "\u6307\u5B9A\u8DEF\u5F84\u4E0D\u662F\u76EE\u5F55" );// 指定路径不是目录
			messages.put( MessageInfo.NOT_EXIST, "\u6307\u5B9A\u8DEF\u5F84\u5E76\u4E0D\u5B58\u5728" );// 指定路径并不存在
			messages.put( MessageInfo.ILLEGAL, "Callback\u53C2\u6570\u540D\u4E0D\u5408\u6CD5" );// callback参数名不合法
			return messages;
		};
		
		public static String getMessage(int key) {
			return MessageInfo.messages().get(key);
		}
	}
	
	static final class UploadType {
		public static final int CONFIG = 0;
		public static final int UPLOAD_IMAGE = 1;
		public static final int UPLOAD_SCRAWL = 2;
		public static final int UPLOAD_VIDEO = 3;
		public static final int UPLOAD_FILE = 4;
		public static final int CATCH_IMAGE = 5;
		public static final int LIST_FILE = 6;
		public static final int LIST_IMAGE = 7;
		public static Map<String, Integer> types() {
			Map<String, Integer> types = new HashMap<String, Integer>();
			types.put("config", UploadType.CONFIG);
			types.put("uploadimage", UploadType.UPLOAD_IMAGE);
			types.put("uploadscrawl", UploadType.UPLOAD_SCRAWL);
			types.put("uploadvideo", UploadType.UPLOAD_VIDEO);
			types.put("uploadfile", UploadType.UPLOAD_FILE);
			types.put("catchimage", UploadType.CATCH_IMAGE);
			types.put("listfile", UploadType.LIST_FILE);
			types.put("listimage", UploadType.LIST_IMAGE);
			return types;
		};
		
		public static int getType ( String key ) {
			return UploadType.types().get( key );
		}
	}
	
	static final class ConfigManager {
		private final String rootPath;
		private String originalPath;
		private static final String configFileName = "config.json";
		private String parentPath = null;
		private JSONObject jsonConfig = null;
		/*
		 * 通过一个给定的路径构建一个配置管理器， 该管理器要求地址路径所在目录下必须存在config.properties文件
		 */
		private ConfigManager(String rootPath, String contextPath, String uri) throws FileNotFoundException, IOException {
			if (StringUtils.isNotBlank(rootPath)) {
				rootPath = rootPath.replace( "\\", "/" );
			}
			this.rootPath = rootPath;
			this.originalPath = null;
			if (contextPath.length() > 0) {
				if (StringUtils.isNotBlank(uri)) {
					this.originalPath = this.rootPath + uri.substring(contextPath.length());
				}
			} else {
				if (StringUtils.isNotBlank(uri)) {
					this.originalPath = this.rootPath + uri;
				}
			}
			this.initEnv();
		}
		
		/**
		 * 配置管理器构造工厂
		 * @param rootPath 服务器根路径
		 * @param contextPath 服务器所在项目路径
		 * @param uri 当前访问的uri
		 * @return 配置管理器实例或者null
		 */
		public static ConfigManager getInstance(String rootPath, String contextPath, String uri) {
			try {
				return new ConfigManager(rootPath, contextPath, uri);
			} catch ( Exception e ) {
				return null;
			}
			
		}
		// 验证配置文件加载是否正确
		public boolean valid() {
			return this.jsonConfig != null;
		}
		public JSONObject getAllConfig () {
			return this.jsonConfig;
			
		}
		public Map<String, Object> getConfig(int type) {
			Map<String, Object> conf = new HashMap<String, Object>();
			conf.put("isBase64", "false");
			conf.put("maxSize", this.jsonConfig.getLong("imageMaxSize"));
			conf.put("allowFiles", this.getArray("imageAllowFiles") );
			conf.put("fieldName", this.jsonConfig.getString("imageFieldName"));
			conf.put("savePath", this.jsonConfig.getString("imagePathFormat"));
			conf.put("rootPath", this.rootPath);
			return conf;
		}
		
		private void initEnv() throws FileNotFoundException, IOException {
			File file = new File(this.originalPath);
			if(!file.isAbsolute()) {
				file = new File(file.getAbsolutePath());
			}
			this.parentPath = file.getParent();
			//String configContent = this.readFile(this.getConfigPath());
			String configContent = "";
			try{
				if (StringUtils.isNotBlank(configContent)) {
					JSONObject jsonConfig = new JSONObject(configContent);
					this.jsonConfig = jsonConfig;
				} else {
					JSONObject jsonConfig = new JSONObject();
					jsonConfig.put("imageActionName", "uploadimage");
					jsonConfig.put("imageFieldName", "upfile");
					jsonConfig.put("imageMaxSize", "2048000");
					List<String> data = new ArrayList<String>();
					data.add(".png");
					data.add(".jpg");
					data.add(".jpeg");
					data.add(".gif");
					data.add(".bmp");
					jsonConfig.put("imageAllowFiles", new JSONArray(data));
					jsonConfig.put("imageCompressEnable", "true");
					jsonConfig.put("imageCompressBorder", "1600");
					jsonConfig.put("imageInsertAlign", "none");
					jsonConfig.put("imageUrlPrefix", "");
					jsonConfig.put("imagePathFormat", "/ueditor/jsp/upload/image/{yyyy}{mm}{dd}/{time}{rand:6}");
					this.jsonConfig = jsonConfig;
				}
			} catch (Exception e) {
				this.jsonConfig = null;
			}
		}
		
		private String getConfigPath() {
			String configPath = this.parentPath + File.separator + ConfigManager.configFileName;
			LOGGER.info("config path : " + configPath);
			/*if (StringUtils.contains(configPath, "/res")) {
				configPath = StringUtils.replace(configPath, "/res", "");
			}
			
			if (StringUtils.contains(configPath, "\\res")) {
				configPath = StringUtils.replace(configPath, "\\res", "");
			}*/
			return configPath;
		}

		private String[] getArray(String key) {
			JSONArray jsonArray = this.jsonConfig.getJSONArray(key);
			String[] result = new String[ jsonArray.length() ];
			for (int i = 0, len = jsonArray.length(); i < len; i++) {
				result[i] = jsonArray.getString( i );
			}
			return result;
			
		}
		
		private String readFile(String path) throws IOException {
			StringBuilder builder = new StringBuilder();
			try {
				InputStreamReader reader = new InputStreamReader(new FileInputStream( path ), "UTF-8");
				BufferedReader bfReader = new BufferedReader(reader);
				String tmpContent = null;
				while(( tmpContent = bfReader.readLine()) != null) {
					builder.append(tmpContent);
				}
				bfReader.close();
			} catch (UnsupportedEncodingException e) {
				// 忽略
			}
			return this.filter( builder.toString() );
			
		}
		
		// 过滤输入字符串, 剔除多行注释以及替换掉反斜杠
		private String filter(String input) {
			return input.replaceAll( "/\\*[\\s\\S]*?\\*/", "" );
		}
	}
}

