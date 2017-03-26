<i><%@page language="java" contentType="text/html; charset=UTF-8"
		pageEncoding="UTF-8"%> <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
	<html>
<head>
<title>添加标签</title>
</head>
<script type="text/javascript" src="js/jquery-3.1.1.min.js"></script>
<script type="text/javascript" src="js/addTag.js"></script>
<script language="JavaScript" type="text/javascript">
	window.onload = function() {
		initType();
		initParentTag();
	};
</script>
<body>
	<button type="button" onclick="returnPage()">返回上级页面</button>
	<br/>
	<br/> 
	cnname:&nbsp<input type="text" id="cnname"/>
	<br /> 
	type:&nbsp<select id="type" onchange="selectTypeClick(this.value)"><option value ="" disabled="">选择标签类型</option></select>
	<br />
	parentTag:
	<select id="selectParentTag" onchange="selectParentTagClick(this)"></select>
	<br />
	sonTags:
	<table id="sonTagTable" border='1' style="display:none">
		<tr>
			<th>
				<select id="selectSonTag" onchange="selectSonTagClick(this)"><option value ="" disabled="">选择子标签</option></select>
			</th>
		</tr>
		<tr>
    		<th>标签ID</th>
    		<th>标签名</th>
    		<th>操作</th>
  		</tr>
	</table>
	
	<br />
	<br />
	<button type="button" onclick="addTagOnclick()">确定添加</button>
	

</body>
	</html> </i>