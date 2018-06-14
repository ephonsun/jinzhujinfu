<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="zh-CN">
<jsp:include page="${pageContext.request.contextPath}/header" flush="true" />
<body>
<jsp:include page="${pageContext.request.contextPath}/navbar" flush="true" />
<div class="container">
	<div id="tip-nav" class="row">
		<div class="col-md-2"></div>
		<div class="col-md-8">
			<div class="row">
				<div id="payFormLabel" class="col-md-2 text-center tip-nav-active">
					<span>1</span>
				</div>
				<div class="col-md-7 text-center">
					<div class="tip-nav-line"></div>
				</div>
				<!-- <div class="col-md-2 text-center tip-nav-default">
					<span>2</span>
				</div>-->
				<!-- <div class="col-md-3">
					<div class="tip-nav-line"></div>
				</div>  -->
				<div id="payResultLabel" class="col-md-2 text-center tip-nav-default">
					<span>2</span>
				</div>
			</div>
			<div class="row">
				<div class="col-md-2 text-center">
					<span id="submitLabel" class="text-primary">提交订单</span>
				</div>
				<div class="col-md-7"></div>
				<!-- <div class="col-md-2 text-center">
					<span class="text-muted">富有支付</span>
				</div> -->
				<!-- <div class="col-md-3"></div> -->
				<div class="col-md-2 text-center">
					<span id="finishLabel" class="text-muted">购买完成</span>
				</div>
			</div>
		</div>
		<div class="col-md-2"></div>
	</div>
</div>
<div class="container" id="payForm">
	<div class="row" style="height:400px">
		<div class="col-md-3"></div>
		<div class="col-md-5" >
			<form class="form-horizontal">
				<div class="form-group">
					<label for="balance" class="col-sm-3 control-label">可用余额：</label>
				    <div class="col-sm-9">
				    	<input type="text" class="form-control" id="balance" value="${balance}" readonly />
				    </div>
				</div>
				<div class="form-group">
					<label for="product" class="col-sm-3 control-label">产品名称：</label>
				    <div class="col-sm-9">
				    	<input type="text" class="form-control" id="product" value="${product.name}" readonly />
				    </div>
				</div>
				<div class="form-group">
					<label for="amount" class="col-sm-3 control-label">购买金额：</label>
				    <div class="col-sm-9">
				    	<input type="text" class="form-control" id="amount" value="${amount}" readonly />
				    </div>
				</div>
				<c:if test="${product.category.property == 'COMMON' }">
					<div class="form-group">
						<label for="coupon" class="col-sm-3 control-label">使用红包：</label>
						<div class="col-sm-9">
					    	<select class="form-control" id="coupon">
								<option value="0">不使用红包</option>
					    		<c:forEach var="coupon" items="${coupons}" varStatus="s">
							  		<option value="${coupon.id}">${coupon.amount}:${coupon.condition}</option>
					    		</c:forEach>
							</select>
				    	</div>
					</div>
				</c:if>
				<div class="form-group">
					<label for="profit" class="col-sm-3 control-label">预期收益：</label>
				    <div class="col-sm-9">
				    	<p id="profit" class="form-control-static">${profit}元</p>
				    </div>
				</div>
				<div class="form-group">
			      	<div class="checkbox">
			        	<label><input type="checkbox" name="type" id="agree" checked="checked" />我已阅读并同意<a href="${pageContext.request.contextPath}/protocol/product">«产品协议»</a></label>
			    	</div>
			  	</div>
				<div class="row">&nbsp;<p id="errorMsg" style="color: red;" ></p></div>
				<div class="form-group">
				    <div>
				    	<input class="btn btn-canary btn-block" onclick="toPay();" type="button" value="下一步">
					</div>
				</div>
			</form>
		</div>
		<div class="col-md-4" >
		</div>
	</div>
</div>
<div class="container" id="payResult" style="display: none;">
	<div class="row" style="margin:0 auto;width:500px;height:400px">
		<p>购买成功，您可进入“我的资产”中查看收益情况</p>
	    <div class="col-sm-9">
   			<button class="btn btn-canary btn-block" onclick="location.href='/asset/my/1';">查看我的资产</button> 
	    </div>
	</div>
</div>
<jsp:include page="${pageContext.request.contextPath}/footer" flush="true" />
</body>
<script type="text/javascript">
	$(document).ready(function() {
		var profit = '${profit}元';
		setProfit();
		$('#coupon').change(function() {
			setProfit();
		});
		
		function setProfit() {
			var coupon = $('#coupon').val();
			if(coupon > 0) {
				$('#profit').html(profit+'+'+$('#coupon').find('option:selected').text());
			} else {
				$('#profit').html(profit);
			}
		}
		
		$('#agree').change(function(value) {
			var cbAgree = document.getElementById('agree');
		    if(cbAgree.checked){
		    	$('#errorMsg').html('');
		    } else {
		    	$('#errorMsg').html('您还未同意产品协议');
		    }
		});
	});
	
	function toPay() {
		var cbAgree = document.getElementById('agree');
	    if(cbAgree.checked){
	    	var customerId = '${customer.id}';
	    	var productId = '${product.id}';
	    	var couponId = $('#coupon').val();
	    	var portion = '${portion}';
	    	var payType = 1;
	    	$.post('/rest/customer/purchase',
	    			{'customerId':customerId,
	    			'productId':productId,
	    			'couponId':couponId,
	    			'portion':portion,
	    			'payType':payType},
	    	function(json){
    			if(json.code == 200){
    				$('#payForm').hide();
    		    	$('#payResult').show();
    		    	$('#payFormLabel').removeClass('tip-nav-active');
    		    	$('#submitLabel').removeClass('text-primary');
    		    	$('#submitLabel').addClass('text-muted');
    		    	$('#finishLabel').removeClass('text-muted');
    		    	$('#finishLabel').addClass('text-primary');
    		    	$('#payResultLabel').addClass('tip-nav-active');
    			} else {
    				$('#errorMsg').html(json.msg);
    				return;
    			}
    		});
	    } else {
	    	$('#errorMsg').html('您还未同意产品协议');
	    }
	}
</script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/adapter.js"></script>
</html>