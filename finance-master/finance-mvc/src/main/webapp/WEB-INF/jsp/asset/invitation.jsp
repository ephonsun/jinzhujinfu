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
			  	<a href="${pageContext.request.contextPath}/asset/coupon" class="list-group-item">我的红包</a>
				<a href="javascript:void();" class="list-group-item disabled">我的邀请</a>
				<a href="${pageContext.request.contextPath}/asset/setting" class="list-group-item">账户设置</a>
			</div>
		</div>
		<div class="col-md-10">
			<div class="row">
				<div class="panel panel-default">
					<div class="panel-heading">
						<span class="panel-title">我的专属链接</span>
					</div>
					<div class="panel-body">
						大家都在使用金竹金服理财，注册并完成交易最高可获得1000元红包哦。<br/>
						https://www.jinzhujinfu.com/register/${base64}
					</div>
				</div>
			</div>
			<div class="row">
				<div class="panel panel-default">
					<div class="panel-heading">
						<span class="panel-title">我的邀请</span>
					</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-md-3">
								<span>已邀请小伙伴${total}人</span>
							</div>
							<div class="col-md-3">
								<div class="input-group input-group-sm">
									<span class="input-group-addon">邀请红包</span>
								  	<input type="text" class="form-control" size="6" value="${total}" placeholder="份数" readonly/>
								  	<span class="input-group-addon">×20</span>
								</div>
							</div>
							<div class="col-md-6"></div>
						</div>
					</div>
				</div>
			</div>
			<div id="asset-my-body" class="row">
				<div class="panel panel-default">
					<div class="panel-heading">
						<span class="panel-title">邀请明细</span>
					</div>
					<div class="panel-body">
						<ul id="invitation-tabs" class="nav nav-tabs" role="tablist">
					    	<li role="presentation" <c:if test="${type == 1}"> class="active"</c:if>>
					      		<a href="#record" id="record-tab" role="tab" data-toggle="tab" aria-controls="record" <c:if test="${type == 1}">aria-expanded="true"</c:if>>邀请记录</a>
					      	</li>
					      	<li role="presentation" <c:if test="${type == 2}"> class="active"</c:if>>
					      		<a href="#coupon" id="coupon-tab" role="tab" data-toggle="tab" aria-controls="coupon" <c:if test="${type == 2}">aria-expanded="true"</c:if>>邀请红包</a>
					      	</li>
					    </ul>
					    <div id="invitation-content" class="tab-content">
					    	<div role="tabpanel" class="tab-pane fade <c:if test="${type == 1}">in active</c:if>" id="record" aria-labelledby="record-tab"></div>
					      	<div role="tabpanel" class="tab-pane fade <c:if test="${type == 2}">in active</c:if>" id="coupon" aria-labelledby="coupon-tab"></div>
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
		var element = category == 2 ? '#coupon' : '#record';
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
					window.location.href = '${pageContext.request.contextPath}/asset/invitation/'+category+'?page='+page;
	            }
		    };
			$('.pagination').bootstrapPaginator(options);
		}
		
		$(element).load('${pageContext.request.contextPath}/asset/invitation/'+category+'/content #invitations', {customerId:'${customer.id}', cellphone:'${customer.cellphone}', page:'${page}', size:'${size}'}, function(responseTxt, statusTxt, xhr) {
		    if(statusTxt == 'error') {
				alert('Error: '+xhr.status+': '+xhr.statusText);
		    }
		});
		
		$('#invitation-tabs a').click(function (e) {
			e.preventDefault()
			if(e.target.toString().indexOf('#coupon') != -1) {
				window.location.href = '${pageContext.request.contextPath}/asset/invitation/2?page=1';
			} else {
				window.location.href = '${pageContext.request.contextPath}/asset/invitation/1?page=1';
			}
		});
	});
</script>
</html>