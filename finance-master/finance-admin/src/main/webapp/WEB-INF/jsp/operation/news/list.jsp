<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<jsp:include page="${pageContext.request.contextPath}/header" flush="true" />
	<body>
		<jsp:include page="${pageContext.request.contextPath}/menu" flush="true" />
        <div class="container-fluid">
            <div class="row-fluid">
                <jsp:include page="${pageContext.request.contextPath}/sidebar" flush="true" />
                <div class="span10" id="content">
                	<jsp:include page="${pageContext.request.contextPath}/breadcrumb" flush="true" />
                    <div class="row-fluid">
                    	<div class="block">
                            <div class="navbar navbar-inner block-header">
                            	<a href="javascript:add();"><button type="button" class="btn btn-success">新增 </button></a>
                            </div>
                            <div class="block-content collapse in">   
                                <div class="span12">
                                    <table cellpadding="0" cellspacing="0" border="0" class="table">
                                         <thead>
                                            <tr>
	                                            <th>序号</th>
	                                            <th>标题</th>
	                                            <th>来源</th>
	                                            <th>日期</th>
	                                            <th>地址</th>
	                                            <th>操作</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                        <c:choose>
                                          <c:when test="${fn:length(newses) > 0}">
                                            <c:forEach var="news" items="${newses}" varStatus="status">
                                           <tr <c:if test="${status.count%2==0}">class="odd"</c:if>>
                                                <td style="width:30px;">${status.count}</td>
                                                <td title="${news.url}">${news.title}</td>
                                                <td>${news.source}</td>
                                                <td>${news.newsDate}</td>
                                                <td class="vertical">
                                 				<c:choose>
                                 				  <c:when test="${!empty news.image}">
                                 				    <a href="${news.url}" target="_blank"><img class="thumbnail" src="${news.image}" alt="${news.url}" style="height:40px;width:60px;"/></a>
                                 				  </c:when>
                                 				  <c:otherwise>
                                 				    <a href="${news.url}" target="_blank">${news.url}</a>
                                 				  </c:otherwise>
                                 				</c:choose>
                                    			</td>
                                                <td><a href="javascript:edit(${news.id});"><span class="label label-info">编辑 </span></a></td>
                                            </tr>
                                            </c:forEach>
                                          </c:when>
                                          <c:otherwise>
                                        	<tr>
                                                <td colspan="6">暂时还没有数据</td>
                                            </tr>  
                                          </c:otherwise>
                                        </c:choose>
                                        </tbody>
                                        <tfoot>
                                        	<tr>
                                        		<td colspan="6"><div id="news-page"></div></td>
                                        	</tr>
                                        </tfoot>
                                    </table>
                                </div>
                            </div>
                        </div>		
                    </div>
                </div>
            </div>
        </div>
		<jsp:include page="${pageContext.request.contextPath}/footer" flush="true" />
	</body>
	<script type="text/javascript">
		var queryString = '?page=${page}&size=${size}';
		$(document).ready(function(){
			$('.breadcrumb').html('<li class="active">运营管理&nbsp;/&nbsp;新闻素材管理</li>');
			var totalPages = '${pages}';
			if(totalPages > 0) {
				var options = {
			        currentPage: '${page}',
			        totalPages: totalPages,
			        size: 'normal',  
					alignment: 'center',
					tooltipTitles: function (type, page, current) {
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
					        return (page === current) ? '当前页 ' + page : '跳到 ' + page;
					    }
					},
		            onPageClicked: function(event, originalEvent, type, page) {
		            	var base64 = $.base64.encode('?page='+page+'&size=${size}');
		            	window.location.href = '${pageContext.request.contextPath}/operation/news/'+base64;
		            }  
			    };
				$('#news-page').bootstrapPaginator(options);
			}
		});
		
		function add() {
			var base64 = $.base64.encode(queryString);
			window.location.href = '${pageContext.request.contextPath}/operation/news/add/'+base64;
		}
		
		function edit(newsId) {
			var base64 = $.base64.encode(queryString);
			window.location.href='${pageContext.request.contextPath}/operation/news/'+newsId+'/edit/'+base64;
		}
	</script>
</html>