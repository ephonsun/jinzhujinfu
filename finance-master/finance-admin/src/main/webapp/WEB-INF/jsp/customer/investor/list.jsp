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
                     	<form id="search" action="${pageContext.request.contextPath}/customer/investor/list" method="post">
                        	<input type="hidden" id="page" name="page" value="${page}" />
                          	<input type="hidden" id="size" name="size" value="${size}" />
                          	<div class="input-group pull-left">
                         		<span>姓名</span>
                                <input class="horizontal marginTop" id="name" name="name" type="text" value="${name}" />
                         		<span>手机</span>
                                <input class="horizontal marginTop" id="cellphone" name="cellphone" type="text" value="${cellphone}" />
                              	<span>开始时间</span> 
                                <input type="text" id="beginTime" name="beginTime" value="${beginTime}" class="horizontal marginTop" onkeypress="return false"/>
				    			<span>&nbsp;结束时间</span> 
				    			<input type="text" id="endTime" name="endTime"  value="${endTime}" class="horizontal marginTop" onkeypress="return false"/>
                              	<span>渠道名称</span>
                              	<select id="channelId" name="channelId">
									<option value="-1" >全部</option>
                                    <c:forEach items="${channels}" var="channel">
                                      <c:choose>
                                        <c:when test="${channelId == channel.id}"><option value="${channel.id}" selected="selected">${channel.name}</option></c:when>
                                     	<c:otherwise><option value="${channel.id}" >${channel.name}</option></c:otherwise>
                                      </c:choose>
                                   	</c:forEach>
								</select>
                                <input type="submit" value="查询" class="btn btn-default" />
                                <input type="reset" value="重置" class="btn btn-default" onclick="resetData()" />	
                        	</div>
                    	</form>
                    </div>
                    <!-- content begin -->
                    <div class="row-fluid">
                        <!-- block -->
                        <div class="block">
                       		<div class="navbar navbar-inner block-header">&nbsp;</div>
                            <div class="block-content collapse in">
                                <div class="span12">
                                    <table cellpadding="0" cellspacing="0" border="0" class="table">
                                        <thead>
                                            <tr>
                                                <th>序号</th>
                                                <th>手机号</th>
                                                <th>姓名</th>
                                                <th>余额</th>
                                                <th>注册时间</th>
                                                <th>首次交易时间</th>
                                                <th>渠道名称</th>
                                                <th>操作</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                        	<c:choose>
                                        		<c:when test="${fn:length(customers) > 0}">
                                        			<c:forEach var ="customer" items="${customers}" varStatus="status">
                                        				<tr <c:if test="${status.count%2==0}">class="odd"</c:if>>
                                                			<td style="width:30px;">${status.count}</td>
                                        					<td>${customer.cellphone}</td>
                                        					<td title="${customer.idcard}">${customer.name}</td>
                                        					<td><fmt:formatNumber value="${customer.balance}" pattern="#,##" /></td>
                                        					<td><fmt:formatDate value="${customer.registerTime}" type="both" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                                        					<td>
                                        						<c:if test="${customer.tradeTime != 'Thu Jan 01 00:00:00 GMT+08:00 1970' and customer.tradeTime != 'Thu Jan 01 00:00:00 CST 1970'}">
		                                        					<fmt:formatDate value="${customer.tradeTime}" type="both" pattern="yyyy-MM-dd HH:mm:ss" />
                                        						</c:if>
                                        					</td>
                                        					<td><c:if test="${customer.channel != null}"> ${customer.channel.name}</c:if></td>
                                        					<td></td>
                                        				</tr>
                                        			</c:forEach>
                                        		</c:when>
                                        		<c:otherwise>
                                        			<tr>
                                        				<td colspan="8">暂时没有投资客数据</td>
                                        			</tr>
                                        		</c:otherwise>
                                        	</c:choose>
                                        </tbody>
                                        <tfoot>
                                        	<c:if test="${fn:length(customers) > 0}">
	                                            <tr>
	                                        		<td colspan="8">总计:${total}</td>
	                                        	</tr>
                                       	    </c:if>
                                        	<tr>
                                        		<td colspan="8"><div id="customer-page"></div></td>
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
			$('.breadcrumb').html('<li class="active">客户管理&nbsp;/&nbsp;用户管理</li>');
			$('#channelId').find('option[value=${channelId}]').attr('selected', true);
			$('#channelId').comboSelect();
			$('.marginTop').css('margin', '2px auto auto auto');
			$('.marginDivTop').css('margin', '10px auto auto auto');
			$('.datepicker').css({width:120});
			$('.horizontal').css({width:120});
			$('.horizontalType').css({width:160});
			var totalPages = '${pages}';
			var date = new Date();
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
		            	$('#search').attr('action','${pageContext.request.contextPath}/customer/investor/list');
		            	$('#page').val(page);
		                $('#search').submit();
		            }  
			    };
				$('#customer-page').bootstrapPaginator(options);
			}
			
			$('#beginTime').datetimepicker({
				format:'Y-m-d',
				maxDate:new Date().toLocaleDateString(),
				lang:'ch',
				timepicker:false,
				scrollInput:false,
				onShow:function() {
					if ($('td').hasClass('xdsoft_other_month')) {
						$('td').removeClass('xdsoft_other_month');
					} 
				},
				onSelectDate:function() {
					var beginTime = $('#beginTime').val();
					var beginDate = new Date(Date.parse(beginTime.replace(/-/g,"/")));
					if(((date.getTime() -beginDate)/(1000*60*60*24) >= 31)){
						$('#endTime').datetimepicker({
							format:'Y-m-d',
							minDate:beginDate.getFullYear()+'/'+(beginDate.getMonth()+1)+'/'+beginDate.getDate(),
							lang:'ch',
							scrollInput:false,
							timepicker:false,
							onShow:function() {
								if ($('td').hasClass('xdsoft_other_month')) {
									$('td').removeClass('xdsoft_other_month');
								} 
							}
						});
					}else{
						$('#endTime').datetimepicker({
							format:'Y-m-d',
							minDate:beginDate.getFullYear()+'/'+(beginDate.getMonth()+1)+'/'+beginDate.getDate(),
							lang:'ch',
							timepicker:false,
							scrollInput:false,
							onShow:function() {
								if ($('td').hasClass('xdsoft_other_month')) {
									$('td').removeClass('xdsoft_other_month');
								} 
							}
						});
					}
				}
			});
			
			$('#endTime').datetimepicker({
				 format:'Y-m-d',
				 maxDate:new Date().toLocaleDateString(),
				 lang:'ch',
				 timepicker:false,
				 scrollInput:false,
				 onShow:function() {
					if ($('td').hasClass('xdsoft_other_month')) {
						$('td').removeClass('xdsoft_other_month');
					} 
				},
				onSelectDate:function() {
					var endTime = $('#endTime').val();
					var endDate = new Date(Date.parse(endTime.replace(/-/g,"/")));
					$('#beginTime').datetimepicker({
						format:'Y-m-d',
						maxDate:endDate.getFullYear()+'/'+(endDate.getMonth()+1)+'/'+endDate.getDate(),
						lang:'ch',
						timepicker:false,
						scrollInput:false,
						onShow:function() {
							if ($('td').hasClass('xdsoft_other_month')) {
								$('td').removeClass('xdsoft_other_month');
							} 
						}
					});
				}
			});
			$('#search').validationEngine('attach', {
		        promptPosition:'bottomLeft',
		        showOneMessage: true,
		        focusFirstField:true,
		        scroll: false
		    });
		});
		
		function resetData() {
			$('#name').val('');
			$('#cellphone').val('');
			$('#beginTime').val('');
			$('#endTime').val('');
			$('#channelId').find('option[value=-1]').attr('selected', true);
			$('#search').attr('action','${pageContext.request.contextPath}/customer/investor/list');
            $('#search').submit();
		}
	</script>
</html>