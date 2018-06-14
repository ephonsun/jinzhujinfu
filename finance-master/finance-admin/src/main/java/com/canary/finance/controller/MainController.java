package com.canary.finance.controller;

import static com.canary.finance.util.ConstantUtil.UTF_8;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Controller
public class MainController extends BaseController {
	
	@RequestMapping("header")
	public String header() {
		
		return "include/header";
	}
	
	@RequestMapping("upload")
	public String upload() {
		return "upload";
	}
	
	@RequestMapping("menu")
	public String menu() {
		
		return "include/menu";
	}
	
	@RequestMapping("breadcrumb")
	public String breadcrumb() {
		
		return "include/breadcrumb";
	}
	
	@RequestMapping("sidebar")
	public String sidebar() {
		
		return "include/sidebar";
	}
	
	@RequestMapping("footer")
	public String footer() {
		
		return "include/footer";
	}

	@RequestMapping("main")
	public String main() {
		return "main";
	}
	
	@RequestMapping("message/board")
	public String messageBoard() {
		return "message-board";
	}
	
	@RequestMapping("/")
	public String login() {
		
		return "login";
	}
	
	@RequestMapping("logout")
	public String logout(HttpSession session) {
		session.removeAttribute("session_canary_key");
		return InternalResourceViewResolver.REDIRECT_URL_PREFIX+"/";
	}
	
	@RequestMapping("/web/upload")
	@ResponseBody
	public void uploadWebImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setCharacterEncoding(UTF_8); 
		PrintWriter out = response.getWriter(); 
        Map<String, String> result = this.upload(request);
        String fileName = StringUtils.substring(result.values().toString(), 1, result.values().toString().length()-1);
        out.print(fileName);  
        out.flush();
	}
}
