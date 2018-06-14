<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="zh-CN">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/base64.js"></script>
<script type="text/javascript">
    function checkOS() {
		var u = navigator.userAgent.toLowerCase();
		var os = 'ie';
		var us = u.split('jzjf');
		if (u.indexOf('jzjf') > -1 && (u.indexOf('android') > -1 || u.indexOf('linux') > -1)) {
			if (us.length > 1) {
				os = 'android';
			}
		} else if (u.indexOf('jzjf') > -1 && (u.indexOf('iphone') > -1 || u.indexOf('ipad') > -1 || u.indexOf('ios') > -1)) {
			if (us.length > 1) {
					os = 'ios';
			} 
		}
		return os;
	};
	var cellphone = '';
	function share() {
		if (cellphone && cellphone != '') {
			if (checkOS() == 'ios') {
				var url='https://jzjf*htmlCallAppShare*title='+Base64.encode("邀请好友")+'&img=""&content='+Base64.encode("大家都在使用金竹金服理财，注册并完成交易最高可获得1000元红包哦。")+'&url=https://www.jinzhujinfu.com/register/'+Base64.encode(cellphone);
				document.location = url;				
			} else if (checkOS() == 'android') {
	    		window.jsApp.htmlCallAppShare('{"title":"邀请好友","img":"","content":"大家都在使用金竹金服理财，注册并完成交易最高可获得1000元红包哦。","url":"https://www.jinzhujinfu.com/register/'+Base64.encode(cellphone)+'"}');
			}
		} else {
			if (checkOS() == 'ios') {
				var url="https://jzjf*appCallHtml*";
			    document.location = url;				
			} else if (checkOS() == 'android') {
				window.jsApp.appCallHtml();
			}
		}
	}
	
	function appLoginSuccess() {
		if (checkOS() == 'ios') {
			var url='https://jzjf*appCallOnloadHtml*';
		    document.location = url;				
		} else {
			window.jsApp.appCallOnloadHtml();
		}
	}
	
	function checkFromApp(value){
    	var params =  eval('(' + value + ')');
    	cellphone = params.cellphone;
    }
    
    function onLoadCheckFromApp(value) {
    	var params =  eval('(' + value + ')');
    	cellphone = params.cellphone;
    }
</script>
<style>
	*{margin:0 auto}
	div{
		width:750px;
		height:2212px;
		background:#fff;
		position:relative;
		background:url(../images/invite.png);
	}	
	input{
		display: block;
		width: 200px;
		height: 100px;
		position: absolute;
		top: 845px;
		left: 270px;
		opacity: 0;
	}
</style>
 <body>
  <%-- <img src="${pageContext.request.contextPath}/images/invite.png" alt="" style="width:100%"> --%>
 </body>
</html>
