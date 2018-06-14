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
	                    <form id="search" action="${pageContext.request.contextPath}/service/feedback/list" method="post">
	                    	<input type="hidden" id="page" name="page" value="${page}" />
		                	<input type="hidden" id="size" name="size" value="${size}" />
		                   	<div class="input-group pull-left">
		                    	<span>联系方式</span>
		                        <input class="horizontal marginTop" id="contact" name="contact" type="text" value="${contact}" />
		                        <span>意见反馈:</span>
								<input class="horizontal marginTop" id="content" name="content" type="text"  value="${content}" />
 								<span >时间:</span> 
                                <input type="text" id="beginTime" name="beginTime" value="${beginTime}" class="span2 marginTop" onkeypress="return false"/>
							    <span style="vertical-align: 3px" >-</span> 
							    <input type="text" id="endTime" name="endTime"  value="${endTime}" class="span2 marginTop" onkeypress="return false"/>
 								<input type="submit" value="查询" class="btn btn-default" />
                              	<input type="reset" value="重置" onclick="resetData()" class="btn btn-default" />
		                    </div>
		                </form>
                    </div>
                    <!-- content begin -->
                    <div class="row-fluid">
                        <!-- block -->
                       	<div class="block">
	                        <!-- block -->
	                        <div class="navbar navbar-inner block-header">&nbsp;</div>
                            <div class="block-content collapse in">
                                <div>
                                   <table class="table">
                                       <thead>
                                           <tr>
                                               <th width="5%">序号</th>
                                               <th width="45%">意见反馈</th>
                                               <th width="10%">联系方式</th>
                                               <th width="10%">手机型号</th>
                                               <th width="8%">系统版本号</th>
                                               <th width="8%">APP版本号</th>
                                               <th width="13%">时间</th>
                                           </tr>
                                       </thead>
                                       <tbody>
                                       	<c:choose>
                                       		<c:when test="${fn:length(feedbacks) > 0}">
                                       			<c:forEach var ="feedback" items="${feedbacks}" varStatus="status">
                                       				<tr <c:if test="${status.count%2==0}">class="odd"</c:if>>
                                                		<td style="width:30px;">${status.count}</td>
                                       					<td style="table-layout: fixed;WORD-BREAK: break-all; WORD-WRAP: break-word" >${feedback.content}</td>
                                       					<td>${feedback.contact}<%-- ${fn:substring(feedback.contact, 0, 3)}****${fn:substring(feedback.contact, fn:length(feedback.contact) - 4, fn:length(feedback.contact))} --%></td>
                                       					<td>${feedback.phoneModel}</td>
                                       					<td>${feedback.deviceVersion}</td>
                                       					<td>${feedback.appVersion}</td>
                                       					<td><fmt:formatDate value="${feedback.feedTime}" type="both" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                                       				</tr>
                                       			</c:forEach>
                                       		</c:when>
                                       		<c:otherwise>
                                       			<tr>
                                       				<td colspan="7">暂时没有意见反馈</td>
                                       			</tr>
                                       		</c:otherwise>
                                       	</c:choose>
                                       </tbody>
                                       <tfoot>
                                       	<tr>
                                       		<td colspan="7"><div id="feedback-page"></div></td>
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
			$('.breadcrumb').html('<li class="active">客服管理&nbsp;/&nbsp;意见反馈</li>');
			$('.marginTop').css('margin', '2px auto auto auto');
			$('.datepicker').css({width:100});
		    $('.horizontal').css({width:100});
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
		            	$('#search').attr('action','${pageContext.request.contextPath}/service/feedback/list');
		            	$('#page').val(page);
		                $('#search').submit();
		            }  
			    };
				$('#feedback-page').bootstrapPaginator(options);
			}
			
			$('#beginTime').datetimepicker({
			      format:'Y-m-d',
				  lang:'ch',
		          timepicker:false,
		          maxDate:new Date().toLocaleDateString(),
		          onSelectDate:function() {
		        	  getBeginTime();
				  }
			  });
			  
			  $('#endTime').datetimepicker({
			      format:'Y-m-d',
				  lang:'ch',
		          timepicker:false,
		          maxDate:new Date().toLocaleDateString(),
		          onSelectDate:function() {
		        	  getEndTime();	
				  }
			  });
			  
			  getBeginTime();
			  getEndTime();	
		});
		
		function getBeginTime(){
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
		
		function getEndTime(){
			var toDate = $('#endTime').val();
			var endDate = new Date(Date.parse(toDate.replace(/-/g,"/")));
			$('#beginTime').datetimepicker({
				format:'Y-m-d',
				maxDate:endDate.getFullYear()+'/'+(endDate.getMonth()+1)+'/'+endDate.getDate(),
				lang:'ch',
				timepicker:false
			});
		}
		
		function resetData() {
			$('#content').val('');
			$('#contact').val('');
			$('#beginTime').val('');
			$('#endTime').val('');
			$('#search').attr('action','${pageContext.request.contextPath}/service/feedback/list');
            $('#search').submit();
		}
	</script>
</html>