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
				<a href="${pageContext.request.contextPath}/aboutus" class="list-group-item">企业介绍</a>
			  	<a href="${pageContext.request.contextPath}/news/1" class="list-group-item">新闻公告</a>
			  	<a href="javascript:void();" class="list-group-item disabled">常见问题</a>
				<a href="${pageContext.request.contextPath}/contactus" class="list-group-item">联系我们</a>
			</div>
		</div>
		<div class="col-md-10">
			<c:if test="${fn:length(faqs) > 0}">
		 	<div class="panel-group" id="faq-list" role="tablist" aria-multiselectable="true">
		 		<c:forEach var="faq" items="${faqs}" varStatus="s">
		 		<div class="panel panel-default">
				    <div id="heading${faq.id}" class="panel-heading" role="tab">
				    	<h4 class="panel-title">
				        	<a role="button" data-toggle="collapse" data-parent="#faq-list" href="#collapse${faq.id}" aria-controls="collapse${faq.id}" aria-expanded="true">
				          		${faq.ask}
				        	</a>
				      	</h4>
				    </div>
				    <div id="collapse${faq.id}" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading${faq.id}">
				    	<div class="panel-body">
				       	 	${faq.question}
				      	</div>
				    </div>
			  	</div>
		 		</c:forEach>
			</div>
			</c:if>
		</div>
	</div>
</div>
<jsp:include page="${pageContext.request.contextPath}/footer" flush="true" />
</body>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/adapter.js"></script>
</html>