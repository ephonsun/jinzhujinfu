<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="format-detection" content="telephone=no">
<title>银行卡限额说明</title>
<style type="text/css">
/*限额页面*/
.limit_title{
	border-top:1px solid #d8d8d8;
	border-bottom:1px solid #d8d8d8;
	background:#f8f8f8;
	padding-left:4%;
	padding-right:4%;

}
.sub_limit1{
	font-size: 14px;
	text-align: left;
}
.sub_limit2{
	font-size: 14px;
	color:#9a9a9a;
	text-align: left;
}
.banks  {
	background:#fff;
}
.banks .bank{
	font-size: 16px;
	padding:10px 0;
}
.bank{
	border-bottom:1px solid #d8d8d8;
	font-size: 14px;
	overflow:hidden;
}
.bank_l{
	float:left;
	margin-left:4%;
	width:120px;
}
.bank_r{
	line-height: 28px;
	height:28px;
	float:right;
	margin-right:4%;	
}
.bank_l img{
	/*width:28px;*/
	height:28px;
	vertical-align: middle;
	margin-right: 10px;
	float:left;
}
.bank_l span{
	display: inline-block;
	vertical-align: middle;
	float:left;
	height: 28px;
	line-height: 28px;

}
.bank_l .icon_gd{
	width:35px;
	margin-right:10px;
}
</style>
</head>
<body>
	<div class="limit_title">
		<p class="sub_limit1">支持银行及限额（仅支持储蓄卡、暂不支持信用卡）</p>
		<p class="sub_limit2">具体金额以银行实际允许额度为准</p>
	</div>
	<div class="banks">
		<c:choose>
			<c:when test="${fn:length(banks) > 0}">
				<c:forEach var="bank" items="${banks}" varStatus="status">
					<div class="bank">
						<div class="bank_l">
							<img
								src="${pageContext.request.contextPath}/images/bank-logo/${bank.bankNO}.png">
							<span>${bank.bankName}</span>
						</div>
						<div class="bank_r">单笔${bank.singleLimit}元/单日${bank.dayLimit}元</div>
					</div>
				</c:forEach>
			</c:when>
			<c:otherwise>
			</c:otherwise>
		</c:choose>
    	</div>
</body>
</html>