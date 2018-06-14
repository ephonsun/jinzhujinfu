<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.combo.select.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-ui.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap-paginator.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/sidebar.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.validation-zh_cn.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.validation.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.datetimepicker.full.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.base64.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.form.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/dateformat.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/icheck.js"></script>

<script type="text/javascript">window.UEDITOR_HOME_URL = '${pageContext.request.contextPath}/plugin/ueditor/';</script>
<script type="text/javascript">window.UEDITOR_UPLOAD_URL = '${pageContext.request.contextPath}/res/plugin/ueditor/';</script>
<script type="text/javascript" src="${pageContext.request.contextPath}/plugin/ueditor/ueditor.config.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/plugin/ueditor/ueditor.all.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/plugin/ueditor/lang/zh-cn/zh-cn.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	$.base64.utf8encode = true;
	$.base64.utf8decode = true;
	$('[data-toggle="tooltip"]').tooltip();
	$('[data-toggle="popover"]').popover();
});

function logout() {
	if (confirm("您确定退出吗?")) {
        window.parent.location.href = "${pageContext.request.contextPath}/logout";
    }
}

function modifyPassword() {
	var id = '${session_silverfox_key.admin.id}';
	if (id == 1) {
		alert('亲,超级管理员不能修改密码!');
	} else {
		$('#oldPassword').val('');
		$('#password').val('');
		$('#confirmPassword').val('');
		$('#newPasswordDialog').modal('show');
		$('#newPasswordDialog').on('shown.bs.modal', function () {
		    $('#oldPassword').focus();
		});
		$('#fm').validationEngine();
	}
}

function startValidate() {
	if($.trim($('#password').val()) != null){
		$('#newPassword').val($.trim($('#password').val()));
	}
	return true;
}
</script>