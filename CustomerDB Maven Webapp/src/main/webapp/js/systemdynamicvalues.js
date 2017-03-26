//systemdynamicvalues。jsp初始化函数
function init(){

	$.ajax({
		type: "GET",
		url: "getAllSystemDynamicValues.do",
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
			alert("发生错误");
		}
	});
}

//动态将systemdynamicvalues数据展示出来
function showdata(datas) {
	//添加最上面固定一栏
	var tb = document.getElementById("mytable");
	var row = tb.insertRow();
	var cell = row.insertCell();
	cell.innerText = "id";
	var cell = row.insertCell();
	cell.innerText = "name";
	var cell = row.insertCell();
	cell.innerText = "dynamicvalues";

	//添加数据
	var jsondatas = eval(datas);

	for(var i=0; i<jsondatas.length; i++){
		var row = tb.insertRow();
		var cell = row.insertCell();
		cell.innerText = jsondatas[i].id;
		var cell = row.insertCell();
		cell.innerText = jsondatas[i].name;
		if(jsondatas[i].name=='tagshowtype'){
			var cell = row.insertCell();
			cell.innerHTML = "<input type='button' value='查看动态值' onclick='showSystemdynamicvaluesOnclick("+jsondatas[i].id+")'>";
		}else{
			var cell = row.insertCell();
			cell.innerHTML = "<input type='button' value='管理动态值' onclick='showSystemdynamicvaluesOnclick("+jsondatas[i].id+")'>";
		}
	}
}

//显示动态值按钮事件
function showSystemdynamicvaluesOnclick(id){
	location.href = "toUpdateDynamicvaluesPage.do?id="+id;
}

//返回上级页面
function returnPage(){
	location.href = "menu.do";
}

