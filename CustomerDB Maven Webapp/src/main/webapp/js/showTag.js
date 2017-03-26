//页面初始化时，得要所有标签的类型
function initType(){
	$.ajax({
		type: "GET",
		url: "getTagShowTypeDynamicValues.do",
		contentType: "application/json; charset=utf-8",
		//data: JSON.stringify(json),
		dataType: "json",
		success: function (response) {
			if (response.code > 0) {
				showTagCnname(response.result);
			}else{
				alert("获取系统标签类型出错,无法添加标签！");
				location.href = "tagstore.do";
			}
		},
		error: function (response) {
			alert("showTag.js-initType-发生错误！");
			location.href = "tagstore.do";
		}
	});
}

function showTagCnname(typeDatas){

	var typeElement = document.getElementById('type');
	
	for(var i=0;i<typeDatas.length;i++){
		var type = typeDatas[i].split("_");
		if(typeElement.value == type[1]){
			//将英文名改为中文
			typeElement.value = type[0];
		}
	}
}

//得到父标签的中文名
function initParentTag(){
	var parentID = document.getElementById('parentTag').value;
	if(parentID=="0"){
		document.getElementById('parentTag').value = '无'
		return;
	}
	var json = {'tagid':parentID};
	$.ajax({
		type: "POST",
		url: "getTag.do",
		contentType: "application/json; charset=utf-8",
		data: JSON.stringify(json),
		dataType: "json",
		success: function (response) {
			if (response.code > 0) {
				document.getElementById('parentTag').value = response.result.cnname;
			}else{
				alert("获取父标签信息失败！");
				location.href = "tagstore.do";
			}
		},
		error: function (response) {
			alert("initParentTag-发生错误！");
			location.href = "tagstore.do";
		}
	});
}

//显示儿子标签
function showSonTags(tagSon_ids){
	var tb = document.getElementById("mytable");
	if(tagSon_ids.length == 0){
		var row = tb.insertRow();
		var cell = row.insertCell();
		cell.innerText = "无子标签";
		return;
	}
	
	var row = tb.insertRow();
	var cell = row.insertCell();
	cell.innerText = "标签ID";
	var cell = row.insertCell();
	cell.innerText = "标签名";
	for(var i=0;i<tagSon_ids.length;i++){
		
		var json = {'tagid':tagSon_ids[i]};
		$.ajax({
			type: "POST",
			url: "getTag.do",
			contentType: "application/json; charset=utf-8",
			data: JSON.stringify(json),
			dataType: "json",
			success: function (response) {
				if (response.code > 0) {
					var row = tb.insertRow();
					var cell = row.insertCell();
					cell.innerText = response.result.id;
					var cell = row.insertCell();
					cell.innerText = response.result.cnname;
				}else{
					alert("获取子标签信息失败！");
					location.href = "tagstore.do";
				}
			},
			error: function (response) {
				alert("showTag.js-showSonTags-发生错误！");
				location.href = "tagstore.do";
			}
		});
		
	}
}

//返回上级页面
function returnPage(){
	location.href = "tagstore.do";
}
