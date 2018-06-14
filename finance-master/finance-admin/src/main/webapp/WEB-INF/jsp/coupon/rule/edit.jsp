<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
                	<!-- content begin -->
                    <div class="row-fluid">
                    	<form class="form-horizontal" id="fm" method="post" onsubmit="return startValidate();" action="${pageContext.request.contextPath}/coupon/rule/save">
                    		<input name="id" value="${rule.id}" type="hidden">
                    		<input name="category" value="${rule.category}" type="hidden">
                    		<input name="status" value="${rule.status}" type="hidden">
                    		<input id="couponIds" name="couponIds" value="${rule.couponIds}" type="hidden">
                    		<input id="couponAmounts" name="couponAmounts" value="${rule.couponIds}" type="hidden">
                    		<input name="base64" value="${base64}" type="hidden">
                    		<div class="well">
	                    		<div class="control-group" id="noticeType">
									<label class="control-label"><span class="required">*</span>赠送规则</label>
									<div class="controls">
									<c:forEach items="${categories}" var="category">
                                    	<c:choose>
	                                  		<c:when test="${category.key == rule.category}">
	                                  		<input type="text" value="${category.value}" class="text-input span4" readonly="readonly" />
	                                  		</c:when>
                                 		</c:choose>
                               		</c:forEach>
									</div>
								</div>
								<div class="control-group">
									<label class="control-label"><span class="required">*</span>选择红包</label>
									<div class="controls">
										<input type="text" id="coupon" class="text-input span4" readonly="readonly" onclick="showCouponDialog();"/>
										<button type="button" class="btn btn-icon btn-default" onclick="showCouponDialog()">选择</button>
									</div>
								</div>
								<div class="control-group">
									<label class="control-label"><span class="required">*</span>红包预览</label>
									<div class="controls" id="ruleCoupons">
									<c:if test="${not empty rule.couponIds }">
                                      <c:forEach var="couponId" items="${fn:split(rule.couponIds,',')}" varStatus="s">
										<c:forEach var="couponAmount" items="${fn:split(rule.couponAmounts,',')}" varStatus="st">
										  <c:if test="${s.index == st.index}">
											<a id="div-${couponId}-${couponAmount}" href="javascript:removeCoupon('div-${couponId}-${couponAmount}', ${couponId}, ${couponAmount});">
												<span class="label label-important" style="height:20px;width:50px;margin-top:1px;padding-top:8px" title="点击可删除">${couponAmount}元</span>
											</a>
										  </c:if>
										</c:forEach>
                                      </c:forEach>
                                    </c:if>
									</div>
								</div>
								<div class="control-group">
									<label class="control-label"></label>
									<div class="controls">
										<button type="submit" id="save" class="btn btn-icon btn-primary glyphicons circle_ok"><i></i>保存</button>
										<button type="reset" class="btn btn-icon btn-default glyphicons circle_remove" onclick="quit();"><i></i>返回</button>
									</div>
								</div>
                    		</div>
                    	</form>
                    </div>
                    <!-- content end -->
                    <div class="modal hide fade" id="couponList" role="dialog" style="top:2%;width:800px;">
					  	<div class="modal-dialog modal-lg">
					    	<div class="modal-content">
					      		<div class="modal-header">
					        		<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					        			<span aria-hidden="true">&times;</span>
					        		</button>
					        		<h4 class="modal-title" id="myModalLabel">选择红包</h4>
					      		</div>
					      		<div class="modal-body" style="max-height:250px;">
			                        <table class="table table-striped">
					                	<thead>
					                    	<tr>
				                                <th style="width:30px;">序号</th>
				                                <th style="width:200px;">使用条件</th>
				                                <th>红包金额(元)</th>
		    	  								<th>失效时间</th>
				                                <th>操作</th>
					                        </tr>
					                   </thead>
					                   <tbody id="couponBody">
			                     	   </tbody>
			                     	   <tfoot>
			                      	        <tr>
			                      				<td colspan="5">
			                      					<div id="coupon-page"></div>
			                      				</td>
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
		$(document).ready(function() {
			$('.breadcrumb').html('<li class="active">红包管理&nbsp;/&nbsp;<a href="javascript:quit();">红包规则</a>/&nbsp;编辑</li>');
		    $('.msg').css('color', 'red');
		    $('#fm').validationEngine('attach', { 
		        promptPosition: 'centerRight', 
		        scroll: false,
		        showOneMessage : true
		   	});
		});	
	
		function showCouponDialog() {
			search(1);
			$('#couponList').modal('show');
		}
		
		function search(page) {
			$.post('${pageContext.request.contextPath}/coupon/list/'+page+'/15', function(result) {
				if (result.total > 0) {
					$('#couponBody').html('');
			       	for (var i=0; i<result.coupons.length; i++) {
			    		var coupon = result.coupons[i];
			    		var tr = '<tr>';
			    	   	tr += '<td>'+(i+1)+'</td>';
			    	   	tr += '<td>'+coupon.condition+'</td>';
			    	   	tr += '<td>'+coupon.amount+'</td>';
			    	   	tr += '<td>'+coupon.expiryDate+'</td>';
			    	   	tr += '<td><a href="javascript:choiceCoupon('+coupon.id+', '+coupon.amount+');">选择</a></td>';
			    	   	tr += '</tr>';
			    	   	$('#couponBody').append(tr);
					}
			       
			       	var totalPages = Math.ceil(result.total/15);
			       	var options = {
				    	currentPage: result.page,
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
			            	search(page);
			            }  
				    };
					$('#coupon-page').bootstrapPaginator(options);
		       	} else {
		    		$('#couponBody').html('<tr><td colspan="5">请先添加红包</td></tr>');	
		    	}
			});	
		}
		
		function choiceCoupon(id, amount) {
			//if(!duplicateCoupon(id, amount)) {
				var html = '<a id="div-'+id+'-'+amount+'" href="javascript:removeCoupon(\'div'+id+'\', '+id+', '+amount+');">';
				html += '<span class="label label-important" style="height:20px;width:50px;margin-top:1px;padding-top:8px" title="点击可删除">'+amount+'元</span>&nbsp;';
				html += '</a>';
				$('#ruleCoupons').append(html);
			//} else {
			//	alert('该红包已经被选择，请选择其它的');
			//}
		}	
		
		function quit(){
			window.location.href='${pageContext.request.contextPath}/coupon/rule/${base64}';
		}
		
		function startValidate() {
			var children = $('#ruleCoupons').children();
			if(children && children.length > 0) {
				var couponIds = [];
				var couponAmounts = [];
				$.each(children, function(index, child) {
					var coupon = child.id.split('-');
					if(coupon.length == 3) {
						couponIds.push(coupon[1]);
						couponAmounts.push(coupon[2])
					}
				});
				$('#couponIds').val(couponIds.join());
				$('#couponAmounts').val(couponAmounts.join());
				return true;
			} else {
				alert('每个规则请至少选择一个红包');
			 	return false;
			}
		}
		
		function removeCoupon(id, couponId, couponAmount) {
			$('#'+id).remove();
		}
		
		function duplicateCoupon(id, amount) {
			var result = false;
			var children = $('#ruleCoupons').children();
			$.each(children, function(index, child) {
				if(child.id == 'div-'+id+'-'+amount) {
					result = true;
					return false;
				}
			});
			return result;
		}
	</script>
</html>