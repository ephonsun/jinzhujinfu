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
                            <div class="block-content collapse in">   
                                <div class="span12">
                                    <table cellpadding="0" cellspacing="0" border="0" class="table">
                                         <thead>
                                            <tr>
	                                            <th>序号</th>
	                                            <th>银行编号</th>
	                                            <th>银行名称</th>
	                                            <th>单笔限额</th>
	                                            <th>单日限额</th>
	                                            <th>单月限额</th>
	                                            <th>类型</th>
	                                            <th>状态</th>
	                                            <th>操作</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                        <c:choose>
                                          <c:when test="${fn:length(banks) > 0}">
                                            <c:forEach var="bank" items="${banks}" varStatus="status">
                                             <tr <c:if test="${status.count%2==0}">class="odd"</c:if>>
                                                <td style="width:30px;">${status.count}</td>
                                                <td>${bank.bankNO}</td>
                                                <td>${bank.bankName}</td>
                                                <td>${bank.singleLimit}</td>
                                                <td>${bank.dayLimit}</td>
                                                <td>${bank.monthLimit}</td>
                                                <td>
                                                <c:if test="${bank.category == 0}">个人</c:if>
                                                <c:if test="${bank.category == 1}">企业</c:if>
                                                <c:if test="${bank.category == 2}">个人/企业</c:if>
                                                </td>
                                                <td>
                                                <c:if test="${bank.status == 0}">禁用</c:if>
                                                <c:if test="${bank.status == 1}">启用</c:if>
                                                </td>
                                                <td>
                                                <c:if test="${bank.status == 0}">
			                                    	<a href="javascript:enable(${bank.id}, 1);"><span class="label label-success">启用</span></a>
	                                               	<a href="javascript:edit(${bank.id});"><span class="label label-important">编辑</span></a>
			                                    </c:if>
                                                <c:if test="${bank.status == 1}">
                                                	<a href="javascript:enable(${bank.id}, 0);"><span class="label label-important">禁用</span></a>
                                                </c:if>
                                                </td>
                                            </tr>
                                            </c:forEach>
                                          </c:when>
                                          <c:otherwise>
                                        	<tr>
                                                <td colspan="8">暂时还没有银行限额数据</td>
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
	</body>
	<script type="text/javascript">
		$(document).ready(function(){
			$('.breadcrumb').html('<li class="active">客服管理&nbsp;/&nbsp;银行限额</li>');
		});
		
		function enable(bankId, operate) {
			$.post('${pageContext.request.contextPath}/service/bank/'+bankId+'/'+operate, function(result){
				if(result){  
                	window.location.href='${pageContext.request.contextPath}/service/bank/list';
                } else { 
					alert('操作失败!');
                }
			});
		}

		function edit(bankId) {
            window.location.href='${pageContext.request.contextPath}/service/bank/edit/'+bankId;
		}
	</script>
</html>