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
               	     <div class="well">
                         <form id="search" style="height:45px;" action="${pageContext.request.contextPath}/operation/message/list" method="post" class="form-search">
	                          <input type="hidden" id="size" name="size" value="${size}" />
							  <div class="input-group marginTop">
	                              <span>发送时间:</span>
								  <input id="beginTime" class="datepicker marginTop" style="background:white;width:90px" onkeypress="return false" name="beginTime" type="text" value="${beginTime}" />
									-
								  <input class="datepicker marginTop" id="endTime" style="background:white;width:90px" onkeypress="return false" name="endTime" type="text" value="${endTime}" />
                              	  <input type="submit" value="查询" style="margin-top: 3px" class="btn btn-default" />
                              	  <input type="reset" value="重置" style="margin-top: 3px" onclick="resetData()" class="btn btn-default" /> 
	                          </div>
                         </form>
                    </div>
                    <div class="row-fluid">
                    	<div class="block">
                    	     <div class="navbar navbar-inner block-header">
                                 <a href="javascript:add()"><button class="btn btn-success">新增</button></a>
                            </div>
                            <div class="block-content collapse in">   
                                <div class="span12">
                                    <table id="product" cellpadding="0" cellspacing="0" border="0" class="table">
                                         <thead>
                                             <tr>
	                                             <th width="5%">序号</th>
	                                             <th width="30%">标题</th>
	                                             <th width="12%">系统</th>
	                                             <th>推送状态</th>
	                                             <th>推送类型</th>
	                                             <th>发送时间</th>
	                                             <th>操作</th>
                                             </tr>
                                        </thead>
                                        <tbody>
	                                        <c:choose>
	                                            <c:when test="${fn:length(messages) > 0}">
		                                            <c:forEach var="message" items="${messages}" varStatus="status">
			                                            <tr onclick="getRow(this, '#BFBFBF')" id="message${message.id}">
			                                                <td class="hidden">${message.id}</td>
			                                                <td class="hidden">${message.status}</td>
			                                                <td>${status.count}</td>
			                                                <td>${message.title}</td>
			                                                <td><c:if test="${message.osType == 0}">ANDROID,IOS</c:if><c:if test="${message.osType == 1}">ANDROID</c:if><c:if test="${message.osType == 2}">IOS</c:if></td>
			                                                <td>
			                                                <c:choose>
			                                                	<c:when test="${message.status == 0}">
			                                                		未推送
			                                                	</c:when>
			                                                	<c:otherwise>
			                                                		已推送
			                                                	</c:otherwise>
			                                                </c:choose>
			                                                </td>
			                                                <td><c:if test="${message.sendTarget == 0}">全部人员</c:if><c:if test="${message.sendTarget == 1}">指定人员</c:if></td>
			                                                <td>
			                                                	<c:if test="${message.status == 0 and message.sendType == 0}"></c:if>
			                                                	<c:if test="${message.status != 0 or message.sendType != 0}">
					                                                <fmt:formatDate value="${message.sendTime}" type="both" pattern="yyyy-MM-dd HH:mm:ss" />
			                                                	</c:if>
			                                                </td>
			                                                <td>
		                                       					<c:if test="${message.status == 0}">
		                                       						<a href="javascript:audit(${message.id});"><span class="label label-success">推送</span></a>
		                                                			<a href="javascript:edit(${message.id});"><span class="label label-info">编辑 </span></a>
		                                       					</c:if>
		                                       				</td>
			                                             </tr>
		                                            </c:forEach>
	                                            </c:when>
	                                            <c:otherwise>
	                                        	    <tr>
	                                                    <td colspan="8">暂时还没有推送消息</td>
	                                                </tr>  
	                                            </c:otherwise>
	                                        </c:choose>
                                        </tbody>
                                         <tfoot>
                                            <tr>
                                       		    <td colspan="8"><div id="message-page"></div></td>
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
		var rowId = 0;
		var status = '';
		$('.marginTop').css('margin', '5px auto auto auto');
		var queryString = '?page=${page}&size=${size}';
		$(document).ready(function(){ 
			var totalPages = '${pages}';
			$('.breadcrumb').html('<li class="active">运营管理&nbsp;/&nbsp;消息推送管理</li>');
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
		            	var beginTime = $('#beginTime').val();
						var endTime = $('#endTime').val();
		            	var base64 = $.base64.encode('?page='+page+'&size=${size}&beginTime='+beginTime+'&endTime=${endTime}');
		            	$('#search').attr('action','${pageContext.request.contextPath}/operation/message/'+base64);
		                $('#search').submit();
		            }  
			    };
				$('#message-page').bootstrapPaginator(options);
			}
		});
		
		function add(){
			var beginTime = $('#beginTime').val();
			var endTime = $('#endTime').val();
			var base64 = $.base64.encode('?page=${page}&size=${size}&beginTime='+beginTime+'&endTime=${endTime}');
			window.location.href = '${pageContext.request.contextPath}/operation/message/add/'+base64;
		}
		
		function edit(messageId) {
			var beginTime = $('#beginTime').val();
			var endTime = $('#endTime').val();
			var base64 = $.base64.encode('?page=${page}&size=${size}&beginTime='+beginTime+'&endTime=${endTime}');
			window.location.href = '${pageContext.request.contextPath}/operation/message/'+messageId+'/edit/'+base64;
		}
		
	    function audit(messageId){
			if (confirm('要确认本次推送吗?')) {
				var beginTime = $('#beginTime').val();
				var endTime = $('#endTime').val();
				var base64 = $.base64.encode('?page=${page}&size=${size}&beginTime='+beginTime+'&endTime=${endTime}');
				window.location.href = '${pageContext.request.contextPath}/operation/message/'+messageId+'/audit/'+base64;
			}
		}
	    
	    $('#beginTime').datetimepicker({
		      format:'Y-m-d',
			  lang:'ch',
	          timepicker:false,
	          maxDate:new Date().toLocaleDateString(),
	          onSelectDate:function() {
					var fromDate = $('#beginTime').val();
					var startDate = new Date(Date.parse(fromDate.replace(/-/g,"/")));
					$('#endTime').datetimepicker({
						format:'Y-m-d',
						minDate:startDate.getFullYear()+'/'+(startDate.getMonth()+1)+'/'+startDate.getDate(),
						maxDate:new Date().toLocaleDateString(),
						lang:'ch',
						timepicker:false
					});
				}
		  });
		  
		  $('#endTime').datetimepicker({
		      format:'Y-m-d',
			  lang:'ch',
	          timepicker:false,
	          maxDate:new Date().toLocaleDateString(),
	          onSelectDate:function() {
					var toDate = $('#endTime').val();
					var endDate = new Date(Date.parse(toDate.replace(/-/g,"/")));
					$('#beginTime').datetimepicker({
						format:'Y-m-d',
						maxDate:endDate.getFullYear()+'/'+(endDate.getMonth()+1)+'/'+endDate.getDate(),
						lang:'ch',
						timepicker:false
					});
				}
		  });
		  
		  function resetData() {
				$('#beginTime').val('');
				$('#endTime').val('');
			    $('#search').attr('action','${pageContext.request.contextPath}/operation/message/list');
	            $('#search').submit(); 
			}
	</script>
</html>