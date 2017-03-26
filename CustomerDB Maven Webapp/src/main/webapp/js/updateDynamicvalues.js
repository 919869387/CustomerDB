function init(dynamicvalues){
	var textvalue = document.getElementById("textarea");
	textvalue.value = "";
	for(var i=0;i<dynamicvalues.length;i++){
		textvalue.value = textvalue.value + dynamicvalues[i]+"\n";
	}
}

function updateDynamicvalues(){
	
	var id = document.getElementById("id").value;
	var textvalue = document.getElementById("textarea").value;
	var textvalueArr = textvalue.split('\n');
	//对每行的的值进行处理
	var newDynamicvalues=new Array();//新动态值
	for(var i=0;i<textvalueArr.length;i++){
		//去除两边的空格
		if(textvalueArr[i].replace(/(^\s*)|(\s*$)/g,"")!=null && textvalueArr[i].replace(/(^\s*)|(\s*$)/g,"")!=""){
			newDynamicvalues.push(textvalueArr[i]);
		}
	}

	var json = {'id':id,'dynamicvalues':newDynamicvalues};
	$.ajax({
		type: "POST",
		url: "updateDynamicvalues.do",
		contentType: "application/json; charset=utf-8",
		data: JSON.stringify(json),
		dataType: "json",
		success: function (response) {
			if (response.code > 0) {
				alert("该动态值修改成功！");
			}else{
				alert("该动态值修改失败！");
			}
			location.href = response.resultURL;
		},
		error: function (response) {
			alert("发生错误");
		}
	});
}

function returnPage(){
	location.href = "systemdynamicvalues.do";
}
