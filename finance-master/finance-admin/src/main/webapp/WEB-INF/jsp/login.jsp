<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><spring:message code="system.name" /></title>
	<link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/images/18.png">
	<%-- <link rel="Bookmark" href="${pageContext.request.contextPath}/images/favicon.ico" /> --%>
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
   <form id="login" action="${pageContext.request.contextPath}/gauth/authorize" method="post" ><!-- onsubmit="return validate()" -->
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
	    <td><img src="${pageContext.request.contextPath}/images/middle.png" style="min-height:336px; min-width:304px" /></td>
	    <td>
	    <table width="347" height="336" border="0" align="left" cellpadding="0" cellspacing="0" background="${pageContext.request.contextPath}/images/from_bg.png">
	      <tr>
	        <td height="60" colspan="2">&nbsp;</td>
	      </tr>
	      <tr>
	        <td height="40" colspan="2">
	        <div class="input-group">
	          <input id="name" name="name" type="text" class="validate[required,minSize[4],maxSize[20],custom[notSpecialCharacter]]  form-control" onblur="checkAdmin()" placeholder="用户名"  >
	        </div>
	      </tr>
	      <tr>
	        <td height="20" colspan="2">&nbsp;</td>
	      </tr>
	      <tr>
	        <td height="40" colspan="2">
	        <div class="input-group">
	          <input id="authCode" name="authCode" type="text" class="validate[required,minSize[6],maxSize[6],custom[number]] form-control" placeholder="验证令牌" >
	        </div>
	        </td>
	      </tr>
	      <tr>
	        <td height="31" colspan="2">
              <label class="uniform">&nbsp;</label>
	        </td>
	      </tr> 
	      <tr>
	        <td height="20" colspan="2"><span style="color:red;">&nbsp;&nbsp;${message}</span></td>
	      </tr>
	      <tr>
	        <td height="45" colspan="2">
	          <button id="action" type="button" class="btn btn-large btn-danger">登录</button>
	        </td>
	      </tr>
	      <tr>
	        <td height="60" colspan="2">&nbsp;</td>
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
  </body>
  <script src="${pageContext.request.contextPath}/js/jquery-1.9.1.min.js"></script>
  <script src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.min.js"></script>
  <script src="${pageContext.request.contextPath}/js/jquery.validation.js"></script>
  <script src="${pageContext.request.contextPath}/js/jquery.validation-zh_cn.js"></script>
  <script type="text/javascript">
  	if(window.top.location != window.self.location){
	  	window.top.location.href = "${pageContext.request.contextPath}/";
	}
	var session = '${session_canary_key}';
	if(session) {
		window.location.replace('${pageContext.request.contextPath}/main');
	}
	
	$(document).ready(function() {
    	$('#name').css({width:232});
    	$('#authCode').css({width:232});
    	$('#action').css({width:230});
    	$('#login').validationEngine('attach',{ 
    		autoHidePrompt:true,
    		autoHideDelay:3000,
    		promptPosition:'bottomRight'
    	});
    	//checkAdmin();
    	$('#action').bind('click', toSubmit);
	});

    $(document).keypress(function(event){  
        var keycode = (event.keyCode ? event.keyCode : event.which);  
        if(keycode == '13'){  
        	$('#login').submit();    
        }  
    });     
	
	function toSubmit(){
		var name = $('#name').val();
		if(name) {
			checkAdmin();
		} 
	}
	
	function checkAdmin() {
		var name = $('#name').val();
		if(name) {
			$.post('${pageContext.request.contextPath}/sys/operator', {'name':name}, function(result){
		        if (result && result.id > 0 && result.totp == 0) {
		        	location.href = '${pageContext.request.contextPath}/gauth/credential/'+name;
		        } else {
		        	if($('#login').validationEngine('validate')) {
			        	$('#action').unbind('click');
		        	}
		        	$('#login').submit();
		        }
	        });
		}
	}
  </script>
</html>