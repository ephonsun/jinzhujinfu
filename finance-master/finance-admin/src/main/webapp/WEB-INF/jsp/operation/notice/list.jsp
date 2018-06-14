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
                	<div class="well">
                		<form id="search" action="${pageContext.request.contextPath}/operation/notice/list" method="post">
                			<input type="hidden" id="page" name="page" value="${page}" />
                    		<input type="hidden" id="size" name="size" value="${size}" />
                      		<div class="input-group pull-left">
								<span>标题</span>
								<input class="horizontal marginTop" id="title" name="title" type="text" value="${title}" />
								<span>类型</span>
	              				<select class="marginTop" id="type" name="type">
              					<c:if test="${type == 0}">
              						<option value="0" selected="selected">全部</option>
              						<option value="1" >网站公告</option>
              						<option value="2" >媒体报道</option>
              					</c:if>
              					<c:if test="${type == 1}">
              						<option value="0">全部</option>
              						<option value="1" selected="selected">网站公告</option>
              						<option value="2" >媒体报道</option>
              					</c:if>
              					<c:if test="${type == 2}">
              						<option value="0">全部</option>
              						<option value="1">网站公告</option>
              						<option value="2" selected="selected" >媒体报道</option>
              					</c:if>
						    	</select>
						    	<a href="javascript:search();"><button type="button" style="margin-top: -10px" class="btn">查询</button></a>
                         	</div>
                    	</form>
			         </div>
                    <div class="row-fluid">
                    	<div class="block">
                            <div class="navbar navbar-inner block-header">
                                 <a href="javascript:add();"><button class="btn btn-lg btn-success">新增</button></a>
                            </div>
                            <div class="block-content collapse in">   
                                <div class="span12">
                                    <table id="notice" cellpadding="0" cellspacing="0" border="0" class="table">
                                         <thead>
                                            <tr>
	                                            <th>序号</th>
	                                            <th>标题</th>
	                                            <th>类型</th>
	                                            <th>来源</th>
	                                            <th>状态</th>
	                                            <th>操作</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                        <c:choose>
                                          <c:when test="${fn:length(notices) > 0}">
                                            <c:forEach var="notice" items="${notices}" varStatus="status">
                                            <tr <c:if test="${status.count%2==0}">class="odd"</c:if>>
                                                <td style="width:30px;">${status.count}</td>
                                                <td>${notice.news.title}</td>
                                                <td>
                                                <c:if test="${notice.type == 1}">网站公告</c:if>
                                                <c:if test="${notice.type == 2}">媒体报道</c:if>
                                                </td>
                                                <td>${notice.news.source}</td>
                                                <td>
                                                <c:if test="${notice.status == 0}">禁用</c:if>
                                                <c:if test="${notice.status == 1}">启用</c:if>
                                                </td>
                                                <td>
                                                <c:if test="${notice.status == 0}">
                                                <a href="javascript:enable(${notice.id}, 1);"><span class="label label-success">启用</span></a>
                                                <a href="javascript:edit(${notice.id});"><span class="label label-info">编辑 </span></a>
                                                </c:if>
                                                <c:if test="${notice.status == 1}">
                                                <a href="javascript:enable(${notice.id}, 0);"><span class="label label-important">禁用</span></a>
                                                </c:if>
                                                </td>
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
                                        		<td colspan="6"><div id="notice-page"></div></td>
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
		var queryString = '?title=${title}&type=${type}&page=${page}&size=${size}';
		$(document).ready(function(){
			$('.breadcrumb').html('<li class="active">运营管理&nbsp;/&nbsp;新闻公告管理</li>');
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
		            onPageClicked: function(event, originalEvent, type, page){
		            	$('#search').attr('action','${pageContext.request.contextPath}/operation/notice/list');
		            	$('#page').val(page);
		                $('#search').submit();
		            }  
			    };
				$('#notice-page').bootstrapPaginator(options);
			}
		});
		
		function add() {
			var base64 = $.base64.encode(queryString);
			window.location.href='${pageContext.request.contextPath}/operation/notice/add/'+base64;
		}
		
		function edit(noticeId) {
			var base64 = $.base64.encode(queryString);
			window.location.href='${pageContext.request.contextPath}/operation/notice/'+noticeId+'/edit/'+base64;
		}
		
		function enable(noticeId, operate){
			$.post('${pageContext.request.contextPath}/operation/notice/'+noticeId+'/'+operate, function(result) {
				if(result) {
					$('#search').attr('action','${pageContext.request.contextPath}/operation/notice/list');
	            	$('#search').submit();
				}
			});
		}
		
		function search() {
			$('#search').attr('action','${pageContext.request.contextPath}/operation/notice/list');
            $('#search').submit(); 
		}
	</script>
</html>