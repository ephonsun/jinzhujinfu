package com.canary.finance.controller;

import static com.canary.finance.util.ConstantUtil.UTF_8;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.canary.finance.domain.PictureLibrary;
import com.canary.finance.pojo.ResponseDTO;
import com.canary.finance.service.BannerService;
import com.canary.finance.util.CaptchaUtil;
import com.canary.finance.util.CaptchaUtil.ComplexLevel;

import io.jsonwebtoken.Claims;

@Controller
public class MainController extends BaseController {
	@Autowired
	private BannerService bannerService;
	
	@RequestMapping("/header")
	public String header() {
		return "include/header";
	}
	
	@RequestMapping("/navbar")
	public String navbar(HttpServletRequest request) throws Exception {
		String cookieName = this.getMessage("cookie.login");
		Cookie cookie = this.getCookie(request, cookieName);
		if(cookie != null) {
			Claims claims = this.jwtService.parseToken(cookie.getValue());
			request.setAttribute("isLogin", true);
			request.setAttribute("cellphone", claims.getId());
		} else {
			request.setAttribute("isLogin", false);
			request.setAttribute("cellphone", "");
		}
		return "include/navbar";
	}
	
	@RequestMapping("/footer")
	public String footer() {
		return "include/footer";
	}

	@RequestMapping("/aboutus")
	public String aboutus() {
		return "aboutus";
	}
	
	@RequestMapping("/contactus")
	public String contactus() {
		return "contactus";
	}
	
	@RequestMapping("/protocol/relief")
	public String relief() {
		return "protocol/relief";
	}
	
	@RequestMapping("/protocol/service")
	public String service() {
		return "protocol/service";
	}
	
	@RequestMapping("/protocol/product")
	public String bannerDetail() {
		return "protocol/product";
	}
	
	@RequestMapping("/banner/detail/{bannerId}")
	public String protocol(@PathVariable("bannerId")int bannerId, Device device, Model model) {
		PictureLibrary banner = bannerService.get(bannerId);
		model.addAttribute("banner", banner);
		if (banner != null && banner.getPlatform() == 1) {
			return "h5/banner-detail";
		} else {
			return "banner-detail";
		}
	}
	
	@RequestMapping("/safety")
	public String safety() {
		return "safety";
	}
	
	@RequestMapping("/guide")
	public String guide() {
		return "guide";
	}
	
	@RequestMapping("/invite")
	public String invite() {
		return "invite";
	}
	
	@RequestMapping("/download")
	public String download() {
		return "download";
	}
	
	@RequestMapping(value="/captcha", method=RequestMethod.GET)
	@ResponseBody
	public void captcha(HttpServletRequest request, HttpServletResponse response){
		try{
			HttpSession session = request.getSession();
			Object[] obj= CaptchaUtil.getCaptchaImage(60, 20, 14, 100, 500, false, false, ComplexLevel.SIMPLE);
			response.setHeader("Pragma","no-cache");
			response.setHeader("Cache-Control","no-cache");
			response.setDateHeader("Expires",0);
			response.setContentType("image/jpeg");
			ServletOutputStream sos=response.getOutputStream();
			ImageIO.write((BufferedImage)obj[0],"jpeg",sos);
			sos.close();
			session.setAttribute("captcha", obj[1]);
		} catch (IOException e){
			LOGGER.error("error", e);
		}
	}
	
	@RequestMapping("/captcha/base64")
	@ResponseBody
	public ResponseDTO<String> captcha() throws Exception {
		ResponseDTO<String> result = new ResponseDTO<String>();
		Color fontColor = new Color(85, 85, 85);
		Color bgColor = new Color(238, 238, 238);
		Object[] obj= CaptchaUtil.getCaptchaImage(60, 20, 14, 100, 500, false, false, fontColor, bgColor, ComplexLevel.SIMPLE);
		if(obj != null && obj.length == 2) {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			ImageIO.write((BufferedImage)obj[0],"jpeg", output);
			result.setCode(HttpStatus.OK.value());
			result.setMsg(obj[1].toString());
			result.setData("data:image/jpeg;base64,"+Base64Utils.encodeToString(output.toByteArray()));
		} else {
			result.setCode(HttpStatus.NOT_FOUND.value());
			result.setMsg(HttpStatus.NOT_FOUND.getReasonPhrase());
		}
		return result;
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
