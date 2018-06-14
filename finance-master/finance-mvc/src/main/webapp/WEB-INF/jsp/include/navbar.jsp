<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<nav class="navbar navbar-default navbar-fixed-top">
	<div class="container">
		<div class="navbar-header">
			<a class="navbar-brand" href="${pageContext.request.contextPath}/">金竹金服</a>
			<c:choose>
				<c:when test="${isLogin == true}">
					<span class="navbar-brand-small" style="">${fn:substring(cellphone, -1, 3)}****${fn:substring(cellphone, 7, -1)}，<a href="${pageContext.request.contextPath}/logout">退出</a></span>
				</c:when>
				<c:otherwise>
					<ul class="nav navbar-nav">
						<li id="login-li"><a href="${pageContext.request.contextPath}/login">登录</a></li>
	                	<li id="register-li"><a href="${pageContext.request.contextPath}/register">注册</a></li>
	        		</ul>
				</c:otherwise>
			</c:choose>
		</div>
		<div class="collapse navbar-collapse">
			<ul class="nav navbar-nav navbar-right">
                <li><a href="${pageContext.request.contextPath}/product/list">我要投资</a></li>
  	         	<li><a href="${pageContext.request.contextPath}/activity">活动专区</a></li>
  	         	<li><a href="${pageContext.request.contextPath}/safety">安全保障</a></li>
  	         	<li><a href="${pageContext.request.contextPath}/aboutus">关于我们</a></li>
  	         	<li><a href="${pageContext.request.contextPath}/asset/my/1"><span class="text-danger"><strong>我的资产</strong></span></a></li>
  	         	<!--  
  	         	<li><a href="#">|</a></li>
  	         	<li>
  	         		<a href="#">
  	         			<span class="glyphicon glyphicon-download-alt" aria-hidden="true"></span>
  	         			<span class="glyphicon glyphicon-qrcode" aria-hidden="true"></span>
  	         		</a>
  	         	</li>
  	         	-->
        	</ul>
		</div>
	</div>
</nav>