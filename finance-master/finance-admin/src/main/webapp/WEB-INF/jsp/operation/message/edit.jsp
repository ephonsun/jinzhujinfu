<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
                    <!-- content begin -->
                    <div class="row-fluid">
                        <!-- block -->
                        <div class="block">
                            <jsp:include page="${pageContext.request.contextPath}/breadcrumb" flush="true" />
                            <div class="block-content collapse in">
	                            <form id="fm" action="${pageContext.request.contextPath}/operation/message/save" method="post" class="form-horizontal" onsubmit="return validate()" >
								<div class="well" style="padding-bottom: 20px; margin: 0;">
									<div class="control-group">
										<input id="id" name="id" value="${message.id}" type="hidden">
										<input name="base64" value="${base64}" type="hidden">
		                    		</div>
			                    	<div class="control-group">
										<label class="control-label"><span class="required">*</span>推送标题</label>
										<div class="controls">
											<input type="text" id="title" name="title" class="validate[required] text-input span3" onkeyup="this.value=this.value.replace(/(^\s*)/g,'')" onblur="this.value=this.value.replace(/(\s*$)/g,'')"/>
										</div>
									</div>
									<div class="control-group">
										<label class="control-label"><span class="required">*</span>内容简介</label>
										<div class="controls">
											<input type="text" id="content" name="content" class="validate[required] text-input span3 content" onkeyup="this.value=this.value.replace(/(^\s*)/g,'')" onblur="this.value=this.value.replace(/(\s*$)/g,'')"/>
										</div>
									</div>
									<div class="control-group" id="osType">
										<label class="control-label">选择系统</label>
										<div class="controls">
											<div class="row-fluid">
												<div class="span2">
													<input type="radio" name="osType" value="0" class="validate[required] radio span4">
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
									<div class="control-group" id="sendType" >
										<label class="control-label">发送类型</label>
										<div class="controls">
											<div class="row-fluid">
												<div class="span2">
													<input type="radio" name="sendType" value="0" class="validate[required] radio span4">
													<span>立即发送</span>
												</div>
												<div class="span2">
													<input type="radio" name="sendType" value="1" class="validate[required] radio span4">
													<span>定时发送</span>
												</div>
												<div class="span2">
												    <input type="text" id="sendTime" name="sendTime" class="text-input span3" />
												</div>
											</div>
										</div>
									</div>
									<div class="control-group" id="sendTarget">
										<label class="control-label">发送对象</label>
										<div class="controls">
											<div class="row-fluid">
												<div class="span2">
													<input type="radio" name="sendTarget" value="0" class="validate[required] radio span4">
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
											 <textarea id="equipment" name="equipment" rows="5" placeholder="多个设备号之间以    ; 区分">${message.equipment}</textarea>
	                                	</div>
									</div>
									<hr class="separator" />
									<div class="form-actions">
										<button type="submit" id="save" class="btn btn-icon btn-primary glyphicons circle_ok" ><i></i>保存</button>
										<a type="reset" id="reset" class="btn btn-icon btn-default glyphicons circle_remove" href="javascript:quit();"><i></i>返回</a>
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
		$(function(){
			$('#sendTime').css({width:'150px'});
			$('#equipment').css({width:'300px'});
			$('#productList').css({width:'50%'});
			$("#product").hide();
			$("#news").hide();
			$("#sendTime").hide();
			$("#equipmentbox").hide();
			$('#fm').validationEngine('attach', { 
		        promptPosition: 'bottomRight', 
		        autoHidePrompt:true,
	    		autoHideDelay:3000,
		        scroll: false 
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
					minDate:new Date().toLocaleDateString(),
					onSelectTime : function() {
						var selectedDatetime = $('#sendTime').val();
						var start=new Date(selectedDatetime.replace("-", "/").replace("-", "/"));
						if (new Date() > start) {
							$('#sendTime').val('');
						}
					},
					onSelectDate : function() {
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
			   }
			);
			
			initEdit();
			$('.breadcrumb').html('<li class="active">运营管理&nbsp;/<a href="javascript:quit();">消息推送管理</a>&nbsp;/&nbsp;编辑</li>');
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
		
		function initEdit() {
			$('#title').val('${message.title}');
			$('.content').val('${message.content}');
			$('#sendTime').val('<fmt:formatDate value="${message.sendTime}" type="both" pattern="yyyy-MM-dd HH:mm:ss" />');
			
			if('${message.osType}'==0) {
				$('#osType').find('input:radio').eq(0).attr('checked','checked');
			} else if('${message.osType}'==1) {
				$('#osType').find('input:radio').eq(1).attr('checked','checked');
			} else {
				$('#osType').find('input:radio').eq(2).attr('checked','checked');
			}
			if('${message.sendType}'==0) {
				$('#sendType').find('input:radio').eq(0).attr('checked','checked');
				$('#sendTime').hide();
				$('#sendTime').val('');
				$("#sendTime").removeClass('validate[required]');
			} else {
				$('#sendType').find('input:radio').eq(1).attr('checked','checked');	
				$('#sendTime').show();
				$("#sendTime").addClass('validate[required]');
			}
			if('${message.sendTarget}'==0) {
				$('#sendTarget').find('input:radio').eq(0).attr('checked','checked');
			} else {
				$('#sendTarget').find('input:radio').eq(1).attr('checked','checked');
				$("#equipmentbox").show();
			}
		}
		
		function quit() {
		   window.location.href='${pageContext.request.contextPath}/operation/message/list';
	    }
	</script>
</html>