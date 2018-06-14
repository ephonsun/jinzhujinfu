<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<div class="navbar navbar-fixed-top">
	<div class="navbar-inner">
        <div class="container-fluid">
            <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse"> 
            	<span class="icon-bar"></span>
             	<span class="icon-bar"></span>
             	<span class="icon-bar"></span>
            </a>
            <span class="brand"><spring:message code="system.name" /></span>
            <div class="nav-collapse collapse">
                <ul class="nav pull-right">
                    <li class="dropdown">
                        <a href="#" role="button" class="dropdown-toggle" data-toggle="dropdown"> 
                        	<i class="icon-user"></i>欢迎您： ${session_canary_key.admin.realName}<i class="caret"></i>
                        </a>
                        <ul class="dropdown-menu">
                            <li>
                                <a href="#" onClick="logout()">退出</a>
                            </li>
                        </ul>
                    </li>
                </ul>
                <ul class="nav" >
					<c:choose>
						 <c:when test="${session_canary_key != null}">
							<c:if test="${session_canary_key.menu != null}">
								<c:forEach items="${session_canary_key.menu }" var="entry" varStatus="s">
									<li class="dropdown" style="margin-left: -15px">
                        			<a href="#" role="button" class="dropdown-toggle" data-toggle="dropdown" <c:if test="${s.last}"> data-options="selected:true"</c:if>>${entry.key.name }<i class="caret"></i></a>
										<ul class="dropdown-menu" >
											<c:forEach items="${entry.value }" var="res">
												<li>
													<a id="${res.key.url }" title="${res.key.name }" href="${res.key.url }" >${res.key.name }</a>
												</li>
											</c:forEach>
										</ul>
									</li>
								</c:forEach>
							</c:if>
						 </c:when>
						 <c:otherwise> 
						 	<!-- login dialog -->
						 </c:otherwise>
					</c:choose>
				</ul>
            </div>
        </div>
    </div>
</div>
