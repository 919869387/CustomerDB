//页面初始化时，将type英文修改为中文
function initType(){
	$.ajax({
		type: "GET",
		url: "systemdynamicvalues/getTagShowTypeDynamicValues.do",
		contentType: "application/json; charset=utf-8",
		//data: JSON.stringify(json),
		dataType: "json",
		success: function (response) {
			if (response.code > 0) {
				showTagCnname(response.result);
			}else{
				alert("获取系统标签类型失败！");
				location.href = "tagstore.do";
			}
		},
		error: function (response) {
			alert("获取系统标签类型时，请求方法出错！");
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
				alert("获取系统父标签失败！");
				location.href = "tagstore.do";
			}
		},
		error: function (response) {
			alert("获取系统父标签时，请求方法出错！");
			location.href = "tagstore.do";
		}
	});
}
function showParentTag(tags){
	var select = document.getElementById('selectParentTag');
	select.options.add(new Option("无父标签",0));
	for(var i=0;i<tags.length;i++){
		if(Number(document.getElementById('parentName').value)==tags[i].id){
			document.getElementById('parentName').value =tags[i].cnname
		}
		select.options.add(new Option(tags[i].cnname,tags[i].id)); //text,value
		//disabled <option value ="0">无父标签</option>
	}
	if(Number(document.getElementById('parentName').value)==0){
		document.getElementById('parentName').value ="无父标签";
	}
}
//selectParentTagClick选中触发事件
function selectParentTagClick(selectObj){
	document.getElementById('parentName').value =selectObj.options[selectObj.selectedIndex].text;
}

//初始化儿子标签
function initSonTags(tagSon_ids){
	var sonTagTable = document.getElementById("sonTagTable");
	sonTagTable.style.display="block";
	
	//将已经存在的子标签写入表格中
	for(var i=0;i<tagSon_ids.length;i++){
		var json = {'tagid':tagSon_ids[i]};
		$.ajax({
			type: "POST",
			url: "tagstore/getTag.do",
			contentType: "application/json; charset=utf-8",
			data: JSON.stringify(json),
			dataType: "json",
			success: function (response) {
				if (response.code > 0) {
					var row = sonTagTable.insertRow();
					var cell = row.insertCell();
					cell.innerText = response.result.id;
					var cell = row.insertCell();
					cell.innerText = response.result.cnname;
					var cell = row.insertCell();
					cell.innerHTML = "<input type='button' value='删除' onclick='delSonTag(this)'>";
				}else{
					alert("获取子标签信息失败！");
				}
			},
			error: function (response) {
				alert("获取子标签信息时发生错误！");
			}
		});
	}
	
	//将系统子标签放入下拉框中
	$.ajax({
		type: "GET",
		url: "getALLSonTags.do",
		contentType: "application/json; charset=utf-8",
		//data: JSON.stringify(json),
		dataType: "json",
		success: function (response) {
			if (response.code > 0) {
				showSonTag(response.result);
			}else{
				alert("获取系统子标签失败！");
				location.href = "tagstore.do";
			}
		},
		error: function (response) {
			alert("获取系统子标签时，请求方法出错！");
			location.href = "tagstore.do";
		}
	});
}
//子标签删除按钮
function delSonTag(o)
{
	var sonTable = document.getElementById('sonTagTable');
	sonTable.deleteRow(o.parentNode.parentNode.rowIndex);
}
//将系统子标签放入下拉框中
function showSonTag(tags){
	var select = document.getElementById('selectSonTag');
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





//更新标签按钮
function updateTag(){
	var id = document.getElementById("id").value;
	var enname = document.getElementById("enname").value;
	var cnname = document.getElementById("cnname").value;
	var type = document.getElementById("type").value;
	
	var textvalue = document.getElementById("textarea").value;
	var textvalueArr = textvalue.split('\n');
	//对每行的的值进行处理
	var newValues=new Array();//新动态值
	for(var i=0;i<textvalueArr.length;i++){
		//去除两边的空格
		if(textvalueArr[i].replace(/(^\s*)|(\s*$)/g,"")!=null && textvalueArr[i].replace(/(^\s*)|(\s*$)/g,"")!=""){
			newValues.push(textvalueArr[i]);
		}
	}

	var json = {'id':id,'enname':enname,'cnname':cnname,'type':type,'values':newValues};
	$.ajax({
		type: "POST",
		url: "updateTag.do",
		contentType: "application/json; charset=utf-8",
		data: JSON.stringify(json),
		dataType: "json",
		success: function (response) {
			if (response.code > 0) {
				alert("标签修改成功！");
				location.href = "tagstore.do";
			}else{
				alert("标签修改失败！");
				location.href = response.resultURL;
			}
		},
		error: function (response) {
			alert("标签修改，请求方法出错！");
		}
	});
}

//刷新按钮
function refresh(id){
	location.href = "toManageTagValuePage.do?id="+id;
}

//返回上级页面
function returnPage(){
	location.href = "tagstore.do";
}
