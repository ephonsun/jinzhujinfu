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
<c:if test="${fn:length(activities) > 0}">
<div id="activity-list" class="container">
	<c:forEach var="activity" items="${activities}" varStatus="s">
	<c:if test="${s.first || s.count%3 == 1}">
	<c:out value="<div class='row'>" escapeXml="false"></c:out>
	</c:if>
		<div class="col-md-4">
  			<div class="thumbnail">
  				<a href="${activity.url}" target="_blank">
  					<img src="${activity.image}" alt="${activity.title}" />
  				</a>
	  			<div class="caption">
	  				<h4>${activity.title}</h4>
	  				<p>${activity.introduction}</p>
	  				<p>${activity.beginDate}至${activity.endDate}</p>
	  			</div>
  			</div>
  		</div>
  	<c:if test="${s.last || (s.count+1)%3 == 1}">
  	<c:out value="</div>" escapeXml="false"></c:out>
  	</c:if>
	</c:forEach>
</div>
<div class="container">
	<div class="row text-center">
		<ul class="pagination"></ul>
	</div>
</div>
</c:if>
<div class="container">
	<div class="row">&nbsp;</div>
</div>
<jsp:include page="${pageContext.request.contextPath}/footer" flush="true" />
</body>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap-paginator.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
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
	            	window.location.href = '${pageContext.request.contextPath}/activity?page='+page;
	            }
		    };
			$('.pagination').bootstrapPaginator(options);
		}
	});
</script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/adapter.js"></script>
</html>