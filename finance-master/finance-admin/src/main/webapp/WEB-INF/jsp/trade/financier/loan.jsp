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
                    	<form id="search" action="${pageContext.request.contextPath}/trade/financier/loan" method="post">
                    		<input type="hidden" id="page" name="page" value="${page}" />
                        	<input type="hidden" id="size" name="size" value="${size}" />
                            <div class="input-group">
                       			<div style="margin:15px auto auto auto">
                         			<span>商户名称</span>
                           			<select id="merchantId" style="width:120px;"  name="merchantId">
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
                         			<input type="text" style="width:120px;"  id="beginTime" name="beginTime" style="background:white;" value="${beginTime}" class="horizontal marginTop" onkeypress="return false"/>
                             		<span>结束时间</span>
                             		<input type="text" style="width:120px;"  id="endTime" name="endTime" style="background:white;" value="${endTime}" class="horizontal marginTop" onkeypress="return false"/>
                             		<span>产品名称</span>
                                 	<input type="text" style="width:120px;"  id="productName" name="productName" value="${productName}" class="horizontal marginTop"/> 
                                 	<span>状态</span>
                           			<select id="status" style="width:120px;" name="status">
                             		    <option value="-1">全部</option>
                             		    <option value="0">待放款</option>
                             		    <option value="1">放款成功</option>
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
                        	<div class="navbar navbar-inner block-header">
                                 <a id="export" href="javascript:exportData()"><button class="btn btn-lg btn-success">导出放款数据</button></a>
                                 <a id="remark" href="javascript:remark()"><button class="btn btn-lg btn-success">标记</button></a>
                            </div>
                            <div class="block-content collapse in">
                                <div class="span12">
                                    <table cellpadding="0" cellspacing="0" border="0" class="div_table">
                                        <thead>
                                            <tr>
                                                <th>序号</th>
                                                <th>借款金额(元)</th>
                                                <th>产品名称</th>
                                                <th>商户名称</th>
                                                <th>起息日期</th>
                                                <th>状态</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                        <c:choose>
                                          <c:when test="${fn:length(orders) > 0}">
                                            <c:forEach var="order" items="${orders}" varStatus="status">
                                            <tr onclick="getRow(this, '#BFBFBF')" id="order${order.id}">
                                                <td class="hidden">${order.id}</td>
                                                <td class="hidden">${order.payback}</td>
                                                <td style="width:30px;">${status.count}</td>
                                                <td>${order.actualAmount}</td>
                                                <td>${order.name }</td>
                                                <td><c:if test="${order.merchant != null}">${order.merchant.name}</c:if></td>
                                                <td><fmt:formatDate value="${order.interestDate }" type="both" pattern="yyyy-MM-dd" /></td>
                                                <td>
	                                                <c:choose>
	                                                  <c:when test="${order.payback == 0}">待放款</c:when>
	                                                  <c:otherwise>放款成功</c:otherwise>
	                                                </c:choose>
                                                </td>
                                            </tr>
                                            </c:forEach>
                                          </c:when>
                                          <c:otherwise>
                                        	<tr>
                                                <td colspan="8">暂时还没有待放款订单</td>
                                            </tr>  
                                          </c:otherwise>
                                        </c:choose>
                                        </tbody>
                                        <tfoot>
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
		var rowId = 0;
		$('#export').hide();
		$('#remark').hide();
		function getRow(tr, color) {
			if (rowId == tr.getElementsByTagName('td')[0].innerHTML) {
				$(tr).css('background-color', 'white');
				$('#export').hide();
				$('#remark').hide();
				rowId = 0;
			} else {
				$('#order'+rowId).css('background-color', 'white');
				$(tr).css('background-color', color);
				rowId = tr.getElementsByTagName('td')[0].innerHTML;
				var payback = tr.getElementsByTagName('td')[1].innerHTML;
				if (payback == 0) {
					$('#export').show();
					$('#remark').show();
				} else {
					$('#export').hide();
					$('#remark').hide();
				}
			}
		}
		
		function exportData() {
			if (rowId > 0) {
				$('#search').attr('action','${pageContext.request.contextPath}/trade/loan/export/'+rowId);
	            $('#search').submit(); 
			} else {
				alert('请选择要操作的数据！');
			}
		}
		
		function remark() {
			if (rowId > 0) {
				if (confirm('是否标记为已放款？')) {
					$.post('${pageContext.request.contextPath}/trade/loan/remark/'+rowId, function(result){
						if(result) {
							$('#search').attr('action','${pageContext.request.contextPath}/trade/financier/loan');
			            	$('#page').val(1);
			            	$('#search').submit(); 
						}
					});
				}
			} else {
				alert('请选择要操作的数据！');
			}
		}
	
		$(document).ready(function(){
			$('.breadcrumb').html('<li class="active">交易管理&nbsp;/&nbsp;放款管理</li>');
			$('.marginTop').css('margin', '2px auto auto auto');
		    $('#merchantId').find('option[value=${merchantId}]').attr('selected',true);
			$('#merchantId').comboSelect();
			
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
		            	$('#search').attr('action','${pageContext.request.contextPath}/trade/financier/loan');
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
			$('#productName').val('');
		    $('#status').find('option[value=${-1}]').attr('selected',true);
		    $('#merchantId').find('option[value=0]').attr('selected',true);
		    $('#search').attr('action','${pageContext.request.contextPath}/trade/financier/loan');
        	$('#page').val(1);
            $('#search').submit();
		}
		
		function search() {
			$('#search').attr('action','${pageContext.request.contextPath}/trade/financier/loan');
        	$('#page').val(1);
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