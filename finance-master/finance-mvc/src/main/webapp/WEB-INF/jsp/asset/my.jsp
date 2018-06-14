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
<div id="container" class="container">
	<div class="row">
		<div class="col-md-2">
		 	<div class="list-group">
				<a href="javascript:void();" class="list-group-item disabled">个人中心</a>
			  	<a href="${pageContext.request.contextPath}/asset/transaction" class="list-group-item">交易记录</a>
			  	<a href="${pageContext.request.contextPath}/asset/coupon" class="list-group-item">我的红包</a>
				<a href="${pageContext.request.contextPath}/asset/invitation/1" class="list-group-item">我的邀请</a>
				<a href="${pageContext.request.contextPath}/asset/setting" class="list-group-item">账户设置</a>
			</div>
		</div>
		<div class="col-md-10">
			<div id="asset-my-top" class="row">
				<div class="panel panel-default">
					<div class="panel-body">
						<div class="row">
							<div class="col-md-4">
								<h4>账户余额：<fmt:formatNumber value="${customer.balance}" pattern="#,##0.00"/>元</h4>
							</div>
							<div class="col-md-4">
							<c:choose>
								<c:when test="${empty customer.cardNO}">
									<a href="${pageContext.request.contextPath}/customer/${customer.id}/open" class="btn btn-success" role="button">开户</a>
								</c:when>
								<c:otherwise>
									<a href="${pageContext.request.contextPath}/customer/deposit" class="btn btn-success" role="button">充值</a>
									<a href="${pageContext.request.contextPath}/customer/withdraw" class="btn btn-danger" role="button">提现</a>
								</c:otherwise>
							</c:choose>
							</div>
							<div class="col-md-4"></div>
						</div>
						<div class="row">
							<div class="col-md-4">总资产(元)</div>
							<div class="col-md-4">可用资产(元)</div>
							<div class="col-md-4">累计收益(元)</div>
						</div>
						<div class="row">
							<div class="col-md-4"><strong><fmt:formatNumber value="${sumAsset}" pattern="#,##0.00"/></strong></div>
							<div class="col-md-4"><strong><fmt:formatNumber value="${balance}" pattern="#,##0.00"/></strong></div>
							<div class="col-md-4"><strong><fmt:formatNumber value="${accumulatedIncome}" pattern="#,##0.00"/></strong></div>
						</div>
					</div>
				</div>
			</div>
			<div id="asset-my-body" class="row">
				<div class="panel panel-default">
					<div class="panel-body">
						<div class="col-md-12">
							<ul id="trade-tabs" class="nav nav-tabs" role="tablist">
						    	<li role="presentation" <c:if test="${type == 1}"> class="active"</c:if>>
						      		<a href="#profit" id="profit-tab" role="tab" data-toggle="tab" aria-controls="profit" <c:if test="${type == 1}">aria-expanded="true"</c:if>>收益中</a>
						      	</li>
						      	<li role="presentation" <c:if test="${type == 2}"> class="active"</c:if>>
						      		<a href="#refund" id="refund-tab" role="tab" data-toggle="tab" aria-controls="refund" <c:if test="${type == 2}">aria-expanded="true"</c:if>>已回款</a>
						      	</li>
						    </ul>
						    <div id="trade-content" class="tab-content">
						    	<div role="tabpanel" class="tab-pane fade <c:if test="${type == 1}">in active</c:if>" id="profit" aria-labelledby="profit-tab"></div>
						    	<div role="tabpanel" class="tab-pane fade <c:if test="${type == 2}">in active</c:if>" id="refund" aria-labelledby="refund-tab"></div>
						    </div>
						</div>
					    <div class="text-center">
							<ul class="pagination"></ul>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<jsp:include page="${pageContext.request.contextPath}/footer" flush="true" />
</body>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap-paginator.min.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		var category = '${type}';
		var element = category == 2 ? '#refund' : '#profit';
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
					window.location.href = '${pageContext.request.contextPath}/asset/my/'+category+'?page='+page;
	            }
		    };
			$('.pagination').bootstrapPaginator(options);
		}
		
		$(element).load('${pageContext.request.contextPath}/asset/my/'+category+'/content #orders', {customerId:'${customer.id}', page:'${page}', size:'${size}'}, function(responseTxt, statusTxt, xhr) {
		    if(statusTxt == 'error') {
				alert('Error: '+xhr.status+': '+xhr.statusText);
		    }
		});
		
		$('#trade-tabs a').click(function (e) {
			e.preventDefault()
			if(e.target.toString().indexOf('#refund') != -1) {
				window.location.href = '${pageContext.request.contextPath}/asset/my/2?page=1';
			} else {
				window.location.href = '${pageContext.request.contextPath}/asset/my/1?page=1';
			}
		});
	});
</script>
</html>