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
                    	<div class="well">
                           	<form id="search" action="${pageContext.request.contextPath}/operation/channel/list" method="post">
                           		<input type="hidden" id="page" name="page" value="${page}" />
                           		<input type="hidden" id="size" name="size" value="${size}" />
                           		<div class="input-group pull-left">
			                    	<span>名称</span>
			                        <input class="horizontal marginTop" id="name" name="name" type="text" value="${name}" />
	 								<input type="submit" style="margin-top: -10px" value="查询" class="btn btn-default" />
			                    </div>
                        	</form>
                        </div>
                    	<div class="block">
                            <div id="authorityButton" class="navbar navbar-inner block-header">&nbsp;</div>
                            <div class="block-content collapse in">   
                                <div class="span12">
                                   <table class="table">
                                       <thead>
                                           <tr>
                                               <th>序号</th>
                                               <th>名称</th>
                                               <th>注册地址</th>
                                               <th>下载地址</th>
                                               <th>备注</th>
                                               <th>状态</th>
                                               <th>操作</th>
                                           </tr>
                                       </thead>
                                       <tbody>
                                       	<c:choose>
                                       		<c:when test="${fn:length(channels) > 0}">
                                       			<c:forEach var ="channel" items="${channels}" varStatus="status">
                                       				<tr <c:if test="${status.count%2==0}">class="odd"</c:if>>
                                                		<td style="width:30px;">${status.count}</td>
                                       					<td>${channel.name}</td>
                                       					<td class="vertical">
                                       					<c:choose>
                                       					  <c:when test="${!empty channel.banner}">
                                       					    <a href="${channel.url}" target="_blank">${channel.url}</a>
                                       					  </c:when>
                                       					  <c:otherwise>
                                       					    <a href="${channel.url}" target="_blank">${channel.url}</a>
                                       					  </c:otherwise>
                                       					</c:choose>
                                       					</td>
                                       					<td class="vertical">
                                       					    <a href="${channel.downloadUrl}" target="_blank">${channel.downloadUrl}</a>
                                       					</td>
                                       					<td>${channel.remark}</td>
                                       					<td>
                                       					<c:if test="${channel.status == 0}">禁用</c:if>
                                       					<c:if test="${channel.status == 1}">启用</c:if>
                                       					</td>
                                       					<td>
                                       					<c:if test="${channel.status == 0}">
                                       					<c:choose>
                                       					  <c:when test="${!empty channel.name}">
                                       					    <a href="javascript:enable(${channel.id}, 1);"><span class="label label-success">启用</span></a>
                                       					  </c:when>
                                       					  <c:otherwise>
                                       					    <a href="javascript:edit(${channel.id});"><span class="label label-info">编辑 </span></a>
                                       					  </c:otherwise>
                                       					</c:choose>
		                                                </c:if>
		                                                <c:if test="${channel.status == 1}">
		                                                	<a href="javascript:enable(${channel.id}, 0);"><span class="label label-important">禁用</span></a>
		                                                </c:if>
                                       					</td>
                                       				</tr>
                                       			</c:forEach>
                                       		</c:when>
                                       		<c:otherwise>
                                       			<tr>
                                       				<td colspan="7">暂时没有渠道信息 </td>
                                       			</tr>
                                       		</c:otherwise>
                                       	</c:choose>
                                       </tbody>
                                       <tfoot>
                                       	<tr>
                                       		<td colspan="7"><div id="version-page"></div></td>
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
		var queryString = '?page=${page}&size=${size}&name=${name}';
		$(document).ready(function(){
			$('.breadcrumb').html('<li class="active">运营管理&nbsp;/&nbsp;运营渠道管理</li>');
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
		            	$('#search').attr('action','${pageContext.request.contextPath}/operation/channel/list');
		            	$('#page').val(page);
		            	$('#search').submit();
		            }  
			    };
				$('#version-page').bootstrapPaginator(options);
			}
		});
		
		function edit(channelId) {
			var base64 = $.base64.encode(queryString);
			window.location.href = '${pageContext.request.contextPath}/operation/channel/'+channelId+'/edit/'+base64;
		}
		
		function enable(channelId, operate) {
			$.post('${pageContext.request.contextPath}/operation/channel/'+channelId+'/'+operate, function(result){
				if(result) {
					$('#search').attr('action','${pageContext.request.contextPath}/operation/channel/list');
	            	$('#search').submit();
				}
			});
		}
	</script>
</html>