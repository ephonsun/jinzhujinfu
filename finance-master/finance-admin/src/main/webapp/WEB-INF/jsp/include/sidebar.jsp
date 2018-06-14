<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="span2" id="sidebar">
    <ul class="nav nav-list bs-docs-sidenav nav-collapse collapse">
		<c:choose>
			 <c:when test="${session_canary_key != null}">
				<c:if test="${session_canary_key.menu != null}">
					<c:forEach items="${session_canary_key.menu }" var="entry" varStatus="s">
						<c:forEach items="${entry.value }" var="res">
							<c:if test="${res.key.shortcut == 1 }">
								<li>
									<a id="${res.key.url }" href="${res.key.url }" ><i class="icon-chevron-right"></i>${res.key.name }</a>
								</li>
							</c:if>
						</c:forEach>
					</c:forEach>
				</c:if>
			 </c:when>
			 <c:otherwise> 
			 	<!-- login dialog -->
			 </c:otherwise>
		</c:choose>
    </ul>
</div>