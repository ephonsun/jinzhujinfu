package com.canary.finance.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.canary.finance.domain.AppVersion;
import com.canary.finance.domain.NewsBulletin;
import com.canary.finance.domain.PictureLibrary;
import com.canary.finance.domain.Product;
import com.canary.finance.service.BannerService;
import com.canary.finance.service.NewsBulletinService;
import com.canary.finance.service.OperationService;
import com.canary.finance.service.ProductService;

@Controller
public class IndexController extends BaseController {
	@Autowired
	private BannerService bannerSerivce;
	@Autowired
	private OperationService operationService;
	@Autowired
	private ProductService productService;
	@Autowired
	private NewsBulletinService newsBulletinService;
	
	@RequestMapping("/")
	public String index(HttpServletRequest request) {
		String cookieName = this.getMessage("cookie.login");
		request.setAttribute("isLogin", this.isLogin(request, cookieName));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		request.setAttribute("systemTime", sdf.format(new Date()));
		request.setAttribute("banners", this.getBannerList());
		request.setAttribute("novice", this.getNoviceProduct());
		request.setAttribute("products", this.getProductList());
		request.setAttribute("notices", this.getNoticeList());
		request.setAttribute("newsset", this.getNewsList());
		AppVersion version = operationService.getLatestAppVersion("apk");
		if (version != null) {
			request.setAttribute("androidUrl", version.getUrl());
		}
		return "index";
	}

	private List<PictureLibrary> getBannerList() {
		return this.bannerSerivce.getWebsiteList();
	}
	
	private Product getNoviceProduct() {
		return this.productService.getTopNovice();
	}
	
	private List<Product> getProductList() {
		return this.productService.getTop4Product();
	}
	
	private List<NewsBulletin> getNoticeList() {
		return this.newsBulletinService.getTopNotice();
	}
	
	private List<NewsBulletin> getNewsList() {
		return this.newsBulletinService.getTopNews();
	}
}
