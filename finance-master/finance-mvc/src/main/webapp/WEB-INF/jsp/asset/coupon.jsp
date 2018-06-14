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
<div class="container">
	<div class="row">
		<div class="col-md-2">
		 	<div class="list-group">
				<a href="${pageContext.request.contextPath}/asset/my/1" class="list-group-item">个人中心</a>
			  	<a href="${pageContext.request.contextPath}/asset/transaction" class="list-group-item">交易记录</a>
			  	<a href="javascript:void();" class="list-group-item disabled">我的红包</a>
				<a href="${pageContext.request.contextPath}/asset/invitation/1" class="list-group-item">我的邀请</a>
				<a href="${pageContext.request.contextPath}/asset/setting" class="list-group-item">账户设置</a>
			</div>
		</div>
		<div id="asset-coupon" class="col-md-10">
			<c:if test="${fn:length(coupons) > 0}">
				<c:forEach var="coupon" items="${coupons}" varStatus="s">
				<c:if test="${s.first || s.count%2 == 1}">
				<c:out value="<div class='row'>" escapeXml="false"></c:out>
				</c:if>
				<div class="col-md-6">
		  			<div class="thumbnail">
			  			<div class="caption">
			  				<p><b>${coupon.coupon.name}</b><strong>¥${coupon.amount}元</strong><p>
			  				<p>使用条件：${coupon.coupon.condition}</p>
			  				<p>使用期限：<c:if test="${coupon.coupon.expiryDate == '2050-12-31' }">永久有效</c:if>
			  				<c:if test="${coupon.coupon.expiryDate != '2050-12-31' }">${coupon.coupon.expiryDate}到期</c:if></p>
			  			</div>
		  			</div>
		  		</div>
				<c:if test="${s.last || (s.count+1)%2 == 1}">
				<c:out value="</div>" escapeXml="false"></c:out>
				</c:if>
				</c:forEach>
			</c:if>
			<div class="row text-center">
				<ul class="pagination"></ul>
			</div>
		</div>
	</div>
</div>
<jsp:include page="${pageContext.request.contextPath}/footer" flush="true" />
</body>
<script type="text/javascript">
	$(document).ready(function(){
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
					window.location.href = '${pageContext.request.contextPath}/asset/coupon/?page='+page;
	            }
		    };
			$('.pagination').bootstrapPaginator(options);
		}
	});
</script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/adapter.js"></script>
</html>