<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="zh-CN">
<style>
	img {
		width:100%;
	}
</style>
<body>
<c:if test="${banner != null}">
	<div style="width: 100%;" >
	  	${banner.content}
	</div>
</c:if>
</body>
</html>