package com.canary.finance.controller;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.alibaba.fastjson.JSONObject;
import com.canary.finance.domain.Admin;
import com.canary.finance.domain.LogLogin;
import com.canary.finance.pojo.GoogleauthCredentialBO;
import com.canary.finance.pojo.SessionVO;
import com.canary.finance.service.LogService;
import com.canary.finance.service.SystemService;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import com.warrenstrange.googleauth.IGoogleAuthenticator;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;

@Controller
@RequestMapping("/gauth")
public class GoogleauthController extends BaseController {
	@Autowired
	private IGoogleAuthenticator googleAuthenticator;
	@Autowired
	private SystemService systemService;
	@Autowired
	private LogService logService;
	
	@GetMapping("/credential/{user}")
	public String getCredential(@PathVariable("user")String user, Model model) throws Exception {
		if(!this.existUser(user)) {
			model.addAttribute("message", this.getMessage("admin.not.exist"));
			return "totp";
		}
		
		GoogleauthCredentialBO credential = new GoogleauthCredentialBO();
		Admin admin = this.systemService.getAdmin(user);
		if(admin != null && StringUtils.isNotBlank(admin.getToken())) {
			Jws<Claims> claims = Jwts.parser().setSigningKey(TextCodec.BASE64.encode(this.properties.getPrivateKey())).parseClaimsJws(admin.getToken());
			credential.setUserName(user);
			credential.setKey(claims.getBody().getId());
			credential.setTotpUrl(claims.getBody().getSubject());
			credential.setToken(admin.getToken());
		} else {
			GoogleAuthenticatorKey key = this.googleAuthenticator.createCredentials(user);
			credential.setUserName(user);
			credential.setKey(key.getKey());
			credential.setVerificationCode(key.getVerificationCode());
			credential.setScratchCodes(key.getScratchCodes());
			credential.setTotpUrl(GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL(this.properties.getIssuer(), user, key));
			long currentMillis = System.currentTimeMillis();
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR)+10);
			String token = BUILDER.signWith(SignatureAlgorithm.HS512, TextCodec.BASE64.encode(this.properties.getPrivateKey()))
				.setIssuer(this.properties.getIssuer())
				.setIssuedAt(new Date(currentMillis))
				.setSubject(credential.getTotpUrl())
				.setId(credential.getKey())
				.setAudience(credential.getUserName())
				.setExpiration(new Date(currentMillis+calendar.getTimeInMillis()))
				.compact();
			credential.setToken(token);
		}
		model.addAttribute("credential", credential);
		return "totp";
	}
	
	@PostMapping("/totp")
	public String bindTotp(GoogleauthCredentialBO credential, int authCodeFirst, int authCodeSecond, Model model) throws Exception {
		model.addAttribute("credential", credential);
		if(credential == null || !this.checkCredential(credential.getUserName(), credential.getToken())) {
			model.addAttribute("message", this.getMessage("totp.credential.tampered"));
			return "totp";
		}
		
		if(authCodeFirst == authCodeSecond) {
			model.addAttribute("message", this.getMessage("totp.same.code"));
			return "totp";
		}
		
		boolean success = this.googleAuthenticator.authorizeUser(credential.getToken(), authCodeFirst);
		success &= this.googleAuthenticator.authorizeUser(credential.getToken(), authCodeSecond);
		if(success) {
			Admin admin = this.systemService.getAdmin(credential.getUserName());
			admin.setTotp(1);
			admin.setToken(credential.getToken());
			this.systemService.saveAdmin(admin);
			model.addAttribute("message", this.getMessage("totp.bind.success"));
		} else {
			model.addAttribute("message", this.getMessage("totp.bind.failure"));
		}
		return "totp";
	}
	
	@RequestMapping(value="/authorize", method=RequestMethod.POST)
	public String authorize(String name, int authCode, HttpServletRequest request) throws Exception {
		if(!this.existUser(name)) {
			request.setAttribute("message", this.getMessage("admin.not.exist"));
			return "login";
		}
		
		Admin admin = this.systemService.getAdmin(name);
		boolean success = this.googleAuthenticator.authorizeUser(admin.getToken(), authCode);
		if(success) {
			LogLogin log = new LogLogin();
			log.setIp(this.getIpAddr(request));
			log.setName(admin.getName());
			this.logService.saveLoginLog(log);
			Integer roleId = admin.getRole().getId();
			SessionVO session = new SessionVO();
			session.setAdmin(admin);
			session.setObject(log);
			session.setRole(this.systemService.getRole(roleId));     
			session.setMenu(this.systemService.getMenu(roleId));
			request.getSession().setAttribute("session_canary_key", session);
			
			return InternalResourceViewResolver.REDIRECT_URL_PREFIX+"/main";
		} else {
			request.setAttribute("message", this.getMessage("admin.login.error"));
			return "login";
		}
	}
	
	@RequestMapping(value="/authorize/code", method=RequestMethod.POST)
	@ResponseBody
	public JSONObject authorizeCode(String name, int authCode, HttpServletRequest request) throws Exception {
		JSONObject result = new JSONObject();
		Admin admin = this.systemService.getAdmin(name);
		if (admin != null && StringUtils.isNotBlank(admin.getToken())) {
			boolean success = this.googleAuthenticator.authorizeUser(admin.getToken(), authCode);
			if (success) {
				result.put("code", 200);
				return result;
			}
		} 
		result.put("code", 400);
		return result;
	}
	
	private boolean checkCredential(String user, String accessToken) {
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(TextCodec.BASE64.encode(this.properties.getPrivateKey())).parseClaimsJws(accessToken);
			return StringUtils.equals(user, claims.getBody().getAudience());
		} catch (Exception e) {
			LOGGER.error("check google authenticator credential({}) error: {}", accessToken, e.getMessage());
		}
		
		return false;
	}

	private boolean existUser(String name) {
		return this.systemService.getAdmin(name) != null ? true : false;
	}
}
