<i><%@page language="java" contentType="text/html; charset=UTF-8"
		pageEncoding="UTF-8"%> <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
	<html>
<head>
<title>标签详细信息</title>
</head>
<script type="text/javascript" src="js/jquery-3.1.1.min.js"></script>
<script type="text/javascript" src="js/showTag.js"></script>
<script language="JavaScript" type="text/javascript">
	window.onload = function() {
		//初始化type值,将其改为中文
		initType();
		//得到父标签的英文名与中文名
		initParentTag();
		//得到所有儿子标签
		var tagSon_ids = ${son_ids };
		showSonTags(tagSon_ids);
	};
</script>
<body>
	<button type="button" onclick="returnPage()">返回上级页面</button>
	<br/>
	<br/>
	id:&nbsp&nbsp&nbsp&nbsp&nbsp<input type="text" id="id" value=${id } readOnly="true"  disabled />
	<br /> 
	cnname:&nbsp<input type="text" id="cnname" value=${cnname } readOnly="true"  disabled/>
	<br />
	type:&nbsp&nbsp&nbsp<input type="text" id="type" readOnly="true" value=${type } disabled />
	<br /> 
	beused_times:&nbsp<input type="text" id="beused_times" readOnly="true" value=${beused_times } disabled />
	<br />
	parentTag:&nbsp&nbsp&nbsp<input type="text" id="parentTag" readOnly="true" value=${parent_id } disabled />
	<br />
	sonTags:<table id="mytable" border='1'></table>
	
</body>
	</html> </i>