//tagstore.jsp初始化函数
function init(){

	$.ajax({
		type: "GET",
		url: "getALLTags.do",
		contentType: "application/json; charset=utf-8",
		//data: JSON.stringify(json),
		dataType: "json",
		success: function (response) {
			if (response.code > 0) {
				showdata(response.result);
			}else{
				alert("用户名 密码有误！"+response.result);
			}
		},
		error: function (response) {
			alert("togstore.js-init-发生错误");
		}
	});
}

//动态将tag数据展示出来
function showdata(datas) {
	//添加最上面固定一栏
	var tb = document.getElementById("mytable");
	var row = tb.insertRow();
	var cell = row.insertCell();
	cell.innerText = "id";
	var cell = row.insertCell();
	cell.innerText = "cnname";
	var cell = row.insertCell();
	cell.innerText = "type";
	var cell = row.insertCell();
	cell.innerText = "beused_times";
	var cell = row.insertCell();
	cell.innerText = "son_ids";
	var cell = row.insertCell();
	cell.innerText = "parent_id";
	var cell = row.insertCell();
	cell.innerText = "操作";
	var cell = row.insertCell();
	cell.innerText = "操作";
	var cell = row.insertCell();
	cell.innerText = "操作";
	
	//添加数据
	var jsondatas = eval(datas);

	for(var i=0; i<jsondatas.length; i++){
		var row = tb.insertRow();
		var cell = row.insertCell();
		cell.innerText = jsondatas[i].id;
		var cell = row.insertCell();
		cell.innerText = jsondatas[i].cnname;
		var cell = row.insertCell();
		cell.innerText = jsondatas[i].type;
		var cell = row.insertCell();
		cell.innerText = jsondatas[i].beused_times;
		var cell = row.insertCell();
		cell.innerText = jsondatas[i].son_ids;
		var cell = row.insertCell();
		cell.innerText = jsondatas[i].parent_id;
		var cell = row.insertCell();
		cell.innerHTML = "<input type='button' value='查看标签' onclick='toShowTagPage("+jsondatas[i].id+")'>";
		
		if(jsondatas[i].beused_times!=0){
			var cell = row.insertCell();
			cell.innerHTML = "<input type='button' value='管理标签父子关系' onclick='toManageTagRelationPage("+jsondatas[i].id+")'>";
			var cell = row.insertCell();
		}else{
			//如果标签已经使用，可以修改
			var cell = row.insertCell();
			cell.innerHTML = "<input type='button' value='管理标签父子关系' onclick='toManageTagRelationPage("+jsondatas[i].id+")'>";
			var cell = row.insertCell();
			cell.innerHTML = "<input type='button' value='删除标签' onclick='deleteTagOnclick("+jsondatas[i].id+")'>";
		}
	}
}

//到查看标签页面
function toShowTagPage(id){
	location.href = "toShowTagPage.do?id="+id;
}

//到管理标签关系页面
function toManageTagRelationPage(id){
	alert("未完成");
	//location.href = "toManageTagRelationPage.do?id="+id;
}

//到管理标签页面
function toManageTagPage(id){
	alert("未完成");
	//location.href = "toManageTagPage.do?id="+id;
}

//到添加标签页面
function toAddTagPage(){
	location.href = "addTag.do";
}

//删除标签
function deleteTagOnclick(id){
	
	var json = {'id':id};
	$.ajax({
		type: "POST",
		url: "daleteTag.do",
		contentType: "application/json; charset=utf-8",
		data: JSON.stringify(json),
		dataType: "json",
		success: function (response) {
			if (response.code > 0) {
				alert("删除标签成功！");
			}else{
				alert("删除标签失败！");
			}
			location.href = response.resultURL;
		},
		error: function (response) {
			alert("发生错误");
		}
	});
}

//返回上级页面
function returnPage(){
	location.href = "menu.do";
}