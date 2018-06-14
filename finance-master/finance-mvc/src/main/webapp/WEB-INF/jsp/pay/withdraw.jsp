<%@ page language="java" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<meta http-equiv="Pragma" content="no-cache" />  
<meta http-equiv="Expires" content="-1" />  
<meta http-equiv="Cache-Control" content="no-cache" /> 
<title>lianlianPay</title>
</head>
<body>
	<form id="withdraw" action="${reqUrl}" method="post">
		<input type="hidden" name="mchnt_cd" value="${merchantNO}"/>
		<input type="hidden" name="mchnt_txn_ssn" value="${orderNO}"/>
		<input type="hidden" name="login_id" value="${cellphone}"/>
		<input type="hidden" name="amt" value="${amount}"/>
		<input type="hidden" name="page_notify_url" value="${backUrl}"/>
		<input type="hidden" name="back_notify_url" value="${notifyUrl}"/>
		<input type="hidden" name="signature" value="${signature}"/>
	</form>
	<script language="javascript" type="text/javascript" >
		window.onload=function() {
		    document.getElementById("withdraw").submit();
		}
	</script>
</body>
</html>