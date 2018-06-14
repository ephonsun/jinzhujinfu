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
                    <div class="row-fluid">
                        <!-- block -->
                        <div class="block">
                        	<jsp:include page="${pageContext.request.contextPath}/breadcrumb" flush="true" />
                        	<div class="navbar navbar-inner block-header">&nbsp;</div>
                            <div class="block-content collapse in">
	                            <div class="span12">
	                            	<table id="roles" cellpadding="0" cellspacing="0" border="0" class="table">
		                                <thead>
		                                    <tr>
		                                        <th>序号</th>
		                                        <th>赠送规则</th>
	                                            <th>优惠券列表</th>
	                                            <th>状态</th>
		                                        <th>操作</th>
		                                    </tr>
		                                </thead>
		                                <tbody>
		                                	<c:choose>
		                                   		<c:when test="${fn:length(rules) > 0}">
		                                   			<c:forEach var ="rule" items="${rules}" varStatus="status">
														<tr <c:if test="${status.count%2==0}">class="odd"</c:if>>
                                                			<td style="width:30px;">${status.count}</td>
		                                   					<td>
		                                   					<c:forEach items="${categories}" var="category">
						                                    	<c:choose>
							                                  		<c:when test="${category.key == rule.category}">${category.value}</c:when>
						                                 		</c:choose>
						                               		</c:forEach>
		                                   					</td>
		                                   					<td>
			                                          		<c:if test="${fn:length(fn:split(rule.couponAmounts,',')) > 0}">
			                                            		<c:forEach var="couponAmount" items="${fn:split(rule.couponAmounts,',')}" varStatus="s">
			                                            			<c:forEach var="couponId" items="${fn:split(rule.couponIds,',')}" varStatus="ss">
			                                            				<c:if test="${s.index == ss.index}">
			                                            					<span class="label label-important" style="height:20px;width:50px;margin-top:1px;padding-top:8px">${couponAmount}元</span>
			                                            				</c:if>
			                                            			</c:forEach>
			                                            		</c:forEach>
	                                          				</c:if>
		                                   					</td>
		                                   					<td>
		                                   					<c:if test="${rule.status == 0}">禁用</c:if>
		                                   					<c:if test="${rule.status == 1}">启用</c:if>
		                                   					</td>
			                                                <td>
			                                                <c:if test="${rule.status == 0}">
			                                                <a href="javascript:enable(${rule.id}, 1);"><span class="label label-success">启用</span></a>
			                                                <a href="javascript:edit(${rule.id});"><span class="label label-info">编辑 </span></a>
			                                                </c:if>
			                                                <c:if test="${rule.status == 1}">
			                                                <a href="javascript:enable(${rule.id}, 0);"><span class="label label-important">禁用</span></a>
			                                                </c:if>
			                                                </td>
		                                   				</tr>
		                                   			</c:forEach>
		                                   		</c:when>
			                                   	<c:otherwise>
		                                   			<tr>
		                                   				<td colspan="5">暂时还没有红包规则</td>
		                                   			</tr>
		                                   		</c:otherwise>
		                                 	</c:choose>
	                                     </tbody>
	                                     <tfoot>
	                                     	<tr>
	                                        	<td colspan="5"><div id="rule-page"></div></td>
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
		$(document).ready(function() {
			$('.breadcrumb').html('<li class="active">红包管理&nbsp;/&nbsp;红包规则</li>');
		});
		
		function edit(ruleId) {
			window.location.href='${pageContext.request.contextPath}/coupon/rule/'+ruleId+'/edit';
		}
		
		function enable(ruleId, operate) {
			$.post('${pageContext.request.contextPath}/coupon/rule/'+ruleId+'/'+operate, function(result){
				if(result){  
                	window.location.href='${pageContext.request.contextPath}/coupon/rule';
                } else { 
					alert('操作失败!');
                }
			});
		}
	</script>
</html>