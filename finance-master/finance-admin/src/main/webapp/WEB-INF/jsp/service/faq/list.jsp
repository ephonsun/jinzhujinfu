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
                                 <a href="${pageContext.request.contextPath}/service/faq/add"><button class="btn btn-lg btn-success">新增</button></a>
                            </div>
                            <div class="block-content collapse in">   
                                <div class="span12">
                                    <table cellpadding="0" cellspacing="0" border="0" class="table">
                                         <thead>
                                            <tr>
	                                            <th>序号</th>
	                                            <th>问题</th>
	                                            <th>回答</th>
	                                            <th>状态</th>
	                                            <th>操作</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                        <c:choose>
                                          <c:when test="${fn:length(faqs) > 0}">
                                            <c:forEach var="faq" items="${faqs}" varStatus="status">
                                            <tr <c:if test="${status.count%2==0}">class="odd"</c:if>>
                                                <td style="width:30px;">${status.count}</td>
                                                <td>${faq.ask}</td>
                                                <td title="${faq.question}"><p class="liq">${faq.question}</p></td>
                                                <td>
                                                <c:if test="${faq.status == 0}">禁用</c:if>
                                                <c:if test="${faq.status == 1}">启用</c:if>
                                                </td>
                                                <td>
                                                <c:if test="${faq.status == 0}">
			                                    	<a href="javascript:enable(${faq.id}, 1);"><span class="label label-success">启用</span></a>
			                                        <a href="javascript:edit(${faq.id});"><span class="label label-info">编辑 </span></a>
			                                        <a href="javascript:remove(${faq.id});"><span class="label label-warning">删除</span></a>
			                                    </c:if>
                                                <c:if test="${faq.status == 1}">
                                                	<a href="javascript:enable(${faq.id}, 0);"><span class="label label-important">禁用</span></a>
                                                </c:if>
                                                </td>
                                            </tr>
                                            </c:forEach>
                                          </c:when>
                                          <c:otherwise>
                                        	<tr>
                                                <td colspan="5">暂时还没有常见问题数据</td>
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
			$('.breadcrumb').html('<li class="active">客服管理&nbsp;/&nbsp;常见问题</li>');
			//$(".lia").css({'width':'300px','display':'block','overflow':'hidden','word-break':'keep-all','white-space':'nowrap','text-overflow':'ellipsis'});
			$(".liq").css({'width':'500px','display':'block','overflow':'hidden','word-break':'keep-all','white-space':'nowrap','text-overflow':'ellipsis'});
		});
		
		function edit(faqId) {
			window.location.href='${pageContext.request.contextPath}/service/faq/'+faqId+'/edit';
		}
		
		function remove(faqId) {
			if(confirm("确认要删除本条问答吗?")){
				window.location.href='${pageContext.request.contextPath}/service/faq/'+faqId+'/remove';
			}
		}
		
		function enable(faqId, operate) {
			$.post('${pageContext.request.contextPath}/service/faq/'+faqId+'/'+operate, function(result){
				if(result){  
                	window.location.href='${pageContext.request.contextPath}/service/faq/list';
                } else { 
					alert('操作失败!');
                }
			});
		}
	</script>
</html>