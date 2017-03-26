//页面初始化时，得要所有标签的类型
function initType(){
	$.ajax({
		type: "GET",
		url: "systemdynamicvalues/getTagShowTypeDynamicValues.do",
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
			alert("发生错误！");
			location.href = "tagstore.do";
		}
	});
}

function showTagType(typeDatas){
	//alert(typeDatas);
	var option1 = document.getElementById('option1');
	var select = document.getElementById('type');
	
	for(var i=0;i<typeDatas.length;i++){
		var type = typeDatas[i].split("_");
		select.options.add(new Option(type[0],type[1])); //text,value
		if(option1.value == type[1]){
			//如果与标签原来value相等,就不加option
			option1.text = type[0];
		}
	}
}

//初始化父标签
function initParentTag(){
	
}
////select选中触发事件
//function selectTypeClick(value){
//	document.getElementById("textarea").value = "";
//	
//	if (value != "text" && value != "parent") {
//		document.getElementById("textarea").readOnly=false;
//		document.getElementById("textarea").disabled=false;
//		document.getElementById("textarea").style.display="block";//打开textare
//	}else{
//		document.getElementById("textarea").style.display="none";//关闭textare
//	}
//}

//对Textarea的初始化
function initTextarea(tagValues){
	var textvalue = document.getElementById("textarea");
	textvalue.value = "";
	for(var i=0;i<tagValues.length;i++){
		textvalue.value = textvalue.value + tagValues[i]+"\n";
	}
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
			alert("发生错误");
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
