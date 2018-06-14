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
				<a href="${pageContext.request.contextPath}/asset/invitation/1" class="list-group-item">我的邀请</a>
				<a href="javascript:void();" class="list-group-item disabled">账户设置</a>
			</div>
		</div>
		<div class="col-md-10">
		 	<div class="panel panel-default">
				<div class="panel-heading">
					<span class="panel-title">账户设置</span>
				</div>
				<div class="panel-body">
					<div class="row asset-div">
						<div class="col-md-12">
							<span>手机号码：${fn:substring(customer.cellphone, -1, 3)}****${fn:substring(customer.cellphone, 7, -1)}</span>
						</div>
					</div>
					<div class="row asset-div">
						<div class="col-md-12">
							<span>登录密码：已设置</span>
							<a href="${pageContext.request.contextPath}/password/modify" target="_blank">修改</a>
						</div>
					</div>
					<div class="row asset-div">
						<div class="col-md-3">
						<c:if test="${fn:length(customer.name) > 1}">
							<span>真实姓名${fn.length(customer.idcard)}：*${fn:substring(customer.name, 1, -1)}</span>
						</c:if>
						</div>
						<div class="col-md-3">
						<c:if test="${fn:length(customer.idcard) > 1}">
							<span>身份证号：${fn:substring(customer.idcard, -1, 4)}**********${fn:substring(customer.idcard, 14, -1)}</span>
						</c:if>
						</div>
						<div class="col-md-6"></div>
					</div>
					<div class="row asset-div">
						<div class="col-md-12">
						<c:if test="${fn:length(customer.cardNO) > 4}">
							<c:set var="bankCardLength" value="${fn:length(customer.cardNO)}"/>
							银行卡：<span  aria-hidden="true"><img
							 style="height: 25px;"	src="${pageContext.request.contextPath}/images/bank-logo/${customer.bankNO}.png"></span>${customer.bankName}&nbsp;&nbsp;尾号${fn:substring(customer.idcard, bankCardLength-5, bankCardLength)}
						</c:if>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="container container-min-height">
	<div class="row">&nbsp;</div>
</div>
<jsp:include page="${pageContext.request.contextPath}/footer" flush="true" />
</body>
<script type="text/javascript">
	$(document).ready(function(){
		
	});
</script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/adapter.js"></script>
</html>