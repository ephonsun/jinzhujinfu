<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="zh-CN">
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
	function getCoupon() {
		if (cellphone && cellphone != '') {
			if(checkOS()=='android'){
				window.jsApp.callAppMyCouponList();
			} else {
				document.location = 'https://jzjf*callAppMyCouponList*';
			}
		} else {
			if (checkOS() == 'android') {
				window.jsApp.callAppRegist();
			} else if (checkOS() == 'ios') {
				var url = 'https://jzjf*callAppRegist*';
				document.location = url;
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
		height:1712px;
		background:#fff;
		position:relative;
		background:url(../images/guide_app.png);
	}	
	input{
		display:block;
		width:400px;
		height:300px;
		position:absolute;
		top:706px;
		left:175px;
		opacity:0;
	}	
	
</style>
<body>
 <img src="${pageContext.request.contextPath}/images/guide-app.png" alt="" style="width:100%">
</body>
</html>