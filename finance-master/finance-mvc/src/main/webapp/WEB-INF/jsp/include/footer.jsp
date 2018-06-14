<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<footer id="footer-wrapper" class="container-fluid">
	<div id="footer-wrapper-top" class="row">
		<div class="col-md-5">
			<div class="col-md-5">&nbsp;</div>
			<div class="col-md-7 text-left">
				<a href="${pageContext.request.contextPath}/aboutus">关于我们</a>
				<a href="${pageContext.request.contextPath}/safety">安全保障</a>
				<a href="${pageContext.request.contextPath}/faq">常见问题</a>
				<a href="${pageContext.request.contextPath}/protocol/relief">免责条款</a>
			</div>
		</div>
		<div class="col-md-7">
			<div class="col-md-6 text-right">
				<div>
					<span>
						<img class="img-rounded" width="100" src="${pageContext.request.contextPath}/images/barcode.png"/>
					</span>
				</div>
			</div>
			<div class="col-md-6 text-left">
				<address>
					<strong>客服热线</strong><br>
				  	<abbr title="400电话">Tel：</abbr><s:message code="tellphone"/><br>
				  	(工作时间 08:00-18:00)
				</address>
			</div>
 		</div>
	</div>
	<div id="footer-wrapper-bottom" class="row">
		<div>
 	  		<p><s:message code="footer.copyright"/></p>
 	  		<p><s:message code="footer.tip"/></p>
		</div>
	</div>
</footer>

<!--[if lte IE 8]><script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.12.4.min.js"></script><![endif]-->
<!--[if gt IE 8]><script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.2.1.min.js"></script><![endif]-->
<!--[if !IE]> --><script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.2.1.min.js"></script><!-- <![endif]-->
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.base64.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		
	});
</script>