<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="format-detection" content="telephone=no">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0;">
	<title>常见问题</title>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.12.4.min.js"></script>
	<style type="text/css">
		.faq {
			/*padding-left:4%;*/
			/*padding-right:4%;*/
		}
		
		.faq .question {
			font-size: 16px;
			border-bottom: 1px solid lightgray;
			position: relative;
		}
		.faq .question p{
			margin:5px 0;
		}
		.faq .question .title {
			line-height: 28px;
			width:88%;
			padding:5px 0;
			padding-left:4%;
		
		}
		
		.faq .question .answer {
			display: none;
			font-size: 14px;
			line-height: 20px;
			border-top:1px dashed lightgray;
			text-align:justify;
			line-height: 24px;
			padding:5px 4%;
		}
		
		.faq .question .arrow {
			position: absolute;
			right: 6px;
			top: 10px;
			margin-right: 0px;
			width: 8px;
		}
		
	    .question{
			-webkit-tap-highlight-color:rgba(255,255,255,0);
		}
	    
		.rotate{
			-webkit-animation-name:rotate;
			-moz-animation-name:rotate;
			-ms-animation-name:rotate;
			-o-animation-name:rotate;
			animation-name:rotate;

			-webkit-animation-fill-mode:both;
			-moz-animation-fill-mode:both;
			-ms-animation-fill-mode:both;
			-o-animation-fill-mode:both;
			animation-fill-mode:both;
			
			-webkit-animation-duration:.5s;
			-moz-animation-duration:.5s;	
			-ms-animation-duration:.5s;
			-o-animation-duration:.5s;
			animation-duration:.5s;		
		}
		@-webkit-keyframes rotate{
			0%{
				-webkit-transform:rotate(0deg);

			}
			100%{
				-webkit-transform:rotate(90deg);

			}
		}
		@-moz-keyframes rotate{
			0%{
				-moz-transform:rotate(0deg);

			}
			100%{
				-moz-transform:rotate(90deg);

			}
		}
		@-ms-keyframes rotate{
			0%{
				-ms-transform:rotate(0deg);

			}
			100%{
				-ms-transform:rotate(90deg);

			}
		}
		@-o-keyframes rotate{
			0%{
				-o-transform:rotate(0deg);

			}
			100%{
				-o-transform:rotate(90deg);

			}
		}
		@keyframes rotate{
			0%{
				transform:rotate(0deg);

			}
			100%{
				transform:rotate(90deg);

			}
		}	

		</style>

	<script type="text/javascript">
		
		$(function() {
			$('.question').each(function() {
				$(this).click(function() {
					$(this).children().eq(1).slideToggle(500);
					$(this).children().eq(2).toggleClass('rotate');
					$(this).siblings('.question').find('.answer').slideUp();
					var oBject = $(this).siblings('.question').find('.arrow');
					if (oBject.hasClass('rotate')) {
						oBject.removeClass('rotate');
					}
				});
			});

		});
	</script>

</head>
	
<body>
	<div class="faq" >
		<c:choose>
			<c:when test="${fn:length(faqs) > 0}">
				<c:forEach var="faq" items="${faqs}" varStatus="status">
					<div class="question">
					    <p class="title">${faq.ask}</p>
						<div class="answer">${faq.question}</div>
						<img src="${pageContext.request.contextPath}/images/arrow_right.png" class="arrow"/>
					</div>
				</c:forEach>
			</c:when>
			<c:otherwise>
                                                        暂时还没有问答列表
            </c:otherwise>
		</c:choose>
		
	</div>
</body>
</html>