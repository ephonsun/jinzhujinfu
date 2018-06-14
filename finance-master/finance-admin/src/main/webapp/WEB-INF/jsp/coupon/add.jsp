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
                    <jsp:include page="${pageContext.request.contextPath}/breadcrumb" flush="true" />
                    <!-- content begin -->
                    <div class="row-fluid">
                    	<form class="form-horizontal" id="fm" method="post" action="${pageContext.request.contextPath}/coupon/save">
                    		<input id="id" name="id" value="0" type="hidden">
                    		<input id="investAmount" name="investAmount" value="0" type="hidden" >
                    		<input name="base64" value="${base64}" type="hidden">
                    		<div class="well">
                    			<div class="control-group">
									<label class="control-label"><span class="required">*</span>红包名称</label>
									<div class="controls">
										<input id="name" name="name" type="text" onkeyup="this.value=this.value.replace(/(^\s*)/g,'')" onblur="this.value=this.value.replace(/(\s*$)/g,'')" placeholder="6~30个字符" class="validate[required, minSize[6], maxSize[30]] text-input span9" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label"><span class="required">*</span>红包金额</label>
									<div class="controls">
										<input type="text" id="amount" name="amount" style="width:100px;" class="validate[custom[integer],min[1],max[9999]] text-input span3" onblur="this.value=this.value.replace(/(\s*$)/g,'')"/>
										<span>元  </span>
									</div>
								</div>
								<div class="control-group" id="investAmountLimit">
									<label class="control-label"><span class="required">*</span>投资金额</label>
									<div class="controls">
										<div class="span2">
											<input type="radio" name="category" checked="checked" value="0" class="validate[required] span1">
											<span>不限 </span>
										</div>
										<div class="span4">
											<input type="radio" name="category" value="1" class="validate[required] span1">
											<span>单笔满 </span>
											<input type="text" id="investAmountSingle" style="width:100px;" onblur="changeCondition()" class="validate[custom[integer],min[100],max[9999999]] span4" onkeyup="this.value=this.value.replace(/^((0)|(0[1|2|3|4|5|6|7|8|9]))/g, '').trim()"/>
											<span>元可用  </span>
										</div>
									</div>
								</div>
								<div class="control-group">
									<label class="control-label"><span class="required">*</span>理财期限</label>
									<div class="controls">
										<input type="text" id="financePeriod" name="financePeriod" onblur="changeCondition()" class="validate[required, custom[integer],min[0],max[999]] span2" onkeyup="this.value=this.value.replace(/^((0)|(0[1|2|3|4|5|6|7|8|9]))/g, '').trim()"/>
										<span>天以上 </span>
									</div>
								</div>
								<div class="control-group">
									<label class="control-label"><span class="required">*</span>有效天数</label>
									<div class="controls">
										<span>领取</span>
										<input type="text" id="validDays" name="validDays" class="validate[required,custom[integer],min[0],max[999]]" />
										<span>天后过期</span>
										<input type="checkbox" id="longTerm" name="longTerm" onchange="changeDate()" /><span>长期有效</span>
									</div>
								</div>
								<div class="control-group" >
									<label class="control-label">使用条件</label>
									<div class="controls">
										<input type="text" id="condition" name="condition" style="color:red;" value="常规产品可用" readonly="readonly" class="text-input span12" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label">备注</label>
									<div class="controls">
										<textarea  id="remark" name="remark" rows="3" class="validate[maxSize[100]] span12"></textarea>
									</div>
								</div>
								<div class="control-group">
									<label class="control-label"></label>
									<div class="controls">
										<button type="submit" class="btn btn-icon btn-primary glyphicons circle_ok"><i></i>保存</button>
										<button type="reset" class="btn btn-icon btn-default glyphicons circle_remove" onclick="quit();"><i></i>返回</button>
									</div>
								</div>
                    		</div>
                    	</form>
                    </div>
                    <!-- content end -->
                </div>
            </div>
		</div>
		<jsp:include page="${pageContext.request.contextPath}/footer" flush="true" />
	</body>
	<script type="text/javascript">
	 	$(document).ready(function() {
			$('.breadcrumb').html('<li class="active">红包管理&nbsp;/&nbsp;红包库/&nbsp;新增</li>');
		    $('.msg').css('color', 'red');
		    $('#name').css({width:450});
		    $('#financePeriod').css({width:150});
		    $('#expiryDate').datetimepicker({
				format:'Y-m-d',
				lang:'ch',
				scrollInput:false,
				timepicker:false,
				minDate:new Date(),
				onSelectDate:function() {
					$("#longTerm").prop("checked", false);
				}
			});
			$('#expiryDate').addClass('validate[required]');
		    $('#expiryDate').css({width:150});
			
			$('#investAmountLimit').find('input:radio').click(function() {
				$('#investAmountSingle').blur();
				if($(this).val() == 0){
					$('#investAmountSingle').removeClass('validate[required]');
					$('#investAmountSingle').val('');
				}
				if($(this).val() == 1){
					$('#investAmountSingle').addClass('validate[required]');
				}
				changeCondition();
			});
			
			$('#fm').validationEngine('attach', {
		        promptPosition:'bottomLeft',
		        showOneMessage: true,
		        focusFirstField:true,
		        scroll: true
		    });
		});
	 	
	 	function changeCondition() {
			var financePeriod = $('#financePeriod').val();
			var investAmountSingle = $('#investAmountSingle').val();
			if(!financePeriod) {
				return;
			}
			if(investAmountSingle) {
				var label = '单笔投资≥'+investAmountSingle+'元，理财期限≥'+financePeriod+'天，常规产品可用';
				$('#investAmount').val(investAmountSingle);
				$('#condition').val(label);
			} else {
				var label = '理财期限≥'+financePeriod+'天，常规产品可用';
				$('#investAmount').val(0);
				$('#condition').val(label);
			}
		}
	 	
	 	function changeDate(){
			if($("#longTerm").prop("checked") == true){
				$('#expiryDate').val("2050-12-31");
				$('#validDays').val("0");
				$('#validDays').attr("disabled",true); 
				$('#validDays').removeClass('validate[required,custom[integer],min[0],max[999]]');
		   	} else {
				$('#validDays').attr("disabled",false); 
				$('#validDays').addClass('validate[required,custom[integer],min[0],max[999]]');
		   	}
	    }
	 	
		function quit(){
			window.location.href='${pageContext.request.contextPath}/coupon/${base64}';
		}
	</script>
</html>