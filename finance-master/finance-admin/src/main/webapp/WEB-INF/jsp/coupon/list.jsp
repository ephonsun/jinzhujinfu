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
                    <!-- content begin -->
                    <div class="row-fluid">
                    	<div class="well">
                           	<form id="search" action="${pageContext.request.contextPath}/coupon/list" method="post">
                           		<input type="hidden" id="page" name="page" value="${page}" />
                           		<input type="hidden" id="size" name="size" value="${size}" />
                           		<div class="input-group pull-left">
			                    	<span>名称</span>
			                        <input class="horizontal marginTop" id="name" name="name" type="text" value="${name}" />
	 								<input type="submit" style="margin-top: -10px"  value="查询" class="btn btn-default" />
			                    </div>
                        	</form>
                        </div>
                        <!-- block -->
                        <div class="block">
                            <div class="navbar navbar-inner block-header">
                       			<a href="javascript:add();"><button type="button" class="btn btn-success">新增</button></a>
                            </div>
                            <div class="block-content collapse in">
	                            <div class="span12">
	                            	<table id="bonuss" cellpadding="0" cellspacing="0" border="0" class="table">
		                                <thead>
		                                    <tr>
		                                        <th>序号</th>
		                                        <th>名称</th>
		                                        <th>类型</th>
		                                        <th>金额</th>
		                                        <th>使用条件</th>
		                                        <th>备注</th>
		                                        <th>状态</th>
		                                        <th>操作</th>
		                                    </tr>
		                                </thead>
		                                <tbody>
		                                	<c:choose>
		                                   		<c:when test="${fn:length(coupons) > 0}">
		                                   			<c:forEach var ="coupon" items="${coupons}" varStatus="status">
														<tr <c:if test="${status.count%2==0}">class="odd"</c:if>>
                                                			<td style="width:30px;">${status.count}</td>
		                                   					<td>${coupon.name}</td>
		                                   					<td>
		                                   					<c:if test="${coupon.category == 0}">不限</c:if>
		                                   					<c:if test="${coupon.category == 1}">单笔可用</c:if>
		                                   					<c:if test="${coupon.category == 2}">累计可用</c:if>
		                                   					</td>
		                                   					<td>${coupon.amount}</td>
		                                   					<td>${coupon.condition}</td>
		                                   					<td>${coupon.remark}</td>
		                                   					<td>
		                                   					<c:if test="${coupon.status == 0}">禁用</c:if>
		                                   					<c:if test="${coupon.status == 1}">启用</c:if>
		                                   					</td>
		                                   					<td>
		                                   					<c:if test="${coupon.status == 0}">
		                                   						<a href="javascript:enable(${coupon.id}, 1);"><span class="label label-success">启用</span></a>
	                                                			<a href="javascript:edit(${coupon.id});"><span class="label label-info">编辑 </span></a>
		                                   					</c:if>
		                                   					<c:if test="${coupon.status == 1}">
		                                   						<a href="javascript:enable(${coupon.id}, 0);"><span class="label label-important">禁用</span></a>
		                                   					</c:if>
		                                   					</td>
		                                   				</tr>
		                                   			</c:forEach>
		                                   		</c:when>
			                                   	<c:otherwise>
		                                   			<tr>
		                                   				<td colspan="8">暂时没有红包数据</td>
		                                   			</tr>
		                                   		</c:otherwise>
		                                 	</c:choose>
	                                     </tbody>
	                                     <tfoot>
	                                     	<tr>
	                                        	<td colspan="8"><div id="bonus-page"></div></td>
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
		$(document).ready(function() {
			var totalPages = '${pages}';
			$('.breadcrumb').html('<li class="active">红包管理&nbsp;/&nbsp;红包库</li>');
			$('[rel="tooltip"]').tooltip();
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
		            	$('#search').attr('action','${pageContext.request.contextPath}/coupon/list');
		                $('#search').submit();
		            }  
			    };
				$('#bonus-page').bootstrapPaginator(options);
			}
		});
		
		function add() {
			var base64 = $.base64.encode(queryString);
			window.location.href='${pageContext.request.contextPath}/coupon/add/'+base64;
		}
		
		function edit(couponId) {
			var base64 = $.base64.encode(queryString);
			window.location.href='${pageContext.request.contextPath}/coupon/'+couponId+'/edit/'+base64;
		}
		
		function enable(couponId, operate) {
			var msg = operate == 1 ? '启用' : '禁用';
			if(confirm('确认要'+msg+'此红包吗?')) {
				$.post('${pageContext.request.contextPath}/coupon/'+couponId+'/'+operate, function(result){
					if(result) {
						$('#search').attr('action','${pageContext.request.contextPath}/coupon/list');
		            	$('#search').submit();
					}
				});
			}
		}
	</script>
</html>