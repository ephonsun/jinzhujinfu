<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<jsp:include page="${pageContext.request.contextPath}/header" flush="true" />
	<body>
		<jsp:include page="${pageContext.request.contextPath}/menu" flush="true" />
        <div class="container-fluid">
            <div class="row-fluid">
                <jsp:include page="${pageContext.request.contextPath}/sidebar" flush="true" />
                <div class="span10" id="content">
                    <!-- content begin -->
                    <div class="row-fluid">
                        <!-- block -->
                        <div class="block">
                            <jsp:include page="${pageContext.request.contextPath}/breadcrumb" flush="true" />
                            <div class="navbar navbar-inner block-header">
                       			<a href="javascript:add();" id="100"><button type="button" class="btn btn-success">新增</button></a>
                            </div>
                            <div class="block-content collapse in">
	                            <div class="span12">
	                            	<table cellpadding="0" cellspacing="0" border="0" class="table">
		                                <thead>
		                                    <tr>
		                                        <th>序号</th>
		                                        <th>用户名</th>
		                                        <th>角色</th>
		                                        <th>真实姓名</th>
		                                        <th>手机</th>
		                                        <th>状态</th>
		                                        <th>操作</th>
		                                    </tr>
		                                </thead>
		                                <tbody>
		                                	<c:choose>
		                                		<c:when test="${fn:length(admins) > 0}">
		                                			<c:forEach var = "admin" items="${admins}" varStatus="status">
		                                				<tr <c:if test="${status.count%2==0}">class="odd"</c:if>>
                                                			<td style="width:30px;">${status.count}</td>
		                                					<td>${admin.name}</td>
		                                					<td>${admin.role.name}</td>
		                                					<td>${admin.realName}</td>
		                                					<td>${admin.cellphone}</td>
		                                					<td>
			                                                <c:if test="${admin.status == 0}">禁用</c:if>
			                                                <c:if test="${admin.status == 1}">启用</c:if>
			                                                </td>
			                                                <td>
			                                                <c:if test="${admin.status == 0}">
			                                                <a href="javascript:enable(${admin.id}, 1);"><span class="label label-success">启用</span></a>
			                                                <a href="javascript:edit(${admin.id});"><span class="label label-info">编辑 </span></a>
			                                                </c:if>
			                                                <c:if test="${admin.status == 1}">
			                                                <a href="javascript:enable(${admin.id}, 0);"><span class="label label-important">禁用</span></a>
			                                                	<c:if test="${admin.totp == 1}">
			                                                	<a href="javascript:resetTotp(${admin.id});"><span class="label label-info">重置令牌</span></a>
			                                                	</c:if>
			                                                </c:if>
			                                                </td>
		                                				</tr>
		                                			</c:forEach>
		                                		</c:when>
			                                   	<c:otherwise>
		                                   			<tr>
		                                   				<td colspan="7">暂时没有管理员数据</td>
		                                   			</tr>
		                                   		</c:otherwise>
		                                	</c:choose>
	                                     </tbody>
	                                     <tfoot>
	                                     	<tr>
	                                        	<td colspan="7"><div id="admin-page"></div></td>
	                                        </tr>
	                                     </tfoot>
	                                </table>
	                        	</div>
                            </div>
                        </div>
                        <!-- /block -->
                    </div>
                    <!-- content end -->
                </div>
            </div>
        </div>
		<jsp:include page="${pageContext.request.contextPath}/footer" flush="true" />
	</body>
	<script type="text/javascript">
		$(document).ready(function(){
			$('.breadcrumb').html('<li class="active">系统管理&nbsp;/&nbsp;操作员管理</li>');
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
		            	window.location.href = '${pageContext.request.contextPath}/sys/admin/list?page='+page;
		            }  
			    };
				$('#admin-page').bootstrapPaginator(options);
			}
		});

		function add() {
			window.location.href='${pageContext.request.contextPath}/sys/admin/add/${page}/${size}';
		}
		
		function edit(adminId) {
			window.location.href='${pageContext.request.contextPath}/sys/admin/'+adminId+'/edit/${page}/${size}';
		}
		
		function resetTotp(adminId) {
			if(confirm("确认要重置令牌?")){
				$.post('${pageContext.request.contextPath}/sys/admin/'+adminId+'/totp', function(result){
					if (result) {
						alert('令牌重置成功');
	 					window.location.href = '${pageContext.request.contextPath}/sys/admin/list?page=${page}';
					}
				});
			}
		}
		
		function enable(adminId, operate) {
			$.post('${pageContext.request.contextPath}/sys/admin/'+adminId+'/'+operate, function(result){
				if(result){  
                	window.location.href='${pageContext.request.contextPath}/sys/admin/list?page=${page}';
                } else { 
					alert('操作失败!');
                }
			});
		}
	</script>
</html>