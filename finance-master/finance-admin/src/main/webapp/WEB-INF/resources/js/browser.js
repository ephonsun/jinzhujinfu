var browser = {
	appCodeName : navigator.appCodeName,
	appName : navigator.appName,
	appVersion : navigator.appVersion,
	cookieEnabled : navigator.cookieEnabled,
	platform : navigator.platform,
	userAgent : navigator.userAgent,
	isChrome : navigator.userAgent.toLowerCase().indexOf("chrome") != -1 ? true : false,
	isFirefox : navigator.userAgent.toLowerCase().indexOf("firefox") != -1 ? true : false,	
	isIe : navigator.userAgent.toLowerCase().indexOf("msie") != -1 ? true : false,
	ieVersion : navigator.userAgent.toLowerCase().indexOf("msie 11") != -1 ? 11 
			: navigator.userAgent.toLowerCase().indexOf("msie 10") != -1 ? 10 
			: navigator.userAgent.toLowerCase().indexOf("msie 9") != -1 ? 9
			: navigator.userAgent.toLowerCase().indexOf("msie 8") != -1 ? 8
			: navigator.userAgent.toLowerCase().indexOf("msie 7") != -1 ? 7
			: navigator.userAgent.toLowerCase().indexOf("msie 6") != -1 ? 6
			: -1,
	isWindows : navigator.platform.toLowerCase().indexOf("win") != -1 ? true : false,
	isMac : navigator.platform.toLowerCase().indexOf("mac") != -1 ? true : false,
	isLinux : navigator.platform.toLowerCase().indexOf("linux") != -1 ? true 
			: navigator.platform.toLowerCase().indexOf("x11") != -1 ? true
			: false,
	verbose : "["
				+"appCodeName:"+ navigator.appCodeName+ "\n"
				+"appName:"+ navigator.appName+ "\n"
				+"appVersion:"+ navigator.appVersion+ "\n"
				+"platform:"+ navigator.platform+ "\n"
				+"userAgent:"+ navigator.userAgent+ "\n"
				+"cookieEnabled:"+ navigator.cookieEnabled+
			  "]"
};