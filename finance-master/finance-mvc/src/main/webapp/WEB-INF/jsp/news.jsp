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
			  	<a href="javascript:void();" class="list-group-item disabled">新闻公告</a>
			  	<a href="${pageContext.request.contextPath}/faq" class="list-group-item">常见问题</a>
				<a href="${pageContext.request.contextPath}/contactus" class="list-group-item">联系我们</a>
			</div>
		</div>
		<div class="col-md-10">
			<div class="panel panel-default">
				<div class="panel-body">
					<div class="col-md-12"> 
						<ul id="news-tabs" class="nav nav-tabs" role="tablist">
					    	<li role="presentation" <c:if test="${type == 1}"> class="active"</c:if>>
					      		<a href="#notice" id="notice-tab" role="tab" data-toggle="tab" aria-controls="notice" <c:if test="${type == 1}">aria-expanded="true"</c:if>>网站公告</a>
					      	</li>
					      	<li role="presentation" <c:if test="${type == 2}"> class="active"</c:if>>
					      		<a href="#media" id="media-tab" role="tab" data-toggle="tab" aria-controls="media" <c:if test="${type == 2}">aria-expanded="true"</c:if>>媒体报道</a>
					      	</li>
					    </ul>
					    <div id="news-content" class="row tab-content">
					    	<div role="tabpanel" class="tab-pane fade <c:if test="${type == 1}">in active</c:if>" id="notice" aria-labelledby="notice-tab"></div>
					      	<div role="tabpanel" class="tab-pane fade <c:if test="${type == 2}">in active</c:if>" id="media" aria-labelledby="media-tab"></div>
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
<jsp:include page="${pageContext.request.contextPath}/footer" flush="true" />
</body>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap-paginator.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		var category = '${type}';
		var element = category == 2 ? '#media' : '#notice';
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
					window.location.href = '${pageContext.request.contextPath}/news/'+category+'?page='+page;
	            }
		    };
			$('.pagination').bootstrapPaginator(options);
		}
		
		$(element).load('${pageContext.request.contextPath}/news/'+category+'/content #newsset', {page:'${page}', size:'${size}'}, function(responseTxt, statusTxt, xhr) {
			/*
			if(statusTxt == 'success') {
				alert(responseTxt);
			}
			*/
		    if(statusTxt == 'error') {
				alert('Error: '+xhr.status+': '+xhr.statusText);
		    }
		});
		
		$('#news-tabs a').click(function (e) {
			e.preventDefault()
			if(e.target.toString().indexOf('#media') != -1) {
				window.location.href = '${pageContext.request.contextPath}/news/2?page=1';
			} else {
				window.location.href = '${pageContext.request.contextPath}/news/1?page=1';
			}
		});
	});
</script>
</html>