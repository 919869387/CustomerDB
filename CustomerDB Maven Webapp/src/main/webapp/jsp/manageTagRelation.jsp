<i><%@page language="java" contentType="text/html; charset=UTF-8"
		pageEncoding="UTF-8"%> <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
	<html>
<head>
<title>标签详细信息</title>
</head>
<script type="text/javascript" src="js/jquery-3.1.1.min.js"></script>
<script type="text/javascript" src="js/manageTagRelation.js"></script>
<script language="JavaScript" type="text/javascript">
	window.onload = function() {
		//初始化Type,将类型改为中文名
		initType();
		//初始化ParentTag
		initParentTag();
		//如果type=parent 初始化sonTags
		if(${type =="parent"}){
			initSonTags(${son_ids });
		}
		
	};
</script>
<body>
	<button type="button" onclick="returnPage()">返回上级页面</button>
	<br/>
	<br/>
	id:&nbsp&nbsp&nbsp&nbsp&nbsp<input type="text" id="id" value=${id } readOnly="true"  disabled />
	<br /> 
	cnname:&nbsp<input type="text" id="cnname" value=${cnname } readOnly="true"  disabled />
	<br />
	type:&nbsp&nbsp&nbsp<input type="text" id="type" value=${type } readOnly="true"  disabled />
	<br /> 
	beused_times:&nbsp<input type="text" id="beused_times" readOnly="true" value=${beused_times } disabled />
	<br />
	parentTag:
	<table id="parentTable" border='1'>
		<tr>
			<td>
				<input type="text" id="parentName" value=${parent_id } readOnly="true" />
			</td>
			<td>
				<select id="selectParentTag" onchange="selectParentTagClick(this)">
					<option value ="" disabled>选择父标签</option>
				</select>
			</td>
		</tr>
	</table>
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
	<button type="button" id="refreshButton" onclick='refresh(${id })'>刷新放弃修改</button>
	<br />
	<br />
	<button type="button" onclick="updateTag()" id="updateButton">确定修改</button>
	

</body>
	</html> </i>