<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="zh-CN">
<jsp:include page="${pageContext.request.contextPath}/header" flush="true" />
<body>
<jsp:include page="${pageContext.request.contextPath}/navbar" flush="true" />
<div class="container container-min-height">
	<div class="row">&nbsp;</div>
</div>
<div class="container">
	<div class="row">
		<div class="col-md-3"></div>
		<div class="col-md-6">
			<form class="form-horizontal" data-toggle="validator" role="form" action="${pageContext.request.contextPath}/rest/customer/recharge" method="get" target="_blank">
				<input name="cellphone" value="${customer.cellphone}" type="hidden" readonly />
				<input name="platform" value="${platform}" type="hidden" readonly />
			  	<div class="form-group">
				    <label class="col-md-3 control-label">充值金额</label>
				    <div class="col-md-9">
				      	<input id="amount" name="amount" type="text" class="form-control" value="${amount}" onkeyup="value=value.replace(/[^\d]/g,'')" pattern="^[1-9]\d*$" data-error="请输入充值金额" placeholder="请输入充值金额" required />
				    </div>
				    <div class="col-md-offset-3 help-block with-errors"></div>
			  	</div>
			  	<div class="form-group">
				    <label class="col-md-3 control-label">银行卡</label>
				    <div class="col-md-9 control-label">
				    	<c:if test="${fn:length(customer.cardNO) > 4}">
							<c:set var="bankCardLength" value="${fn:length(customer.cardNO)}"/>
							<span>
								<img style="height:25px;" src="${pageContext.request.contextPath}/images/bank-logo/${customer.bankNO}.png" />
								${customer.bankName}&nbsp;&nbsp;尾号${fn:substring(customer.idcard, bankCardLength-5, bankCardLength)}
							</span>
						</c:if>
				    </div>
			  	</div>
			  	<div class="form-group">
				    <label class="col-md-3 control-label">银行限额</label>
				    <div class="col-md-9 control-label">
				    	<span class="text-primary">${bank.singleLimit}元/笔 ，${bank.dayLimit}元/日， ${bank.monthLimit}元/月</span>
				    </div>
			  	</div>
			  	<div class="row">
					<label class="col-md-3"></label>
				    <div class="col-md-9">
						<font id="message" color="red"></font>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-3"></label>
				    <div class="col-md-9">
				    	<input class="btn btn-canary btn-block" type="submit" value="充值">
					</div>
				</div>
			</form>
		</div>
		<div class="col-md-3"></div>
	</div>
</div>
<div class="container">
	<div class="row">&nbsp;</div>
</div>
<jsp:include page="${pageContext.request.contextPath}/footer" flush="true" />
</body>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.bootstrap.validator.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		var singleLimit = '${bank.singleLimit}';
		$('#amount').change(function() {
			if($('#amount').val() > singleLimit) {
				$('#amount').val(singleLimit);
				$('#message').html('单笔充值金额最多'+singleLimit+'元').fadeOut(3000);
			} else if($('#amount').val() < 100){
				$('#amount').val(100);
				$('#message').html('单笔充值金额最少100元').fadeOut(3000);
			} else {
				$('#message').html('');
			}
		});
	});
</script>
</html>