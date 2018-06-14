<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<jsp:include page="${pageContext.request.contextPath}/header" flush="true" />
	<link href="${pageContext.request.contextPath}/css/zTreeStyle/zTreeStyle.css" rel="stylesheet" media="screen">
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
                       			<a href="javascript:add();"><button type="button" class="btn btn-success">新增</button></a>
                            </div>
                            <div class="block-content collapse in">
	                            <div class="span12">
	                            	<table cellpadding="0" cellspacing="0" border="0" class="table">
		                                <thead>
		                                    <tr>
		                                        <th>序号</th>
		                                        <th>角色名称</th>
		                                        <th>状态</th>
		                                        <th>描述</th>
		                                        <th>操作</th>
		                                    </tr>
		                                </thead>
		                                <tbody>
		                                	<c:choose>
		                                   		<c:when test="${fn:length(roles) > 0}">
		                                   			<c:forEach var ="role" items="${roles}" varStatus="status">
														<tr <c:if test="${status.count%2==0}">class="odd"</c:if>>
                                                			<td style="width:30px;">${status.count}</td>
		                                   					<td>${role.name}</td>
		                                   					<td>
		                                   					<c:if test="${role.status == 0}">禁用</c:if>
		                                   					<c:if test="${role.status == 1}">启用</c:if>
		                                   					</td>
		                                   					<td>${role.remark}</td>
			                                                <td>
			                                                <c:if test="${role.id > 1}">
				                                                <c:if test="${role.status == 0}">
				                                                <a href="javascript:enable(${role.id}, 1);"><span class="label label-success">启用</span></a>
				                                                <a href="javascript:edit(${role.id});"><span class="label label-info">编辑 </span></a>
				                                                </c:if>
				                                                <c:if test="${role.status == 1}">
				                                                <a href="javascript:enable(${role.id}, 0);"><span class="label label-important">禁用</span></a>
				                                                <a href="javascript:authorization(${role.id}, '${role.name}');"><span class="label label-info">授权 </span></a>
				                                                </c:if>
			                                                </c:if>
			                                                </td>
		                                   				</tr>
		                                   			</c:forEach>
		                                   		</c:when>
			                                   	<c:otherwise>
		                                   			<tr>
		                                   				<td colspan="5">暂时没有角色数据</td>
		                                   			</tr>
		                                   		</c:otherwise>
		                                 	</c:choose>
	                                     </tbody>
	                                     <tfoot>
	                                     	<tr>
	                                        	<td colspan="5"><div id="role-page"></div></td>
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
        <div class="modal hide fade" id="authorizationDiv" role="dialog">
			<form id="fmg" class="modal-content form-horizontal password-modal" >
				<input type="hidden" id="roleId" value="0"/>
				<div class="modal-footer"><span id="roleName" class="label label-info">&nbsp;</span></div>
				<div class="zTreeDemo">
					<ul id="treeDemo" class="ztree" ></ul>
				</div>
				<div class="modal-footer">
		            <button type="button" onclick="saveAuthorization()" class="btn btn-primary">确认</button>
		            <button type="reset" class="btn btn-default" data-dismiss="modal">取消</button>
		        </div>
			</form>		 
		</div>
		<jsp:include page="${pageContext.request.contextPath}/footer" flush="true" />
		<script src="${pageContext.request.contextPath}/js/jquery.ztree.core-3.5.min.js"></script>
		<script src="${pageContext.request.contextPath}/js/jquery.ztree.excheck-3.5.min.js"></script>
	</body>
	<script type="text/javascript">
		$(document).ready(function() {
			$('.breadcrumb').html('<li class="active">系统管理&nbsp;/&nbsp;角色管理</li>');
			$('.zTreeDemo').css({height:'500px',overflow:'auto',width:'550px'});
		});
		
		function add() {
			window.location.href='${pageContext.request.contextPath}/sys/role/add';
		}
		
		function edit(roleId) {
			window.location.href='${pageContext.request.contextPath}/sys/role/'+roleId+'/edit';
		}
		
		function enable(roleId, operate) {
			$.post('${pageContext.request.contextPath}/sys/role/'+roleId+'/'+operate, function(result){
				if(result){  
                	window.location.href='${pageContext.request.contextPath}/sys/role/list';
                } else { 
					alert('操作失败!');
                }
			});
		}
		
		var setting = {
			check: {
				enable: true,
				chkboxType: {'Y':'ps', 'N':'s'}
			},
			data: {
				simpleData: {
					enable: true
				}
			}
		};
		function authorization(roleId, roleName) {
			$.post('${pageContext.request.contextPath}/sys/role/'+roleId+'/resource', function(result){
				if(result){  
					$.fn.zTree.init($('#treeDemo'), setting, result);
		    		$('#authorizationDiv').modal('show');
		    		$('#roleId').val(roleId);
		    		$('#roleName').html(roleName);
					$('#fmg').validationEngine();;
                } else { 
                	alert('获取权限信息出错!');
                }
			});
		}
		
		function saveAuthorization() {
			var treeObj = $.fn.zTree.getZTreeObj('treeDemo');
			var nodes = treeObj.getCheckedNodes(true);
			var resourceIds = [];
			if(nodes && nodes.length > 0) {
				for( var i = 0; i < nodes.length; i++) {
					resourceIds.push(nodes[i].id);
				}
			}
			if(resourceIds.length > 0) {
				$.post('${pageContext.request.contextPath}/sys/role/resource/save', {roleId:$('#roleId').val(), resourceIds:resourceIds.toString()}, function(result){
					if(result){  
						$('#authorizationDiv').modal('hide');
						$.fn.zTree.destroy();
						alert('操作成功!');
	                } else { 
	                	alert('操作失败!');
	                }
				});
			}
		}

	</script>
</html>