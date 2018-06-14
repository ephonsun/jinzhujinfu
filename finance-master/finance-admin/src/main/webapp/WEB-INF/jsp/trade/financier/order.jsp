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
                    	<form id="search" action="${pageContext.request.contextPath}/trade/financier/order" method="post">
                    		<input type="hidden" id="page" name="page" value="${page}" />
                        	<input type="hidden" id="size" name="size" value="${size}" />
                            <div class="input-group">
                 				<span>交易类型</span>
                 				<select id="orderType" name="orderType" class="horizontalType marginTop">
               					  <c:choose>
               					    <c:when test="${orderType == 1}">
               					      <option value="0">还款订单</option>
               					      <option value="1" selected="selected">放款订单</option>
               					    </c:when>
               					    <c:otherwise>
               					      <option value="0" selected="selected">还款订单</option>
               					      <option value="1">放款订单</option>
               					    </c:otherwise>
               					  </c:choose>
							    </select>
                       			<div style="margin:15px auto auto auto">
                         			<span>商户名称</span>
                           			<select id="merchantId" name="merchantId">
                             		    <option value="0">全部</option>
										<c:forEach var="financier" items="${financiers}" varStatus="status">
										  <c:choose>
										    <c:when test="${merchantId == financier.id}">
										      <option value="${financier.id}" selected="selected">${financier.name}</option>
										    </c:when>
										    <c:otherwise>
										      <option value="${financier.id}">${financier.name}</option>
										    </c:otherwise>
										  </c:choose>
                                           </c:forEach>
									</select>
                         			<span>开始时间</span>
                         			<input type="text" id="beginTime" name="beginTime" style="background:white;" value="${beginTime}" class="horizontal marginTop" onkeypress="return false"/>
                             		<span>结束时间</span>
                             		<input type="text" id="endTime" name="endTime" style="background:white;" value="${endTime}" class="horizontal marginTop" onkeypress="return false"/>
                           		</div>
                           		<div style="margin:15px auto auto auto">
                             		<span>订&nbsp;&nbsp;单&nbsp;&nbsp;号</span>
                             		<input type="text" id="orderNO" name="orderNO" value="${orderNO}" class="horizontal marginTop" />
                             		<span>产品名称</span>
                                 	<input type="text" id="productName" name="productName" value="${productName}" class="horizontal marginTop"/> 
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
                                  <c:if test="${orderType == 0}">
                                    <table cellpadding="0" cellspacing="0" border="0" class="table">
                                        <thead>
                                            <tr>
                                                <th>序号</th>
                                                <th>还款时间</th>
                                                <th>订单号</th>
                                                <th>还款商户</th>
                                                <th>产品名称</th>
                                                <th>放款金额(元)</th>
                                                <th>还款金额(元)</th>
                                                <th>状态</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                        <c:choose>
                                          <c:when test="${fn:length(orders) > 0}">
                                            <c:forEach var="order" items="${orders}" varStatus="status">
                                            <tr <c:if test="${status.count%2==0}">class="odd"</c:if>>
                                                <td style="width:30px;">${status.count}</td>
                                                <td><fmt:formatDate value="${order.paybackTime}" type="both" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                                                <td>${order.paybackNO}</td>
                                                <td><c:if test="${order.merchant != null}">${order.merchant.name}</c:if></td>
                                                <td><c:if test="${order.product != null}">${order.product.name }</c:if></td>
                                                <td><fmt:formatNumber value="${order.principal}" pattern="#,##0" /></td>
                                                <td><fmt:formatNumber value="${order.paybackAmount}" pattern="#,##0.00" /></td>
                                                <td>
                                                <c:choose>
                                                  <c:when test="${order.status == 1}">放款成功</c:when>
                                                  <c:when test="${order.status == 2}">还款成功</c:when>
                                                  <c:otherwise>待放款</c:otherwise>
                                                </c:choose>
                                                </td>
                                            </tr>
                                            </c:forEach>
                                          </c:when>
                                          <c:otherwise>
                                        	<tr>
                                                <td colspan="8">暂时还没有还款订单</td>
                                            </tr>  
                                          </c:otherwise>
                                        </c:choose>
                                        </tbody>
                                        <tfoot>
                                        	<c:if test="${fn:length(orders) > 0}">
	                                        <tr>
	                                        	<td colspan="8">
	                                        		交易数:${total}笔&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;还款总金额:<fmt:formatNumber value="${tradeAmount}" pattern="#,##0.00" />元
	                                        	</td>
	                                        </tr>
                                        	</c:if>
                                        	<tr>
                                        		<td colspan="8">
                                        		    <div id="order-page"></div>
                                        		</td>
                                        	</tr>
                                        	<tr></tr>
                                        </tfoot>
                                    </table>
                                  </c:if>
                                  <c:if test="${orderType == 1}">
                                    <table cellpadding="0" cellspacing="0" border="0" class="div_table">
                                        <thead>
                                            <tr>
                                                <th>序号</th>
                                                <th>放款时间</th>
                                                <th>订单号</th>
                                                <th>还款商户</th>
                                                <th>产品名称</th>
                                                <th>放款金额(元)</th>
                                                <th>状态</th>
                                                <th>操作</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                        <c:choose>
                                          <c:when test="${fn:length(orders) > 0}">
                                            <c:forEach var="order" items="${orders}" varStatus="status">
                                            <tr <c:if test="${status.count%2==0}">class="odd"</c:if>>
                                                <td style="width:30px;">${status.count}</td>
                                                <td><fmt:formatDate value="${order.orderTime}" type="both" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                                                <td>${order.orderNO}</td>
                                                <td><c:if test="${order.merchant != null}">${order.merchant.name}</c:if></td>
                                                <td><c:if test="${order.product != null}">${order.product.name }</c:if></td>
                                                <td><fmt:formatNumber value="${order.principal}" pattern="#,##" /></td>
                                                <td>
                                                <c:choose>
                                                  <c:when test="${order.status == 1}">放款成功</c:when>
                                                  <c:when test="${order.status == 2}">还款成功</c:when>
                                                  <c:otherwise>待放款</c:otherwise>
                                                </c:choose>
                                                </td>
                                                <td>
                                                <c:if test="${order.status == 0 or order.status == 3}">
                                                <a href="javascript:payOrder(${order.id}, '${order.merchant.name}', ${order.principal});"><span class="label label-success">放款</span></a>
                                                </c:if>
                                                </td>
                                            </tr>
                                            </c:forEach>
                                          </c:when>
                                          <c:otherwise>
                                        	<tr>
                                                <td colspan="8">暂时还没有放款订单</td>
                                            </tr>  
                                          </c:otherwise>
                                        </c:choose>
                                        </tbody>
                                        <tfoot>
                                        	<c:if test="${fn:length(orders) > 0}">
	                                        <tr>
	                                        	<td colspan="8">
	                                        		交易数:${total}笔&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;购买总金额:<fmt:formatNumber value="${tradeAmount}" pattern="#,##" />元
	                                        	</td>
	                                        </tr>
	                                        </c:if>
                                        	<tr>
                                        		<td colspan="8">
                                        		    <div id="order-page"></div>
                                        		</td>
                                        	</tr>
                                        </tfoot>
                                    </table>
                                  </c:if>
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
			$('.breadcrumb').html('<li class="active">交易管理&nbsp;/&nbsp;商户订单</li>');
		    $('#orderType').find('option[value=${orderType}]').attr('selected',true);
		    $('#merchantId').find('option[value=${merchantId}]').attr('selected',true);
			$('#merchantId').comboSelect();
			$('.marginTop').css('margin', '2px auto auto auto');
			
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
		            	$('#search').attr('action','${pageContext.request.contextPath}/trade/financier/order');
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
			$('#orderNO').val('');
			$('#productName').val('');
			$('#orderType').find('option[value=${orderType}]').attr('selected',true);
		    $('#merchantId').find('option[value=${merchantId}]').attr('selected',true);
	        $('#search').submit(); 
		}
		
		function search() {
	    	$('#search').submit(); 
		}
		
		function payOrder(orderId, merchant, principal) {
			if(confirm('同意给商户【'+merchant+'】放款【'+principal+'】元?')) {
				$.post('${pageContext.request.contextPath}/trade/financier/order/'+orderId+'/1', function(result){
					if(result) {
		            	$('#search').submit();
					}
				});
			}
		}
	</script>
</html>