<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
	<title>提现成功</title>
</head>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.min.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.min.js"></script>
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
	(function() {
		if (checkOS() == 'android') {
			window.jsApp.callAppMyAssert();
		} else if (checkOS() == 'ios') {
				var url = 'https://jzjf*callAppMyAssert*';
				document.location = url;
		}
	})();
</script>
</html>