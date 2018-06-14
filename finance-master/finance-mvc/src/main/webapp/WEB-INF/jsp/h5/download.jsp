<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no, minimal-ui">
    <title>金竹金服App下载</title>
    <style>
        html { width:100%;height:100%;font-size:65%}
        body {
            max-width: 640px;
            font-size: 1.2rem;
            font-family: "Microsoft YaHei",Helvetica,STHeiTi,sans-serif;
            background-color: #ddd;
            min-width: 320px;
            margin: 0 auto;
            height:100%;
            position:relative;
        }
        a,a:active {text-decoration:none;}
        .main {
            width: 100%;
            background-color: #6dd2cc;
            position: relative;
            height:100%;
        }
        .img-responsive {width:100%;}
        .download-box {
            width:100%;
        }
        .download {
            width: 80%;
            margin: 10px auto;
            text-align: center;
            display: block;
            background-color: #e0555b;
            border-radius:10px;
            height:70px;
            line-height:70px;
            color:#fff;
            font-size:30px;         
        }
        .icon-d3 { margin-right:1.2rem;vertical-align:middle;width:3rem;}
        .txt {
            color: #fff;
            text-align: center;
            font-size: 24px;
            font-family:"黑体";
        }
        .bottom-img { width:100%;position:absolute;bottom:0;left:0;right:0}
        .t1 {
            position: absolute;
            top: 0;
            left: 0;
            text-align: center;
            color: #fff;
            font-size: 2.2rem;
            transform: rotate(-5deg);
            display: block;       
            -webkit-transform: rotate(-5deg);
            -moz-transform: rotate(-5deg);
            -o-transform: rotate(-5deg);
        }
        .t1 img {margin-right:.5rem;width:2rem;vertical-align:middle;}
        .tip-cover { position:absolute;top:0;left:0;right:0;bottom:0;background-color:rgba(0,0,0,.7);width:100%;display:none;}
        .tip-cover img { width:100%;}

        @media (min-width:370px) {
            html {
                font-size:80%;
            }
        }
        @media (min-width:480px) {
            html {
                font-size: 100%;
            }
        }
        @media (min-width:640px) {
            html {
                font-size: 125%;
            }
        }
    </style>
    <script type="text/javascript">
    function checkOS() {
		var u = navigator.userAgent.toLowerCase();
		var os = 'ie';
		var us = u.split('jzjf');
		if (u.indexOf('jzjf') > -1 && (u.indexOf('android') > -1 || u.indexOf('linux') > -1)) {
			if (us.length > 1) {
				os = 'android';
			}
		} else if (u.indexOf('jzjf') > -1 && (u.indexOf('iphone') > -1 || u.indexOf('ipad') > -1 || u.indexOf('ios') > -1)) {
			if (us.length > 1) {
					os = 'ios';
			} 
		}
		return os;
	};
	
	function download() {
		var u = navigator.userAgent.toLowerCase();
		if(u.indexOf('iphone') > -1 || u.indexOf('ipad') > -1){
			location.href = 'https://www.jinzhujinfu.com'
		} else {
			location.href = '${url}'
		}
	}
</script>
</head>
<body>
   <div class="main">
       <p class="t1"><img src="${pageContext.request.contextPath}/images/d2.png" />注册即送<span style="color:yellow;font-size:36px;">1000</span><span style="color:yellow">元</span>新手红包</p>
       <img class="img-responsive" src="${pageContext.request.contextPath}/images/d1.jpg?v=1" />
       <div class="download-box">
           <a class="download" href="javascript:download();"><img class="icon-d3" src="${pageContext.request.contextPath}/images/d3.png" />APP下载</a>
           <p class="txt">尊享多重安全保障计划</p>
       </div>
       <img class="bottom-img" src="${pageContext.request.contextPath}/images/d4.png" />
       <div class="tip-cover">
           <img src="${pageContext.request.contextPath}/images/d5.png" />
       </div>
   </div>
</body>
</html>