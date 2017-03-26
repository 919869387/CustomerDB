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
				showTagType(response.result);
			}else{
				alert("获取系统标签类型出错,无法添加标签！");
				location.href = "tagstore.do";
			}
		},
		error: function (response) {
			alert("addTag.js-initType-发生错误！");
			location.href = "tagstore.do";
		}
	});
}
function showTagType(typeDatas){
	//alert(typeDatas);
	var select = document.getElementById('type');

	for(var i=0;i<typeDatas.length;i++){
		var type = typeDatas[i].split("_");
		select.options.add(new Option(type[0],type[1])); //text,value
	}
}
//selectType选中触发事件
function selectTypeClick(value){
	if (value != "parent") {
		document.getElementById("sonTagTable").style.display="none";
	}else{
		document.getElementById("sonTagTable").style.display="block";
	}
}

//初始化父标签，将所有父标签放入下拉框内
function initParentTag(){
	$.ajax({
		type: "GET",
		url: "getALLParentTags.do",
		contentType: "application/json; charset=utf-8",
		//data: JSON.stringify(json),
		dataType: "json",
		success: function (response) {
			if (response.code > 0) {
				showParentTag(response.result);
			}else{
				alert("获取系统父标签类型出错,无法添加标签！");
				location.href = "tagstore.do";
			}
		},
		error: function (response) {
			alert("initParentTag-发生错误！");
			location.href = "tagstore.do";
		}
	});
}
function showParentTag(tags){
	
	var select = document.getElementById('selectParentTag');
	
	select.options.add(new Option("选择父标签",0,true,true)); //text,value
	select.options[0].disabled=true;
	select.options.add(new Option("无父标签",0)); //text,value
	
	for(var i=0;i<tags.length;i++){
		select.options.add(new Option(tags[i].cnname,tags[i].id)); //text,value
	}
}

//selectParentTag选中触发事件
//这里要根据父标签，得到相应的子标签
function selectParentTagClick(value){
	var parent_id = Number(document.getElementById("selectParentTag").value);
	initSonTag(parent_id);
}

//初始化子标签，将所有子标签放入下拉框内
function initSonTag(parent_id){
	var json = {'parent_id':parent_id};
	$.ajax({
		type: "POST",
		url: "getSonTagsByParent.do",
		contentType: "application/json; charset=utf-8",
		data: JSON.stringify(json),
		dataType: "json",
		success: function (response) {
			if (response.code > 0) {
				showSonTag(response.result);
			}else{
				alert("可用子标签获取失败！");
				location.href = "tagstore.do";
			}
		},
		error: function (response) {
			alert("获取可用子标签发生错误！");
			location.href = "tagstore.do";
		}
	});
}
function showSonTag(tags){
	var select = document.getElementById('selectSonTag');
	//首先将select清空
	select.options.length = 0;
	//将可用子标签添加进select
	select.options.add(new Option("选择子标签",0,true,true)); //text,value
	select.options[0].disabled=true;
	for(var i=0;i<tags.length;i++){
		select.options.add(new Option(tags[i].cnname,tags[i].id)); //text,value
	}
}


//selectSonTag选中触发事件
function selectSonTagClick(selectObj){
	var tb = document.getElementById("sonTagTable");

	var row = tb.insertRow();

	var cell = row.insertCell();
	cell.innerText = selectObj.value;
	var cell = row.insertCell();
	cell.innerText = selectObj.options[selectObj.selectedIndex].text;
	var cell = row.insertCell();
	cell.innerHTML = "<input type='button' value='删除' onclick='delSonTag(this)'>";
}
//子标签删除按钮
function delSonTag(o)
{
	var sonTable = document.getElementById('sonTagTable');
	sonTable.deleteRow(o.parentNode.parentNode.rowIndex);
}

//确定添加按钮
function addTagOnclick(){

	var cnname = document.getElementById("cnname").value;
	var type = document.getElementById("type").value;
	var parent_id = Number(document.getElementById("selectParentTag").value);

	var son_ids = new Array();//儿子标签id
	if(type == 'parent'){
		//如果是parent才会有儿子标签
		var sonTable = document.getElementById('sonTagTable');
		var rowNum = sonTable.rows.length;
		for(var i=2;i<rowNum;i++){
			var cols = sonTable.rows[i].childNodes;
			son_ids.push(Number(cols[0].innerText));
		}
	}
	
	var json = {'cnname':cnname,'type':type,'son_ids':son_ids,'parent_id':parent_id};
	$.ajax({
		type: "POST",
		url: "insertTag.do",
		contentType: "application/json; charset=utf-8",
		data: JSON.stringify(json),
		dataType: "json",
		success: function (response) {
			if (response.code > 0) {
				alert("标签添加成功！");
				location.href = response.resultURL;
			}else{
				alert("标签添加失败！");
			}
		},
		error: function (response) {
			alert("标签添加时发生错误！");
			location.href = "tagstore.do";
		}
	});
}


//返回上级页面
function returnPage(){
	location.href = "tagstore.do";
}