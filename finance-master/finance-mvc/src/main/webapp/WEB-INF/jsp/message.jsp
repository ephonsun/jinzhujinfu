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
<c:choose>
	<c:when test="${function == 'register'}">
	<div class="container">
		<div id="tip-nav" class="row">
			<div class="col-md-2"></div>
			<div class="col-md-8">
				<div class="row">
					<div class="col-md-2 text-center tip-nav-default">
						<span>1</span>
					</div>
					<div class="col-md-3 text-center">
						<div class="tip-nav-line"></div>
					</div>
					<div class="col-md-2 text-center tip-nav-default">
						<span>2</span>
					</div>
					<div class="col-md-3">
						<div class="tip-nav-line"></div>
					</div>
					<div class="col-md-2 text-center tip-nav-active">
						<span>3</span>
					</div>
				</div>
				<div class="row">
					<div class="col-md-2 text-center">
						<span class="text-muted">填写手机号</span>
					</div>
					<div class="col-md-3"></div>
					<div class="col-md-2 text-center">
						<span class="text-muted">设置密码</span>
					</div>
					<div class="col-md-3"></div>
					<div class="col-md-2 text-center">
						<span class="text-primary">注册成功</span>
					</div>
				</div>
			</div>
			<div class="col-md-2"></div>
		</div>
	</div>
	<div id="message-container" class="container">
		<div class="row">
			<div class="col-md-4"></div>
			<div class="col-md-4">
				<div class="alert alert-danger" role="alert">${message}</div>
				<div>
				<c:if test="${result == 'success'}">
					<a class="btn btn-canary" href="${pageContext.request.contextPath}/login" role="button">马上登录</a>
				</c:if>
				<c:if test="${result == 'failure'}">
					<a class="btn btn-canary" href="${pageContext.request.contextPath}/register" role="button">重新注册</a>
				</c:if>
				</div>
			</div>
			<div class="col-md-4"></div>
		</div>
	</div>
	</c:when>
	<c:when test="${function == 'reset-password'}">
	<div class="container">
		<div id="tip-nav" class="row">
			<div class="col-md-2"></div>
			<div class="col-md-8">
				<div class="row">
					<div class="col-md-2 text-center tip-nav-default">
						<span>1</span>
					</div>
					<div class="col-md-3 text-center">
						<div class="tip-nav-line"></div>
					</div>
					<div class="col-md-2 text-center tip-nav-default">
						<span>2</span>
					</div>
					<div class="col-md-3">
						<div class="tip-nav-line"></div>
					</div>
					<div class="col-md-2 text-center tip-nav-active">
						<span>3</span>
					</div>
				</div>
				<div class="row">
					<div class="col-md-2 text-center">
						<span class="text-muted">填写手机号</span>
					</div>
					<div class="col-md-3"></div>
					<div class="col-md-2 text-center">
						<span class="text-muted">设置密码</span>
					</div>
					<div class="col-md-3"></div>
					<div class="col-md-2 text-center">
						<span class="text-primary">重置成功</span>
					</div>
				</div>
			</div>
			<div class="col-md-2"></div>
		</div>
	</div>
	<div id="message-container" class="container">
		<div class="row">
			<div class="col-md-4"></div>
			<div class="col-md-4">
				<div class="alert alert-danger" role="alert">${message}</div>
				<div>
				<c:if test="${result == 'success'}">
					<a class="btn btn-canary" href="${pageContext.request.contextPath}/login" role="button">马上登录</a>
				</c:if>
				<c:if test="${result == 'failure'}">
					<a class="btn btn-canary" href="${pageContext.request.contextPath}/" role="button">返回首页</a>
				</c:if>
				</div>
			</div>
			<div class="col-md-4"></div>
		</div>
	</div>
	</c:when>
	<c:when test="${function == 'customer-bank'}">
	<div id="message-container" class="container">
		<div class="row">
			<div class="col-md-4"></div>
			<div class="col-md-4">
				<div class="alert alert-danger" role="alert">${message}</div>
				<c:if test="${result == 'success'}">
					<a class="btn btn-canary" href="${pageContext.request.contextPath}/asset/my/1" role="button">我的资产</a>
				</c:if>
				<c:if test="${result == 'failure'}">
					<a class="btn btn-canary" href="${referer}" role="button">继续操作</a>
					<a class="btn btn-canary" href="${pageContext.request.contextPath}/" role="button">返回首页</a>
				</c:if>
			</div>
			<div class="col-md-4"></div>
		</div>
	</div>
	</c:when>
	<c:otherwise>
	<div id="message-container" class="container">
		<div class="row">
			<div class="col-md-4"></div>
			<div class="col-md-4">
				<div class="alert alert-danger" role="alert">${message}</div>
				<a class="btn btn-canary" href="${pageContext.request.contextPath}/" role="button">返回首页</a>
			</div>
			<div class="col-md-4"></div>
		</div>
	</div>
	</c:otherwise>
</c:choose>
<jsp:include page="${pageContext.request.contextPath}/footer" flush="true" />
</body>
<script type="text/javascript">
	$(document).ready(function(){
		
	});
</script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/adapter.js"></script>
</html>