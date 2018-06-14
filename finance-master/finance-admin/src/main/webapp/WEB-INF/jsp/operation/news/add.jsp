<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
			                    <form class="form-horizontal" enctype="multipart/form-data" id="fm" method="post">
									<div class="well" style="padding-bottom:20px; margin:0;">
										<input id="id" name="id" value="0" type="hidden">
										<input id="image" name="image" value="" type="hidden">
				                    	<input name="base64" value="${base64}" type="hidden">
										<div class="control-group">
											<label class="control-label"><span class="required">*</span>新闻标题</label>
											<div class="controls">
												<input type="text" id="title" name="title" class="validate[required,minSize[6],maxSize[60],ajax[validateNews]] text-input span12" onkeyup="this.value=this.value.replace(/(^\s*)/g,'')" onblur="this.value=this.value.replace(/(\s*$)/g,'')"/>
											</div>
										</div>
										<div class="row-fluid">
											<div class="span6">
												<div class="control-group">
													<label class="control-label"><span class="required">*</span>新闻来源</label>
													<div class="controls">
														<input type="text" id="source" name="source" class="validate[required,maxSize[16]] text-input span12" onkeyup="this.value=this.value.replace(/(^\s*)/g,'')" onblur="this.value=this.value.replace(/(\s*$)/g,'')"/>
													</div>
												</div>
											</div>
											<div class="span6">
												<div class="control-group">
													<label class="control-label"><span class="required">*</span>新闻日期</label>
													<div class="controls">
														<input type="text" id="newsDate" name="newsDate" class="validate[required] text-input span12" onkeypress="return false"/>
													</div>
												</div>
											</div>
										</div>
										<div class="control-group">
											<label class="control-label">缩略图</label>
											<div class="controls">
												 <input id="files" type="file" name="files" accept="image/jpeg,image/png" onchange="selectFile();" />
												 <span><font class="msg">（图片建议尺寸：360*240，png或者jpg格式）</font></span>
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
										<div class="control-group">
											<label class="control-label">新闻简介</label>
											<div class="controls">
												<input type="text" id="remark" name="remark" placeholder="50个汉字以内" class="validate[maxSize[100]] text-input span12" onkeyup="this.value=this.value.replace(/(^\s*)/g,'')" onblur="this.value=this.value.replace(/(\s*$)/g,'')"/>
											</div>
										</div>
										<div class="control-group">
											<label class="control-label"><span class="required"></span>链接地址</label>
											<div class="controls">
												<input type="text" id="url" name="url" value="" class="text-input span12" />
											</div>
										</div>
										<div class="control-group">
											<label class="control-label"><span class="required"></span>新闻内容</label>
											<div class="controls">
												<textarea cols="150" rows="5" id="editor" name="content" onkeyup="this.value=this.value.replace(/(^\s*)/g,'')" onblur="this.value=this.value.replace(/(\s*$)/g,'')"></textarea>
											</div>
										</div>
										<div class="form-actions">
											<button type="button" id="save" onclick="uploadFile();" class="btn btn-icon btn-primary glyphicons circle_ok">保存</button>
											<a href="javascript:quit();"><button type="button" class="btn">返回</button></a>
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
	    var editor = '';
	    $(document).ready(function() {
			$('.breadcrumb').html('<li class="active">运营管理&nbsp;/<a href="javascript:quit();">新闻素材管理</a>&nbsp;/&nbsp;新增</li>');
			$('#title').focus();
			$('#progress').hide();
		    $('.msg').css('color', 'red');
		    $('#newsDate').datetimepicker({
				format:'Y-m-d',
				lang:'ch',
				scrollInput:false,
				timepicker:false,
				maxDate:new Date().toLocaleDateString()
			});	
			editor = UE.getEditor('editor');
			
	    	$('#fm').validationEngine('attach', { 
		        promptPosition: 'bottomLeft', 
		        scroll: false,
		        showOneMessage : true
		    });
		});		
	   
		function startValidate() {
			if($('#title').val().length <= 0){
	    		return false;
			}
			if($('#source').val().length <= 0){
	    		return false;
			}
			if($('#newsDate').val().length <= 0){
	    		return false;
			}
			
	    	var _s = $('#source').val();
	    	var url = $('#url').val();
	    	$('#source').val(_s.replace(' ', ''));
			
			var txt = editor.getContent();
		    if($.trim(txt) == '' && url == '') {
				alert('链接地址和新闻内容选填一项...');
				return false;
		    } else if(txt.length > 10000) {
		    	 alert('活动内容字数超出限制');
		    	 return false;
		    }  
		    return true;
		}
	   
		function quit() {
			window.location.href='${pageContext.request.contextPath}/operation/news/${base64}';
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
	              	$('#fileSize').html('文件大小:'+fileSize);
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
		        	$('#save').attr('disabled','disabled');
				    var fd = new FormData();
				    fd.append('url', document.getElementById('files').files[0]);
				    var xhr = new XMLHttpRequest();
				    xhr.upload.addEventListener('progress', uploadProgress, true);
				    xhr.addEventListener('load', uploadComplete, false);
				    xhr.open('POST', '${pageContext.request.contextPath}/web/upload', true);
				    xhr.send(fd); 
		        } else {
		        	$('#fm').attr('action', '${pageContext.request.contextPath}/operation/news/save');
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
				$('#image').val(evt.target.responseText);
		   	   	$('#fm').attr('action', '${pageContext.request.contextPath}/operation/news/save');
               	$('#fm').submit();
		   	}
	   	}
	</script>
</html>