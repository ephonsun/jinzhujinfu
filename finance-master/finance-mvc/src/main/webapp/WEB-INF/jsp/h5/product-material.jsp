<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="zh-CN">
<body>
<c:choose>
	<c:when test="${product != null && product.attachment != null && fn:length(fn:trim(product.attachment)) > 0}">
  		<c:forEach var="attachment" items="${fn:split(fn:trim(product.attachment), ',')}" varStatus="s">
   			<img src="${attachment}" style="width: 99%;" class="thumbnail" alt="资料"><br>
   		</c:forEach>
  	</c:when>
  	<c:otherwise>
	</c:otherwise>
</c:choose>
</body>
</html>