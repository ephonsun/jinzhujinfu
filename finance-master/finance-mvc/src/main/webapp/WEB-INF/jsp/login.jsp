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
	<div class="row">&nbsp;</div>
</div>
<div class="container">
	<div class="row">
		 <div class="col-md-9">
		 	<img class="col-md-12" src="${pageContext.request.contextPath}/images/login_img.png" alt="..."/>
		</div>
		<div class="col-md-3">
			<form class="form-horizontal" data-toggle="validator" role="form" action="${pageContext.request.contextPath}/login" method="post">
				<input id="code" name="code" type="hidden" />
				<div class="col-md-12 page-header text-center">
				  <h4>用户登录</h4>
				</div>
				<div class="form-group">
			    	<div class="input-group">
			    		<input name="cellphone" type="text" value="${cellphone}" class="form-control" pattern="^0?(13[0-9]|15[0-9]|17[0-9]|18[0-9]|14[0-9])[0-9]{8}$" data-error="请输入正确的手机号" placeholder="请输入要注册手机号" aria-describedby="cellphoneSpan" required >
			    		<span class="input-group-addon input-addon" id="cellphoneSpan">手机号码</span>
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
				    	<input name="captcha" type="text" class="form-control" placeholder="请输入右边的验证码" aria-describedby="validCodeSpan" required >
				      	<span class="input-group-addon input-addon" id="validCodeSpan">
				      		<img id="captchaImg" class="img-responsive" title="看不清，换一张" />
				      	</span>
			    	</div>
			    	<div class="help-block with-errors"></div>
				</div>
				<div id="message" class="row"><font color="red">${message}</font></div>
				<div class="form-group">
				    <div class="col-md-12 text-center">
				      <a href="${pageContext.request.contextPath}/password/reset">忘记密码</a>
				      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				      <a href="${pageContext.request.contextPath}/register">马上注册</a>
					</div>
				</div>
				<div class="form-group">
				    <div>
				    	<input class="btn btn-canary btn-block" type="submit" value="登录">
					</div>
				</div>
			</form>
		</div>
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