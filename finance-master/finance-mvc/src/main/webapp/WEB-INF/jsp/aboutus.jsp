<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<jsp:include page="${pageContext.request.contextPath}/header" flush="true" />
<body>
<jsp:include page="${pageContext.request.contextPath}/navbar" flush="true" />
<div class="container">
	<div class="row">&nbsp;</div>
</div>
<div class="container">
	<div class="row">
		<div class="col-md-2">
		 	<div class="list-group">
				<a href="javascript:void();" class="list-group-item disabled">企业介绍</a>
			  	<a href="${pageContext.request.contextPath}/news/1" class="list-group-item">新闻公告</a>
			  	<a href="${pageContext.request.contextPath}/faq" class="list-group-item">常见问题</a>
				<a href="${pageContext.request.contextPath}/contactus" class="list-group-item">联系我们</a>
			</div>
		</div>
		<div class="col-md-10">
		 	<div class="panel panel-default">
				<div class="col-md-12">
					<div class="row">&nbsp;</div>
					<div class="page-header">
						<h4 class="h4">公司简介</h4>
					</div>
					<div style="padding-left:10px;">
					<img src="${pageContext.request.contextPath}/images/company.png" alt="" class="col-md-4" >
					<p class="rol-md-8" style="padding-top:20px; line-height: 30px; margin-left:20px;">金竹金服专注于车辆抵质押贷业务，凭借多年金融服务和互联网金融信息服务的实操经验，以规范的运作模式、专注和完善的风控体系，致力于成为国内卓越领先的互联网金融服务-站式平台，为投资人增加安全可信赖的投资渠道，为“中小微企业”和个人等借入者快速借款，解决资金之忧。</p>
					</div>
				</div>
				
			  	<div class="panel-body">
			  		<div class="row">&nbsp;</div>
			  		<div class="page-header">
					  <h4 class="h4">行业背景</h4>
					</div>
			    	<p style="padding-top:20px; line-height: 30px; margin-left:20px;">金竹金服，杭州鑫珂金融服务外包有限公司旗下互联网金融平台，专注于车辆抵质押贷业务，凭借多年金融服务和互联网金融信息服务的实操经验，以科学的管理制度、规范的运作模式、专注和完善的风控体系，致力于成为国内卓越领先的互联网金融服务一站式平台——将金融资讯、产品、服务、技术一体化融合，从而实现从投资资讯到产品投资的无缝链接。平台以“诚信经营、开放透明”为己任，秉承“诚信、安全”的经营理念，致力为金竹金服两端的客户搭建一个便捷、安全、透明的互联网金融平台，最终实现其真正的社会价值，为借出者增加安全可信赖的投资渠道，为“中小微企业”和个人等借入者快速借款，解决资金之忧。</p>
			    	<div class="row">&nbsp;</div>
			  		<div class="page-header">
					  <h4 class="h4">企业愿景</h4>
					</div>
					<p style="padding-top:20px; line-height: 30px; margin-left:20px;">作为联接借款人与广大投资者之间的桥梁，公司坚持第三方独立，专业，客观的宗旨，打造互联网金融健康可持续发展的投资关系生态链，以客户财富的长期稳定增长为己任，相伴成长，价值共享。</p>
			    	<div class="row">&nbsp;</div>
			  		<div class="page-header">
					  <h4 class="h4">工商信息</h4>
					</div>
					<div style="padding-left:20px ">
						<p>公司全称：杭州鑫珂金融服务外包有限公司</p>
						<p>平台简称：金竹金服</p>
						<p>平台网址：http://www.jinzhujinfu.com</p>
						<p>注册资本：5000万元人民币</p>
						<p>成立时间：2015年10月16日</p>
						<p>注册地址：浙江省杭州市滨江区长河街道滨盛路1688号名豪大厦311室（仅限办公用途）</p>
						<p>办公地址：浙江省杭州市滨江区长河街道滨盛路1688号名豪大厦311室（注册地址一致）</p>
						<p>登记状态：存续</p>
						<p>法定代表人：吴荣华</p>
						<p>经营范围：接受金融机构委托从事金融业务流程外部（具体经营项目请登录国家企业信用信息公式系统查询）</p>
						<p>联系方式：400-851-3788</p>
						<p>备案：浙ICP备17043049号-1</p>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<jsp:include page="${pageContext.request.contextPath}/footer" flush="true" />
</body>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/adapter.js"></script>
</html>