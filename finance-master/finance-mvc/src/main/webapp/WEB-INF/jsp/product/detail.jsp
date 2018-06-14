<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html lang="zh-CN">
<jsp:include page="${pageContext.request.contextPath}/header" flush="true" />
<body>
<jsp:include page="${pageContext.request.contextPath}/navbar" flush="true" />
<div class="container">
	<div class="row">&nbsp;</div>
</div>
<c:if test="${product != null}">
<div id="product-detail-top" class="container">
	<div class="row">
		<div class="col-md-8">
			<div class="panel panel-default product-detail-top-panel">
				<div class="row page-header">
					<div class="col-md-8"><h4>${product.name}</h4></div>
					<div class="col-md-4 product-refund">
					<c:if test="${product.repayment == 0}">一次性还本付息</c:if>
					<c:if test="${product.repayment == 1}">按月付息到期还本</c:if>
					</div>
				</div>
				<div class="panel-body text-center">
					<div class="row">
						<div class="col-md-3">年化收益</div>
						<div class="col-md-2">理财期限</div>
						<div class="col-md-3">起购金额</div>
						<div class="col-md-4">募集金额</div>
					</div>
					<div class="row">
						<div class="col-md-3">
							<span><strong>${product.yearIncome}</strong><small>%</small>
							<c:if test="${product.increaseInterest > 0}">+<small>${product.increaseInterest}%</small></c:if>
							</span>
						</div>
						<div class="col-md-2">
							<span><strong>${product.financePeriod}天</strong></span>
						</div>
						<div class="col-md-3">
							<span><strong>${product.lowestMoney}元</strong></span>
						</div>
						<div class="col-md-4"><span><strong>${product.totalAmount}元</strong></span></div>
					</div>
					<div class="row product-list-div">
						<div class="col-md-9 text-left">
							<span class="glyphicon glyphicon-time" aria-hidden="true"></span>起息时间：T(满标日)+1
						</div>
						<div class="col-md-3 text-right">
							<span class="glyphicon glyphicon-user" aria-hidden="true"></span>已投次数：${total}次
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="col-md-4">
			<div class="panel panel-default product-detail-top-panel">
				<div class="row product-list-div">&nbsp;</div>
				<div class="row">
					<div class="col-md-1"></div>
	  				<div class="col-md-10">
						<span>剩余可投金额：<small>${product.totalAmount-product.actualAmount}元</small></span>
					</div>
					<div class="col-md-1"></div>
				</div>
				<div class="row product-list-div">
	  				<div class="col-md-1"></div>
	  				<div class="col-md-10">
		  				<div class=" progress">
		  					<c:set var="percent" value="${product.actualAmount/product.totalAmount}" />
							<div class="progress-bar progress-bar-small" role="progressbar" aria-valuenow="<fmt:formatNumber value="${percent}" pattern="0.00"/>" aria-valuemin="0" aria-valuemax="100" style="width:<fmt:formatNumber value="${percent}" type="percent" />">
						    	<span class="sr-only"><fmt:formatNumber value="${percent}" type="percent" /></span>
						  	</div>
						</div>
	  				</div>
	  				<div class="col-md-1"></div>
	  			</div>
	  			<div class="row product-list-div">
	  				<div class="col-md-1"></div>
	  				<div class="col-md-10">
		  				<div class="input-group input-group-sm">
							<span class="input-group-addon"><font id="lowestAmount">${product.lowestMoney}</font>&nbsp;×&nbsp;</span>
						  	<input id="number" type="text" class="form-control" style="text-align: center;" size="6" value="1" placeholder="份数" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" />
						  	<span class="input-group-addon input-addon">份&nbsp;=&nbsp;<small id="purchaseAmount">${product.lowestMoney}</small>元</span>
						</div>
					</div>
					<div class="col-md-1"></div>
	  			</div>
	  			<div class="row text-center">
	  				<div class="col-md-1"></div>
	  				<div class="col-md-10">
	  				<c:choose>
	  					<c:when test="${isLogin == true}">
	  						<a id="product-${product.id}" href="javascript:void();" class="btn btn-default btn-block" role="button">&nbsp;</a>
	  					</c:when>
	  					<c:otherwise>
	  						<a href="${pageContext.request.contextPath}/login" class="btn btn-canary btn-block" role="button">立即登录</a>
	  					</c:otherwise>
	  				</c:choose>
	  				</div>
	  				<div class="col-md-1"></div>
	  			</div>
			</div>
		</div>
	</div>
</div>
<div id="product-detail-body" class="container">
	<div class="row">
		<div class="col-md-8">
			<div class="panel panel-default">
				<div class="panel-body">
					<ul id="product-detail-tabs" class="nav nav-tabs" role="tablist">
				    	<li role="presentation" class="active">
				      		<a href="#remark" id="remark-tab" role="tab" data-toggle="tab" aria-controls="remark" aria-expanded="true">项目概述</a>
				      	</li>
				      	<li role="presentation">
				      		<a href="#risk" role="tab" id="risk-tab" data-toggle="tab" aria-controls="risk">风险控制</a>
				      	</li>
				      	<li role="presentation">
				      		<a href="#attachment" role="tab" id="attachment-tab" data-toggle="tab" aria-controls="attachment">相关资料</a>
				      	</li>
				    </ul>
				    <div id="product-detail-content" class="row tab-content product-detail-content-div">
				    	<div role="tabpanel" class="tab-pane fade in active" id="remark" aria-labelledby="remark-tab">
						${product.summary}
				      	</div>
				      	<div role="tabpanel" class="tab-pane fade" id="risk" aria-labelledby="risk-tab">
						${product.risk}
				      	</div>
				      	<div role="tabpanel" class="tab-pane fade" id="attachment" aria-labelledby="attachment-tab">
				      	<c:choose>
				      		<c:when test="${isLogin == true && product.attachment != null && fn:length(fn:trim(product.attachment)) > 0}">
				      			<c:forEach var="attachment" items="${fn:split(fn:trim(product.attachment), ',')}" varStatus="s">
					      			<c:if test="${s.first || s.count%3 == 1}">
									<c:out value="<div class='row'>" escapeXml="false"></c:out>
									</c:if>
									<div class="col-xs-6 col-md-4" class="bigImages">
							      		<img src="${attachment}" width="200" class="thumbnail" alt="资料">
							  		</div>
									<c:if test="${s.last || (s.count+1)%3 == 1}">
								  	<c:out value="</div>" escapeXml="false"></c:out>
								  	</c:if>
					      		</c:forEach>
				      		</c:when>
				      		<c:otherwise>
				      			<div class="row">
									<div class="col-md-3"></div>
									<div class="col-md-6">
										<p class="text-muted">相关资料需要登录查看，请先<a href="${pageContext.request.contextPath}/login">登录</a></p>
									</div>
									<div class="col-md-3"></div>
								</div>
				      		</c:otherwise>
				      	</c:choose>
				      	</div>
				    </div>
				</div>
			</div>
		</div>
		<div id="product-order-list" class="col-md-4">
			<div class="panel panel-default">
				<div class="panel-heading order-list-header">
					<span>投资记录</span>
				</div>
				<div class="panel-body order-list-body">
					<c:choose>
                       	<c:when test="${fn:length(orders) > 0}">
							<div class="row marquee">
								<table class="table table-hover">
									<tbody>
	                                   <c:forEach var="order" items="${orders}" varStatus="s">
											<tr>
												<td>${fn:substring(order.customer.cellphone, -1, 3)}****${fn:substring(order.customer.cellphone, 7, -1)}</td> 
												<td><fmt:formatDate value="${order.orderTime}" type="both" pattern="yyyy-MM-dd HH:mm:ss" /></td>
												<td><fmt:formatNumber value="${order.principal}" pattern="#,##0" type="currency"/></td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>
                   		</c:when>
                   		<c:otherwise>
                   		</c:otherwise>
                  </c:choose>
                  <c:choose>
                       	<c:when test="${fn:length(orders) == 0}">
							<div class="row">
	                  	    	<div style="text-align: center; margin-top:80px; vertical-align: middle;color:#F00;">暂无投资记录</div>  
							</div>
                   		</c:when>
                   		<c:otherwise>
                   		</c:otherwise>
                  </c:choose>
				</div>
			</div>
		</div>
	</div>
</div>
</c:if>
<div class="container">
	<div class="row">&nbsp;</div>
</div>
<div id="dialog" class="modal fade" tabindex="-1" role="dialog">
	<div class="modal-dialog" role="document">
    	<div class="modal-content">
      		<div class="modal-header">
        		<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        		<h4 class="modal-title"><span class="glyphicon glyphicon-info-sign" aria-hidden="true"></span>提示信息</h4>
      		</div>
      		<div class="modal-body">
        		<p>您还未登录，请先登录</p>
      		</div>
      		<div class="modal-footer">
        		<button type="button" class="btn btn-canary" data-dismiss="modal">确定</button>
      		</div>
    	</div><!-- /.modal-content -->
  	</div><!-- /.modal-dialog -->
</div>
<jsp:include page="${pageContext.request.contextPath}/footer" flush="true" />
</body>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.marquee.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/show.original.image.js"></script>
<script type="text/javascript">
	function showImageEvent(){
		var workImg=document.getElementsByTagName('img');
		for(var j=0; j<workImg.length;j++){
			workImg[j].onclick=ImgShow;
		}
	}

	$(document).ready(function() {
		var nowTime = new Date('${systemTime}');
		$('#product-detail-tabs a').click(function(e) {
			e.preventDefault();
			$(this).tab('show');
		});
		showImageEvent();
		/*
		$('#product-detail-tabs a[href="#profile"]').tab('show') // Select tab by name
		$('#product-detail-tabs a:first').tab('show') // Select first tab
		$('#product-detail-tabs a:last').tab('show') // Select last tab
		$('#product-detail-tabs li:eq(2) a').tab('show') // Select third tab (0-indexed)
		*/
		
		$('#number').change(function() {
			$('#purchaseAmount').html($('#lowestAmount').html()*$('#number').val());
		});
		
		$('.marquee').marquee({
			delayBeforeStart:0,
			duration:8000,
	        direction:'up',
	        duplicated:true,
	        gap:0,
	        startVisible:true
		});
		
		/*<c:if test="${product != null && isLogin == true}">*/
		countDown();
		function countDown() {
			var raisedTime = new Date('<fmt:formatDate value="${product.raisedTime}" pattern="yyyy-MM-dd HH:mm:ss"/>');
			var diffMillis = raisedTime.getTime()-nowTime.getTime();
			if(diffMillis >= 1000) {
				$('#product-${product.id}').text(getCountDown(diffMillis));
				var interval = window.setInterval(function() {
			        if(diffMillis >= 1000) {
				    	diffMillis = diffMillis-1000;
				    	$('#product-${product.id}').unbind();
				    	$('#product-${product.id}').attr('class', 'btn btn-default btn-block');
				    	$('#product-${product.id}').text(getCountDown(diffMillis));
			        } else {
			        	$('#product-${product.id}').bind('click', function() {
  			    			buy('${product.id}');
  			    		});
			        	$('#product-${product.id}').attr('class', 'btn btn-canary btn-block');
  			    		$('#product-${product.id}').text('立即购买');
				    	window.clearInterval(interval);
			        }
			    }, 1000);
			} else {
				$('#product-${product.id}').bind('click', function() {
		    		buy('${product.id}');
		    	});
				$('#product-${product.id}').attr('class', 'btn btn-canary btn-block');
		    	$('#product-${product.id}').text('立即购买');
			}
		}
		/*</c:if>*/
		
		function getCountDown(diffMillis) {
			var result = '';
			var day = Math.floor(diffMillis/(1000*60*60*24));
	    	var hour = Math.floor(diffMillis/(1000*60*60)) - (day*24);;
	        var minute = Math.floor(diffMillis/(1000*60)) - (day*24*60) - (hour*60);;
	        var second = Math.floor(diffMillis/1000) - (day*24*60*60) - (hour*60*60) - (minute*60);
	        if(hour <= 9) {
		    	hour = '0'+hour;
	    	}
	    	if(minute <= 9) {
	    		minute = '0'+minute;
	    	}
	    	if(second <= 9){
	    		second = '0'+second;
	    	}
	    	result = hour+'时'+minute+'分'+second+'秒';
	    	if(day > 0) {
	    		result = day+'天'+result;
	    	}
	    	
	    	return result;
		}
	});
	
	function buy(productId) {
		var number = $('#number').val();
		if(productId > 0 && number > 0) {
			window.location.href = '${pageContext.request.contextPath}/product/buy/'+productId+'?number='+number;
		}
	}
</script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/adapter.js"></script>
</html>