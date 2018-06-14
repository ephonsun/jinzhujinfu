<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
			                    <form class="form-horizontal" style="margin-bottom: 0;" enctype="multipart/form-data" id="fm" method="post">
									<div class="well" style="padding-bottom: 20px; margin: 0;">
										<input id="id" name="id" value="${banner.id}" type="hidden">
				                    	<input id="url" name="url" value="${banner.url}" type="hidden">
				                    	<input name="base64" value="${base64}" type="hidden">
										<div class="row-fluid">
											<div class="span6">
												<div class="control-group">
													<label class="control-label"><span class="required">*</span> 名&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;称</label>
													<div class="controls"><input id="name" name="name" type="text" value="${banner.name}" readonly="readonly" /></div>
												</div>
											</div>
										</div>
										<div class="row-fluid">
											<div class="span6">
												<div class="control-group">
													<label class="control-label"><span class="required">*</span> 显示平台</label>
													<div class="row-fluid">
														<div class="span3">
															<div class="control-group ">
																<input type="radio" name="platform" value="1"  class="validate[required] radio span4">
																<span>app</span>
															</div>
														</div>
														<div class="span3">
															<div class="control-group ">
																<input type="radio" name="platform" value="2" class="validate[required] radio span4">
																<span>web</span>
															</div>
														</div>
													</div>
												</div> 
											</div>
										</div>
										<div class="control-group">
											<label class="control-label"><span class="required">*</span> 图片上传</label>
											<div class="controls" id="bigImage">
											<c:choose>
											  	<c:when test="${!empty banner.url}">
											  	  <c:if test="${banner.platform == 1}"><img class="thumbnail bigImage" src="${banner.url}" style="height:200px;width:100px"/></c:if>
                                       			  <c:if test="${banner.platform == 2}"><img class="thumbnail bigImage" src="${banner.url}" style="height:100px;width:300px"/></c:if>
												  <a href="javascript:reUpload('${banner.url}');"><button type="button" class="btn btn-primary">重新上传 </button></a>
											  	</c:when>
											  	<c:otherwise>
											  	  <input id="files" type="file" name="files" accept="image/jpeg, image/png" onchange="selectFile();" /><span><font style="color:red;">（APP大图建议尺寸：750*408；网站大图建议尺寸：1920*400（内容区域1000），png或者jpg格式）</font></span>
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
										<div class="control-group" id="innerContent">
											<label class="control-label"><span class="required"></span>链接地址</label>
											<div class="controls"><input id="link" name="link" type="text" value="${banner.link}" onkeyup="this.value=this.value.replace(/(^\s*)/g,'')" onblur="this.value=this.value.replace(/(\s*$)/g,'')" placeholder="10~200个字符" class="validate[minSize[10], maxSize[200]] text-input span9" /></div>
										</div>
										<div class="control-group">
											<label class="control-label">内容</label>
											<div class="controls">
												<textarea cols="150" rows="5" id="editor" name="content" onkeyup="this.value=this.value.replace(/(^\s*)/g,'')" onblur="this.value=this.value.replace(/(\s*$)/g,'')">${banner.content}</textarea>
											</div>
										</div>
										<div class="form-actions">
											<button type="button" id="save" onclick="uploadFile()" class="btn btn-icon btn-primary glyphicons circle_ok">保存</button>
											<a type="button" class="btn" href="javascript:quit();">返回</a>
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
	<script src="${pageContext.request.contextPath}/js/show.original.image.js"></script>
	<script type="text/javascript">
	    $(document).ready(function() {
			$('.breadcrumb').html('<li class="active">运营管理&nbsp;/<a href="javascript:quit();">Banner图管理</a>&nbsp;/&nbsp;编辑</li>');
			$('#progress').hide();
		    $('.msg').css('color', 'red');
		    $('input[name=platform][value=${banner.platform}]').attr('checked', true);
			$('.bigImage').click(ImgShow);
			var editor = UE.getEditor('editor');
	    	$('#fm').validationEngine('attach', { 
		        promptPosition: 'bottomLeft', 
		        scroll: false,
		        showOneMessage : true
		    });
		});
	    
	    var index = 0;
	    function startValidate() {
	    	if(index > 0) {
		    	if(!$('#files').val()) {
					alert('请选择要上传的图片');
					return false;
				}
	    	}
	   		
		    return true;
		}
	   	
	   	function quit() {
	   		window.location.href='${pageContext.request.contextPath}/operation/banner/${base64}';
	   	}
	   
	   	function reUpload(image) {
	   		if(confirm('您确认要重新上传此图片吗?')){
				index++;
				$('#bigImage').html('<input id="files" type="file" name="files" accept="image/jpeg, image/png" onchange="selectFile();" /><span><font style="color:red;">（APP大图建议尺寸：750*408；网站大图建议尺寸：1920*400（内容区域1000），png或者jpg格式）</font></span>');
			}
	   	}
	   
		function selectFile() {
			var file = document.getElementById('files').files[0];
			$('#progress').hide();
           	if(file) {
        		if(/.*[\u4e00-\u9fa5]+.*$/.test(file.name)) {  
	            	alert('文件名中不能含有中文！'); 
	                $('#files').val('');
	                return false;  
                }  
				
				var suffix = file.name.split('.').pop().toLowerCase();
	       	   	if(suffix == 'png' || suffix == 'jpg') {
	              	if (Math.round(file.size/1024) > 2048) {
	            	  	alert('亲，图片不能超过2M');
	        		  	$('#files').val('');
		              	return false;
	              	}
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
	       		   	alert('请选择png、jpg的图片！');
	       		   	$('#files').val('');
	               	return false;
				}
        	}
		}
		
		function uploadFile() {
			if(startValidate()) {
				var file = document.getElementById('files');
		       	if(file) {
			   		var fd = new FormData();
			       	$('#save').attr('disabled','disabled');
			       	fd.append('file', document.getElementById('files').files[0]);
			       	var xhr = new XMLHttpRequest();
			       	xhr.upload.addEventListener('progress', uploadProgress, false);
			       	xhr.addEventListener('load', uploadComplete, false);
			       	xhr.open('POST', '${pageContext.request.contextPath}/web/upload');
			       	xhr.send(fd);
		       	} else {
		    	   	$('#fm').attr('action', '${pageContext.request.contextPath}/operation/banner/save');
					$('#fm').submit();
		       	}
			}
		}
	   
	    function uploadProgress(evt) {
	    	if(evt.lengthComputable) {
	        	var percentComplete = Math.round(evt.loaded*100/evt.total);
	            $('.progress-bar').css('width', percentComplete.toString()+'%');
	            $('.sr-only').html(percentComplete.toString()+'% 完成');
	        }
	    }
	      
	    function uploadComplete(evt) {
			if(startValidate()) {
				$('#url').val(evt.target.responseText);
				//$('#image').val(evt.target.responseText);
		   	   	$('#fm').attr('action', '${pageContext.request.contextPath}/operation/banner/save');
               	$('#fm').submit();
		   	}
	   	}
	</script>
</html>