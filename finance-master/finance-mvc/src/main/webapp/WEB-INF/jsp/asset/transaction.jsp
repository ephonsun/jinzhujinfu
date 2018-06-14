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
			  	<a href="javascript:void();" class="list-group-item disabled">交易记录</a>
			  	<a href="${pageContext.request.contextPath}/asset/coupon" class="list-group-item">我的红包</a>
				<a href="${pageContext.request.contextPath}/asset/invitation/1" class="list-group-item">我的邀请</a>
				<a href="${pageContext.request.contextPath}/asset/setting" class="list-group-item">账户设置</a>
			</div>
		</div>
		<div class="col-md-10">
		 	<div class="panel panel-default">
			  	<div class="panel-body">
			  		<div>
			  			<table class="table table-striped">
							<thead>
								<tr>
									<th>交易时间</th>
									<th>交易类型</th>
									<th>金额(元)</th>
									<th>交易状态</th>
								</tr>
							</thead>
							<tbody>
							<c:if test="${fn:length(transactions) > 0}">
								<c:forEach var="transaction" items="${transactions}" varStatus="s">
								<tr>
									<td><fmt:formatDate value="${transaction.createTime}" type="both" pattern="yyyy-MM-dd HH:mm:ss" /></td>
									<td>
										${transaction.responseDesc}
									<%-- <c:choose>
										<c:when test="${transaction.category == 1}">充值</c:when>
										<c:when test="${transaction.category == 2}">提现</c:when>
										<c:when test="${transaction.category == 3}">购买</c:when>
										<c:when test="${transaction.category == 4}">回款</c:when>
										<c:otherwise>其它</c:otherwise>
									</c:choose> --%>
									</td>
									<td><fmt:formatNumber value="${transaction.amount/100}" pattern="#,##0.00" type="currency"/></td>
									<td title="${transaction.responseDesc}">
									<c:choose>
										<c:when test="${transaction.responseCode == '0000'}">成功</c:when>
										<c:otherwise>失败</c:otherwise>
									</c:choose>
									</td>
								</tr>
								</c:forEach>
							</c:if>
							</tbody>
						</table>
			  		</div>
			  		<c:if test="${fn:length(transactions) > 0}">
			  		<div class="text-center">
						<ul class="pagination"></ul>
					</div>
					</c:if>
			 	</div>
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
					window.location.href = '${pageContext.request.contextPath}/asset/transaction/?page='+page;
	            }
		    };
			$('.pagination').bootstrapPaginator(options);
		}
	});
</script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/adapter.js"></script>
</html>