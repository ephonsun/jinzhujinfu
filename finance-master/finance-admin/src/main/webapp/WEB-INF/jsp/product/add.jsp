<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<style>
	<!--
	.combo-select {
	  position: relative;
	  max-width: 91%;
	  min-width: 91%;
	  /* margin-bottom: 15px; */
	  font: 100% Helvetica, Arial, Sans-serif;
	  border: 1px #ccc solid;
	  border-radius: 3px;
	  display:inline-block;
	  vertical-align: top;
	} 
	-->
	</style>
	<jsp:include page="${pageContext.request.contextPath}/header" flush="true" />
	<body>
		<jsp:include page="${pageContext.request.contextPath}/menu" flush="true" />
            <div class="row-fluid">
                <jsp:include page="${pageContext.request.contextPath}/sidebar" flush="true" />
                <div class="span9" id="content">
                	<jsp:include page="${pageContext.request.contextPath}/breadcrumb" flush="true" />
                    <!-- content begin -->
					<div class="row-fluid">
                    <form class="form-horizontal" id="fm" method="post" enctype="multipart/form-data">
						<input id="id" name="id" value="0" type="hidden">
						<input name="status" value="0" type="hidden">
						<input name="base64" value="${base64}" type="hidden">
						<div class="well">
                    		<div class="control-group">
								<label class="control-label"><span class="required">*</span>产品名称</label>
								<div class="controls"><input type="text" id="name" name="name" class="validate[required,maxSize[20],ajax[validateProduct]] text-input span12"  placeholder="不超过10个汉字"  onkeyup="this.value=this.value.replace(/(^\s*)/g,'')" onblur="this.value=this.value.replace(/(\s*$)/g,'')"/></div>
							</div>
							<div class="row-fluid">
								<div class="span6">
									<div class="control-group">
										<label class="control-label"><span class="required">*</span>产品类型</label>
										<div class="controls">
										    <select id="category" name="category.id" class="validate[required] span11">
											    <option></option>
	                                            <c:forEach items="${categories}" var="category">
		                                            <option value="${category.id}">${category.name}</option>
	                                            </c:forEach>
											</select>
									    </div>
									</div>
									<div class="control-group">
										<label class="control-label"><span class="required">*</span>产品年化收益</label>
										<div class="controls"><input type="text" id="yearIncome" name="yearIncome" class="validate[required,custom[numberSp],min[0.01],max[36]] text-input span11"   onkeyup="this.value=this.value.replace(/^((0{2,})|(\.{0,})|(0[1|2|3|4|5|6|7|8|9]))/g, '').trim()" placeholder="输入的数字不大于36"/> %</div>
									</div>
									<div class="control-group" id="totalAmountGroup">
										<label class="control-label"><span class="required">*</span>募集金额</label>
										<div class="controls"><input type="text" id="totalAmount" name="totalAmount" class="validate[required,custom[number],min[1],max[99999999]] text-input span11" onkeyup="this.value=this.value.replace(/^((0{2,})|(\.{0,})|(0[1|2|3|4|5|6|7|8|9]))/g, '').trim()"/>元</div>
									</div>
									<div class="control-group">
										<label class="control-label"><span class="required">*</span>上架时间</label>
										<div class="controls"><input type="text" id="raisedTime" name="raisedTime" class="validate[required]  text-input span11" onkeypress="return false"/></div>
									</div>
									<div class="control-group" id="lowestMoneyGroup">
										<label class="control-label"><span class="required">*</span>起投金额</label>
										<div class="controls">
											<select id="lowestMoney" name="lowestMoney" class="validate[required] span11">
												<option value="100">100</option>
												<option value="200">200</option>
												<option value="500">500</option>
												<option value="1000">1000</option>
											</select>
											<span>元</span>
										</div>
									</div>
								</div>
								<div class="span6">
									<div class="control-group">
										<label class="control-label"><span class="required">*</span>关联商户</label>
										<div class="controls">
											<select id="merchantId" name="merchant.id" class="validate[required] span11" onchange="showMerchantInfo(this.value)">
										        <option></option>
										        <c:forEach items="${merchants}" var="merchant">
		                                            <option value="${merchant.id}">${merchant.name}</option>
	                                            </c:forEach>
										    </select>
										</div>
									</div>
									<div class="control-group">
										<label class="control-label"><span class="required">*</span>借款年化利率</label>
										<div class="controls"><input type="text" id="loanYearIncome" name="loanYearIncome" class="validate[required,custom[numberSp],min[0.01],max[36]] text-input span11" onkeyup="this.value=this.value.replace(/^((0{2,})|(\.{0,})|(0[1|2|3|4|5|6|7|8|9]))/g, '').trim()" placeholder="输入的数字不大于36"/>%</div>
									</div>
									<div class="control-group">
										<label class="control-label">产品加息</label>
										<div class="controls"><input type="text" id="increaseInterest" name="increaseInterest" class="validate[required,custom[numberSp],min[0],max[20]] text-input span11" onkeyup="this.value=this.value.replace(/^((0{2,})|(\.{0,})|(0[1|2|3|4|5|6|7|8|9]))/g, '').trim()" value="0" placeholder="输入的数字不大于 20"/> %</div>
									</div>
								    <div class="control-group">
										<label class="control-label" id="higheLabel">投资上限</label>
										<div class="controls">
											<input type="text" id="highestMoney" name="highestMoney" value="10000000" class="validate[required,custom[integer] text-input span11"/> 元
										</div>
									</div>
				      				<div class="control-group">
										<label class="control-label"><span class="required">*</span>理财期限</label>
										<div class="controls">
											<input type="text" id="financePeriod" name="financePeriod" class="validate[required,custom[integer],min[1],max[999]] text-input span11" onblur="colcDateTime()" onkeyup="this.value=this.value.replace(/^((0{2,})|(0[1|2|3|4|5|6|7|8|9]))/g, '').trim()"/> 天
										</div>
									</div>
								</div>
							</div>
						    <div class="control-group">
							  <label class="control-label"><span class="required"></span>标签</label>
							  <div class="controls"><input type="text" id="label" name="label" class="validate[maxSize[16] text-input span12"  placeholder="不超过8个汉字" onkeyup="this.value=this.value.replace(/(^\s*)/g,'')" onblur="this.value=this.value.replace(/(\s*$)/g,'')"/></div>
						    </div>
						    <div class="control-group" id="innerContent">
								<label class="control-label"><span class="required">*</span>项目概述</label>
								<div class="controls">
									<textarea cols="150" rows="10" id="summary" name="summary"></textarea>
								</div>
							</div>
							<div class="control-group">
						       <label class="control-label"><span class="required">*</span>风险控制管理</label>
						       <div class="controls">			
						       	<textarea cols="150" rows="10" id="risk" name="risk"></textarea>
					           </div>
					        </div>
					        <div class="control-group">
								<label class="control-label">相关资料</label>
								<div class="controls">
									<div id="uploadAttachments"></div>
									<a id="previewAttachment" class="thumbnail" style="float:left;height:327px; width:210px;">
										<div style="float:left;height:297px; width:210px;">&nbsp;</div>
										<button type="button" id="uploadAttachment" class="btn btn-lg">添加</button>
									</a>
								</div>
							</div>
						    <div class="form-actions">
								<button type="button" id="save" onclick="return saveProduct();" class="btn btn-icon btn-primary glyphicons circle_ok">保存</button>
								<a href="javascript:quit();"><button type="button" class="btn">返回</button></a>
							</div>
                    	</div>
					</form>
                    <!-- content end -->
                </div>
            </div>
        </div>
        <div class="popOver" style="width:100%; height:100%;position:fixed;z-index:100000;left:0;top:0;background:#000;opacity:.3;display:none;text-align:center;vertical-align:middle">
        	<img alt="" src="${pageContext.request.contextPath}/images/loading.gif" style="position:absolute; margin:auto;top:-9999px;right:-9999px;bottom:-9999px;left:-9999px;  ">
        </div>      
		<jsp:include page="${pageContext.request.contextPath}/footer" flush="true" />
	</body>
	<script type="text/javascript">
		var attachmentCount = 0;
		$(document).ready(function() {
			$('.breadcrumb').html('<li class="active"><a href="javascript:quit();">产品管理</a>&nbsp;/&nbsp;新增</li>');
			$('.marginTop').css('margin', '2px auto auto auto');
			$('.popOver').hide();
			$('.msg').css('color', 'red');
			$('#name').focus();
			$('#merchantId').comboSelect();
			$('#raisedTime').datetimepicker({
				format:'Y-m-d H:i:00',
				step: 5,
				minDate:true,
				minTime:true,
				lang:'ch',
				timepicker:true,
				scrollInput:false,
				onShow:function() {
					if ($('td').hasClass('xdsoft_other_month')) {
						$('td').removeClass('xdsoft_other_month');
					} 
				},
				onSelectDate:function() {
					var beginTime = $('#raisedTime').val();
					var beginDate = new Date(Date.parse(beginTime.replace(/-/g,'/')));
					if($.trim('${systemTime}'.split(' ')[0]) == $.trim(beginTime.split(' ')[0])) {
						$('#raisedTime').datetimepicker({
							format:'Y-m-d H:i:00',
							step: 5,
							minDate:d.getFullYear()+'/'+(d.getMonth()+1)+'/'+d.getDate(),
							minTime:d.getHours()+1+':00',
							lang:'ch',
							timepicker:true,
							onShow:function() {
								if ($('td').hasClass('xdsoft_other_month')) {
									$('td').removeClass('xdsoft_other_month');
								} 
							}
						});
						if(($.trim('${systemTime}').split(' ')[1].split(':')[0] == beginTime.split(' ')[1].split(':')[0])){
							var dateTime =  new Date(Date.parse((beginDate.getFullYear()+'-'+(beginDate.getMonth()+1)+'-'+beginDate.getDate()+' '+fix((parseInt(beginDate.getHours())+1),2)+':00:00').replace(/-/g,'/')));
							$('#raisedTime').val(dateTime.getFullYear()+'-'+(dateTime.getMonth()+1)+'-'+dateTime.getDate()+" "+fix(dateTime.getHours(),2)+":00:00");
						}
					} else {
						$('#raisedTime').datetimepicker({
							format:'Y-m-d H:i:00',
							step: 5,
							minDate:'${systemTime}',
							minTime:'00:00',
							lang:'ch',
							timepicker:true,
							onShow:function() {
								if ($('td').hasClass('xdsoft_other_month')) {
									$('td').removeClass('xdsoft_other_month');
								} 
							}
						});
					}
				}
			});
			
			UE.getEditor('summary');
			UE.getEditor('risk');
			
			$("#uploadAttachment").click(function() {
				if(attachmentCount <= 4) {
					var html = '<a id="aAttachment'+attachmentCount+'" class="thumbnail" style="float:left;height:327px;width:210px;">';
					html += '<div id="divAttachment'+attachmentCount+'" style="height:297px;width:210px;"></div>';
					html += '<input id="attachment'+attachmentCount+'" name="productAttachment'+attachmentCount+'" type="file" accept="image/jpeg,image/png" onchange="selectFile('+attachmentCount+');"/>';
					html += '<input type="button" class="btn btn-lg" value="删除" onclick="removeAttachment('+attachmentCount+')"/>';
					html += '</a>';
					$('#uploadAttachments').append(html);
					$('#attachment'+attachmentCount).trigger('click');
					attachmentCount = attachmentCount+1;
					if(attachmentCount < 4) {
						$('#previewAttachment').show();
					} else {
						$('#previewAttachment').hide();
					}
					
				} else {
					alert('相关资料数量不超过4张!');
				}
			});
			
			$('#fm').validationEngine('attach', {
		        promptPosition:'bottomLeft',
		        inlineValidation: true, 
		        validationEventTriggers:'click blur', 
		        showOneMessage: true,
		        focusFirstField:true,
		        scroll: true
		    });
		});
		
		function removeAttachment(attachmentId) {
			$('#aAttachment'+attachmentId).remove();
		    attachmentCount = attachmentCount-1;
		    $('#previewAttachment').show();
		}
		
		function startValidate() {
			if($('#raisedTime').val() != '') {
				if('${systemTime}' > $('#raisedTime').val()){
					alert("上架时间不能小于当前时间,请修改上架时间!");
					return false;
				}
			}
			
			var summary = UE.getEditor('summary').getContent();
		    if($.trim(summary) == '') {
		    	alert('项目概述内容不能为空');
		    	return false;
		    } else if(summary.length > 10000) {
		    	 alert('项目概述内容超出限制');
		    	 return false;
		    }
		    
		    var risk = UE.getEditor('risk').getContent();
		    if($.trim(risk) == '') {
		    	alert('风险控制管理内容不能为空');
		    	return false;
		    } else if(risk.length > 10000) {
		    	 alert('风险控制管理内容超出限制');
		    	 return false;
		    }
		    return true;
		}
		
		function selectFile(attachmentId) {
			var file = document.getElementById('attachment'+attachmentId).files[0];
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
	        		  	$('#attachment'+attachmentId).val('');
		              	return false;
					}
	              	
	              	var img = $('<img style="float:left;height:297px; width:210px;" src="'+window.URL.createObjectURL(file)+'" />');
	              	$('#divAttachment'+attachmentId).empty().append(img);
	       		  	return true; 
				} else {
	       		   	alert('请选择png、jpg的图片！');
	       			$('#attachment'+attachmentId).val('');
	               	return false;
				}
        	}
		}
		
		function saveProduct() {
			if(startValidate()) {
				$('.popOver').show();
				$('#fm').validationEngine('detach');
				$('#fm').attr('action', '${pageContext.request.contextPath}/product/save');
	            $('#fm').submit();
	            return true;
			}
			return false;
		}
		
		function quit() {
			window.location.href='${pageContext.request.contextPath}/product/${base64}';
	    }
	</script>
</html>