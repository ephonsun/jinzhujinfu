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
                    <div class="row-fluid">
                    	<div class="block">
                    		<jsp:include page="${pageContext.request.contextPath}/breadcrumb" flush="true" />
                            <div class="navbar navbar-inner block-header">
                                 <a href="javascript:add();"><button class="btn btn-success">新增</button></a>
                            </div>
                            <div class="block-content collapse in">   
                                <div class="span12" id="productCategory">
                                    <table  cellpadding="0" cellspacing="0" border="0" class="table">
                                         <thead>
                                            <tr>
	                                            <th>序号</th>
	                                            <th>名称</th>
	                                            <th>备注</th>
	                                            <th>状态</th>
	                                            <th>操作</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                        <c:choose>
                                          <c:when test="${fn:length(categories) > 0}">
                                            <c:forEach var="category" items="${categories}" varStatus="status">
                                            <tr <c:if test="${status.count%2==0}">class="odd"</c:if>>
                                                <td style="width:30px;">${status.count}</td>
                                                <td>${category.name}</td>
                                                <td>${category.remark}</td>
                                                <td>
                                                <c:if test="${category.status == 0}">禁用</c:if>
                                                <c:if test="${category.status == 1}">启用</c:if>
                                                </td>
                                                <td>
                                                <c:if test="${category.status == 0}">
                                                <a href="javascript:enable(${category.id}, 1);"><span class="label label-success">启用</span></a>
                                                </c:if>
                                                <c:if test="${category.status == 1}">
                                                <a href="javascript:enable(${category.id}, 0);"><span class="label label-important">禁用</span></a>
                                                </c:if>
                                                </td>
                                            </tr>
                                            </c:forEach>
                                          </c:when>
                                          <c:otherwise>
                                        	<tr>
                                                <td colspan="5">暂时还没有产品分类</td>
                                            </tr>  
                                          </c:otherwise>
                                        </c:choose>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>		
                    </div>
                </div>
            </div>
        </div>
		<jsp:include page="${pageContext.request.contextPath}/footer" flush="true" />
        <div class="modal hide fade" id="productCategoryDialog" role="dialog">
			<div class="modal-dialog">
		    	<form id="fm" class="modal-content form-horizontal" action="${pageContext.request.contextPath}/product/category" method="post">
		    		<input id="id" name="id" value="0" type="hidden">
		        	<div class="modal-header">
		        		<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true"> &times;</span></button>
		        		<h4 class="modal-title">新增产品分类</h4>
		      		</div>
		        	<div class="control-group">
						<label class="control-label"><span class="required">*</span> 分类名称</label>
						<div class="controls"><input id="name" name="name" placeholder="4~40个字符" class="validate[required, minSize[4], maxSize[40], ajax[validateProductCategory]] text-input" type="text" onkeyup="this.value=this.value.replace(/(^\s*)/g,'')" onblur="this.value=this.value.replace(/(\s*$)/g,'')"></div>
					</div>
					<div class="control-group">
						<label class="control-label"><span class="required">*</span>字典类型</label>
						<div class="controls">
						    <select id="property" name="property" class="validate[required]">
							    <option></option>
	                            <c:forEach items="${properties}" var="property">
	                             <option value="${property.key}">${property.value}</option>
	                            </c:forEach>
							</select>
					    </div>
					</div>
					<div class="control-group">
						<label class="control-label">分类描述</label>
						<div class="controls">
							<textarea id="remark" name="remark" rows="5" cols="150" placeholder="最多不能超过60个字符" onkeyup="this.value=this.value.replace(/(^\s*)/g,'')" onblur="this.value=this.value.replace(/(\s*$)/g,'')" class="validate[maxSize[60]]"></textarea>
						</div>
					</div> 
			        <div class="modal-footer">
			            <button type="submit" class="btn btn-primary">确认</button>
			            <button type="reset" class="btn btn-default" data-dismiss="modal">返回</button>
			        </div>
		    	</form>
		  	</div> 
		</div>
	</body>
	<script type="text/javascript">
		$(document).ready(function(){
			$('.breadcrumb').html('<li class="active">产品管理&nbsp;/&nbsp;产品分类</li>');
		});
		
		function enable(categoryId, operate) {
			$.post('${pageContext.request.contextPath}/product/category/'+categoryId+'/'+operate, function(result){
				if(result) {
 					window.location.href = '${pageContext.request.contextPath}/product/category/list';
				}
			});
		}
		
		function add() {
			$('#name').val('');
			$('#property').val('');
			$('#remark').val('');
			$('#fm')[0].reset();
			$('#productCategoryDialog').modal('show');
			$('#fm').validationEngine('attach', { 
		        promptPosition: 'bottomLeft', 
		        scroll: false,
		        showOneMessage : true
		   }); 
		}
	</script>
</html>