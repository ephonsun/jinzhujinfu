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
				<div class="col-md-2 text-center tip-nav-default">
					<span>1</span>
				</div>
				<div class="col-md-3 text-center">
					<div class="tip-nav-line"></div>
				</div>
				<div class="col-md-2 text-center tip-nav-active">
					<span>2</span>
				</div>
				<div class="col-md-3">
					<div class="tip-nav-line"></div>
				</div>
				<div class="col-md-2 text-center tip-nav-default">
					<span>3</span>
				</div>
			</div>
			<div class="row">
				<div class="col-md-2 text-center">
					<span class="text-muted">填写手机号</span>
				</div>
				<div class="col-md-3"></div>
				<div class="col-md-2 text-center">
					<span class="text-primary">设置密码</span>
				</div>
				<div class="col-md-3"></div>
				<div class="col-md-2 text-center">
					<span class="text-muted">注册成功</span>
				</div>
			</div>
		</div>
		<div class="col-md-2"></div>
	</div>
</div>
<div id="addon-container" class="container">
	<div class="row">
		<div class="col-md-4"></div>
		<div class="col-md-4">
			<form class="form-horizontal" data-toggle="validator" role="form" action="${pageContext.request.contextPath}/register/confirm" method="post">
				<input name="inviter" value="${inviter}" type="hidden" readonly />
				<div class="form-group">
			    	<div class="input-group">
			    		<input name="cellphone" id="cellphone" type="text" class="form-control" value="${cellphone}" aria-describedby="cellphoneSpan" readonly >
			    		<span class="input-group-addon input-addon" id="cellphoneSpan">手机号码</span>
			    	</div>
				    <div class="help-block with-errors"></div>
				</div>
				<div class="form-group">
			    	<div class="input-group">
				    	<input name="sms" type="text" class="form-control" placeholder="请输入短信验证码" aria-describedby="validCodeSpan" required >
				      	<span class="input-group-addon input-addon" id="validCodeSpan">60S</span>
			    	</div>
			    	<div class="help-block with-errors"></div>
				</div>
				<div class="form-group">
			    	<div class="input-group">
			      		<input id="password" name="password" type="password" class="form-control" data-minlength="6" data-maxlength="20" data-error="登陆密码长度为6~20位" placeholder="请输入登录密码" aria-describedby="passwordSpan" required >
			      		<span class="input-group-addon input-addon" id="passwordSpan">登录密码</span>
			      	</div>
			      	<div class="help-block with-errors"></div>
				</div>
				<div class="form-group">
			    	<div class="input-group">
			      		<input type="password" class="form-control" data-match="#password" data-match-error="两次密码不一致，请确认" placeholder="请再次输入登录密码" aria-describedby="passwordVerifySpan" required >
			      		<span class="input-group-addon input-addon" id="passwordVerifySpan">确认密码</span>
			      	</div>
			      	<div class="help-block with-errors"></div>
				</div>
				<div id="message" class="row"><font color="red">${message}</font></div>
				<div class="form-group">
				    <div>
				    	<input class="btn btn-canary btn-block" type="submit" value="注册">
					</div>
				</div>
			</form>
		</div>
		<div class="col-md-4"></div>
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
		var countDown = 120;
		$('#validCodeSpan').click(function() {
			if($.trim($('#validCodeSpan').text()) == '重新获取') {
				var cellphone = $('#cellphone').val();
				var type = 'reg';
				$.post('${pageContext.request.contextPath}/rest/customer/sms/code', {'cellphone':cellphone, 'type':type}, function(result){
					showTime();
				});
			}
		});
		
		showTime();
		
		function showTime() {
			if(countDown == 0) {
				$('#validCodeSpan').text('重新获取');
				countDown = 120;
			} else {
				$('#validCodeSpan').text(countDown+'S');
				countDown--;
				setTimeout(showTime, 1000);
			}
		}
	});
</script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/adapter.js"></script>
</html>