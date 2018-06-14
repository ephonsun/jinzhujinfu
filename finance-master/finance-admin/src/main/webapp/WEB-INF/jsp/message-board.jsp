<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><spring:message code="system.name" /></title>
	<link type="images/x-icon" rel="shortcut icon" href="${pageContext.request.contextPath}/images/favicon.ico">
	<link rel="shortcut icon" href="${pageContext.request.contextPath}/images/favicon.ico" />
    <link href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen">
    <link href="${pageContext.request.contextPath}/bootstrap/css/bootstrap-responsive.min.css" rel="stylesheet" media="screen">
    <link href="${pageContext.request.contextPath}/css/validation.jquery.css" rel="stylesheet" media="screen">
  </head>

  <body background="${pageContext.request.contextPath}/images/backgroud.png" style="margin:0;background-size:cover; ">
   <form id="login" style="width:100%;text-align:center;margin:200px auto;" method="post" >
		<h4>留言板</h4>
		<div class="control-group">
			<div class="controls">
				<span class="required">*</span>昵称
			    <input type="text" id="name" name="name" class="validate[required,maxSize[12],custom[anotherNotSpecialCharacter],ajax[ajaxValidateRoleName]] span8" />
			</div>
	        <div class="controls">
	        	<span class="required">*</span>内容
	        	<textarea id="content" name="content" rows="4" class="validate[maxSize[60]] span8" ></textarea>
	        </div>
        </div>
		<div class="row-fluid">
		  	<div class="span6" align="right">
		  		<button type="button" id="save" class="btn btn-success btn-lg">保存</button>
		  	</div>
		  	<div class="span6" align="left">
		  		<a class="btn btn-default btn-lg" href="javascript:quit();">取消</a>
		  		<a class="btn btn-default btn-lg" style="margin:0px 0px 0px 40px;" href="${pageContext.request.contextPath}/main">返回主页</a>
		  	</div>
		</div>
   </form>
  </body>
  <script src="${pageContext.request.contextPath}/js/jquery-1.9.1.min.js"></script>
  <script src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.min.js"></script>
  <script src="${pageContext.request.contextPath}/js/jquery.validation.js"></script>
  <script src="${pageContext.request.contextPath}/js/jquery.validation-zh_cn.js"></script>
  <script type="text/javascript">
  		function quit() {
  			$('#name').val('');
  			$('#content').val('');
  		}
  		$('#save').click(function(){
  			var name = $('#name').val();
  			var content = $('#content').val();
  			if (!name) {
  				alert('昵称不能为空！');
  				return;
  			}
  			if (!content) {
  				alert('内容不能为空！');
  				return;
  			}
	  		$.post('${pageContext.request.contextPath}/message/board/save', {'name':name, 'content':content}, function(result){
				 if(result) {
					 $('#name').val('');
			  		 $('#content').val('');
					 alert('保存成功!');
				 }
	        });
  		});
  </script>
</html>