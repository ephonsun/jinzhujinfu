<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:if test="${fn:length(newsset) > 0}">
<ul id="newsset" class="list-group">
	<c:forEach var="news" items="${newsset}" varStatus="s">
	<li class="list-group-item">
		<a href="${pageContext.request.contextPath}/news/bulletin/detail/${news.id}" target="_blank">
			<span><img src="${pageContext.request.contextPath}/images/notice_bj.png"/></span>
			<span>${news.news.title}</span>
			<!-- <span><img src="${pageContext.request.contextPath}/images/notice_new.png"/></span> -->
			<span class="fr"><fmt:formatDate value="${news.createTime}" pattern="yyyy-MM-dd"/></span>
		</a>
	</li>
	</c:forEach>
</ul>
</c:if>

<c:if test="${fn:length(orders) > 0}">
<table id="orders" class="table table-striped">
	<c:choose>
		<c:when test="${type == 2}">
			<thead>
				<tr>
					<th>产品名称</th>
					<th>购买金额(元)</th>
					<th>收益(元)</th>
					<th>购买时间</th>
					<th>到期时间</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach var="order" items="${orders}" varStatus="s">
				<tr>
					<td>${order.product.name}</td>
					<td><fmt:formatNumber value="${order.principal}" pattern="#,##0" type="currency"/></td>
					<td><fmt:formatNumber value="${order.paybackAmount-order.principal}" pattern="#,##0.00" type="currency"/></td>
					<td><fmt:formatDate value="${order.orderTime}" pattern="yyyy-MM-dd"/></td>
					<td><fmt:formatDate value="${order.paybackTime}" pattern="yyyy-MM-dd"/></td>
				</tr>
			</c:forEach>
			</tbody>
		</c:when>
		<c:otherwise>
			<thead>
				<tr>
					<th>产品名称</th>
					<th>购买金额(元)</th>
					<th>收益(元)</th>
					<th>购买时间</th>
					<th>到期时间</th>
					<th>剩余天数</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach var="order" items="${orders}" varStatus="s">
				<%-- <c:set var="interval" value="${now-order.product.interestDate}"/>
				<!-- 8640000 millisecond equals 1 day -->
				<c:choose>
					<c:when test="${interval%86400000 == 0}">
						<c:set var="passDay" value="${interval/86400000}"/>
					</c:when>
					<c:otherwise>
						<c:set var="passDay" value="${(interval+(86400000-(interval%86400000)))/86400000}"/>
					</c:otherwise>
				</c:choose>
				<c:if test="${passDay > order.product.financePeriod}">
					<c:set var="passDay" value="${order.product.financePeriod}"/>
				</c:if>
				<c:set var="profit" value="${order.principal*order.product.yearIncome*passDay/36500}" /> --%>
				<tr>
					<td>${order.product.name}</td>
					<td><fmt:formatNumber value="${order.principal}" pattern="#,##0" type="currency"/></td>
					<td><fmt:formatNumber value="${order.fee}" pattern="#,##0.00" type="currency"/></td>
					<td><fmt:formatDate value="${order.orderTime}" pattern="yyyy-MM-dd"/></td>
					<c:if test="${order.product.interestDate != null }">
						<td><fmt:formatDate value="${order.paybackTime}" pattern="yyyy-MM-dd"/></td>
						<td>${order.product.financePeriod}</td>
					</c:if>
					<c:if test="${order.product.interestDate == null }">
						<td>募集中</td>
						<td></td>
					</c:if>
				</tr>
			</c:forEach>
			</tbody>
		</c:otherwise>
	</c:choose>
</table>
</c:if>

<c:if test="${fn:length(invitations) > 0}">
<table id="invitations" class="table table-striped">
	<c:choose>
		<c:when test="${type == 2}">
			<thead>
				<tr>
					<th>邀请时间</th>
					<th>手机号码</th>
					<th>红包金额(元)</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach var="invitation" items="${invitations}" varStatus="s">
				<tr>
					<td>${invitation.createTime}</td>
					<td>${fn:substring(invitation.cellphone, 0, 3)}****${fn:substring(invitation.cellphone, 7, 11)}</td>
					<td>${invitation.amount}</td>
				</tr>
			</c:forEach>
			</tbody>
		</c:when>
		<c:otherwise>
			<thead>
				<tr>
					<th>邀请时间</th>
					<th>手机号码</th>
					<th>状态</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach var="invitation" items="${invitations}" varStatus="s">
				<tr>
					<td><fmt:formatDate value="${invitation.registerTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<td>${fn:substring(invitation.cellphone, 0, 3)}****${fn:substring(invitation.cellphone, 7, 11)}</td>
					<td>
					<c:choose>
						<c:when test="${invitation.tradeTime != null}">已投资</c:when>
						<c:otherwise>未投资</c:otherwise>
					</c:choose>
					</td>
				</tr>
			</c:forEach>
			</tbody>
		</c:otherwise>
	</c:choose>
</table>
</c:if>
