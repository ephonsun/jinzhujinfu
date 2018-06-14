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
                    <!-- content begin -->
                    <div class="row-fluid">
                        <!-- block -->
                        <div class="block">
                            <jsp:include page="${pageContext.request.contextPath}/breadcrumb" flush="true" />
                            <div class="block-content collapse in">
	                             <form id="fm" action="${pageContext.request.contextPath}/operation/message/save" method="post" class="form-horizontal" onsubmit="return validate()" >
								<div class="well" style="padding-bottom: 20px; margin: 0;">
									<div class="control-group">
										<input id="id" name="id" value="0" type="hidden">
										<input name="base64" value="${base64}" type="hidden">
		                    		</div>
			                    	<div class="control-group">
										<label class="control-label"><span class="required">*</span>推送标题</label>
										<div class="controls">
											<input type="text" id="title" name="title" class="validate[required,minSize[6],maxSize[120]] text-input span3" onkeyup="this.value=this.value.replace(/(^\s*)/g,'')" onblur="this.value=this.value.replace(/(\s*$)/g,'')"/>
										</div>
									</div>
									<div class="control-group">
										<label class="control-label"><span class="required">*</span>内容简介</label>
										<div class="controls">
											<input type="text" id="content" name="content" class="validate[required,minSize[6],maxSize[120]] text-input span3" onkeyup="this.value=this.value.replace(/(^\s*)/g,'')" onblur="this.value=this.value.replace(/(\s*$)/g,'')"/>
										</div>
									</div>
									<div class="control-group">
										<label class="control-label">选择系统</label>
										<div class="controls">
											<div class="row-fluid">
												<div class="span2">
													<input type="radio" name="osType" value="0" checked="checked" class="validate[required] radio span4">
													<span>全部</span>
												</div>
												<div class="span2">
													<input type="radio" name="osType" value="1" class="validate[required] radio span4">
													<span>ANDROID</span>
												</div>
												<div class="span2">
													<input type="radio" name="osType" value="2" class="validate[required] radio span4">
													<span>IOS</span>
												</div>
											</div>
										</div>
									</div>
									<div class="control-group" id="sendType">
										<label class="control-label">发送类型</label>
										<div class="controls">
											<div class="row-fluid">
												<div class="span2">
													<input type="radio" name="sendType" value="0" checked="checked" class="validate[required] radio span4">
													<span>立即发送</span>
												</div>
												<div class="span2">
													<input type="radio" name="sendType" value="1" class="validate[required] radio span4">
													<span>定时发送</span>
												</div>
												<div class="span2">
												    <input type="text" id="sendTime" name="sendTime" class=" text-input span3" onkeypress="return false"/>
												</div>
											</div>
										</div>
									</div>
									<div class="control-group" id="sendTarget">
										<label class="control-label">发送对象</label>
										<div class="controls">
											<div class="row-fluid">
												<div class="span2">
													<input type="radio" name="sendTarget" value="0" checked="checked" class="validate[required] radio span4">
													<span>全部人员</span>
												</div>
												<div class="span2">
													<input type="radio" name="sendTarget" value="1" class="validate[required] radio span4">
													<span>指定人员</span>
												</div>		
											</div>
										</div>
									</div>
									<div class="control-group" id="equipmentbox">
										<label class="control-label"><span class="required">*</span>设备号<br><span style="color:red"></span></label>
										<div class="controls">
											 <textarea id="equipment" name="equipment" rows="5" placeholder="多个设备号之间以    ; 区分"></textarea>
	                                	</div>
									</div>
									<hr class="separator" />
									<div class="form-actions">
										<button type="submit" class="btn btn-icon btn-primary glyphicons circle_ok"><i></i>保存</button>
										<button type="reset" class="btn btn-icon btn-default glyphicons circle_remove" onclick="quit();"><i></i>返回</button>
									</div>
								</div>
							</form>
                            </div>
                        </div>
                        <!-- /block -->
                    </div>
                    <!-- content end -->
                </div>
            </div>
        </div>
		<jsp:include page="${pageContext.request.contextPath}/footer" flush="true" />
	</body>
	<script type="text/javascript">
		var productPage = parseInt(1);
		$(function(){
			$('.breadcrumb').html('<li class="active">运营管理&nbsp;/<a href="javascript:quit();">消息推送管理</a>&nbsp;/&nbsp;新增</li>');
			$('#sendTime').css({width:'150px'});
			$('#equipment').css({width:'300px'});
			$('#productList').css({width:'50%'});
			$("#product").hide();
			$("#news").hide();
			$("#sendTime").hide();
			$("#equipmentbox").hide();
			$('#fm').validationEngine('attach', { 
				promptPosition:'bottomLeft',
		        showOneMessage: true,
		        focusFirstField:true,
		        scroll: true
		    });
			
			$('#sendType').find('input:radio').click(function(){
				var val=$(this).val();
				if(val==0){
					$("#sendTime").hide();
					$("#sendTime").val('');
					$("#sendTime").removeClass('validate[required]');
				}else{
					$("#sendTime").show();
					$("#sendTime").addClass('validate[required]');
				}
			});
			
			$('#sendTarget').find('input:radio').click(function(){
				var val=$(this).val();
				if(val==0){
					$("#equipmentbox").hide();
				}else{
					$("#equipmentbox").show();
				}
			});
			
			$('#sendTime').datetimepicker({
				lang:'ch',
				format:'Y-m-d H:00:00',
				timepicker:true,
				//minTime:true,
				minDate:true,
				onSelectDate : function() {
					var selectedDatetime = $('#sendTime').val();
					var start=new Date(selectedDatetime.replace("-", "/").replace("-", "/"));
					if (new Date() > start) {
						$('#sendTime').val('');
					}
				},
				onSelectTime : function() {
					var selectedDatetime = $('#sendTime').val();
					var start=new Date(selectedDatetime.replace("-", "/").replace("-", "/"));
					if (new Date() > start) {
						$('#sendTime').val('');
					}
				},
				onClose : function() {
					var selectedDatetime = $('#sendTime').val();
					var start=new Date(selectedDatetime.replace("-", "/").replace("-", "/"));
					return start > new Date();
				}
				//minDate:new Date().toLocaleDateString()
			});
			search(productPage);
		});
		
		function validate() {
	    	if($("input[name='sendTarget']:checked").val()==1) {
		    	var equipments= $('#equipment').val();
	            if(equipments=="") {
	            	alert('如指定人员，请加设备号。');
	            	return false;
	            }
				if(equipments.split('\n').length > 20){
					alert('设备最多20个。');
					return false ;
				}
	    	}
            return true;
		}
		
		function quit() {
		   window.location.href='${pageContext.request.contextPath}/operation/message/list';
	    }
	</script>
</html>