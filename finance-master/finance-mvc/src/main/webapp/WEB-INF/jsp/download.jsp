<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="zh-CN">
<jsp:include page="${pageContext.request.contextPath}/header" flush="true" />
<body>
<jsp:include page="${pageContext.request.contextPath}/navbar" flush="true" />
<div id="download-app" class="container">
	<div class="row">
		<div class="col-md-6">
			<img class="img-rounded center-block" src="${pageContext.request.contextPath}/images/app_thumbnail.png" alt="...">
		</div>
		<div class="col-md-2 padding-hundred-fifty">
			<div class="row padding-fifty"></div>
			<div class="row">
				<a class="btn btn-canary btn-block" href="http://101.69.121.36/appdl.hicloud.com/dl/appdl/application/apk/5b/5b90aec89a5f40adbdc75c19238cb33b/com.tencent.mm.1708221757.apk?mkey=59c207ad5acc43cf&f=9f16&c=0&sign=portal@portal1505894734638&source=portalsite&p=.apk" role="button" target="_blank">Android版下载</a>
			</div>
			<div class="row padding-hundred"></div>
			<div class="row">
				<a class="btn btn-canary btn-block" href="https://itunes.apple.com/cn/app/id1008896468" role="button" target="_blank">iPhone版下载</a>
			</div>
		</div>
		<div class="col-md-4 padding-hundred-fifty">
			<img class="img-rounded center-block" src="${pageContext.request.contextPath}/images/barcode_download.png" alt="扫二维码下载">
		</div>
	</div>
</div>
<jsp:include page="${pageContext.request.contextPath}/footer" flush="true" />
</body>
<script type="text/javascript">
	$(document).ready(function(){
		
	});
</script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/adapter.js"></script>
</html>