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
                    <div class="well" style="height:175px;">
                    	<form id="search" onsubmit="return validate();" action="${pageContext.request.contextPath}/trade/investor/balance" method="post">
                    		<input type="hidden" id="page" name="page" value="${page}" />
                        	<input type="hidden" id="size" name="size" value="${size}" />
                            <div class="input-group">
                 				<span>类&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;型</span>
                 				<select id="orderType" name="orderType" class="horizontalType marginTop">
                 				  <c:choose>
                 				    <c:when test="${orderType == 1}">
                 				      <option value="0">充值</option>
                 				      <option value="1" selected="selected">提现</option>
                 				    </c:when>
                 				    <c:otherwise>
                 				      <option value="0" selected="selected">充值</option>
                 				      <option value="1">提现</option>
                 				    </c:otherwise>
                 				  </c:choose>
							    </select>
                       			<div style="margin:15px auto auto auto">
                         			<span>开始时间</span>
                         			<input type="text" id="beginTime" name="beginTime" style="background:white;" value="${beginTime}" class="horizontal marginTop" onkeypress="return false"/>
                             		<span>结束时间</span>
                             		<input type="text" id="endTime" name="endTime" style="background:white;" value="${endTime}" class="horizontal marginTop" onkeypress="return false"/>
                             		<span id="amountFromSpan">起始金额</span>
                         			<input type="text" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" onpaste="return !clipboardData.getData('text').match(/\D/)" ondragenter="return false" style="ime-mode:Disabled" id="amountFrom" name="amountFrom" value="${amountFrom}" class="horizontal marginTop" />
                             		<span id="amountToSpan">结束金额</span>
                             		<input type="text" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" onpaste="return !clipboardData.getData('text').match(/\D/)" ondragenter="return false" style="ime-mode:Disabled" id="amountTo" name="amountTo" value="${amountTo}" class="horizontal marginTop" />
                           		</div>
                           		<div style="margin:15px auto auto auto">
                           			<span>流&nbsp;&nbsp;水&nbsp;&nbsp;号</span>
                             		<input type="text" id="orderNO" name="orderNO" value="${orderNO}" class="horizontal marginTop" />
                         			<span>姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名</span>
                                 	<input type="text" id="name" name="name" value="${name}" class="horizontal marginTop"/>
                             		<span>手&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;机</span>
                                 	<input type="text" id="cellphone" name="cellphone" value="${cellphone}" class="horizontal marginTop"/>
                                 	<span>渠道来源</span>
                           			<select id="channelId" name="channelId">
                             		    <option value="0">全部</option>
										<c:forEach var="channel" items="${channels}" varStatus="status">
										  <c:choose>
										    <c:when test="${channelId == channel.id}">
										      <option value="${channel.id}" selected="selected">${channel.name}</option>
										    </c:when>
										    <c:otherwise>
										      <option value="${channel.id}">${channel.name}</option>
										    </c:otherwise>
										  </c:choose>
                                           </c:forEach>
									</select>
                           			<a href="javascript:search();"><button type="button" class="btn">查询</button></a>
                           			<a href="javascript:resetData();"><button type="button" class="btn">重置</button></a>
                 				</div>
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
                                    <table cellpadding="0" cellspacing="0" border="0" class="div_table">
                                        <thead>
                                            <tr>
                                                <th>序号</th>
                                                <th>流水号</th>
                                                <th>姓名</th>
                                                <th>手机号</th>
                                                <th>金额(元)</th>
                                                <th>渠道</th>
                                                <th>时间</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                        <c:choose>
                                          <c:when test="${fn:length(balances) > 0}">
                                            <c:forEach var="balance" items="${balances}" varStatus="status">
                                            <tr <c:if test="${status.count%2==0}">class="odd"</c:if>>
                                                <td style="width:30px;">${status.count}</td>
                                                <td>${balance.serialNO}</td>
                                                <td><c:if test="${balance.customer != null}">${balance.customer.name}</c:if></td>
                                                <td><c:if test="${balance.customer != null}">${balance.customer.cellphone}</c:if></td>
                                                <td>
                                                <c:set var="money" value="${balance.amount/100}" scope="page" />
                                                <c:set var="reminder" value="${balance.amount%100}" scope="page" />
                                                <fmt:formatNumber value="${money+reminder}" pattern="#,##0.00" />
                                                </td>
                                                <td><c:if test="${balance.customer != null}">${balance.customer.channel.name}</c:if></td>
                                                <td><fmt:formatDate value="${balance.createTime}" type="both" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                                                <td>
                                               <%--  <c:if test="${balance.category == 1}">
                                                	<a href="javascript:audit(${balance.id}, '${balance.customer.cellphone}', ${balance.amount});"><span class="label label-important">审核</span></a>
                                                </c:if> --%>
                                               </td>
                                            </tr>
                                            </c:forEach>
                                          </c:when>
                                          <c:otherwise>
                                        	<tr>
                                                <td colspan="8">暂时还没有数据</td>
                                            </tr>  
                                          </c:otherwise>
                                        </c:choose>
                                        </tbody>
                                        <tfoot>
                                        	<c:if test="${fn:length(balances) > 0}">
	                                        <tr>
	                                        	<td colspan="8">总笔数:${total}笔</td>
	                                        </tr>
	                                        </c:if>
                                        	<tr>
                                        		<td colspan="8">
                                        		    <div id="order-page"></div>
                                        		</td>
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
			$('.breadcrumb').html('<li class="active">交易管理&nbsp;/&nbsp;充值提现</li>');
			$('.marginTop').css('margin', '2px auto auto auto');
		    $('#orderType').find('option[value=${orderType}]').attr('selected',true);
		    $('#channelId').find('option[value=${channelId}]').attr('selected',true);
			$('#channelId').comboSelect();
			$('#orderType').change(function() {
				search();
		    });
			
			var totalPages = '${pages}';
			var beforeDate = new Date();
			beforeDate.setTime(beforeDate.getTime()-1000*60*60*24*6);
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
		            	$('#search').attr('action','${pageContext.request.contextPath}/trade/investor/balance');
		            	$('#page').val(page);
		                $('#search').submit();
		            }  
			    };
				$('#order-page').bootstrapPaginator(options);
			}
			$('#fm').validationEngine('attach', { 
		        promptPosition: 'bottomRight', 
		        scroll: false 
		    }); 
			$('.horizontal').css({width:150});
			$('.horizontalType').css({width:160});
			$('td').addClass('xdsoft_date xdsoft_day_of_week2 xdsoft_date'); 
			$('#beginTime').datetimepicker({
				format:'Y-m-d',
				maxDate:new Date().toLocaleDateString(),
				lang:'ch',
				scrollInput:false,
				timepicker:false,
				onShow:function() {
					if ($('td').hasClass('xdsoft_other_month')) {
						$('td').removeClass('xdsoft_other_month');
					} 
				},
				onSelectDate:function() {
					var beginTime = $('#beginTime').val();
					var beginDate = new Date(Date.parse(beginTime.replace(/-/g,"/")));
					var afterDate = new Date();
					afterDate.setTime(beginDate.getTime()+1000*60*60*24*6);
					$('#endTime').datetimepicker({
						format:'Y-m-d',
						minDate:beginDate.getFullYear()+'/'+(beginDate.getMonth()+1)+'/'+beginDate.getDate(),
						maxDate:new Date().toLocaleDateString(),
						lang:'ch',
						scrollInput:false,
						timepicker:false,
						onShow:function() {
							if ($('td').hasClass('xdsoft_other_month')) {
								$('td').removeClass('xdsoft_other_month');
							}  
						}
					});
				}
			});
			
			$('#endTime').datetimepicker({
				 format:'Y-m-d',
				 maxDate:new Date().toLocaleDateString(),
				 lang:'ch',
				 scrollInput:false,
				 timepicker:false,
				 onShow:function() {
					if ($('td').hasClass('xdsoft_other_month')) {
						$('td').removeClass('xdsoft_other_month');
					}  
				},
				onSelectDate:function() {
					var endTime = $('#endTime').val();
					var endDate = new Date(Date.parse(endTime.replace(/-/g,"/")));
					beforeDate.setTime(endDate.getTime()-1000*60*60*24*6);
					$('#beginTime').datetimepicker({
						format:'Y-m-d',
						maxDate:endDate.getFullYear()+'/'+(endDate.getMonth()+1)+'/'+endDate.getDate(),
						lang:'ch',
						scrollInput:false,
						timepicker:false,
						onShow:function() {
							if ($('td').hasClass('xdsoft_other_month')) {
								$('td').removeClass('xdsoft_other_month');
							}  
						}
					});
				}
			});
		});
	    
		function resetData() { 
			$('#beginTime').val('');
			$('#endTime').val('');
			$('#amountFrom').val('');
			$('#amountTo').val('');
			$('#orderNO').val('');
			$('#orderType').find('option[value=${orderType}]').attr('selected',true);
		    $('#channelId').find('option[value=${channelId}]').attr('selected',true);
			$('#name').val('');
			$('#cellphone').val('');
	        $('#search').submit(); 
		}
		
		function validate() {
			if ($('#amountFrom').val() || $('#amountTo').val()) {
				if (!$('#amountFrom').val()) {
			    	alert('起始金额不能为空！');
			        return false;
				}
				if (!$('#amountTo').val()) {
					$('#amountTo').val(500000);
				}
				if($('#amountFrom').val() != null && $('#amountFrom').val() > 0 && $('#amountTo').val() != null && $('#amountTo').val() > 0){
					if(parseFloat($.trim($('#amountFrom').val())) > parseFloat($.trim($('#amountTo').val()))){
						alert('起始金额不能大于结束金额！');
						return false;
					}
				}
			}
			return true;
		}
		
		function search() {
	    	$('#search').submit(); 
		}
		
		function audit(id, cellphone, amount) {
			var money = amount/100;
			var remainder = amount%100;
			if(confirm('同意用户【'+cellphone+'】提现【'+(money+remainder)+'】元?')) {
				$.post('${pageContext.request.contextPath}/trade/investor/balance/'+id+'/1', function(result){
					if(result) {
						$('#search').submit();
					}
				});
			}
		}
	</script>
</html>