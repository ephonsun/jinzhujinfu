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
                    <jsp:include page="${pageContext.request.contextPath}/breadcrumb" flush="true" />
                    <!-- content begin -->
                    <div class="row-fluid">
                    	<form class="form-horizontal" id="fm" method="post"  action="${pageContext.request.contextPath}/operation/notice/save">
                    		<input name="id" value="${notice.id}" type="hidden">
                    		<input id="newsId" name="news.id" value="${notice.news.id}" type="hidden">
                    		<input name="base64" value="${base64}" type="hidden">
                    		<div class="well">
	                    		<div class="control-group" id="noticeType">
									<label class="control-label">类型</label>
									<div class="controls">
										<div class="row-fluid">
											<div class="span2">
												<input type="radio" name="type" value="1"  class="validate[required] radio span4">
												<span>网站公告</span>
											</div>
											<div class="span2">
												<input type="radio" name="type" value="2"  class="validate[required] radio span4">
												<span>媒体报道</span>
											</div>
										</div>
									</div>
								</div>
								<div class="control-group">
									<label class="control-label"><span class="required">*</span>新闻素材</label>
									<div class="controls">
										<input type="text" id="title" name="news.title" value="${notice.news.title}" class="validate[required] text-input span9" readonly="readonly" onclick="showNewsDialog()"/>
										<button type="button" class="btn btn-icon btn-default" onclick="showNewsDialog()">选择</button>
									</div>
								</div>
								<div class="control-group">
									<label class="control-label"></label>
									<div class="controls">
										<button type="submit" id="save" class="btn btn-icon btn-primary glyphicons circle_ok"><i></i>保存</button>
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
        <div class="modal hide fade" id="newsList" role="dialog" >
		  	<div class="modal-dialog modal-lg">
		    	<div class="modal-content">
		      		<div class="modal-header">
		        		<button type="button" class="close" data-dismiss="modal" aria-label="Close">
		        			<span aria-hidden="true">&times;</span>
		        		</button>
		        		<h4 class="modal-title" id="myModalLabel">选择新闻</h4>
		      		</div>
		      		<div class="modal-body">
                        <table cellpadding="0" cellspacing="0" border="0" class="table table-striped">
		                	<thead>
		                    	<tr>
	                                <th>序号</th>
	                                <th>新闻标题</th>
	                                <th>操作</th>
		                        </tr>
		                   </thead>
		                   <tbody id="newsBody">
                     	   </tbody>
                     	   <tfoot>
                      	        <tr>
                      				<td colspan="3">
                      					<div id="news-page"></div>
                      				</td>
                      			</tr>
                      		</tfoot>
             			</table>
		      		</div>
		    	</div>
		    </div>
		</div> 
		<jsp:include page="${pageContext.request.contextPath}/footer" flush="true" />
	</body>
	<script type="text/javascript">
		$(document).ready(function() {
			$('.breadcrumb').html('<li class="active">运营管理&nbsp;/<a href="javascript:quit();">新闻公告管理</a>&nbsp;/&nbsp;编辑</li>');
		    $('.msg').css('color', 'red');
		    $('input[name=type][value=${notice.type}]').attr("checked",true);
			$('#fm').validationEngine('attach', {
		        promptPosition:'bottomLeft',
		        showOneMessage: true,
		        focusFirstField:true,
		        scroll: true
		    });
		});	
	
		function quit(){
			window.location.href='${pageContext.request.contextPath}/operation/notice/${base64}';
		}
		
		function showNewsDialog() {
			search(1);
			$('#newsList').modal('show');
		}
		
		function search(page) {
			$.post('${pageContext.request.contextPath}/operation/news/'+page+'/15', function(result) {
				if (result.total > 0) {
					$('#newsBody').html('');
			       	for (var i=0; i<result.news.length; i++) {
			    		var news = result.news[i];
			    		var tr = '<tr>';
			    	   	tr += '<td>'+(i+1)+'</td>';
			    	   	tr += '<td>'+news.title+'</td>';
			    	   	tr += '<td><a href="javascript:choice('+news.id+', \''+news.title+'\');">选择</a></td>';
			    	   	tr += '</tr>';
			    	   	$('#newsBody').append(tr);
					}
			       
			       	var totalPages = Math.ceil(result.total/15);
			       	var options = {
				    	currentPage: result.page,
				        totalPages: totalPages,
				        size: 'normal',
						alignment: 'center',
						tooltipTitles: function (type, page, current) {
							switch (type) {
						    case 'first':
						        return '首页';
						    case 'prev':
						        return '上一页';
						    case 'next':
						        return '下一页';
						    case 'last':
						        return '末页';
						    case 'page':
						        return (page === current) ? '当前页 ' + page : '跳到 ' + page;
						    }
						},
			            onPageClicked: function(event, originalEvent, type, page){
			            	search(page);
			            }  
				    };
					$('#news-page').bootstrapPaginator(options);
		       	} else {
		    		$('#newsBody').html('<tr><td colspan="3">请先添加新闻素材</td></tr>');	
		    	}
			});	
		}
		
		function choice(id, title) {
            $('#newsList').modal('hide');
			$('#newsId').val(id);
			$('#title').val(title);
		}
	</script>
</html>