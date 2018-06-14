<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><spring:message code="system.name" /></title>
	<link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/images/18.png">
	<link rel="Bookmark" href="${pageContext.request.contextPath}/images/favicon.ico" />
	<!-- Bootstrap -->
    <link href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen">
    <link href="${pageContext.request.contextPath}/bootstrap/css/bootstrap-responsive.min.css" rel="stylesheet" media="screen">
     <link href="${pageContext.request.contextPath}/css/validation.jquery.css" rel="stylesheet" media="screen">
    <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
      <script src="${pageContext.request.contextPath}/js/html5.js"></script>
    <![endif]-->
  </head>

  <body background="${pageContext.request.contextPath}/images/backgroud.png" style="margin:0;background-size:cover; ">
    <form id="totp" action="${pageContext.request.contextPath}/gauth/totp" method="post" ><!-- onsubmit="return validate()" -->
    <input id="userName" name="userName" type="hidden" value="${credential.userName}" />
    <input id="key" name="key" type="hidden" value="${credential.key}" />
    <input id="totpUrl" name="totpUrl" type="hidden" value="${credential.totpUrl}" />
    <input id="token" name="token" type="hidden" value="${credential.token}" />
	<table width="100%" border="0" align="left" cellpadding="0" cellspacing="0">
	  <tr>
	    <td height="220">&nbsp;</td>
	    <td width="543">&nbsp;</td>
	    <td width="304">&nbsp;</td>
	    <td width="347">&nbsp;</td>
	    <td width="175">&nbsp;</td>
	  </tr>
	  <tr>
	  	<td>&nbsp;</td>
	    <td>&nbsp;</td>
	    <td style="min-height:336px; min-width:304px" background="${pageContext.request.contextPath}/images/from_bg.png">
	    	<div id="totpRQCode" style="text-align:center"></div>
	    </td>
	    <td>
	    <table width="347" height="336" border="0" align="left" cellpadding="0" cellspacing="0" background="${pageContext.request.contextPath}/images/from_bg.png">
	      <tr>
	        <td height="26">&nbsp;</td>
	      </tr>
	      <tr>
	        <td height="30"><b><a href="#" onclick="javascript:showBarcode();">Google Authenticator</a>身份验证信息 </b></td>
	      </tr>
	      <tr>
	        <td height="40">帐户：${credential.userName}</td>
	      </tr>
	      <tr>
	        <td height="40">密钥：${credential.key}</td>
	      </tr>
	      <tr>
	      <tr>
	        <td height="40" colspan="2">
	        <div class="input-group">
	          <input id="authCodeFirst" name="authCodeFirst" type="text" class="validate[required,minSize[6],maxSize[6],custom[number]] form-control" placeholder="第一组验证令牌" >
	        </div>
	        </td>
	      </tr>
	      <tr>
	        <td height="40" colspan="2">
	        <div class="input-group">
	          <input id="authCodeSecond" name="authCodeSecond" type="text" class="validate[required,minSize[6],maxSize[6],custom[number]] form-control" placeholder="第二组验证令牌" >
	        </div>
	        </td>
	      </tr>
	      <tr>
	        <td height="25"><span style="color:red;">&nbsp;&nbsp;${message}</span></td>
	      </tr>
	      <tr>
	        <td height="45">
	          <button id="action" type="button" class="btn btn-large btn-danger" onclick="javascript:doSubmit();">绑定</button>&nbsp;&nbsp;&nbsp;&nbsp;
	          <button id="cancel" type="button" class="btn btn-large btn-success" onclick="javascript:doCancel();">返回</button>
	        </td>
	      </tr>
	      <tr>
	        <td height="50">&nbsp;</td>
	      </tr>
	    </table>
	    </td>
	    <td>&nbsp;</td>
	  </tr>
	  <tr>
	  	<td>&nbsp;</td>
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
	  </tr>
	</table>
    </form>
   <div class="modal fade" id="barcodeDialog">
	  <div class="modal-dialog">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	        <h4 id="qrcode-title" class="modal-title">身份验证器</h4>
	      </div>
	      <div class="modal-body">
	      	<div>
	        	<div id="rqCodeAndroid" style="float:left"></div>
				<div id="rqCodeIOS" style="float:right"></div>
			</div>
				<div id="rqCodeAndroidTip" style="float:left;padding-left:60px;"></div>
				<div id="rqCodeIOSTip" style="float:right;padding-right:60px;"></div>
			<div>
			</div>
	      </div>
	    </div>
	  </div>
	</div>
  </body>
  <script src="${pageContext.request.contextPath}/js/jquery-1.9.1.min.js"></script>
  <script src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.min.js"></script>
  <script src="${pageContext.request.contextPath}/js/jquery.qrcode.min.js"></script>
  <script src="${pageContext.request.contextPath}/js/jquery.validation.js"></script>
  <script src="${pageContext.request.contextPath}/js/jquery.validation-zh_cn.js"></script>
  <script src="${pageContext.request.contextPath}/js/browser.js"></script>
  <script type="text/javascript">
	var session = '${session_canary_key}';
	if(session) {
		window.location.replace('${pageContext.request.contextPath}/main');
	}
	
    $(function() {
    	var totpURL = '${credential.totpUrl}';
    	var render = 'canvas';
    	if(browser.isIe) {
    		render = 'table';
    	}
    	if(totpURL) {
    		$("#totpRQCode").html('');
	    	$("#totpRQCode").qrcode({ 
			    render: render,
			    width: 200,
			    height:200, 
			    text: totpURL
			});
    	}
    	
    	$('#action').css({width:100});
    	$('#cancel').css({width:100});
    	$('#totp').validationEngine('attach',{
    		autoHidePrompt:true,
    		autoHideDelay:3000,
    		promptPosition:'bottomRight'
    	});
	});
	
	function doSubmit(){
		$('#totp').submit();
	}
	
	function doCancel(){
		window.location.href = '${pageContext.request.contextPath}/';
	}
	
	function showBarcode() {
		var render = 'canvas';
    	if(browser.isIe) {
    		render = 'table';
    	}
		$('#barcodeDialog').css({width:460,height:300});
		$("#qrcode-title").html('身份验证器（验证令牌）');
		$('#barcodeDialog').modal('show');
		$("#rqCodeAndroid").html('');
		$("#rqCodeAndroid").qrcode({ 
		    render: render,
		    width: 200,
		    height:200, 
		    text: 'http://oss.aliyuncs.com/silverfox-cn-file/googleAuth/Google_Authenticator.apk'
		});
		$("#rqCodeAndroidTip").html('Android版下载');
		
		$("#rqCodeIOS").html('');
		$("#rqCodeIOS").qrcode({ 
		    render: render,
		    width: 200,
		    height:200, 
		    text: 'https://itunes.apple.com/cn/app/google-authenticator/id388497605'
		});
		$("#rqCodeIOSTip").html('iPhone版下载');
	}
  </script>
</html>