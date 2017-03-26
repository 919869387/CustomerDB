<i><%@page language="java" contentType="text/html; charset=UTF-8"
		pageEncoding="UTF-8"%> <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
	<html>
<head>
<title>登录</title>
</head>
<script type="text/javascript" src="js/jquery-3.1.1.min.js"></script>
<script type="text/javascript" src="js/login.js"></script>

<body>
	<form id="form1" action="" method="post">
		<p>
			用户名： <input id="username" type="text">
		</p>
		<p>
			密&nbsp;&nbsp;码： <input id="password" type="password">
		</p>
		<p>
			<input type="button" name="submit" onclick="onClik();" value="提交">
		</p>
	</form>

	<font color="red">${error }</font>

</body>
	</html></i>