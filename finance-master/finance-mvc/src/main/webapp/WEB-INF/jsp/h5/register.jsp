<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no, minimal-ui">
    <title>金竹金服渠道注册</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.min.css" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css" />
    <style>
        .l_btn_next {
            margin-top: 1.33rem;
            color: #fff;
            background: #ccc;
        }
        .section1 a:link,
        .section1 a:visited,
        .section1 a:hover,
        .section1 a:active
        {
            color: #fff;
        }
    </style>
</head>
<body>
 <div id="addon-container" class="container">
	<div class="row">
		<div class="col-md-4"></div>
		<div class="col-md-4">
		<form class="form-horizontal" data-toggle="validator" role="form" action="${pageContext.request.contextPath}/channel/register/confirm" method="post">
    <input name="inviter" value="${inviter}" type="hidden" readonly />
    <input name="channelId" value="${channelId}" type="hidden" readonly />
	<div class="form-group" style="margin-left: 20px;margin-right: 20px;">
    	<div class="input-group"> 
    		<input name="cellphone" type="text" value="${cellphone}" class="form-control" pattern="^0?(13[0-9]|15[0-9]|17[0-9]|18[0-9]|14[0-9])[0-9]{8}$" data-error="请输入正确的手机号" placeholder="请输入要注册手机号" aria-describedby="cellphoneSpan" required >
    		<span class="input-group-addon input-addon" id="cellphoneSpan">手机号码</span>
    	</div>
	    <div class="help-block with-errors"></div>
	</div>
	<div class="form-group" style="margin-left: 20px;margin-right: 20px;">
    	<div class="input-group">
	    	<input name="sms" type="text" class="form-control" placeholder="请输入短信验证码" aria-describedby="validCodeSpan" data-minlength="6" data-maxlength="6" data-error="短信验证码长度为6位" required >
	      	<span class="input-group-addon input-addon" id="validCodeSpan">验证码</span>
    	</div>
    	<div class="help-block with-errors"></div>
	</div>
    <div class="form-group" style="margin-left: 20px;margin-right: 20px;">
    	<div class="input-group">
      		<input id="password" name="password" type="password" class="form-control" data-minlength="6" data-maxlength="20" data-error="登陆密码长度为6~20位" placeholder="请输入登录密码" aria-describedby="passwordSpan" required >
      		<span class="input-group-addon input-addon" id="passwordSpan">登录密码</span>
      	</div>
      	<div class="help-block with-errors"></div>
	</div>
	<div id="message" class="row" style="margin-left: 20px;"><font color="red">${message}</font></div>
    <div class="form-group" style="margin-left: 20px;margin-right: 20px;">
	    <div>
	    	<input class="btn btn-canary btn-block" type="submit" value="注册">
		</div>
	</div>
	</form>
		</div>
		<div class="col-md-4"></div>
	</div>
</div>
</body>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.2.1.min.js"></script><!-- <![endif]-->
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.base64.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.bootstrap.validator.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		var countDown = 120;
		$('#validCodeSpan').click(function() {
			if($.trim($('#validCodeSpan').text()) == '重新获取' || $.trim($('#validCodeSpan').text()) == '验证码') {
				var cellphone = $('#cellphone').val();
				var type = 'reg';
				$.post('${pageContext.request.contextPath}/rest/customer/sms/code', {'cellphone':cellphone, 'type':type}, function(result){
					showTime();
				});
			}
		});
		
		//showTime();
		
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