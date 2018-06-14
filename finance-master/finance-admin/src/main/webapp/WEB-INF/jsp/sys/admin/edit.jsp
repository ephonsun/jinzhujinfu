<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
                            <div class="block-content collapse in">
	                            <form id="fm" onsubmit="return validate()" action="${pageContext.request.contextPath}/sys/admin/save" method="post" class="form-horizontal" >
								<div class="well" style="padding-bottom: 20px; margin: 0;">
									<div class="control-group">
										<input id="id" name="id" value="${admin.id}" type="hidden">
										<input name="page" value="${page}" type="hidden">
										<input name="size" value="${size}" type="hidden">
		                    		</div>
			                    	<div class="control-group">
										<label class="control-label"><span class="required">*</span>用户名</label>
										<div class="controls">
											<input type="text" id="name" name="name" value="${admin.name}" readonly="readonly" />
										</div>
									</div>
									<div class="control-group">
										<label class="control-label"><span class="required">*</span>真实姓名</label>
										<div class="controls">
											<input type="text" onkeyup="this.value=this.value.replace(/(^\s*)/g,'')" onblur="this.value=this.value.replace(/(\s*$)/g,'')" id="realName" name="realName" value="${admin.realName}" class="validate[required,minSize[4],maxSize[8],custom[chinese]] text-input span3" />
										</div>
									</div>
									<div class="control-group">
										<label class="control-label"><span class="required">*</span>手机</label>
										<div class="controls">
											<input type="text" id="cellphone" name="cellphone" value="${admin.cellphone}" class="text-input validate[required,custom[cellphone],ajax[ajaxValidateAdminCellphone]] text-input span3" />
										</div>
									</div>
									<div id="roleIdk" class="control-group">
										<label class="control-label"><span class="required">*</span>角色类型</label>
										<div class="controls">
											<select id="roleId" name="role.id" class="validate[required] span3">
	                                        	<option value="">请选择</option>
		                                        <c:forEach var="role" items="${roles}" varStatus="s">
		                                        	<c:choose>
		                                        		<c:when test="${role.id == admin.role.id}">
		                                        		<option value="${role.id}" selected="selected">${role.name}</option>
		                                        		</c:when>
		                                        		<c:otherwise>
		                                        		<option value="${role.id}">${role.name}</option>
		                                        		</c:otherwise>
		                                        	</c:choose>
		                                       	</c:forEach>
		                                	</select>
	                                	</div>
									</div>
									<div class="form-actions">
										<button type="submit" class="btn btn-icon btn-primary glyphicons circle_ok"><i></i>保存</button>
										<a href="javascript:quit();"><button type="button" class="btn">返回</button></a>
									</div>
								</div>
							</form>
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
		$(function(){
			$('.breadcrumb').html('<li class="active">系统管理&nbsp;/&nbsp;<a href="javascript:quit();">操作员管理</a>&nbsp;/&nbsp;编辑</li>');
			$('#fm').validationEngine('attach', { 
		        promptPosition: 'bottomRight', 
		        scroll: false 
		    });
			$('#name').focus();
		});
		
		function quit() {
			window.location.href='${pageContext.request.contextPath}/sys/admin/list?page=${page}';
	    }
	</script>
</html>