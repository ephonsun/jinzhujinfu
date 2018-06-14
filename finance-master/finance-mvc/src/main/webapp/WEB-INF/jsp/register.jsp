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
				<div class="col-md-2 text-center tip-nav-active">
					<span>1</span>
				</div>
				<div class="col-md-3 text-center">
					<div class="tip-nav-line"></div>
				</div>
				<div class="col-md-2 text-center tip-nav-default">
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
					<span class="text-primary">填写手机号</span>
				</div>
				<div class="col-md-3"></div>
				<div class="col-md-2 text-center">
					<span class="text-muted">设置密码</span>
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
			<form class="form-horizontal" data-toggle="validator" role="form" action="${pageContext.request.contextPath}/register" method="post">
				<input id="code" name="code" type="hidden" />
				<input name="inviter" value="${inviter}" type="hidden" readonly />
				<div class="form-group">
			    	<div class="input-group">
			    		<input name="cellphone" type="text" value="${cellphone}" class="form-control" pattern="^0?(13[0-9]|15[0-9]|17[0-9]|18[0-9]|14[0-9])[0-9]{8}$" data-error="请输入正确的手机号" placeholder="请输入要注册手机号" aria-describedby="cellphoneSpan" required >
			    		<span class="input-group-addon input-addon" id="cellphoneSpan">手机号码</span>
			    	</div>
				    <div class="help-block with-errors"></div>
				</div>
				<div class="form-group">
			    	<div class="input-group">
				    	<input name="captcha" type="text" class="form-control" placeholder="请输入右边的验证码" aria-describedby="validCodeSpan" required >
				      	<span class="input-group-addon input-addon" id="validCodeSpan">
				      		<img id="captchaImg" class="img-responsive" title="看不清，换一张" />
				      	</span>
			    	</div>
			    	<div class="help-block with-errors"></div>
				</div>
				<div id="message" class="row"><font color="red">${message}</font></div>
				<div class="form-group">
			      	<div class="checkbox">
			        	<label><input type="checkbox" checked="checked" required/>我已阅读并同意<a href="${pageContext.request.contextPath}/protocol/service">«金竹金服服务条款»</a></label>
			    	</div>
			    	<div class="help-block with-errors"></div>
			  	</div>
				<div class="form-group">
				    <div>
				    	<input class="btn btn-canary btn-block" type="submit" value="下一步">
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
		getCaptcha();
		$('#captchaImg').click(function(){
			getCaptcha();
		});
	});
	
	function getCaptcha() {
		$.get('${pageContext.request.contextPath}/captcha/base64', function(result){
			if(result && result.code == 200){
				$('#code').val(result.msg);
				$('#captchaImg').attr('src', result.data);
			}
		});
	}
</script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/adapter.js"></script>
</html>