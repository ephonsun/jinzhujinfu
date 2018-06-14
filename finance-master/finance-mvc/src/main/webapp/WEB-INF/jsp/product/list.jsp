<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="zh-CN">
<jsp:include page="${pageContext.request.contextPath}/header" flush="true" />
<body>
<jsp:include page="${pageContext.request.contextPath}/navbar" flush="true" />
<div class="container">
	<div class="row">&nbsp;</div>
</div>
<!--  
<div id="product-search" class="container">
	<div class="panel panel-default">
		<div class="panel-body">
			<div class="row">
				<span>期限：</span>
				<a class="main_bg" href="javascript:research(0);">全部</a>
				<a href="javascript:research(30);">一个月以内</a>
				<a href="javascript:research(180);">1-6个月</a>
				<a href="javascript:research(365);">6-12个月</a>
				<a href="javascript:research(-1);">12个月以上</a>
			</div>
			<div class="row">
				<span>产品：</span>
				<a class="main_bg" href="#">全部</a>
				<a href="#">融资租赁</a>
				<a href="#">安心计划</a>
				<a href="#">车贷宝</a>
				<a href="#">房贷宝</a>
			</div>
		</div>
	</div>
</div>
-->
<c:if test="${fn:length(products) > 0}">
<div id="product-list" class="container">
	<c:forEach var="product" items="${products}" varStatus="s">
	<c:if test="${s.first || s.count%4 == 1}">
	<c:out value="<div class='row'>" escapeXml="false"></c:out>
	</c:if>
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
	  				<div class="col-md-10">
	  					<c:set var="percent" value="${product.actualAmount/product.totalAmount}" />
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
	<c:if test="${s.last || (s.count+1)%4 == 1}">
	<c:out value="</div>" escapeXml="false"></c:out>
	</c:if>
	</c:forEach>
</div>
<div class="container">
	<div class="row text-center">
		<ul class="pagination"></ul>
	</div>
</div>
</c:if>
<div class="container">
	<div class="row">&nbsp;</div>
</div>
<jsp:include page="${pageContext.request.contextPath}/footer" flush="true" />
</body>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap-paginator.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		var nowTime = new Date('${systemTime}');
		var totalPages = '${pages}';
		if(totalPages > 0) {
			var options = {
		        currentPage: '${page}',
		        totalPages: totalPages,
		        size: 'small',
				alignment: 'center',
				bootstrapMajorVersion:3,
				tooltipTitles: function(type, page, current) {
					switch (type) {
				    case 'first':
				        return '首页';
				    case 'prev':
				        return '上一页';
				    case 'next':
				        return '下一页';
				    case 'last':
				        return '末页';
				    case 'page':
				    	return (page === current) ? '当前页 '+page : '跳到 '+page;
				    }
				},
				onPageClicked: function(event, originalEvent, type, page) {
	            	window.location.href = '${pageContext.request.contextPath}/product/list?page='+page+'&category=${category}&start=${start}&end=${end}';
	            }
		    };
			$('.pagination').bootstrapPaginator(options);
		}
		
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
</script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/adapter.js"></script>
</html>