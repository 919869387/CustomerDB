<i><%@page language="java" contentType="text/html; charset=UTF-8"
		pageEncoding="UTF-8"%> <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
	<html>
<head>
<title>标签详细信息</title>
</head>
<script type="text/javascript" src="js/jquery-3.1.1.min.js"></script>
<script type="text/javascript" src="js/manageTag.js"></script>
<script language="JavaScript" type="text/javascript">
	window.onload = function() {
		//初始化Type,并得到所有type值
		initType();
		initParentTag();
		
	};
</script>
<body>
	<button type="button" onclick="returnPage()">返回上级页面</button>
	<br/>
	<br/>
	id:&nbsp&nbsp&nbsp&nbsp&nbsp<input type="text" id="id" value=${id } readOnly="true"  disabled />
	<br /> 
	enname:&nbsp<input type="text" id="enname" value=${enname } />
	<br /> 
	cnname:&nbsp<input type="text" id="cnname" value=${cnname } />
	<br />
	type:&nbsp&nbsp&nbsp<select id="type" onchange="selectTypeClick(this.value)"><option id="option1" value ="${type }" disabled></option></select>
	<br /> 
	beused_times:&nbsp<input type="text" id="beused_times" readOnly="true" value=${beused_times } disabled />
	<br />
	parentTag:&nbsp&nbsp&nbsp<select id="parentTag"><option id="option1" value ="${parent_id }" disabled></option></select>
	<br />
	sonTags:<table id="mytable" border='1'></table>
	<br />
	<button type="button" id="refreshButton" onclick='refresh(${id })'>刷新放弃修改</button>
	<br />
	<br />
	<button type="button" onclick="updateTag()" id="updateButton">确定修改</button>
	

</body>
	</html> </i>