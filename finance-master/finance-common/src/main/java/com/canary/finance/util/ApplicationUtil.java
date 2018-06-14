package com.canary.finance.util;

import java.io.File;
import java.net.URLDecoder;

public class ApplicationUtil {
	private static final String WEB_INFO = "WEB-INF";
	private static final String CLASSES = "classes";
	private static final String UTF8 = "UTF-8";
	private static final String CLASS_PATH = ApplicationUtil.class.getClassLoader().getResource("").getFile();
	
	public static String getApplicationPath() throws Exception {
		File file = new File(URLDecoder.decode(CLASS_PATH, UTF8));
		String path = "";
		if(file.exists() && file.isDirectory()){
			//web application path is WEB-INFO/classes's parent path[decode %20 to blank if exists]
			if(CLASS_PATH.indexOf(WEB_INFO) != -1) {
				path = file.getCanonicalFile().getParentFile().getParentFile().getCanonicalPath();
			} else if(CLASS_PATH.indexOf(CLASSES) != -1) {
				path = file.getCanonicalFile().getParentFile().getCanonicalPath();
			} else {
				path = file.getCanonicalPath();
			}
			if(!path.endsWith("/") && !path.endsWith("\\")) {
				path = path+"/";
			}
		}
		return path;
	}
}
