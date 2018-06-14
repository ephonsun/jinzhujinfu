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
                <div class="span10">
                    <div class="row-fluid">
                    <jsp:include page="${pageContext.request.contextPath}/breadcrumb" flush="true" />
                       	<div class="well">
		                    <form id="search" action="${pageContext.request.contextPath}/operation/version/list" method="post">
			                	<input type="hidden" id="page" name="page" value="${page}" />
			                	<input type="hidden" id="size" name="size" value="${size}" />
			                   	<div class="input-group pull-left">
			                    	<span>版本号:</span>
			                        <input class="horizontal marginTop" id="version" name="version" type="text" value="${version}" onkeyup="this.value=this.value.replace(/(^\s*)/g,'')" onblur="this.value=this.value.replace(/(\s*$)/g,'')"/>
			                        <span>更新内容:</span>
									<input class="horizontal marginTop" id="content" name="content" type="text"  value="${content}" onkeyup="this.value=this.value.replace(/(^\s*)/g,'')" onblur="this.value=this.value.replace(/(\s*$)/g,'')"/>
	                              	<input type="submit" style="margin-top: -10px" value="查询" class="btn btn-default" />
			                    </div>
			                </form>
	                    </div>
                       	<div class="block">
                            <div class="navbar navbar-inner block-header">
	                            <a href="javascript:add();"><button type="button" class="btn btn-success">新增 </button></a>
                            </div>
                            <div class="block-content collapse in">
                                <div class="span12">
                                   <table class="table">
                                       <thead>
                                           <tr>
                                               <th>序号</th>
                                               <th>版本号</th>
                                               <th>升级类型</th>
                                               <th>更新内容</th>
                                               <th>状态</th>
                                               <th>操作</th>
                                           </tr>
                                       </thead>
                                       <tbody>
                                       	<c:choose>
                                       		<c:when test="${fn:length(versions) > 0}">
                                       			<c:forEach var ="version" items="${versions}" varStatus="status">
                                       				<tr <c:if test="${status.count%2==0}">class="odd"</c:if>>
                                                		<td style="width:30px;">${status.count}</td>
                                       					<td>${version.version}</td>
                                       					<td>
                                       					<c:if test="${version.type == 0}">可选升级</c:if>
                                       					<c:if test="${version.type == 1}">强制升级</c:if>
                                       					</td>
                                       					<td>${version.content}</td>
                                       					<td>
                                       					<c:if test="${version.status == 0}">禁用</c:if>
                                       					<c:if test="${version.status == 1}">启用</c:if>
                                       					</td>
                                       					<td>
                                       					<c:if test="${version.status == 0}">
		                                                <a href="javascript:enable(${version.id}, 1);"><span class="label label-success">启用</span></a>
		                                                <a href="javascript:edit(${version.id});"><span class="label label-info">编辑 </span></a>
		                                                </c:if>
		                                                <c:if test="${version.status == 1}">
		                                                <a href="javascript:enable(${version.id}, 0);"><span class="label label-important">禁用</span></a>
		                                                </c:if>
                                       					</td>
                                       				</tr>
                                       			</c:forEach>
                                       		</c:when>
                                       		<c:otherwise>
                                       			<tr>
                                       				<td colspan="6">暂时没有版本升级信息 </td>
                                       			</tr>
                                       		</c:otherwise>
                                       	</c:choose>
                                       </tbody>
                                       <tfoot>
                                       	<tr>
                                       		<td colspan="6"><div id="version-page"></div></td>
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
		var queryString = '?page=${page}&size=${size}&version=${version}&content=${content}';
		$(document).ready(function(){
			$('.breadcrumb').html('<li class="active">运营管理&nbsp;/&nbsp;APP版本管理</li>');
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
		            	$('#search').attr('action','${pageContext.request.contextPath}/operation/version/list');
		            	$('#page').val(page);
		            	$('#search').submit();
		            }  
			    };
				$('#version-page').bootstrapPaginator(options);
			}
		});
		
		function add() {
			var base64 = $.base64.encode(queryString);
			window.location.href = '${pageContext.request.contextPath}/operation/version/add/'+base64;
		}
		
		function edit(versionId) {
			var base64 = $.base64.encode(queryString);
			window.location.href = '${pageContext.request.contextPath}/operation/version/'+versionId+'/edit/'+base64;
		}
		
		function enable(versionId, operate) {
			var msg = '';
			if(operate == 1){
				msg = '启用';
			} else {
				msg = '禁用';
			}
			if(confirm('确认要'+msg+'此APP版本吗?')) {
				$.post('${pageContext.request.contextPath}/operation/version/'+versionId+'/'+operate, function(result){
					if(result) {
						$('#search').attr('action','${pageContext.request.contextPath}/operation/version/list');
		            	$('#search').submit();
					}
				});
			}
		}
	</script>
</html>