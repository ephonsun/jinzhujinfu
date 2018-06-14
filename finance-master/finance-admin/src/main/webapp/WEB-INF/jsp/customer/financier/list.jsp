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
                    <!-- content begin -->
                    <jsp:include page="${pageContext.request.contextPath}/breadcrumb" flush="true" />
                    <div class="well">
	                    <form id="search" action="${pageContext.request.contextPath}/customer/financier/list" method="post">
		                	<input type="hidden" id="page" name="page" value="${page}" />
		                	<input type="hidden" id="size" name="size" value="${size}" />
		                   	<div class="input-group">
		                   		<span>商户名称</span>
	                   		 	<select id="name" name="name">
									<option></option>
                                    <c:forEach items="${financiers}" var="financier">
                                    	<c:choose>
                                  		<c:when test="${financier.name == name}">
                                  			<option value="${financier.name}" selected="selected">${financier.name}</option>
                                  		</c:when>
                                  		<c:otherwise>
                                  			<option value="${financier.name}">${financier.name}</option>
                                  		</c:otherwise>
                                 		</c:choose>
                                    </c:forEach>	
								</select>
								<input type="submit" value="查询" class="btn btn-default" />
		                    </div>
		                </form>
                    </div>
                    <div class="row-fluid">
                        <!-- block -->
                       	<div class="block">
                       		<div class="navbar navbar-inner block-header">
			                    <a href="javascript:add();"><button type="button" class="btn btn-success">新增</button></a>
                       		</div>
	                        <!-- block -->
                            <div class="block-content collapse in">
                                <div class="span12">
                                   <table cellpadding="0" cellspacing="0" border="0" class="table">
                                       <thead>
                                           <tr>
                                               <th>序号</th>
                                               <th>名称</th>
                                               <th>证件号码</th>
                                               <th>手机</th>
                                               <th>银行卡号</th>
                                               <th>状态</th>
                                               <th>操作</th>
                                           </tr>
                                       </thead>
                                       <tbody>
                                       	<c:choose>
                                       		<c:when test="${fn:length(merchants) > 0}">
                                       			<c:forEach var ="merchant" items="${merchants}" varStatus="status">
                                       				<tr <c:if test="${status.count%2==0}">class="odd"</c:if>>
                                                		<td style="width:30px;">${status.count}</td>
                                       					<td>${merchant.name}</td>
                                       					<td>${merchant.license}</td>
                                       					<td>${merchant.cellphone}</td>
                                       					<td>${merchant.cardNO}</td>
                                       					<td>
                                       					<c:if test="${merchant.status == 0 }">禁用</c:if>
                                       					<c:if test="${merchant.status == 1 }">启用</c:if>
                                       					</td>
                                       					<td>
                                       					<a href="javascript:detail(${merchant.id})"><span class="label label-info">查看 </span></a>
                                       					<c:if test="${merchant.status == 0 }">
                                       					<a href="javascript:enable(${merchant.id}, 1)"><span class="label label-success">启用</span></a>
                                       					<a href="javascript:edit(${merchant.id})"><span class="label label-info">编辑 </span></a>
                                       					</c:if>
                                       					<c:if test="${merchant.status == 1}">
                                       					<a href="javascript:enable(${merchant.id}, 0);"><span class="label label-important">禁用</span></a>
                                       					</c:if>
                                       					</td>
                                       				</tr>
                                       			</c:forEach>
                                       		</c:when>
                                       		<c:otherwise>
                                       			<tr>
                                       				<td colspan="7">暂时没有商户信息</td>
                                       			</tr>
                                       		</c:otherwise>
                                       	</c:choose>
                                       </tbody>
                                       <tfoot>
                                       	<tr>
                                       		<td colspan="7"><div id="merchant-page"></div></td>
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
		var queryString = '?name=${name}&page=${page}&size=${size}';
		$(document).ready(function(){
			$('.breadcrumb').html('<li class="active">客户管理&nbsp;/&nbsp;商户管理</li>');
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
		            onPageClicked: function(event, originalEvent, type, page){
		            	$('#page').val(page);
		                $('#search').submit();
		            }  
			    };
				$('#merchant-page').bootstrapPaginator(options);
			}
			$('#name').find('option[value=${name}]').attr('selected',true);
			$('#name').comboSelect();
		});
		
		function add() {
			var base64 = $.base64.encode(queryString);
			window.location.href = '${pageContext.request.contextPath}/customer/financier/add/'+base64;
		}
		
		function edit(financierId) {
			var base64 = $.base64.encode(queryString);
			window.location.href = '${pageContext.request.contextPath}/customer/financier/'+financierId+'/edit/'+base64;
		}

		function detail(financierId) {
			var base64 = $.base64.encode(queryString);
			window.location.href = '${pageContext.request.contextPath}/customer/financier/'+financierId+'/detail/'+base64;
		}
		
		function enable(financierId, operate) {
			$.post('${pageContext.request.contextPath}/customer/financier/'+financierId+'/'+operate, function(result){
				if(result) {
					$('#search').submit();
				}
			});
		}
	</script>
</html>