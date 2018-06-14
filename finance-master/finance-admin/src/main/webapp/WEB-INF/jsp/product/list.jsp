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
                   	<jsp:include page="${pageContext.request.contextPath}/breadcrumb" flush="true" />
					<div class="well">
						<div class="row-fluid">
							<form id="search"  action="${pageContext.request.contextPath}/product/list" method="post">
								<input type="hidden" id="page" name="page" value="${page}" />
								<input type="hidden" id="size" name="size" value="${size}" />
	                         	<div class="row-fluid">
	                         		<span style="vertical-align: 3px">产品名称</span>
									<input id="name" name="name" value="${name}" type="text"/>&nbsp;
	                         		<span style="vertical-align: 3px">所属分类</span>
									<select id="categoryId" name="categoryId">
										<option value="0"></option>
										<c:forEach items="${categories}" var="category">
                                          	<c:choose>
                                           		<c:when test="${category.id == categoryId}">
                                           			<option value="${category.id}" selected="selected">${category.name}</option>
                                           		</c:when>
                                           		<c:otherwise>
                                           			<option value="${category.id}" >${category.name}</option>
                                           		</c:otherwise>
                                          	</c:choose>
                                        </c:forEach>
									</select>
	                         		<span style="vertical-align: 3px">关联商户</span>
                         			<select id="merchantId" name="merchantId">
										<option value="0"></option>
                                           <c:forEach items="${merchants}" var="merchant">
                                           	<c:choose>
                                           		<c:when test="${merchant.id == merchantId}">
                                           			<option value="${merchant.id}" selected="selected">${merchant.name}</option>
                                           		</c:when>
                                           		<c:otherwise>
                                           			<option value="${merchant.id}">${merchant.name}</option>
                                           		</c:otherwise>
                                          		</c:choose>
                                           </c:forEach>	
									</select>
	                         		<span style="vertical-align: 3px">理财期限</span> 
                                    <input type="text" id="periodStart" name="periodStart" value="${periodStart}" class="validate[custom[integer]] text-input  horizontal marginTop" placeholder="请输入整数" onkeypress="addClass(this.id)" onblur="addClass(this.id)"/>
								    <span style="vertical-align: 3px">至</span> 
								    <input type="text" id="periodEnd" name="periodEnd"  value="${periodEnd}" class="validate[custom[integer]] text-input  horizontal marginTop" placeholder="请输入整数" onkeypress="addClass(this.id)" onblur="addClass(this.id)"/>
                                    <input type="submit" value="查询" style="margin-top: -10px" class="btn btn-default" />
	                         	</div>
							</form>
						</div>
					</div>
                    <div class="row-fluid">
                        <!-- block -->
                        <div class="block">
                            <div class="navbar navbar-inner block-header">
                                 <a href="javascript:add()"><button class="btn btn-lg btn-success">新增</button></a>
                            </div>
                            <div class="block-content collapse in">   
                                <div class="span12">
                                    <table cellpadding="0" cellspacing="0" border="0" class="div_table">
                                         <thead>
                                            <tr>
                                                <th>序号</th>
                                                <th>产品名称</th>
                                                <th>年化收益</th>
                                                <th>募集金额(元)</th>
                                                <th>募集进度</th>
                                                <th>所属分类</th>
                                                <th>理财期限</th>
                                                <th>状态</th>
                                                <th>审核状态</th>
                                                <th>开售时间</th>
                                                <th>操作</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                        <c:choose>
                                          <c:when test="${fn:length(products) > 0}">
                                            <c:forEach var="product" items="${products}" varStatus="status">
                                            <c:set var="raisedTime"><fmt:formatDate value="${product.raisedTime}" type="both" pattern="yyyy-MM-dd HH:mm:ss" /></c:set>          
                                            <tr <c:if test="${status.count%2==0}">class="odd"</c:if>>
                                                <td style="width:30px;">${status.count}</td>
                                                <td style="text-align: left">${product.name}</td>
                                                <td>
                                                <c:choose>
                                                  <c:when test="${product.increaseInterest > 0}">
                                                 	${product.yearIncome}%+${product.increaseInterest}%
                                                  </c:when>
                                                  <c:otherwise>
                                                 	${product.yearIncome}%
                                                  </c:otherwise>
                                                </c:choose>
                                                </td>
                                                <td><fmt:formatNumber type="currency" value="${product.totalAmount}" currencySymbol="" /></td>
                                                <td>
                                               	<c:choose>
                                                  <c:when test="${product.status == 1}">
                                                	<c:choose>
                                              		  <c:when test="${raisedTime > systemTime && product.actualAmount < product.totalAmount}">
														<div class="progress" style="margin-bottom: 1px;height: 17px;width: 60px">
			                                            	<div class="progress-bar" role="progressbar" aria-valuemin="0" aria-valuemax="100" style="width:<fmt:formatNumber value="${fn:substring(product.actualAmount/product.totalAmount * 100,0,5)}" type="number" pattern="#,##0.00"/>%;background-color:green;">
														    	<span class="sr-only"><fmt:formatNumber value="${fn:substring(product.actualAmount/product.totalAmount * 100,0,5)}" type="number" pattern="#,##0.00"/>%</span>
														    </div>
				                                        </div>
													  </c:when>
													  <c:when test="${raisedTime <= systemTime && product.actualAmount < product.totalAmount}">
														<div class="progress" style="margin-bottom: 1px;height: 17px;width: 60px">
			                                            	<div class="progress-bar" role="progressbar" aria-valuemin="0" aria-valuemax="100" style="width:<fmt:formatNumber value="${fn:substring(product.actualAmount/product.totalAmount * 100,0,5)}" type="number" pattern="#,##0.00"/>%;background-color:#3498DB;">
														    	<span class="sr-only"><fmt:formatNumber value="${fn:substring(product.actualAmount/product.totalAmount * 100,0,5)}" type="number" pattern="#,##0.00"/>%</span>
														    </div>
				                                        </div> 
													  </c:when>
													  <c:otherwise>
														   		<div class="progress" style="margin-bottom: 1px;height: 17px;width: 60px">
			                                               			<div class="progress-bar" role="progressbar" aria-valuemin="0" aria-valuemax="100" style="width:<fmt:formatNumber value="${fn:substring(product.actualAmount/product.totalAmount * 100,0,5)}" type="number" pattern="#,##0.00"/>%;background-color:#E74C3C;">
														    			<span class="sr-only">
														    				<fmt:formatNumber value="${fn:substring(product.actualAmount/product.totalAmount * 100,0,5)}" type="number" pattern="#,##0.00"/>%
														    			</span>
														    		</div>
				                                                </div> 
													  </c:otherwise>
                                              		</c:choose>
                                                  </c:when>
                                                  <c:otherwise>
                                                	<span style="color:green;">--</span> 
                                                  </c:otherwise>
											    </c:choose>
                                                </td>
                                                <td>${product.category.name}</td>
                                                <td>${product.financePeriod}天</td>
                                                <td class="status">
                                                <c:choose>
                                                  <c:when test="${product.status == 1}">
                                                	<c:choose>
                                               		  <c:when test="${raisedTime > systemTime && product.actualAmount < product.totalAmount}">
													  	<span style="color:#3498DB;">未开始</span> 
													  </c:when>
													  <c:when test="${raisedTime <= systemTime && product.actualAmount < product.totalAmount}">
														<span style="color:#E74C3C;">在售</span> 
													  </c:when>
													  <c:otherwise>
														<span style="color:#000000;">售罄</span> 
													  </c:otherwise>
                                               		</c:choose>
                                                  </c:when>
                                                  <c:otherwise>
                                                	<span style="color:green;">--</span> 
                                                  </c:otherwise>
												</c:choose>
                                                </td>
                                                <td>
                                                <c:choose>
                                               	  <c:when test="${product.status == 0}">
													<span style="color:fuchsia;">待审核</span> 
												  </c:when>
												  <c:when test="${product.status == 1}">
													<span style="color:#3498DB;">已审核</span> 
												  </c:when>
												  <c:otherwise>
													<span style="color:green;">--</span> 
												  </c:otherwise>
                                               	</c:choose>
                                                </td>
                                                <td><fmt:formatDate value="${product.raisedTime}" type="both" pattern="yyyy-MM-dd HH:mm" /></td>
                                                <td>
                                                <c:if test="${product.status == 0}">
                                                	<a href="javascript:enable(${product.id}, 1)"><span class="label label-success">审核</span></a>
                                       				<a href="javascript:edit(${product.id})"><span class="label label-info">编辑 </span></a>
                                                </c:if>
                                                <c:if test="${product.status == 1}">
                                                	<!-- <a href="javascript:void();"><span class="label label-important">满标</span></a> -->
                                                </c:if>
                                                </td>
                                            </tr>            
                                            </c:forEach>
                                          </c:when>
                                          <c:otherwise>
                                        	<tr>
                                                <td colspan="12">暂时还没产品列表</td>
                                            </tr>  
                                          </c:otherwise>
                                        </c:choose>
                                        </tbody>
                                        <tfoot>
                                        	<tr>
                                        		<td colspan="12"><div id="product-page"></div></td>
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
		var queryString = '?name=${name}&categoryId=${categoryId}&merchantId=${merchantId}&periodStart=${periodStart}&periodEnd=${periodEnd}&page=${page}&size=${size}';
		$(document).ready(function(){
			$('.breadcrumb').html('<li class="active">产品管理</li>');
			$('.marginTop').css('width', '100px');
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
		            	$('#search').attr('action','${pageContext.request.contextPath}/product/list');
		            	$('#page').val(page);
		                $('#search').submit();
		            }  
			    };
				$('#product-page').bootstrapPaginator(options);
			}
			
			$('input').iCheck({
				checkboxClass: 'icheckbox_flat-green',
			});
			$('#categoryId').find('option[value=${categoryId}]').attr('selected',true);
			$('#categoryId').comboSelect();
			$('#merchantId').find('option[value=${merchantId}]').attr('selected',true);
			$('#merchantId').comboSelect();
		});
		
		function add() {
			var base64 = $.base64.encode(queryString);
			window.location.href = '${pageContext.request.contextPath}/product/add/'+base64;
		}
		
		function edit(productId) {
			var base64 = $.base64.encode(queryString);
			window.location.href = '${pageContext.request.contextPath}/product/'+productId+'/edit/'+base64;
		}
		
		function enable(productId, operate) {
			if(confirm('确认要审核此标的?')) {
				$.post('${pageContext.request.contextPath}/product/'+productId+'/'+operate, function(result){
					if(result) {
						$('#search').submit();
					}
				});
			}
		}
	</script>
</html>