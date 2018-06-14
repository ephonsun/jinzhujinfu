<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html lang="zh-CN">
<jsp:include page="${pageContext.request.contextPath}/header" flush="true" />
<body>
<!--[if IE 8]>
<div class="alert alert-warning text-center" style="height:80px;margin-bottom:0;z-index:9999;">
  <p>你的浏览器不支持点融的一些新特性，请升级你的浏览器至<a href="http://se.360.cn/" target="_blank">360浏览器</a>或<a href="http://www.google.cn/chrome/browser/desktop/index.html" target="_blank">Chrome</a>。</p>
  <p>2017年了，IE8老了...</p>
</div>
<![endif]-->

<!--[if lt IE 8]>
<div class="alert alert-danger text-center" style="height:80px;margin-bottom:0;z-index:9999;">
  <p>你的浏览器不支持点融的一些新特性，请升级你的浏览器至<a href="http://se.360.cn/" target="_blank">360浏览器</a>或<a href="http://www.google.cn/chrome/browser/desktop/index.html" target="_blank">Chrome</a>。</p> 
  <p>2017年了，IE7及以下都老了...</p>
</div>
<![endif]-->
<jsp:include page="${pageContext.request.contextPath}/navbar" flush="true" />

<c:if test="${fn:length(banners) > 0}">
<div class="container-fluid" style="margin-left: 225px;">
	<div id="index-bargain" class="row">
		<div class="px1000 index-bargain-position" >
			 <div class="index-bargain-div">
				<div class="index-bargain-mover">
					<p class="index-bargain-income-padding">
						<span class="index-bargain-income-number">12.00</span>
						<span class="index-bargain-income-percent">%</span>	
					</p>
					<p class="index-bargain-one">平均年化收益率</p>
					<p class="index-bargain-two">
						近<b>40</b>倍活期存款收益
					</p>
					<c:choose>
						<c:when test="${isLogin == true}">
							<p class="index-bargain-three">
								<a class="index-bargain-three-font" href="${pageContext.request.contextPath}/asset/my/1">我的资产</a>
							</p>
							<p class="index-bargain-four"></p>
						</c:when>
						<c:otherwise>
							<p class="index-bargain-three">
								<a class="index-bargain-three-font" href="${pageContext.request.contextPath}/register">注册领红包</a>
							</p>
							<p class="index-bargain-four">
								<span>已注册帐号？</span><span><a class="index-bargain-login" href="${pageContext.request.contextPath}/login">立即登录</a></span>
							</p>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
			<div class="index-bargain-opacity"></div>
		</div>
	</div>
</div>
<div class="container-fluid">
  	<div id="index-carousel" class="carousel slide row">
	    <ol class="carousel-indicators">
	      	<c:forEach var="banner" items="${banners}" varStatus="s">
			<li data-target="#index-carousel" data-slide-to="${s.index}" <c:if test="${s.index == 0}">class="active"</c:if>></li>
	      	</c:forEach>
	    </ol>   
	    <div class="carousel-inner">
	      	<c:forEach var="banner" items="${banners}" varStatus="s">
	      	<div class="item <c:if test="${s.index == 0}">active</c:if>">
	      	  	<c:choose>
		      	  	<c:when test="${banner.link != null && fn:startsWith(banner.link, 'http') }">
		      	  	<a href="${banner.link}" target="_blank">
			            <img style="height: 260px;" src="${banner.url}" alt="${banner.name}">
		        	</a>
		      	  	</c:when>
		      	  	<c:otherwise>
		            <img src="${banner.url}" alt="${banner.name}">
		      	  	</c:otherwise>
	      	  	</c:choose>
	        </div>
	      	</c:forEach>
	    </div>
	    <%-- <a class="left carousel-control" href="#index-carousel" role="button" data-slide="prev">
			<span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
		    <span class="sr-only">前一屏</span>
		</a>
		<a class="right carousel-control" href="#index-carousel" role="button" data-slide="next">
		    <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
		    <span class="sr-only">下一屏</span>
		</a> --%>
	</div>
</div>
</c:if>

<div id="index-treasure" class="container">
	<div class="row">
		<div class="col-md-9 text-center">
			<div class="row">
				<div class="col-md-4">
					<img class="img-rounded" src="${pageContext.request.contextPath}/images/treasure_kind04.png" alt="...">
				</div>
				<div class="col-md-4">
					<img class="img-rounded" src="${pageContext.request.contextPath}/images/treasure_kind02.png" alt="...">
				</div>
				<div class="col-md-4">
					<img class="img-rounded" src="${pageContext.request.contextPath}/images/treasure_kind03.png" alt="...">
				</div>
			</div>
			<div class="row">
				<div class="col-md-4">
					<span>小额分散</span>
				</div>
				<div class="col-md-4">
					<span>收益可观</span>
				</div>
				<div class="col-md-4">
					<span>本息保障</span>
				</div>
			</div>
			<div class="row">
				<div class="col-md-4">
					<span class="font-gray">专注小额分散致力于融资租赁业务</span>
				</div>
				<div class="col-md-4">
					<span class="font-gray">历史平均数据理财年化可达12%</span>
				</div>
				<div class="col-md-4">
					<span class="font-gray">项目风险准备金保驾护航</span>
				</div>
			</div>
		</div>
		<div class="col-md-3">
			<div class="row">
				<div class="col-md-6">
					<img class="img-rounded" width="120" src="${pageContext.request.contextPath}/images/barcode_download.png" alt="扫二维码下载" />
				</div>
				<div class="col-md-6">
					<div class="row padding-five"></div>
					<div class="row padding-ten">
						<a class="btn btn-default btn-block" href="${androidUrl }" role="button" >Android版下载</a>
					</div>
					<div class="row padding-ten">
						<a class="btn btn-default btn-block" href="https://itunes.apple.com/cn/app/id1008896468" role="button" target="_blank">iPhone版下载</a>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<c:if test="${novice != null}">
<div id="index-novice" class="container novice-panel">
	<div class="row">
		<div class="col-md-2">
			<a href="${pageContext.request.contextPath}/guide" target="_blank">
				<img src="${pageContext.request.contextPath}/images/novice.png" alt="..." />
			</a>
		</div>
		<div class="col-md-10">
			<div class="panel panel-default">
				<div class="panel-body">
					<div class="col-md-9">
						<div class="row line-div">
							<div class="col-md-8">${novice.name}</div>
							<%-- <div class="col-md-4">
							<c:if test="${novice.repayment == 0}">一次性还本付息</c:if>
							<c:if test="${novice.repayment == 1}">按月付息到期还本</c:if>
							</div>  --%>
						</div>
						<div class="row text-center">
							<div class="row">
								<div class="col-md-3">年化收益</div>
								<div class="col-md-2">理财期限</div>
								<div class="col-md-3">起投金额</div>
								<div class="col-md-4">募集金额</div>
							</div>
							<div class="row">
								<div class="col-md-3">
									<span><strong>${novice.yearIncome}</strong><small>%</small>
									<c:if test="${novice.increaseInterest > 0}">+<small>${novice.increaseInterest}%</small></c:if>
									</span>
								</div>
								<div class="col-md-2">
									<span><strong>${novice.financePeriod}天</strong></span>
								</div>
								<div class="col-md-3">
									<span><strong>${novice.lowestMoney}元</strong></span>
								</div>
								<div class="col-md-4"><span><strong>${novice.totalAmount}元</strong></span></div>
							</div>
							<div class="novice-interest-div">
								<%-- <div class="text-left">
									<span class="glyphicon glyphicon-time" aria-hidden="true"></span>起息时间：T(满标日)+1
								</div> --%>
							</div>
						</div>
					</div>
					<div class="col-md-3">
						<div class="row">
							<span>剩余可投金额：<small>${novice.totalAmount-novice.actualAmount}</small></span>
						</div>
						<div class="row line-div">
		  					<c:set var="percent" value="${novice.actualAmount/novice.totalAmount}" />
			  				<div class=" progress">
								<div class="progress-bar progress-bar-small" role="progressbar" aria-valuenow="<fmt:formatNumber value="${percent}" pattern="0.00"/>" aria-valuemin="0" aria-valuemax="100" style="width:<fmt:formatNumber value="${percent}" type="percent" />">
							    	<span class="sr-only"><fmt:formatNumber value="${percent}" type="percent" /></span>
							  	</div>
							</div>
			  			</div>
			  			<%-- <div class="row line-div">
			  				<div class="input-group input-group-sm">
								<span class="input-group-addon"><font id="lowestAmount">${novice.lowestMoney}</font>×</span>
							  	<input id="number" type="text" class="form-control" size="6" value="1" placeholder="份数" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" />
							  	<span class="input-group-addon input-addon">份=<small id="purchaseAmount">${novice.lowestMoney}</small>元</span>
							</div>
			  			</div> --%>
			  			<div class="row">
				  			<a id="novice-${novice.id}" href="javascript:void();" class="btn btn-default btn-block" role="button">&nbsp;</a>
			  			</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
</c:if>

<c:if test="${fn:length(products) > 0}">
<div id="index-product-list" class="container">
	<div class="row">
	<c:forEach var="product" items="${products}" varStatus="s">
		<div class="col-md-3">
  			<div class="thumbnail text-center">
  				<c:if test="${product.category.property == 'NOVICE' }">
	  				<img alt="" style="float: left;" src="${pageContext.request.contextPath}/images/flag_new.png">
  				</c:if>
  				<c:if test="${product.category.property == 'ACTIVITY' }">
	  				<img alt="" style="float: left;" src="${pageContext.request.contextPath}/images/flag_active.png">
  				</c:if>
	  			<div class="page-header text-center">
	  				<h3>${product.name}</h3>
	  			</div>
	  			<div class="row">	
	  				<div class="col-md-4"></div>
	  				<div class="col-md-4"></div>
		  			<div class="col-md-4 text-right product-list-div">
		  				<span class="label label-warning">${product.label}</span>
		  			</div>
	  			</div>
	  			<div class="row product-list-income">	
	  				<span><strong>${product.yearIncome}</strong><small>%</small>
	  				<c:if test="${product.increaseInterest > 0}">+<small>${product.increaseInterest}%</small></c:if>
	  				</span>
	  			</div>
	  			<div class="row product-list-div">	
	  				<span class="font-gray">年化收益率</span>
	  			</div>
	  			<div class="row">
	  				<div class="col-md-1"></div>
	  				<c:set var="percent" value="${product.actualAmount/product.totalAmount}" />
	  				<div class="col-md-10">
		  				<div class="progress">
							<div class="progress-bar progress-bar-small" role="progressbar" aria-valuenow="<fmt:formatNumber value="${percent}" pattern="0.00"/>" aria-valuemin="0" aria-valuemax="100" style="width:<fmt:formatNumber value="${percent}" type="percent" />">
						    	<span class="sr-only"><fmt:formatNumber value="${percent}" type="percent" /></span>
						  	</div>
						</div>
	  				</div>
	  				<div class="col-md-1"></div>
	  			</div>
	  			<div class="row">
	  				<div class="col-md-1"></div>
	  				<div class="col-md-4">
	  					<span><small>${product.financePeriod}天</small></span>
	  				</div>
	  				<div class="col-md-2"></div>
		  			<div class="col-md-4">
		  				<span><small>${product.lowestMoney}元</small></span>
		  			</div>
		  			<div class="col-md-1"></div>
	  			</div>
	  			<div class="row">
	  				<div class="col-md-1"></div>
	  				<div class="col-md-4">理财期限</div>
	  				<div class="col-md-2"></div>
		  			<div class="col-md-4">起投金额</div>
		  			<div class="col-md-1"></div>
	  			</div>
	  			<div class="row">
	  				<div class="col-md-1"></div>
	  				<div class="col-md-10">
	  					<a id="product-${product.id}" href="javascript:void();" class="btn btn-default btn-block" role="button">&nbsp;</a>
	  				</div>
	  				<div class="col-md-1"></div>
	  			</div>
  			</div>
  		</div>
	</c:forEach>
	</div>
</div>
</c:if>

<div class="container">
	<div class="row">
		<div class="col-md-6">
			<div class="panel panel-default">
				<div class="row page-header">
					<div class="col-md-10"><h4>网站公告</h4></div>
					<div class="col-md-2" style="margin-top: 10px;"><a href="${pageContext.request.contextPath}/news/1">更多>></a></div>
				</div>
				<div class="panel-body">
					<c:if test="${fn:length(notices) > 0}">
					<ul class="list-group">
						<c:forEach var="notice" items="${notices}" varStatus="s">
						<li class="list-group-item">
							<a href="${pageContext.request.contextPath}/news/bulletin/detail/${notice.id}" target="_blank">
								<span><img src="${pageContext.request.contextPath}/images/notice_bj.png"/></span>
								<span>${notice.news.title}</span>
								<!--<span><img src="${pageContext.request.contextPath}/images/notice_new.png"/></span>-->
								<span class="fr"><fmt:formatDate value="${notice.createTime}" pattern="yyyy-MM-dd"/> </span>
							</a>
						</li>
						</c:forEach>
					</ul>
					</c:if>
				</div>
			</div>
		</div>
		<div class="col-md-6">
			<div class="panel panel-default">
				<div class="row page-header">
					<div class="col-md-10"><h4>媒体报道</h4></div>
					<div class="col-md-2" style="margin-top: 10px;"><a href="${pageContext.request.contextPath}/news/2">更多>></a></div>
				</div>
				<div class="panel-body">
					<c:if test="${fn:length(newsset) > 0}">
					<ul class="list-group">
						<c:forEach var="news" items="${newsset}" varStatus="s">
						<li class="list-group-item">
							<a href="${pageContext.request.contextPath}/news/bulletin/detail/${news.id}" target="_blank">
								<span><img src="${pageContext.request.contextPath}/images/notice_bj.png"/></span>
								<span>${news.news.title}</span>
								<!--<span><img src="${pageContext.request.contextPath}/images/notice_new.png"/></span>-->
								<span class="fr"><fmt:formatDate value="${news.createTime}" pattern="yyyy-MM-dd"/> </span>
							</a>
						</li>
						</c:forEach>
					</ul>
					</c:if>
				</div>
			</div>
		</div>
	</div>
</div>

<div class="container">
	<div class="panel panel-default">
	  	<div class="row page-header">
	  		<div class="col-md-12"><h4>合作伙伴</h4></div>
	  	</div>
		<div id="index-partner" class="row panel-body text-center">
			<img src="${pageContext.request.contextPath}/images/fy.png"/>
	    	<img src="${pageContext.request.contextPath}/images/cfca.png"/>
	    	<img src="${pageContext.request.contextPath}/images/netease.png"/>
	    	<img src="${pageContext.request.contextPath}/images/sohu.png"/>
	    	<img src="${pageContext.request.contextPath}/images/sina.png"/>
	  	</div>
	</div>
</div>
<!--  
<div class="container">
	<div id="index-guide" class="text-center">
		<a href=""><img src="${pageContext.request.contextPath}/images/guide_1.png"/></a>
		<a href=""><img src="${pageContext.request.contextPath}/images/guide_2.png"/></a>
		<a href=""><img src="${pageContext.request.contextPath}/images/guide_3.png"/></a>
		<a href=""><img src="${pageContext.request.contextPath}/images/guide_4.png"/></a>
		<a href=""><img src="${pageContext.request.contextPath}/images/guide_5.png"/></a>
	</div>
</div>
-->
<jsp:include page="${pageContext.request.contextPath}/footer" flush="true" />
</body>
<script type="text/javascript">
	$(document).ready(function() {
		var nowTime = new Date('${systemTime}');
		$('.carousel').carousel();
		$('#number').change(function() {
			$('#purchaseAmount').html($('#lowestAmount').html()*$('#number').val());
		});
		
		resizeCarousel();
		$(window).resize(function() {
			resizeCarousel();
		});
		
		/*<c:if test="${novice != null}">*/
		countDown();
		function countDown() {
			var raisedTime = new Date('<fmt:formatDate value="${novice.raisedTime}" pattern="yyyy-MM-dd HH:mm:ss"/>');
			var diffMillis = raisedTime.getTime()-nowTime.getTime();
			if(diffMillis >= 1000) {
				$('#novice-${novice.id}').text(getCountDown(diffMillis));
				var interval = window.setInterval(function() {
			        if(diffMillis >= 1000) {
				    	diffMillis = diffMillis-1000;
				    	$('#novice-${novice.id}').unbind();
				    	$('#novice-${novice.id}').attr('class', 'btn btn-default btn-block');
				    	$('#novice-${novice.id}').text(getCountDown(diffMillis));
			        } else {
			        	$('#novice-${novice.id}').bind('click', function() {
  			    			buy('${novice.id}');
  			    		});
			        	$('#novice-${novice.id}').attr('class', 'btn btn-canary btn-block');
  			    		$('#novice-${novice.id}').text('立即购买');
				    	window.clearInterval(interval);
			        }
			    }, 1000);
			} else {
				$('#novice-${novice.id}').bind('click', function() {
		    		buy('${novice.id}');
		    	});
				$('#novice-${novice.id}').attr('class', 'btn btn-canary btn-block');
		    	$('#novice-${novice.id}').text('立即购买');
			}
		}
		/*</c:if>*/
		
		/*<c:if test="${fn:length(products) > 0}">*/
		/*<c:forEach var="product" items="${products}" varStatus="s">*/
		countDown${product.id}();
		function countDown${product.id}() {
			var raisedTime = new Date('<fmt:formatDate value="${product.raisedTime}" pattern="yyyy-MM-dd HH:mm:ss"/>');
			var diffMillis = raisedTime.getTime()-nowTime.getTime();
			if(diffMillis >= 1000) {
				$('#product-${product.id}').text(getCountDown(diffMillis));
				var interval = window.setInterval(function() {
			        if(diffMillis >= 1000) {
				    	diffMillis = diffMillis-1000;
				    	$('#product-${product.id}').attr('class', 'btn btn-default btn-block');
				    	$('#product-${product.id}').text(getCountDown(diffMillis));
			        } else {
			        	$('#product-${product.id}').attr('class', 'btn btn-canary btn-block');
			        	$('#product-${product.id}').attr('href', '${pageContext.request.contextPath}/product/detail/${product.id}');
  			    		$('#product-${product.id}').text('立即购买');
				    	window.clearInterval(interval);
			        }
			    }, 1000);
			} else {
				$('#product-${product.id}').attr('class', 'btn btn-canary btn-block');
				$('#product-${product.id}').attr('href', '${pageContext.request.contextPath}/product/detail/${product.id}');
		    	$('#product-${product.id}').text('立即购买');
			}
		}
		/*</c:forEach>*/
		/*</c:if>*/
		
		function getCountDown(diffMillis) {
			var result = '';
			var day = Math.floor(diffMillis/(1000*60*60*24));
	    	var hour = Math.floor(diffMillis/(1000*60*60)) - (day*24);;
	        var minute = Math.floor(diffMillis/(1000*60)) - (day*24*60) - (hour*60);;
	        var second = Math.floor(diffMillis/1000) - (day*24*60*60) - (hour*60*60) - (minute*60);
	        if(hour <= 9) {
		    	hour = '0'+hour;
	    	}
	    	if(minute <= 9) {
	    		minute = '0'+minute;
	    	}
	    	if(second <= 9){
	    		second = '0'+second;
	    	}
	    	result = hour+'时'+minute+'分'+second+'秒';
	    	if(day > 0) {
	    		result = day+'天'+result;
	    	}
	    	
	    	return result;
		}
	});
	
	function resizeCarousel() {
		window.setTimeout(function(){
			var carouselHeight = $('#index-carousel').height();
			var topHeight = (carouselHeight-240)/2;
			$('.index-bargain-opacity').animate({'top':topHeight}, 'slow');
			$('.index-bargain-div').animate({'margin-top':(topHeight+5)}, 'slow');
			
			//$('.index-bargain-opacity').css('top', topHeight);
			//$('.index-bargain-div').css('margin-top', (topHeight+5));
		}, 200);
	}
	
	function buy(productId) {
		if(productId > 0) {
			window.location.href = '${pageContext.request.contextPath}/product/detail/'+productId;
		}
	}
</script>
</html>