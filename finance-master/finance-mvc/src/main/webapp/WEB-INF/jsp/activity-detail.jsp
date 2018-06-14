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
<c:if test="${activity != null}">
<div class="container">
	<div class="row">
	 	<div class="panel panel-default">
			<div class="panel-heading text-center">
				<span class="panel-title">${activity.title}</span>
			</div>
		  	<div class="panel-body">
		  		<div>
		  			<span>新闻时间：${activity.beginDate}至${activity.endDate}</span>
		  		</div>
		  		<div>${activity.content}</div>
		  		<img class="thumbnail" src="${activity.image}" alt="${activity.title}"/>
		 	</div>
		</div>
	</div>
</div>
</c:if>
<jsp:include page="${pageContext.request.contextPath}/footer" flush="true" />
</body>
<script type="text/javascript">
	$(document).ready(function(){
		
	});
</script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/adapter.js"></script>
</html>