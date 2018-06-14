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
                               <div class="navbar navbar-inner block-header" >
                        	        <a href="javascript:add();"><button type="button" class="btn btn-success">新增 </button></a>
		                        </div>
                               <div class="block-content collapse in">
                                <div class="span12">
                                   	<table id="images" class="table">
                                    	<thead>
                                           <tr>
                                               <th>序号</th>
                                               <th>名称</th>
                                               <th>缩略图</th>
                                               <th>显示平台</th>
                                               <th>状态</th>
                                               <th>操作</th>
                                           </tr>
                                       	</thead>
                                       	<tbody>
                                       		<c:choose>
                                       		<c:when test="${fn:length(banneres) > 0}">
                                       			<c:forEach var ="banner" items="${banneres}" varStatus="status">
                                       				<tr <c:if test="${status.count%2==0}">class="odd"</c:if>>
                                                		<td style="width:30px;vertical-align:middle;">${status.count}</td>
                                       					<td style="vertical-align:middle;">${banner.name}</td>
                                       					<td style="vertical-align:middle;">
                                       					<c:if test="${banner.platform == 1}"><img class="thumbnail" src="${banner.url}" style="height:50px;width:150px"/></c:if>
                                       					<c:if test="${banner.platform == 2}"><img class="thumbnail" src="${banner.url}" style="height:50px;width:150px"/></c:if>
                                       					</td>
                                       					<td style="vertical-align:middle;">
                                       					<c:if test="${banner.platform == 1}">APP</c:if>
                                       					<c:if test="${banner.platform == 2}">WEB</c:if>
                                       					</td>
                                       					<td style="vertical-align:middle;">
                                       					<c:if test="${banner.status == 0}">禁用</c:if>
                                       					<c:if test="${banner.status == 1}">启用</c:if>
                                       					</td>
                                       					<td style="vertical-align:middle;">
                                       					<c:if test="${banner.status == 0}">
                                       					<a href="javascript:enable(${banner.id}, 1);"><span class="label label-success">启用</span></a>
                                                		<a href="javascript:edit(${banner.id});"><span class="label label-info">编辑 </span></a>
                                       					</c:if>
                                       					<c:if test="${banner.status == 1}">
                                       					<a href="javascript:enable(${banner.id}, 0);"><span class="label label-important">禁用</span></a>
                                       					</c:if>
                                       					</td>
                                       				</tr>
                                       			</c:forEach>
                                       		</c:when>
                                       		<c:otherwise>
                                       			<tr>
                                       				<td colspan="7">暂时没有数据</td>
                                       			</tr>
                                       		</c:otherwise>
                                       		</c:choose>
                                       	</tbody>
                                       	<tfoot>
                                       		<tr>
                                       			<td colspan="7"><div id="image-page"></div></td>
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
			$('.breadcrumb').html('<li class="active">运营管理 &nbsp;/&nbsp;Banner图管理</li>');
			$('.marginTop').css('margin', '2px auto auto auto');
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
		            	window.location.href = '${pageContext.request.contextPath}/operation/banner/'+base64;
		            }  
			    };
				$('#image-page').bootstrapPaginator(options);
			}
		});
		
		function add() {
			var base64 = $.base64.encode(queryString);
			window.location.href = '${pageContext.request.contextPath}/operation/banner/add/'+base64;
		}
		
		function edit(bannerId) {
			var base64 = $.base64.encode(queryString);
			window.location.href = '${pageContext.request.contextPath}/operation/banner/'+bannerId+'/edit/'+base64;
		}
		
		function enable(bannerId, operate) {
			var base64 = $.base64.encode(queryString);
			$.post('${pageContext.request.contextPath}/operation/banner/'+bannerId+'/'+operate, function(result){
				if (result) {
					window.location.href = '${pageContext.request.contextPath}/operation/banner/'+base64;
				}
			});
		}
	</script>
</html>