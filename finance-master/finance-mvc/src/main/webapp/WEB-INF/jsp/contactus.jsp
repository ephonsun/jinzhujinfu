<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
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
				<a href="${pageContext.request.contextPath}/aboutus" class="list-group-item">企业介绍</a>
			  	<a href="${pageContext.request.contextPath}/news/1" class="list-group-item">新闻公告</a>
			  	<a href="${pageContext.request.contextPath}/faq" class="list-group-item">常见问题</a>
				<a href="javascript:void();" class="list-group-item disabled">联系我们</a>
			</div>
		</div>
		<div class="col-md-10">
		 	<div class="panel panel-default">
				<img class="img-responsive center-block" src="${pageContext.request.contextPath}/images/map.png" alt="..."/>
				<div class="row">&nbsp;</div>
			  	<div class="panel-body">
			  		<div class="page-header">
					  <h5><span class="glyphicon glyphicon-globe" aria-hidden="true"></span>客服邮箱：<s:message code="email"/></h5>
					</div>
			  		<div class="page-header">
					  <h5><span class="glyphicon glyphicon-earphone" aria-hidden="true"></span>客服热线：<s:message code="tellphone"/></h5>
					</div>
			  		<div class="page-header">
					  <h5><span class="glyphicon glyphicon-map-marker" aria-hidden="true"></span>公司地址：<s:message code="address"/></h5>
					</div>
					<div class="row">&nbsp;</div>
					<div class="col-md-6 text-center">
					    <img class="img-rounded" width="120" src="${pageContext.request.contextPath}/images/barcode.png" alt="..."/>
					</div>
					<div class="col-md-6 text-center">
					    <img class="img-rounded" width="120" src="${pageContext.request.contextPath}/images/barcode.png" alt="..."/>
					</div>
			 	</div>
			</div>
		</div>
	</div>
</div>
<jsp:include page="${pageContext.request.contextPath}/footer" flush="true" />
</body>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/adapter.js"></script>
</html>