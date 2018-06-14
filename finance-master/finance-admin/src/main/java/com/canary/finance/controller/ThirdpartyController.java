package com.canary.finance.controller;

import java.io.File;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Controller
public class ThirdpartyController extends BaseController {
	
	@RequestMapping("viewer")
	public String viewer(String file, Model model) throws Exception {
		model.addAttribute("file", file);
		return "viewer";
	}
	
	@RequestMapping("viewer/signature")
	public String viewURL(URL url, HttpServletRequest request) throws Exception {
		String realPath = request.getServletContext().getRealPath("/")+"WEB-INF/resources";
		String fileName = "/"+System.currentTimeMillis()+".pdf";
		FileUtils.copyURLToFile(url, new File(realPath+fileName));
		LOGGER.info(realPath+fileName);
		return InternalResourceViewResolver.REDIRECT_URL_PREFIX+"/viewer?file="+fileName;
	}
}
