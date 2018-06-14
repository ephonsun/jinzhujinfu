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
			<form class="form-horizontal" data-toggle="validator" role="form" action="${pageContext.request.contextPath}/customer/${customer.id}/open" method="post">
			<!-- <form class="form-horizontal" data-toggle="validator" role="form">  -->
				<input name="id" value="${customer.id}" type="hidden" readonly />
				<input name="cellphone" value="${customer.cellphone}" type="hidden" readonly />
				<input id="bankNO" name="bankNO" value="${customer.bankNO}" type="hidden" />
				<input id="bankName" name="bankName" value="${customer.bankName}" type="hidden" />
				<div class="form-group">
				    <label class="col-md-3 control-label">姓名</label>
				    <div class="col-md-9">
				    	<input name="name" type="text" class="form-control" value="${customer.name}" data-minlength="2" data-error="请输入真实姓名" placeholder="请输入真实姓名" required />
				    </div>
				    <div class="col-md-offset-3 help-block with-errors"></div>
			  	</div>
			  	<div class="form-group">
				    <label class="col-md-3 control-label">身份证</label>
				    <div class="col-md-9">
				      	<input name="idcard" type="text" class="form-control" value="${customer.idcard}" pattern="(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)" data-error="请输入正确的身份证号" placeholder="请输入正确的身份证号" required />
				    </div>
				    <div class="col-md-offset-3 help-block with-errors"></div>
			  	</div>
			  	<div class="form-group">
				    <label class="col-md-3 control-label">开户行省市</label>
				    <div class="col-md-9">
				    	<select id="province" class="form-control">
				    	<option value="">请选择</option>
				    	<c:forEach items="${provinces}" var="province" varStatus="s">
				    		<option value="${province.provinceId}">${province.province}</option>
				    	</c:forEach>
				    	</select>
				    </div>
				    <div class="col-md-offset-3 help-block with-errors"></div>
			  	</div>
			  	<div class="form-group">
				    <label class="col-md-3 control-label">开户行区县</label>
				    <div class="col-md-9">
				    	<select id="city" name="city" class="form-control" required>
				    		<option value="">请选择</option>
				    	</select>
				    </div>
				    <div class="col-md-offset-3 help-block with-errors"></div>
			  	</div>
			  	<div class="form-group">
				    <label class="col-md-3 control-label">开户行行别</label>
				    <div class="col-md-9">
				    	<select id="bank" class="form-control" required>
				    	<c:forEach items="${banks}" var="bank" varStatus="s">
				    		<option value="${bank.bankNO}">${bank.bankName}</option>
				    	</c:forEach>
				    	</select>
				    </div>
				    <div class="col-md-offset-3 help-block with-errors"></div>
			  	</div>
			  	<div id="bankLimit" class="form-group">
				    <label class="col-md-3 control-label">银行限额</label>
				    <div id="bankDesc" class="col-md-9 control-label"></div>
			  	</div>
			  	<div class="form-group">
				    <label class="col-md-3 control-label">银行卡号</label>
				    <div class="col-md-9">
				      <input name="cardNO" type="text" class="form-control" value="${customer.cardNO}" required />
				    </div>
				    <div class="col-md-offset-3 help-block with-errors"></div>
			  	</div>
			  	<div class="form-group">
			  		<label class="col-md-3 control-label">交易密码</label>
				    <div class="col-md-9">
				    	<input id="tradePassword" name="tradePassword" type="password" class="form-control" data-minlength="6" data-maxlength="20" data-error="密码长度为6~20位" placeholder="请输入密码" required >
				    </div>
				    <div class="col-md-offset-3 help-block with-errors"></div>
				</div>
				<div class="form-group">
			  		<label class="col-md-3 control-label">确认密码</label>
				    <div class="col-md-9">
				      	<input type="password" class="form-control" data-match="#tradePassword" data-match-error="两次密码不一致，请确认" placeholder="请再次输入密码" required >
				    </div>
				    <div class="col-md-offset-3 help-block with-errors"></div>
				</div>
				<div id="message" class="row">
					<label class="col-md-3"></label>
				    <div class="col-md-9">
						<font color="red">${message}</font>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-3"></label>
				    <div class="col-md-9">
				    	<input class="btn btn-canary btn-block" type="submit" value="下一步">
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
		$('#bankLimit').hide();
		
		var banks = [];
		$.get('${pageContext.request.contextPath}/rest/service/pay/bank', function(result){
			if(result && result.code == 200){
				banks = result.data.banks;
			}
		});
		
		$('#province').change(function() {
			var province = $('#province').val();
			if(province != null && province != '') {
				$.get('${pageContext.request.contextPath}/rest/service/cities', {provinceId:province}, function(result){
					if(result && result.code == 200){
						$('#city option').remove();
						$.each(result.data.cities, function(i, city){
							$('#city').append('<option value="'+city.cityId+'">'+city.city+'</option>');
						});
					}
				});
			}
		});
		
		$('#bank').change(function() {
			var bankNO = $('#bank').val();
			var bankName = $('#bank').find('option:selected').text();
			if(bankNO != null && bankNO != '') {
				$('#bankNO').val(bankNO);
				$('#bankName').val(bankName);
				$.each(banks, function(i, bank){
					if(bank.bankNO == bankNO) {
						$('#bankLimit').show();
						$('#bankDesc').html('<span class="text-primary">'+bank.singleLimit+'元/笔 '+bank.dayLimit+'元/日 '+bank.monthLimit+'元/月</span>');
					}
				});
			}
		});
	});
</script>
</html>