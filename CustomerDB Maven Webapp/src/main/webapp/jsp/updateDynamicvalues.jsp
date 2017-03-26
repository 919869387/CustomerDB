<i><%@page language="java" contentType="text/html; charset=UTF-8"
		pageEncoding="UTF-8"%> <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
	<html>
<head>
<title>动态值维护</title>
</head>
<script type="text/javascript" src="js/jquery-3.1.1.min.js"></script>
<script type="text/javascript" src="js/updateDynamicvalues.js"></script>
<script language="JavaScript" type="text/javascript">
	window.onload = function() {
		//如果是tagshowtype不能修改值
		if(${name=="tagshowtype" }){
			document.getElementById("updateButton").style.display="none";
			document.getElementById("textarea").disabled = true;
		}
		
		var dynamicvalues = ${dynamicvalues };
		init(dynamicvalues);
	};
</script>
<body>
	<button type="button" onclick="returnPage()">返回上级页面</button>
	<br/>
	<br/>
	id:&nbsp&nbsp&nbsp<input type="text" id="id" readOnly="true" value = ${id } disabled/>
	<br />
	name:&nbsp<input type="text" id="name" readOnly="true" value = ${name } disabled/>
	<br />
	<textarea rows="10" cols="50" id="textarea"></textarea>
	<br />
	<br />
	<button id="updateButton" type="button" onclick="updateDynamicvalues()">确定修改</button>
	

</body>
	</html> </i>