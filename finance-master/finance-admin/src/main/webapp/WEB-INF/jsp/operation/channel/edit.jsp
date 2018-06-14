<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<jsp:include page="${pageContext.request.contextPath}/header" flush="true" />
	<body>
		<jsp:include page="${pageContext.request.contextPath}/menu" flush="true" />
        <div class="container-fluid">
            <div class="row-fluid">
                <jsp:include page="${pageContext.request.contextPath}/sidebar" flush="true" />
                <div class="span10" id="content">
                    <div class="row-fluid">
                        <!-- block -->
                        <div class="block">
                            <jsp:include page="${pageContext.request.contextPath}/breadcrumb" flush="true" />
                            <div class="block-content collapse in">
			                    <form id="fm" class="form-horizontal" method="post" enctype="multipart/form-data">
									<div class="well">
										<input id="id" name="id" value="${channel.id}" type="hidden">
										<input id="banner" name="banner" value="${channel.banner}" type="hidden">
										<input name="base64" value="${base64}" type="hidden">
				                    	<div class="control-group">
											<label class="control-label"><span class="required">*</span>渠道编号</label>
											<div class="controls"><input type="text" value="${channel.id}" class="text-input span2" readonly="readonly" /></div>
										</div>
				                    	<div class="control-group">
											<label class="control-label"><span class="required">*</span>渠道名称</label>
											<div class="controls"><input type="text" id="name" name="name" value="${channel.name}" <c:if test="${channel.name != null && channel.name != ''}">readonly="readonly"</c:if> class="validate[required, minSize[2], maxSize[20]] text-input span11" placeholder="不超过30个字符" onkeyup="this.value=this.value.replace(/(^\s*)/g,'')" onblur="this.value=this.value.replace(/(\s*$)/g,'')"/></div>
										</div>
										<div class="control-group">
											<label class="control-label"><span class="required">*</span>渠道注册地址</label>
											<div class="controls"><input type="text" id="url" name="url" value="${channel.url}" readonly="readonly" class="text-input span11" onkeyup="this.value=this.value.replace(/(^\s*)/g,'')" onblur="this.value=this.value.replace(/(\s*$)/g,'')"/></div>
										</div>
										<div class="control-group">
											<label class="control-label"><span class="required">*</span>渠道下载地址</label>
											<div class="controls"><input type="text" id="downloadUrl" name="downloadUrl" value="${channel.downloadUrl}" readonly="readonly" class="text-input span11" onkeyup="this.value=this.value.replace(/(^\s*)/g,'')" onblur="this.value=this.value.replace(/(\s*$)/g,'')"/></div>
										</div>
										<div class="control-group">
											<label class="control-label">Banner图</label>
											<div class="controls" id="bigImage">
											  <c:choose>
											  	<c:when test="${!empty channel.banner}">
											  	  <img style="height:200px; width: 300px;" class="thumbnail" src="${channel.banner}"/>
												  <a href="javascript:reUpload('${channel.banner}');"><button type="button" class="btn btn-primary">重新上传 </button></a>
											  	</c:when>
											  	<c:otherwise>
											  	  <input id="files" name="files" type="file" accept="image/jpeg,image/png" onchange="selectFile();" /><span><font style="color:red;">（建议尺寸：750*280，png或者jpg格式）</font></span>
											  	</c:otherwise>
											  </c:choose>
											</div>
										</div>
										<div class="control-group" id="progress">
											<div class="controls" id="fileName"></div>
										    <div class="controls" id="fileSize"></div>
										    <div class="controls">
										    	<div class="progress">
											    	<div class="progress-bar" role="progressbar" aria-valuemin="0" aria-valuemax="100" style="width:0%;background-color:red;">
											      		<span class="sr-only">0% 完成</span>
											   		</div>
											   </div>
										    </div>
										</div>
										<%-- <div class="control-group">
											<label class="control-label">红包</label>
											<div class="controls">
												<select id="couponId" name="coupon.id">
													<option value="0">无</option>
				                                    <c:forEach items="${coupons}" var="coupon">
				                                      <c:choose>
				                                        <c:when test="${coupon.id == channel.coupon.id}"><option value="${coupon.id}" selected="selected">${coupon.name}</option></c:when>
				                                     	<c:otherwise><option value="${coupon.id}" >${coupon.name}</option></c:otherwise>
				                                      </c:choose>
				                                   	</c:forEach>
												</select>
											</div>
										</div> --%>
										<br>
										<div class="row-fluid">
											<div class="control-group" id="contentWrap">
												<label class="control-label">备注</label>
												<div class="controls">
													<textarea cols="150" rows="3" id="remark" name="remark">${channel.remark}</textarea>
												</div>
											</div>
										</div>
										<div class="control-group">
											<label class="control-label"></label>
											<div class="controls">
												<a href="javascript:uploadFile();"><button type="button" id="save" class="btn btn-icon btn-primary glyphicons circle_ok"><i></i>保存</button></a>
												<a type="reset" class="btn btn-icon btn-default glyphicons circle_remove" href="javascript:quit()"><i></i>返回</a>
											</div>
										</div>
									</div>
								</form>
							</div>
						</div>
					</div>
                </div>
            </div>
        </div>
		<jsp:include page="${pageContext.request.contextPath}/footer" flush="true" />
	</body>
	<script type="text/javascript">
		$(document).ready(function() {
			$('.breadcrumb').html('<li class="active">运营管理&nbsp;/&nbsp;运营渠道管理/&nbsp;编辑</li>');
			$('#progress').hide();
			$('#fm').validationEngine('attach', {
		        promptPosition:'bottomLeft',
		        showOneMessage: true,
		        focusFirstField:true,
		        scroll: true
		    });
		});
		
		function uploadFile() {
			if(confirm('渠道信息一旦保存后就无法无法修改，确认要保存?')) {
				var file = document.getElementById('files');
		        if(file) {
		        	$('#save').attr('disabled','disabled');
				    var fd = new FormData();
				    fd.append('file', document.getElementById('files').files[0]);
				    var xhr = new XMLHttpRequest();
				    xhr.upload.addEventListener('progress', uploadProgress, true);
				    xhr.addEventListener('load', uploadComplete, false);
				    xhr.open('POST', '${pageContext.request.contextPath}/web/upload', true);
				    xhr.send(fd); 
		        } else {
		        	$('#fm').attr('action', '${pageContext.request.contextPath}/operation/channel/save');
		            $('#fm').submit();
		        } 
			} else {
    			$('#name').focus();
    		}
		}
		
		function uploadProgress(evt) {
			var percentComplete = 0;
		    if (evt.lengthComputable) {
		        percentComplete = Math.round(evt.loaded * 100 / evt.total);
		        $('.progress-bar').css('width', percentComplete + '%');
		        $('.sr-only').html(percentComplete + '% 完成');
		    }
		}
		      
		function uploadComplete(evt) {
			$('#banner').val(evt.target.responseText);
		   	$('#fm').attr('action', '${pageContext.request.contextPath}/operation/channel/save');
            $('#fm').submit();
		}
		
	    function reUpload(banner) {
	    	if($.trim(banner) != '') {
	    		if(confirm('您确认要重新上传Banner图片吗?')) {
	    			$('#bigImage').html('<input id="files" type="file" name="files" accept="image/jpeg,image/png" onchange="selectFile();" /><span><font style="color:red;">（建议尺寸：750*280，png或者jpg格式）</font></span>');
				}
	    	}
		}
		   
	   	function selectFile() {
			var file = document.getElementById('files').files[0];
			$('#progress').hide();
	        if(file) {
	        	if(/.*[\u4e00-\u9fa5]+.*$/.test(file.name)) {  
	                alert('文件名中不能含有中文！'); 
	                $('#file').val('');
	                return false;  
                }  
	        	var suffix = file.name.split('.').pop().toLowerCase();
        	    if (suffix == 'jpg' || suffix == 'png') {
	        	    $('#progress').show();
	                var fileSize = 0;
	                if (file.size > 1024 * 1024) {
	                    fileSize = Math.round(file.size/(1024 * 1024)).toString()+'MB';
	                } else {
	                    fileSize = Math.round(file.size/1024).toString()+'KB';
	                }
	                $('#fileName').html('文件名称:'+file.name);
	              	$('#fileSize').html('文件大小: '+fileSize);
	       		  	return true; 
        	    } else {
        		    alert('请选择jpg文件或png文件！');
        		    $('#file').val('');
	                return false;
        		}
	    	}
		}
	   
		function quit() {
			window.location.href='${pageContext.request.contextPath}/operation/channel/${base64}';
	   	}
	</script>
</html>